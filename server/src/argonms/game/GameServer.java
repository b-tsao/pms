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

package argonms.game;

import argonms.common.GlobalConstants;
import argonms.common.LocalServer;
import argonms.common.ServerType;
import argonms.common.loading.DataFileType;
import argonms.common.loading.item.ItemDataLoader;
import argonms.common.loading.string.StringDataLoader;
import argonms.common.net.external.CheatTracker;
import argonms.common.net.external.CommonPackets;
import argonms.common.net.external.RemoteClient;
import argonms.common.net.internal.RemoteCenterSession;
import argonms.common.util.DatabaseManager;
import argonms.common.util.DatabaseManager.DatabaseType;
import argonms.common.util.Scheduler;
import argonms.game.character.GameCharacter;
import argonms.game.loading.beauty.BeautyDataLoader;
import argonms.game.loading.map.MapDataLoader;
import argonms.game.loading.mob.MobDataLoader;
import argonms.game.loading.npc.NpcDataLoader;
import argonms.game.loading.quest.QuestDataLoader;
import argonms.game.loading.reactor.ReactorDataLoader;
import argonms.game.loading.shop.NpcShopDataLoader;
import argonms.game.loading.skill.SkillDataLoader;
import argonms.game.net.WorldChannel;
import argonms.game.net.external.handler.ChatHandler;
import argonms.game.net.internal.CrossServerSynchronization;
import argonms.game.net.internal.GameCenterInterface;
import argonms.game.script.NpcScriptManager;
import argonms.game.script.PortalScriptManager;
import argonms.game.script.ReactorScriptManager;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GoldenKevin
 */
public class GameServer implements LocalServer {
	private static final Logger LOG = Logger.getLogger(GameServer.class.getName());

	private static GameServer instance;

	private Map<Byte, WorldChannel> channels;
	private GameCenterInterface gci;
	private final byte serverId;
	private byte world;
	private String address;
	private boolean preloadAll;
	private DataFileType wzType;
	private String wzPath, scriptsPath;
	private String[] initialEvents;
	private boolean useNio;
	private boolean centerConnected;
	private final GameRegistry registry;
	private final Map<Byte, Set<Byte>> remoteGameChannelMapping;
	private volatile boolean terminated;

	private GameServer(byte serverid) {
		this.serverId = serverid;
		this.registry = new GameRegistry();
		this.remoteGameChannelMapping = new HashMap<Byte, Set<Byte>>();
	}

	public byte getServerId() {
		return serverId;
	}

	public Map<Byte, WorldChannel> getChannels() {
		return Collections.unmodifiableMap(channels);
	}

