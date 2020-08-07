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

package argonms.center;

import argonms.center.net.internal.CenterGameInterface;
import argonms.center.net.internal.CenterLoginInterface;
import argonms.center.net.internal.CenterRemoteInterface;
import argonms.center.net.internal.CenterShopInterface;
import argonms.center.net.internal.RemoteServerListener;
import argonms.center.net.remoteadmin.TelnetListener;
import argonms.common.ServerType;
import argonms.common.net.external.CheatTracker;
import argonms.common.net.external.RemoteClient;
import argonms.common.net.internal.CenterRemoteOps;
import argonms.common.util.DatabaseManager;
import argonms.common.util.DatabaseManager.DatabaseType;
import argonms.common.util.Scheduler;
import argonms.common.util.output.LittleEndianByteArrayWriter;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GoldenKevin
 */
public class CenterServer {
	private static final Logger LOG = Logger.getLogger(CenterServer.class.getName());

	private static CenterServer instance;

	private RemoteServerListener listener;
	private CenterLoginInterface loginServer;
	private CenterShopInterface shopServer;
	private final Map<Byte, CenterGameInterface> gameServers;
	private final Map<Byte, IntraworldGroups> worldGroups;
	private final Lock readLock;
	private final Lock writeLock;

	private CenterServer() {
		gameServers = new HashMap<Byte, CenterGameInterface>();
		worldGroups = new HashMap<Byte, IntraworldGroups>();
		ReentrantReadWriteLock locks = new ReentrantReadWriteLock();
		readLock = locks.readLock();
		writeLock = locks.writeLock();
	}

	public void init() {
		Properties prop = new Properties();
		int port;
		String authKey;
		int telnetPort;
		boolean useNio;
		try {
			FileReader fr = new FileReader(System.getProperty("argonms.center.config.file", "center.properties"));
			prop.load(fr);
			fr.close();
			port = Integer.parseInt(prop.getProperty("argonms.center.port"));
			authKey = prop.getProperty("argonms.center.auth.key");
			telnetPort = Integer.parseInt(prop.getProperty("argonms.center.telnet"));
			useNio = Boolean.parseBoolean(prop.getProperty("argonms.center.usenio"));

			String temp = prop.getProperty("argonms.center.tz");
			//always set default TimeZone setting last in this block so the
			//timezone of logged messages that caught exceptions from this block
			//are consistently inconsistent - i.e. they always use the server's
			//time zone rather than the intended time zone from config.
			TimeZone.setDefault(temp.isEmpty() ? TimeZone.getDefault() : TimeZone.getTimeZone(temp));
		} catch (IOException ex) {
			//Do note that the time shown by SimpleFormatter is the server's
			//time zone, NOT any time zone we intended to use from the config
			//(because we can't access the config after all!)
			LOG.log(Level.SEVERE, "(Time zone of reported date/time: " + TimeZone.getDefault().getID() + ")\nCould not load center server properties!", ex);
			System.exit(2);
			return;
		}
		prop = new Properties();
		try {
			FileReader fr = new FileReader(System.getProperty("argonms.db.config.file", "db.properties"));
			prop.load(fr);
			fr.close();
			DatabaseManager.setProps(prop, false, useNio);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Could not load database properties!", ex);
			System.exit(3);
			return;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Could not initialize database!", ex);
			System.exit(3);
			return;
		}
		Connection con;
		try {
			con = DatabaseManager.getConnection(DatabaseType.STATE);
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Could not connect to database!", e);
			System.exit(3);
			return;
		}
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("UPDATE `accounts` SET `connected` = ?");
			ps.setInt(1, RemoteClient.STATUS_NOTLOGGEDIN);
			ps.executeUpdate();
		} catch (SQLException ex) {
			LOG.log(Level.WARNING, "Could not reset logged in status of all accounts.", ex);
		} finally {
			DatabaseManager.cleanup(DatabaseType.STATE, null, ps, con);
		}

		Scanner scan = null;
		try {
			scan = new Scanner(new FileReader(System.getProperty("argonms.ct.macbanblacklist.file", "macbanblacklist.txt")));
			CheatTracker.setBlacklistedMacBans(scan);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Could not load macban blacklist!", ex);
			System.exit(3);
			return;
		} finally {
			if (scan != null)
				scan.close();
		}

		Scheduler.enable(true, true);

