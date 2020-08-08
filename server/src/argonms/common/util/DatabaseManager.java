/*
 * ArgonMS MapleStory server emulator written in Java
 * Copyright (C) 2011-2013  GoldenKevin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package argonms.common.util;

import argonms.common.util.collections.LockableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: abstract singleton class?
/**
 * Provides a central location to store database connections used for pooling,
 * and common operations when accessing the database (such as the finalizing of
 * PreparedStatements, ResultSets). Depending on the value of the "nio" argument
 * passed to setProps, this class may either use a ThreadLocal model or a
 * CachedConnectionPool model.
 *
 * Improved from OdinMS' DatabaseConnection class by adding a check for stale
 * connections before returning it to the caller of getConnection().
 *
 * Support for connections to MCDB or other kinds of databases that store WZ
 * data have also been integrated into this class.
 *
 * @author GoldenKevin
 * @version 2.0
 */
public final class DatabaseManager {
	public enum DatabaseType { STATE, WZ }

	private final static Logger LOG = Logger.getLogger(DatabaseManager.class.getName());

	private static final Map<DatabaseType, ConnectionPool> connections;
	private static String driver;

	static {
		connections = new EnumMap<DatabaseType, ConnectionPool>(DatabaseType.class);
	}

	private static String getNonFullyQualifiedClassName(String fullyQualified) {
		return fullyQualified.substring(Math.max(fullyQualified.indexOf('.'), fullyQualified.indexOf('$')) + 1);
	}

	public static Connection getConnection(DatabaseType type) throws SQLException {
		ConnectionPool pool = connections.get(type);
		try {
			return pool.getConnection();
		} finally {
			LOG.log(Level.FINEST, "Database pool: {0}, Taken connections: {1}, All connections: {2}, Impl: {3}, Caller: {4}",
					new Object[] { type, pool.connectionsInUse(), pool.totalConnections(), getNonFullyQualifiedClassName(pool.getClass().getName()), Thread.currentThread().getStackTrace()[2] });
		}
	}

	public static void cleanup(DatabaseType type, ResultSet rs, PreparedStatement ps, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException ex) {
			}
		}
		if (con != null) {
			connections.get(type).returnConnection(con);
		}
	}

	public static void setProps(Properties props, boolean useMcdb, boolean nio) throws SQLException {
		driver = props.getProperty("driver");
		try {
			Class.forName(driver); //load the jdbc driver
		} catch (ClassNotFoundException e) {
			throw new SQLException("Unable to find JDBC library. Do you have MySQL Connector/J (if using default JDBC driver)?"/*, e*/);
		}
		String url = props.getProperty("url");
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		connections.put(DatabaseType.STATE, nio ? new ThreadLocalConnections(url, user, password) : new CachedConnectionPool(url, user, password));
		if (useMcdb) {
			String wz = props.getProperty("mcdb");
			connections.put(DatabaseType.WZ, nio ? new ThreadLocalConnections(wz, user, password) : new CachedConnectionPool(wz, user, password));
		}
	}

	public static Map<DatabaseType, Map<Connection, SQLException>> closeAll() {
		Map<DatabaseType, Map<Connection, SQLException>> exceptions = new EnumMap<DatabaseType, Map<Connection, SQLException>>(DatabaseType.class);
		for (Entry<DatabaseType, ConnectionPool> pool : connections.entrySet()) {
			DatabaseType poolType = pool.getKey();
			LockableList<Connection> allConnections = pool.getValue().allConnections();
			allConnections.lockWrite();
			try {
				for (Iterator<Connection> iter = allConnections.iterator(); iter.hasNext();) {
					Connection con = iter.next();
					try {
						con.close();
						iter.remove();
					} catch (SQLException e) {
						Map<Connection, SQLException> subExceptions = exceptions.get(poolType);
						if (subExceptions == null) {
							subExceptions = new HashMap<Connection, SQLException>();
							exceptions.put(poolType, subExceptions);
						}
						subExceptions.put(con, e);
					}
				}
			} finally {
				allConnections.unlockWrite();
			}
		}
		return exceptions;
	}

	private static interface ConnectionPool {
		public Connection getConnection() throws SQLException;
		public void returnConnection(Connection con);
		public LockableList<Connection> allConnections();
		public int connectionsInUse();
		public int totalConnections();
	}

	private static class ThreadLocalConnections extends ThreadLocal<Connection> implements ConnectionPool {
		private final LockableList<Connection> allConnections;
		private final AtomicInteger taken;
		private final ThreadLocal<SQLException> exceptions;
		private final String url, user, password;

		protected ThreadLocalConnections(String url, String user, String password) {
			allConnections = new LockableList<Connection>(new LinkedList<Connection>());
			taken = new AtomicInteger(0);
			exceptions = new ThreadLocal<SQLException>();
			this.url = url;
			this.user = user;
			this.password = password;
		}

		@Override
		protected Connection initialValue() {
			try {
				Connection con = DriverManager.getConnection(url, user, password);
				allConnections.addWhenSafe(con);
				return con;
			} catch (SQLException e) {
				exceptions.set(/*new SQLException("Could not connect to database.", */e/*)*/);
				return null;
			}
		}

		private Connection tryGetConnection() throws SQLException {
			Connection con = get();
			if (con == null) {
				remove();
				SQLException ex = exceptions.get();
				exceptions.remove();
				throw ex;
			}
			return con;
		}

		@Override
		public Connection getConnection() throws SQLException {
			Connection con = tryGetConnection();
			if (!con.isValid(0)) {
				try {
					con.close();
					allConnections.removeWhenSafe(con);
				} catch (SQLException e) {
					throw new SQLException("Could not remove invalid connection to database.", e);
				}
				remove();
				con = tryGetConnection();
			}
			taken.incrementAndGet();
			return con;
		}

		@Override
		public void returnConnection(Connection con) {
			taken.decrementAndGet();
		}

		@Override
		public LockableList<Connection> allConnections() {
			return allConnections;
		}

		@Override
		public int connectionsInUse() {
			return taken.get();
		}

		@Override
		public int totalConnections() {
			return allConnections.size();
		}
	}

	private static class CachedConnectionPool implements ConnectionPool {
		private final LockableList<Connection> allConnections;
		private final Queue<Connection> available;
		private final AtomicInteger taken;
		private final String url, user, password;

		protected CachedConnectionPool(String url, String user, String password) {
			allConnections = new LockableList<Connection>(new LinkedList<Connection>());
			available = new ConcurrentLinkedQueue<Connection>();
			taken = new AtomicInteger(0);
			this.url = url;
			this.user = user;
			this.password = password;
		}

		@Override
		public Connection getConnection() throws SQLException {
			Connection next = available.poll();
			while (next != null && !next.isValid(0)) {
				try {
					next.close();
					allConnections.removeWhenSafe(next);
				} catch (SQLException e) {
					throw new SQLException("Could not remove invalid connection to database.", e);
				}
				next = available.poll();
			}
			if (next == null) {
				next = DriverManager.getConnection(url, user, password);
				allConnections.addWhenSafe(next);
			}
			taken.incrementAndGet();
			return next;
		}

		@Override
		public void returnConnection(Connection con) {
			available.offer(con);
			taken.decrementAndGet();
		}

		@Override
		public LockableList<Connection> allConnections() {
			return allConnections;
		}

		@Override
		public int connectionsInUse() {
			return taken.get(); //should = available.size()
		}

		@Override
		public int totalConnections() {
			return allConnections.getSizeWhenSafe();
		}
	}

	private DatabaseManager() {
		//uninstantiable...
	}
}