	public void init() {
		Properties prop = new Properties();
		String centerIp;
		int centerPort;
		String authKey;
		String[] chList;
		try {
			FileReader fr = new FileReader(System.getProperty("argonms.game.config.file", "game" + serverId + ".properties"));
			prop.load(fr);
			fr.close();
			address = prop.getProperty("argonms.game." + serverId + ".host");
			wzType = DataFileType.valueOf(prop.getProperty("argonms.game." + serverId + ".data.type"));
			//wzPath = prop.getProperty("argonms.game." + serverId + ".data.dir");
			preloadAll = Boolean.parseBoolean(prop.getProperty("argonms.game." + serverId + ".data.preload"));
			world = Byte.parseByte(prop.getProperty("argonms.game." + serverId + ".world"));
			chList = prop.getProperty("argonms.game." + serverId + ".channels").replaceAll("\\s", "").split(",");

			centerIp = prop.getProperty("argonms.game." + serverId + ".center.ip");
			centerPort = Integer.parseInt(prop.getProperty("argonms.game." + serverId + ".center.port"));
			authKey = prop.getProperty("argonms.game." + serverId + ".auth.key");
			useNio = Boolean.parseBoolean(prop.getProperty("argonms.game." + serverId + ".usenio"));

			registry.setExpRate(Short.parseShort(prop.getProperty("argonms.game." + serverId + ".exprate")));
			registry.setMesoRate(Short.parseShort(prop.getProperty("argonms.game." + serverId + ".mesorate")));
			registry.setDropRate(Short.parseShort(prop.getProperty("argonms.game." + serverId + ".droprate")));
			registry.setItemsWillExpire(Boolean.parseBoolean(prop.getProperty("argonms.game." + serverId + ".itemexpire")));
			registry.setBuffsWillCooldown(Boolean.parseBoolean(prop.getProperty("argonms.game." + serverId + ".enablecooltime")));
			registry.setMultiLevel(Boolean.parseBoolean(prop.getProperty("argonms.game." + serverId + ".enablemultilevel")));
			registry.setNewsTickerMessage(prop.getProperty("argonms.game." + serverId + ".tickermessage"));

			String temp = prop.getProperty("argonms.game." + serverId + ".events").replaceAll("\\s", "");
			initialEvents = temp.isEmpty() ? new String[0] : temp.split(",");
			temp = prop.getProperty("argonms.game." + serverId + ".tz");
			//always set default TimeZone setting last in this block so the
			//timezone of logged messages that caught exceptions from this block
			//are consistently inconsistent - i.e. they always use the server's
			//time zone rather than the intended time zone from config.
			TimeZone.setDefault(temp.isEmpty() ? TimeZone.getDefault() : TimeZone.getTimeZone(temp));
		} catch (IOException ex) {
			//Do note that the time shown by SimpleFormatter is the server's
			//time zone, NOT any time zone we intended to use from the config
			//(because we can't access the config after all!)
			LOG.log(Level.SEVERE, "(Time zone of reported date/time: " + TimeZone.getDefault().getID() + ")\nCould not load game" + serverId + " server properties!", ex);
			System.exit(2);
			return;
		}
		wzPath = System.getProperty("argonms.data.dir");
		scriptsPath = System.getProperty("argonms.scripts.dir");

		channels = new HashMap<Byte, WorldChannel>(chList.length);
		for (int i = 0; i < chList.length; i++) {
			byte chNum = Byte.parseByte(chList[i]);
			WorldChannel ch = new WorldChannel(world, chNum, Integer.parseInt(prop.getProperty("argonms.game." + serverId + ".channel." + chNum + ".port")));
			ch.createWorldComm();
			channels.put(Byte.valueOf(chNum), ch);
		}
		Map<Byte, CrossServerSynchronization> initializedCss = new HashMap<Byte, CrossServerSynchronization>();
		for (Entry<Byte, WorldChannel> entry : channels.entrySet()) {
			entry.getValue().getCrossServerInterface().initializeLocalChannels(initializedCss);
			initializedCss.put(entry.getKey(), entry.getValue().getCrossServerInterface());
		}

		boolean mcdb = (wzType == DataFileType.MCDB);
		prop = new Properties();
		try {
			FileReader fr = new FileReader(System.getProperty("argonms.db.config.file", "db.properties"));
			prop.load(fr);
			fr.close();
			DatabaseManager.setProps(prop, mcdb, useNio);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Could not load database properties!", ex);
			System.exit(3);
			return;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Could not initialize database!", ex);
			System.exit(3);
			return;
		}
		try {
			DatabaseManager.cleanup(DatabaseType.STATE, null, null, DatabaseManager.getConnection(DatabaseType.STATE));
			if (mcdb) {
				Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					con = DatabaseManager.getConnection(DatabaseType.WZ);
					ps = con.prepareStatement("SELECT `version`,`subversion`,`maple_version` FROM `mcdb_info`");
					rs = ps.executeQuery();
					if (rs.next()) {
						int realVersion = rs.getInt(1);
						int realSubversion = rs.getInt(2);
						int realGameVersion = rs.getInt(3);
						if (realVersion != GlobalConstants.MCDB_VERSION || realSubversion != GlobalConstants.MCDB_SUBVERSION) {
							LOG.log(Level.SEVERE, "MCDB version imcompatible. Expected: {0}.{1} Have: {2}.{3}", new Object[] { GlobalConstants.MCDB_VERSION, GlobalConstants.MCDB_SUBVERSION, realVersion, realSubversion });
							System.exit(3);
							return;
						}
						if (realGameVersion != GlobalConstants.MAPLE_VERSION) //carry on despite the warning...
							LOG.log(Level.WARNING, "Your copy of MCDB is based on an incongruent version of the WZ files. ArgonMS: {0} MCDB: {1}", new Object[] { GlobalConstants.MAPLE_VERSION, realGameVersion });
					}
				} finally {
					DatabaseManager.cleanup(DatabaseType.WZ, rs, ps, con);
				}
			}
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Could not connect to database!", e);
			System.exit(3);
			return;
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

		gci = new GameCenterInterface(serverId, world, this);
		RemoteCenterSession<GameCenterInterface> session = RemoteCenterSession.connect(centerIp, centerPort, authKey, gci);
		if (session != null) {
			session.awaitClose();
			LOG.log(Level.SEVERE, "Lost connection with center server!");
		}
		System.exit(4); //connection with center server lost before we were able to shutdown
	}

