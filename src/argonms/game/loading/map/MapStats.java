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

package argonms.game.loading.map;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author GoldenKevin
 */
public class MapStats {
	private final int mapid;
	private final Map<Byte, PortalData> portals;
	private final Map<String, AreaData> areas;
	private final FootholdTree footholds;
	private final Map<Integer, SpawnData> life;
	private final Map<Integer, ReactorData> reactors;
	private int returnMapId;
	private float monsterRate;
	private boolean town;
	private boolean clock;
	private boolean everlast;
	private String shipScript;
	private byte shipKind;
	private int forcedReturn;
	/**
	 * 19-bit bit field, with the flags (from most significant to least significant bits):
	 * (?)(?)(?)(?)(?)(?)(?)(chalkboard)(dropDown)(potionUse)(mount)(minigames)(vipRock)(regularExpLoss)(channelSwitching)(mysticDoor)(summoningBag)(movementSkills)(jump)
	 * when a bit is set, that functionality is disabled on the map.
	 */
	private int fieldLimit;
	private int protectItem;
	private int decHp;
	private int timeLimit;

	protected MapStats(int mapid) {
		this.mapid = mapid;
		portals = new HashMap<Byte, PortalData>();
		areas = new HashMap<String, AreaData>();
		footholds = new FootholdTree();
		life = new HashMap<Integer, SpawnData>();
		reactors = new HashMap<Integer, ReactorData>();
	}

	protected void setTown() {
		this.town = true;
	}

	protected void setReturnMap(int mapid) {
		this.returnMapId = mapid;
	}

	protected void setForcedReturn(int mapid) {
		this.forcedReturn = mapid;
	}

	protected void setMobRate(float rate) {
		this.monsterRate = rate;
	}

	protected void setFieldLimit(int bitset) {
		this.fieldLimit = bitset;
	}

	protected void setDecHp(int dec) {
		this.decHp = dec;
	}

	protected void setTimeLimit(int limit) {
		this.timeLimit = limit;
	}

	protected void setProtectItem(int item) {
		this.protectItem = item;
	}

	protected void setEverlast() {
		this.everlast = true;
	}

	protected void addLife(int id, SpawnData l) {
		life.put(Integer.valueOf(id), l);
	}

	protected void addArea(String id, AreaData a) {
		areas.put(id, a);
	}

	protected void setClock() {
		this.clock = true;
	}

	protected void setShipObj(String shipObj) {
		int end = shipObj.lastIndexOf('/'); //last part of path is too specific for a script
		int start = shipObj.lastIndexOf('/', shipObj.lastIndexOf('/', end - 1) - 1) + 1; //use next two elements in path
		this.shipScript = shipObj.substring(start, end).replaceAll("/", "_");
	}

	protected void setShipKind(byte kind) {
		this.shipKind = kind;
	}

	protected void addReactor(int id, ReactorData rt) {
		reactors.put(Integer.valueOf(id), rt);
	}

	protected void addFoothold(Foothold fh) {
		footholds.load(fh);
	}

	protected void addPortal(int id, PortalData p) {
		portals.put(Byte.valueOf((byte) id), p);
	}

	protected void finished() {
		footholds.finished();
	}

	public boolean isTown() {
		return town;
	}

	public int getMapId() {
		return mapid;
	}

	public int getReturnMap() {
		return returnMapId;
	}

	public int getForcedReturn() {
		return forcedReturn;
	}

	public float getMobRate() {
		return monsterRate;
	}

	public boolean reducedExpLoss() {
		return isTown() || (fieldLimit & 0x20) != 0; //regularExpLoss flag
	}

	public int getDecHp() {
		return decHp;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public int getProtectItem() {
		return protectItem;
	}

	public boolean isEverlast() {
		return everlast;
	}

	public Map<Integer, SpawnData> getLife() {
		return life;
	}

	public Map<String, AreaData> getAreas() {
		return areas;
	}

	public boolean hasClock() {
		return clock;
	}

	public String getShipScript() {
		return shipScript;
	}

	public byte getShipKind() {
		return shipKind;
	}

	public Map<Integer, ReactorData> getReactors() {
		return reactors;
	}

	public FootholdTree getFootholds() {
		return footholds;
	}

	public Map<Byte, PortalData> getPortals() {
		return portals;
	}
}