		listener = new RemoteServerListener(authKey, useNio);
		if (listener.bind(port))
			LOG.log(Level.INFO, "Center Server is online.");
		else
			System.exit(5);
		if (telnetPort != -1) {
			if (new TelnetListener(useNio).bind(telnetPort))
				LOG.log(Level.INFO, "Telnet Server online.");
			else
				System.exit(5);
		}
	}

	/**
	 * All calls to this method must check for themselves if each
	 * CenterGameInterface in the list is disconnected before sending a message.
	 */
	public List<CenterGameInterface> getAllServersOfWorld(byte world, byte exclusion) {
		readLock.lock();
		try {
			List<CenterGameInterface> servers = new ArrayList<CenterGameInterface>();
			for (Entry<Byte, CenterGameInterface> entry : gameServers.entrySet()) {
				byte serverId = entry.getKey().byteValue();
				CenterGameInterface server = entry.getValue();
				if (serverId != exclusion && server.getWorld() == world)
					servers.add(server);
			}
			return servers;
		} finally {
			readLock.unlock();
		}
	}

	public void registerLogin(CenterLoginInterface remote) {
		LOG.log(Level.INFO, "{0} server registered.", remote.getServerName());
		writeLock.lock();
		try {
			loginServer = remote;
			remote.serverOnline();
			sendConnectedGames(remote);
		} finally {
			writeLock.unlock();
		}
	}

	public void registerGame(CenterGameInterface remote) {
		byte serverId = remote.getServerId();
		LOG.log(Level.INFO, "{0} server registered.", remote.getServerName());
		writeLock.lock();
		try {
			gameServers.put(Byte.valueOf(serverId), remote);
			if (!worldGroups.containsKey(Byte.valueOf(remote.getWorld())))
				worldGroups.put(Byte.valueOf(remote.getWorld()), new IntraworldGroups(remote.getWorld()));
			remote.serverOnline();
			notifyGameConnected(serverId, remote.getWorld(), remote.getHost(), remote.getClientPorts());
			sendConnectedShop(remote);
			sendConnectedGamesOfWorld(remote);
		} finally {
			writeLock.unlock();
		}
	}

	public void registerShop(CenterShopInterface remote) {
		LOG.log(Level.INFO, "{0} server registered.", remote.getServerName());
		writeLock.lock();
		try {
			shopServer = remote;
			remote.serverOnline();
			notifyShopConnected(remote.getHost(), remote.getClientPort());
			sendConnectedGames(remote);
		} finally {
			writeLock.unlock();
		}
	}

	public void unregisterLogin() {
		LOG.log(Level.INFO, "{0} server unregistered.", ServerType.getName(ServerType.LOGIN));
		writeLock.lock();
		try {
			loginServer = null;
		} finally {
			writeLock.unlock();
		}
	}

	public void unregisterGame(CenterGameInterface remote) {
		byte serverId = remote.getServerId();
		LOG.log(Level.INFO, "{0} server unregistered.", remote.getServerName());
		writeLock.lock();
		try {
			notifyGameDisconnected(remote.getWorld(), serverId);
			boolean deleteWorldParty = true;
			for (CenterGameInterface cgi : getAllServersOfWorld(remote.getWorld(), serverId)) {
				if (!cgi.isStartingUp()) {
					//don't delete the parties if another game server in this
					//world is online or is shutting down. if it's shutting
					//down, that means it's queued to unregisterGame after us,
					//and we should delete the party on the last remaining game
					//server of this world.
					//if the game server is starting up, then it'll just
					//recreate parties in registerGame anyway, so allow us to
					//delete it if no other servers are online or shutting down
					deleteWorldParty = false;
					break;
				}
			}
			if (deleteWorldParty)
				worldGroups.remove(Byte.valueOf(remote.getWorld()));
			gameServers.remove(Byte.valueOf(serverId));
		} finally {
			writeLock.unlock();
		}
	}

	public void unregisterShop() {
		LOG.log(Level.INFO, "{0} server unregistered.", ServerType.getName(ServerType.SHOP));
		writeLock.lock();
		try {
			notifyShopDisconnected();
			shopServer = null;
		} finally {
			writeLock.unlock();
		}
	}

	public void channelPortChanged(byte world, byte serverId, byte channel, int newPort) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);
		lew.writeByte(CenterRemoteOps.CHANNEL_PORT_CHANGE);
		lew.writeByte(world);
		lew.writeByte(channel);
		lew.writeInt(newPort);
		byte[] bytes = lew.getBytes();

		readLock.lock();
		try {
			if (loginServer != null && loginServer.isOnline())
				loginServer.getSession().send(bytes);
			if (shopServer != null && shopServer.isOnline())
				shopServer.getSession().send(bytes);
			for (CenterGameInterface gameServer : getAllServersOfWorld(world, serverId))
				if (gameServer.isOnline())
					gameServer.getSession().send(bytes);
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void notifyGameConnected(byte serverId, byte world, String host, Map<Byte, Integer> ports) {
		byte[] bytes = writeGameConnected(serverId, world, host, ports);

		if (loginServer != null && loginServer.isOnline())
			loginServer.getSession().send(bytes);
		if (shopServer != null && shopServer.isOnline())
			shopServer.getSession().send(bytes);
		for (CenterGameInterface gameServer : getAllServersOfWorld(world, serverId))
			if (gameServer.isOnline())
				gameServer.getSession().send(bytes);
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void notifyGameDisconnected(byte world, byte serverId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(2);
		lew.writeByte(CenterRemoteOps.GAME_DISCONNECTED);
		lew.writeByte(serverId);
		lew.writeByte(world);
		byte[] bytes = lew.getBytes();

		if (loginServer != null && loginServer.isOnline())
			loginServer.getSession().send(bytes);
		if (shopServer != null && shopServer.isOnline())
			shopServer.getSession().send(bytes);
		for (CenterGameInterface gameServer : getAllServersOfWorld(world, serverId))
			if (gameServer.isOnline())
				gameServer.getSession().send(bytes);
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void notifyShopConnected(String host, int port) {
		byte[] bytes = writeShopConnected(host, port);

		for (CenterGameInterface gameServer : gameServers.values())
			if (gameServer.isOnline())
				gameServer.getSession().send(bytes);
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void notifyShopDisconnected() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(1);
		lew.writeByte(CenterRemoteOps.SHOP_DISCONNECTED);
		byte[] bytes = lew.getBytes();

		for (CenterGameInterface gameServer : gameServers.values())
			if (gameServer.isOnline())
				gameServer.getSession().send(bytes);
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void sendConnectedShop(CenterGameInterface game) {
		if (shopServer != null && shopServer.isOnline())
			game.getSession().send(writeShopConnected(shopServer.getHost(),
					shopServer.getClientPort()));
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void sendConnectedGames(CenterRemoteInterface connected) {
		for (CenterGameInterface game : gameServers.values())
			if (game.isOnline())
				connected.getSession().send(writeGameConnected(game.getServerId(),
						game.getWorld(), game.getHost(), game.getClientPorts()));
	}

	/**
	 * All calls of this method must have acquired either a read or write lock.
	 */
	private void sendConnectedGamesOfWorld(CenterGameInterface connected) {
		byte ourServerId = connected.getServerId();
		byte ourWorld = connected.getWorld();
		for (CenterGameInterface game : gameServers.values())
			if (game.isOnline() && game.getServerId() != ourServerId && game.getWorld() == ourWorld)
				connected.getSession().send(writeGameConnected(game.getServerId(),
						game.getWorld(), game.getHost(), game.getClientPorts()));
	}

	public boolean isServerConnected(byte serverId) {
		readLock.lock();
		try {
			CenterRemoteInterface server = null;
			if (ServerType.isGame(serverId))
				server = gameServers.get(Byte.valueOf(serverId));
			if (ServerType.isLogin(serverId))
				server = loginServer;
			if (ServerType.isShop(serverId))
				server = shopServer;
			return (server != null && server.isOnline());
		} finally {
			readLock.unlock();
		}
	}

	public void sendToLogin(byte[] message) {
		readLock.lock();
		try {
			if (loginServer != null && loginServer.isOnline())
				loginServer.getSession().send(message);
		} finally {
			readLock.unlock();
		}
	}

	public void sendToGame(byte serverId, byte[] message) {
		readLock.lock();
		try {
			CenterGameInterface gameServer = gameServers.get(Byte.valueOf(serverId));
			if (gameServer != null && gameServer.isOnline())
				gameServer.getSession().send(message);
		} finally {
			readLock.unlock();
		}
	}

	public void sendToShop(byte[] message) {
		readLock.lock();
		try {
			if (shopServer != null && shopServer.isOnline())
				shopServer.getSession().send(message);
		} finally {
			readLock.unlock();
		}
	}

	public IntraworldGroups getGroupsDb(byte world) {
		return worldGroups.get(Byte.valueOf(world));
	}

	private static byte[] writeGameConnected(byte serverId, byte world, String host, Map<Byte, Integer> ports) {
		byte size = (byte) ports.size();
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(host.length() + 6 + size * 5);
		lew.writeByte(CenterRemoteOps.GAME_CONNECTED);
		lew.writeByte(serverId);
		lew.writeByte(world);
		lew.writeLengthPrefixedString(host);
		lew.writeByte(size);
		for (Entry<Byte, Integer> entry : ports.entrySet()) {
			lew.writeByte(entry.getKey().byteValue());
			lew.writeInt(entry.getValue().intValue());
		}
		return lew.getBytes();
	}

	private static byte[] writeShopConnected(String host, int port) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(host.length() + 7);
		lew.writeByte(CenterRemoteOps.SHOP_CONNECTED);
		lew.writeLengthPrefixedString(host);
		lew.writeInt(port);
		return lew.getBytes();
	}

	public static CenterServer getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		instance = new CenterServer();
		instance.init();
	}
}