	private void initializeData(boolean preloadAll, DataFileType wzType, String wzPath) {
		StringDataLoader.setInstance(wzType, wzPath);
		QuestDataLoader.setInstance(wzType, wzPath);
		BeautyDataLoader.setInstance(wzType, wzPath);
		SkillDataLoader.setInstance(wzType, wzPath);
		ReactorDataLoader.setInstance(wzType, wzPath);
		MobDataLoader.setInstance(wzType, wzPath);
		ItemDataLoader.setInstance(wzType, wzPath);
		MapDataLoader.setInstance(wzType, wzPath);
		NpcShopDataLoader.setInstance(wzType, wzPath);
		NpcDataLoader.setInstance(wzType, wzPath);
		NpcScriptManager.setInstance(scriptsPath);
		PortalScriptManager.setInstance(scriptsPath);
		ReactorScriptManager.setInstance(scriptsPath);
		long start, end;
		start = System.nanoTime();
		System.out.print("Loading String data...");
		StringDataLoader.getInstance().loadAll();
		System.out.println("\tDone!");
		System.out.print("Loading Quest data...");
		QuestDataLoader.getInstance().loadAll();
		System.out.println("\tDone!");
		System.out.print("Loading Beauty data...");
		BeautyDataLoader.getInstance().loadAll();
		System.out.println("\tDone!");
		if (preloadAll) {
			System.out.print("Loading Skill data...");
			SkillDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
			System.out.print("Loading Reactor data...");
			ReactorDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
			System.out.print("Loading Mob data...");
			MobDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
			System.out.print("Loading Item data...");
			ItemDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
			System.out.print("Loading Map data...");
			MapDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
			System.out.print("Loading Shop data...");
			NpcShopDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
			System.out.print("Loading Storage data...");
			NpcDataLoader.getInstance().loadAll();
			System.out.println("\tDone!");
		}
		end = System.nanoTime();
		System.out.println("Preloaded data in " + ((end - start) / 1000000.0) + "ms.");
		for (WorldChannel ch : channels.values())
			ch.initializeEventManager(scriptsPath, initialEvents);
	}

	@Override
	public void registerCenter() {
		LOG.log(Level.INFO, "Center server registered.");
		centerConnected = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				initializeData(preloadAll, wzType, wzPath);
				boolean doingWork = false;
				for (WorldChannel ch : channels.values()) {
					ch.listen(useNio);
					if (ch.getPort() != -1)
						doingWork = true;
				}
				if (doingWork)
					gci.serverReady();
				else
					System.exit(5);
			}
		}, "data-preloader-thread").start();
	}

	@Override
	public void unregisterCenter() {
		if (centerConnected) {
			LOG.log(Level.INFO, "Center server unregistered.");
			centerConnected = false;
		}
	}

	public void registerGame(byte serverId, String host, Map<Byte, Integer> ports) {
		try {
			byte[] ip = InetAddress.getByName(host).getAddress();
			remoteGameChannelMapping.put(Byte.valueOf(serverId), ports.keySet());
			for (WorldChannel ch : channels.values())
				ch.getCrossServerInterface().addRemoteChannels(serverId, ip, ports);
			LOG.log(Level.INFO, "{0} server registered as {1}.", new Object[] { ServerType.getName(serverId), host });
		} catch (UnknownHostException e) {
			LOG.log(Level.INFO, "Could not accept shop server because its"
					+ " address could not be resolved!", e);
		}
	}

	public void unregisterGame(byte serverId) {
		LOG.log(Level.INFO, "{0} server unregistered.", ServerType.getName(serverId));
		Set<Byte> remove = remoteGameChannelMapping.remove(Byte.valueOf(serverId));
		for (WorldChannel ch : channels.values())
			ch.getCrossServerInterface().removeRemoteChannels(remove);
	}

	public void registerShop(String host, int port) {
		try {
			byte[] ip = InetAddress.getByName(host).getAddress();
			for (WorldChannel ch : channels.values())
				ch.getCrossServerInterface().addShopServer(ip, port);
			LOG.log(Level.INFO, "Shop server registered as {0}:{1}.", new Object[] { host, port });
		} catch (UnknownHostException e) {
			LOG.log(Level.INFO, "Could not accept shop server because its"
					+ " address could not be resolved!", e);
		}
	}

	public void unregisterShop() {
		LOG.log(Level.INFO, "Shop server unregistered.");
		for (WorldChannel ch : channels.values())
			ch.getCrossServerInterface().removeShopServer();
	}

	public void updateRemoteChannelPort(byte channel, int port) {
		for (WorldChannel ch : channels.values())
			ch.getCrossServerInterface().changeRemoteChannelPort(channel, port);
	}

	@Override
	public String getExternalIp() {
		return address;
	}

	public Map<Byte, Integer> getClientPorts() {
		Map<Byte, Integer> ports = new HashMap<Byte, Integer>(channels.size());
		for (Entry<Byte, WorldChannel> entry : channels.entrySet())
			ports.put(entry.getKey(), Integer.valueOf(entry.getValue().getPort()));
		return ports;
	}

	public GameCenterInterface getCenterInterface() {
		return gci;
	}

	public byte channelOfPlayer(int characterid) {
		for (Entry<Byte, WorldChannel> entry : channels.entrySet())
			if (entry.getValue().isPlayerConnected(characterid))
				return entry.getKey().byteValue();
		return -1;
	}

	public byte channelOfPlayer(String characterName) {
		for (Entry<Byte, WorldChannel> entry : channels.entrySet())
			if (entry.getValue().getPlayerByName(characterName) != null)
				return entry.getKey().byteValue();
		return -1;
	}

	public void serverWideMessage(byte style, String message, byte channel, boolean megaEar) {
		if (style == ChatHandler.TextStyle.TICKER.byteValue())
			getVariables().setNewsTickerMessage(message);
		
		byte[] packet = CommonPackets.writeServerMessage(style, message, channel, megaEar);
		for (WorldChannel chn : GameServer.getInstance().getChannels().values())
			for (GameCharacter p : chn.getConnectedPlayers())
				p.getClient().getSession().send(packet);
	}
	
	public void serverWideNotice(byte style, String message) {
		serverWideMessage(style, message, (byte) -1, false);
	}

	public GameRegistry getRegistry() {
		return registry;
	}

	private void terminate(boolean halt) {
		terminated = true;
		List<GameCharacter> toSave = new ArrayList<GameCharacter>();
		for (WorldChannel chn : channels.values()) {
			chn.shutdown();
			for (GameCharacter p : chn.getConnectedPlayers()) {
				p.getClient().getSession().close("Shutdown");
				toSave.add(p);
			}
		}
		for (GameCharacter p : toSave) {
			p.saveCharacter();
			p.getClient().updateState(RemoteClient.STATUS_NOTLOGGEDIN);
			p.disconnect();
		}
		if (halt) {
			Scheduler.getInstance().shutdown();
			Scheduler.getWheelTimer().shutdown();
			gci.getSession().close("Halt");
		} else {
			for (WorldChannel chn : channels.values()) {
				chn.getMapFactory().clear();
				chn.resetConnectedPlayers();
			}
		}
	}

	public void shutdown(final boolean halt, int time) {
		if (time == 0) {
			terminate(halt);
		} else {
			Scheduler.getInstance().runAfterDelay(new Runnable() {
				@Override
				public void run() {
					terminate(halt);
				}
			}, time);
		}
	}

	public boolean isTerminated() {
		return terminated;
	}

	public static GameServer getInstance() {
		return instance;
	}

	public static WorldChannel getChannel(byte ch) {
		return instance.channels.get(Byte.valueOf(ch));
	}

	public static GameRegistry getVariables() {
		return instance.getRegistry();
	}

	public static void main(String[] args) {
		instance = new GameServer(Byte.parseByte(System.getProperty("argonms.game.serverid", "0")));
		instance.init();
	}
}
