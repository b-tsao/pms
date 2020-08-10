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

package argonms.common.net.external;

import argonms.common.GlobalConstants;
import argonms.common.character.Cooldown;
import argonms.common.character.LoggedInPlayer;
import argonms.common.character.Player;
import argonms.common.character.QuestEntry;
import argonms.common.character.SkillEntry;
import argonms.common.character.Skills;
import argonms.common.character.inventory.Equip;
import argonms.common.character.inventory.Inventory.InventoryType;
import argonms.common.character.inventory.InventorySlot;
import argonms.common.character.inventory.InventorySlot.ItemType;
import argonms.common.character.inventory.InventoryTools;
import argonms.common.character.inventory.Pet;
import argonms.common.character.inventory.Ring;
import argonms.common.util.TimeTool;
import argonms.common.util.output.LittleEndianByteArrayWriter;
import argonms.common.util.output.LittleEndianWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author GoldenKevin
 */
public final class CommonPackets {
	private static final int[] ROCK_MAPS = { //there has to be exactly 5!
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP,
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP,
		GlobalConstants.NULL_MAP
	};

	private static final int[] VIP_MAPS = { //there has to be exactly 10!
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP,
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP,
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP,
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP,
		GlobalConstants.NULL_MAP, GlobalConstants.NULL_MAP
	};

	public static byte[] writeNewGameHost(byte[] host, int port) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9);
		lew.writeShort(ClientSendOps.GAME_HOST_ADDRESS);
		lew.writeBool(true);
		lew.writeBytes(host);
		lew.writeShort((short) port);
		return lew.getBytes();
	}

	/**
	 * Append item expiration time info to an existing LittleEndianWriter.
	 *
	 * @param lew The LittleEndianWriter to write to.
	 * @param time The expiration time.
	 * @param show Show the expiration time.
	 */
	public static void writeItemExpire(LittleEndianWriter lew, long time, boolean show) {
		if (!show || time <= 0)
			time = TimeTool.NO_EXPIRATION;
		lew.writeLong(TimeTool.unixToWindowsTime(time));
	}

	public static void writeCharStats(LittleEndianWriter lew, Player p) {
		lew.writeInt(p.getId()); // character id
		lew.writePaddedAsciiString(p.getName(), 13);
		lew.writeByte(p.getGender()); // gender (0 = male, 1 = female)
		lew.writeByte(p.getSkinColor()); // skin color
		lew.writeInt(p.getEyes()); // face
		lew.writeInt(p.getHair()); // hair
		Pet[] pets = p.getPets();
		for (int i = 0; i < 3; i++)
			lew.writeLong(pets[i] == null ? 0 : pets[i].getUniqueId());
		lew.writeByte((byte) p.getLevel()); // level
		lew.writeShort(p.getJob()); // job
		lew.writeShort(p.getStr()); // str
		lew.writeShort(p.getDex()); // dex
		lew.writeShort(p.getInt()); // int
		lew.writeShort(p.getLuk()); // luk
		lew.writeShort(p.getHp()); // hp (?)
		lew.writeShort(p.getMaxHp()); // maxhp
		lew.writeShort(p.getMp()); // mp (?)
		lew.writeShort(p.getMaxMp()); // maxmp
		lew.writeShort(p.getAp()); // remaining ap
		lew.writeShort(p.getSp()); // remaining sp
		lew.writeInt(p.getExp()); // current exp
		lew.writeShort(p.getFame()); // fame
		lew.writeInt(p.getSpouseId());
		lew.writeInt(p.getMapId()); // current map id
		lew.writeByte(p.getSpawnPoint()); // spawnpoint
		lew.writeInt(0);
	}

	public static void writeAvatar(LittleEndianWriter lew, byte gender, byte skin,
			int eyes, boolean messenger, int hair, Map<Short, Integer> equips, Pet[] pets) {
		lew.writeByte(gender);
		lew.writeByte(skin);
		lew.writeInt(eyes);
		lew.writeBool(!messenger);
		lew.writeInt(hair); // hair

		Map<Byte, Integer> visEquip = new LinkedHashMap<Byte, Integer>();
		Map<Byte, Integer> maskedEquip = new LinkedHashMap<Byte, Integer>();
		int cashWeapon = 0;
		for (Entry<Short, Integer> ent : equips.entrySet()) {
			//assume that all items in equipped have negative positions
			byte pos = (byte) (ent.getKey().shortValue() * -1);
			if (pos > 100) { //cash equips
				Byte oPos = Byte.valueOf((byte) (pos - 100));
				if (pos != 111) { //accessories/armor
					if (visEquip.containsKey(oPos)) //existing normal equip needs to be moved to masked
						maskedEquip.put(oPos, visEquip.get(oPos));
					visEquip.put(oPos, ent.getValue());
				} else { //cash weapon
					cashWeapon = ent.getValue().intValue();
				}
			} else { //normal equips
				Byte oPos = Byte.valueOf(pos);
				if (visEquip.containsKey(oPos)) //cash equip already masked this
					maskedEquip.put(oPos, ent.getValue());
				else
					visEquip.put(oPos, ent.getValue());
			}
		}

		for (Entry<Byte, Integer> entry : visEquip.entrySet()) {
			lew.writeByte(entry.getKey().byteValue());
			lew.writeInt(entry.getValue().intValue());
		}
		lew.writeByte((byte) 0xFF); //end of visible equipped

		for (Entry<Byte, Integer> entry : maskedEquip.entrySet()) {
			lew.writeByte(entry.getKey().byteValue());
			lew.writeInt(entry.getValue().intValue());
		}
		lew.writeByte((byte) 0xFF); //end of masked equipped

		lew.writeInt(cashWeapon);

		for (int i = 0; i < 3; i++)
			lew.writeInt(pets[i] == null ? 0 : pets[i].getDataId());
	}

	public static void writeAvatar(LittleEndianWriter lew, Player p, boolean messenger) {
		writeAvatar(lew, p.getGender(), p.getSkinColor(), p.getEyes(), messenger,
				p.getHair(), p.getInventory(InventoryType.EQUIPPED).getItemIds(), p.getPets());
	}

	public static void writeItemInfo(LittleEndianWriter lew, InventorySlot item,
			boolean showExpire, boolean shopTransfer) {
		boolean cashItem = item.getUniqueId() > 0;
		lew.writeByte(item.getTypeByte());
		lew.writeInt(item.getDataId());
		lew.writeBool(cashItem);
		if (cashItem)
			lew.writeLong(item.getUniqueId());
		writeItemExpire(lew, item.getExpiration(), showExpire);

		switch (item.getType()) {
			case PET: {
				Pet pet = (Pet) item;
				lew.writePaddedAsciiString(pet.getName(), 13);
				lew.writeByte(pet.getLevel());
				lew.writeShort(pet.getCloseness());
				lew.writeByte(pet.getFullness());
				writeItemExpire(lew, pet.getExpiration(), !pet.isExpired());
				lew.writeLengthPrefixedString(pet.getOwner());
				lew.writeShort(pet.getFlag());
				break;
			}
			case EQUIP:
			case MOUNT:
			case RING: {
				Equip equip = (Equip) item;
				lew.writeByte(equip.getUpgradeSlots());
				lew.writeByte(equip.getLevel());
				lew.writeShort(equip.getStr());
				lew.writeShort(equip.getDex());
				lew.writeShort(equip.getInt());
				lew.writeShort(equip.getLuk());
				lew.writeShort(equip.getHp());
				lew.writeShort(equip.getMp());
				lew.writeShort(equip.getWatk());
				lew.writeShort(equip.getMatk());
				lew.writeShort(equip.getWdef());
				lew.writeShort(equip.getMdef());
				lew.writeShort(equip.getAcc());
				lew.writeShort(equip.getAvoid());
				lew.writeShort(equip.getHands());
				lew.writeShort(equip.getSpeed());
				lew.writeShort(equip.getJump());
				lew.writeLengthPrefixedString(equip.getOwner());

				if (!shopTransfer) {
					lew.writeShort(equip.getFlag());
					if (!cashItem) //Vicious' Hammer was introduced in v0.59...
						lew.writeLong(0); //one of these values has to be for it
				} else {
					lew.writeBytes(new byte[] { 0x40, (byte) 0xE0, (byte) 0xFD, 0x3B, 0x37, 0x4F, 0x01 });
					lew.writeInt(-1);
				}
				break;
			}
			default: {
				lew.writeShort(item.getQuantity());
				lew.writeLengthPrefixedString(item.getOwner());
				lew.writeShort(item.getFlag());
				if (InventoryTools.isThrowingStar(item.getDataId())
						|| InventoryTools.isBullet(item.getDataId())) {
					//Might be rechargeable ID for internal tracking/duping tracking
					lew.writeLong(0);
				}
				break;
			}
		}
	}

	public static void writeItemInfo(LittleEndianWriter lew, short pos,
			InventorySlot item) {
		if (pos < 0) { //equipped inventory has negative positions
			pos *= -1;
			if (pos > 100) //masking equips (cash equips) have positions < -100
				pos -= 100;
		}
		lew.writeByte((byte) pos);
		writeItemInfo(lew, item, true, false);
	}

	public static byte[] writeInventoryUpdateSlotQuantity(InventoryType type, short pos, InventorySlot item) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 1);
		lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
		lew.writeByte(type.byteValue());
		lew.writeShort(pos);
		lew.writeShort(item.getQuantity());
		return lew.getBytes();
	}

	public static byte[] writeInventoryNoChange() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 0);
		return lew.getBytes();
	}

	public static byte[] writeInventoryUpdateEquipStats(short pos, InventorySlot item) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 1);
		lew.writeByte(PacketSubHeaders.INVENTORY_STAT_UPDATE);
		lew.writeByte(InventoryType.EQUIP.byteValue());
		lew.writeShort(pos);
		CommonPackets.writeItemInfo(lew, item, true, false);
		return lew.getBytes();
	}

	public static byte[] writeInventoryMoveItemCombineQuantities(InventoryType type, short src, short dst, short total) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 2);
		lew.writeByte(PacketSubHeaders.INVENTORY_CLEAR_SLOT);
		lew.writeByte(type.byteValue());
		lew.writeShort(src);
		lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
		lew.writeByte(type.byteValue());
		lew.writeShort(dst);
		lew.writeShort(total);
		return lew.getBytes();
	}

	public static byte[] writeInventoryUpdateEquipFromScroll(short scrollPos, short whiteScrollPos, short equipPos, InventorySlot scroll, InventorySlot whiteScroll, InventorySlot equip) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) (whiteScrollPos == 0 ? 2 : 3));
		if (scroll == null) {
			lew.writeByte(PacketSubHeaders.INVENTORY_CLEAR_SLOT);
			lew.writeByte(InventoryType.USE.byteValue());
			lew.writeShort(scrollPos);
		} else {
			lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
			lew.writeByte(InventoryType.USE.byteValue());
			lew.writeShort(scrollPos);
			lew.writeShort(scroll.getQuantity());
		}
		if (whiteScrollPos != 0) {
			if (whiteScroll == null) {
				lew.writeByte(PacketSubHeaders.INVENTORY_CLEAR_SLOT);
				lew.writeByte(InventoryType.USE.byteValue());
				lew.writeShort(whiteScrollPos);
			} else {
				lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
				lew.writeByte(InventoryType.USE.byteValue());
				lew.writeShort(whiteScrollPos);
				lew.writeShort(whiteScroll.getQuantity());
			}
		}
		if (equip == null) {
			lew.writeByte(PacketSubHeaders.INVENTORY_CLEAR_SLOT);
			lew.writeByte(InventoryType.EQUIP.byteValue());
			lew.writeShort(equipPos);
			if (equipPos < 0) {
				lew.writeBool(true);
			}
		} else {
			lew.writeByte(PacketSubHeaders.INVENTORY_STAT_UPDATE);
			lew.writeByte(InventoryType.EQUIP.byteValue());
			lew.writeShort(equipPos);
			CommonPackets.writeItemInfo(lew, equip, true, false);
		}
		return lew.getBytes();
	}

	public static byte[] writeInventoryClearSlot(InventoryType type, short slot) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(slot >= 0 ? 8 : 9);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 1);
		lew.writeByte(PacketSubHeaders.INVENTORY_CLEAR_SLOT);
		lew.writeByte(type.byteValue());
		lew.writeShort(slot);
		if (slot < 0) {
			lew.writeBool(true);
		}
		return lew.getBytes();
	}

	public static byte[] writeInventoryMoveItemShiftQuantities(InventoryType type, short src, short srcQty, short dst, short dstQty) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 2);
		lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
		lew.writeByte(type.byteValue());
		lew.writeShort(src);
		lew.writeShort(srcQty);
		lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
		lew.writeByte(type.byteValue());
		lew.writeShort(dst);
		lew.writeShort(dstQty);
		return lew.getBytes();
	}

	public static byte[] writeInventoryDropItem(InventoryType type, short src, short quantity) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 1);
		lew.writeByte(PacketSubHeaders.INVENTORY_QUANTITY_UPDATE);
		lew.writeByte(type.byteValue());
		lew.writeShort(src);
		lew.writeShort(quantity);
		return lew.getBytes();
	}

	public static byte[] writeInventoryMoveItem(InventoryType type, short src, short dst, byte equipIndicator) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(equipIndicator == -1 ? 10 : 11);
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 1);
		lew.writeByte(PacketSubHeaders.INVENTORY_CHANGE_POSITION);
		lew.writeByte(type.byteValue());
		lew.writeShort(src);
		lew.writeShort(dst);
		if (equipIndicator != -1) {
			lew.writeByte(equipIndicator);
		}
		return lew.getBytes();
	}

	public static byte[] writeInventoryAddSlot(InventoryType type, short pos, InventorySlot item) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 1);
		lew.writeByte(PacketSubHeaders.INVENTORY_STAT_UPDATE);
		lew.writeByte(type.byteValue());
		lew.writeShort(pos);
		CommonPackets.writeItemInfo(lew, item, true, false);
		return lew.getBytes();
	}

	public static byte[] writeInventoryUpdatePet(short pos, InventorySlot item) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.MODIFY_INVENTORY_SLOT);
		lew.writeBool(true);
		lew.writeByte((byte) 2);
		lew.writeByte(PacketSubHeaders.INVENTORY_CLEAR_SLOT);
		lew.writeByte(InventoryType.CASH.byteValue());
		lew.writeShort(pos);
		lew.writeByte(PacketSubHeaders.INVENTORY_STAT_UPDATE);
		lew.writeByte(InventoryType.CASH.byteValue());
		lew.writeShort(pos);
		CommonPackets.writeItemInfo(lew, item, true, false);
		return lew.getBytes();
	}

	public static byte[] writeInventoryUpdateCapacity(InventoryType type, short capacity) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);
		lew.writeShort(ClientSendOps.UPDATE_INVENTORY_CAPACITY);
		lew.writeByte(type.byteValue());
		lew.writeByte((byte) capacity);
		return lew.getBytes();
	}

	public static void writeCharData(LittleEndianWriter lew, LoggedInPlayer p) {
		lew.writeLong(-1);
		writeCharStats(lew, p);
		lew.writeByte((byte) p.getBuddyList().getCapacity());

		lew.writeInt(p.getMesos()); // mesos
		lew.writeByte((byte) p.getInventory(InventoryType.EQUIP).getMaxSlots()); // equip slots
		lew.writeByte((byte) p.getInventory(InventoryType.USE).getMaxSlots()); // use slots
		lew.writeByte((byte) p.getInventory(InventoryType.SETUP).getMaxSlots()); // set-up slots
		lew.writeByte((byte) p.getInventory(InventoryType.ETC).getMaxSlots()); // etc slots
		lew.writeByte((byte) p.getInventory(InventoryType.CASH).getMaxSlots()); // cash slots

		Map<Short, InventorySlot> iv = p.getInventory(InventoryType.EQUIPPED).getAll();
		Map<Short, InventorySlot> visible = new TreeMap<Short, InventorySlot>();
		Map<Short, InventorySlot> masked = new TreeMap<Short, InventorySlot>();
		List<Ring> coupleRings = new ArrayList<Ring>();
		List<Ring> friendshipRings = new ArrayList<Ring>();
		List<Ring> weddingRings = new ArrayList<Ring>();
		synchronized(iv) {
			for (Entry<Short, InventorySlot> entry : iv.entrySet()) {
				InventorySlot item = entry.getValue();
				if (entry.getKey().shortValue() < -100)
					masked.put(entry.getKey(), item);
				else
					visible.put(entry.getKey(), item);
				if (item.getType() == ItemType.RING)
					if (InventoryTools.isCoupleRing(item.getDataId()))
						coupleRings.add((Ring) item);
					else if (InventoryTools.isFriendshipRing(item.getDataId()))
						friendshipRings.add((Ring) item);
					else if (InventoryTools.isWeddingRing(item.getDataId()))
						weddingRings.add((Ring) item);
			}
		}

		for (Entry<Short, InventorySlot> item : visible.entrySet())
			writeItemInfo(lew, item.getKey().shortValue(), item.getValue());
		lew.writeByte((byte) 0); //end of visible equipped
		for (Entry<Short, InventorySlot> item : masked.entrySet())
			writeItemInfo(lew, item.getKey().shortValue(), item.getValue());
		lew.writeByte((byte) 0); //end of masked equipped

		iv = p.getInventory(InventoryType.EQUIP).getAll();
		synchronized(iv) {
			for (Entry<Short, InventorySlot> entry : iv.entrySet()) {
				if (entry.getValue().getType() == ItemType.RING)
					if (InventoryTools.isCoupleRing(entry.getValue().getDataId()))
						coupleRings.add((Ring) entry.getValue());
					else if (InventoryTools.isFriendshipRing(entry.getValue().getDataId()))
						friendshipRings.add((Ring) entry.getValue());
					else if (InventoryTools.isWeddingRing(entry.getValue().getDataId()))
						weddingRings.add((Ring) entry.getValue());
				writeItemInfo(lew, entry.getKey().shortValue(), entry.getValue());
			}
		}
		lew.writeByte((byte) 0); //end of equip inventory

		for (InventoryType invType : new InventoryType[] { InventoryType.USE, InventoryType.SETUP, InventoryType.ETC, InventoryType.CASH }) {
			iv = p.getInventory(invType).getAll();
			synchronized(iv) {
				for (Entry<Short, InventorySlot> entry : iv.entrySet())
					writeItemInfo(lew, entry.getKey().shortValue(), entry.getValue());
			}
			lew.writeByte((byte) 0); //end of inventory
		}

		Map<Integer, SkillEntry> skills = p.getSkillEntries();
		lew.writeShort((short) skills.size());
		for (Entry<Integer, SkillEntry> entry : skills.entrySet()) {
			int skillid = entry.getKey().intValue();
			SkillEntry skill = entry.getValue();
			lew.writeInt(skillid);
			lew.writeInt(skill.getLevel());
			if (Skills.isFourthJob(skillid))
				lew.writeInt(skill.getMasterLevel());
		}
		Map<Integer, Cooldown> cooldowns = p.getCooldowns();
		lew.writeShort((short) cooldowns.size());
		for (Entry<Integer, Cooldown> cooling : cooldowns.entrySet()) {
			lew.writeInt(cooling.getKey().intValue());
			lew.writeShort(cooling.getValue().getSecondsRemaining());
		}

		Map<Short, QuestEntry> started = new HashMap<Short, QuestEntry>();
		Map<Short, QuestEntry> completed = new HashMap<Short, QuestEntry>();
		p.readLockQuests();
		try {
			for (Entry<Short, QuestEntry> entry : p.getAllQuests().entrySet()) {
				QuestEntry status = entry.getValue();
				switch (status.getState()) {
					case QuestEntry.STATE_NOT_STARTED:
						break;
					case QuestEntry.STATE_STARTED:
						started.put(entry.getKey(), status);
						break;
					case QuestEntry.STATE_COMPLETED:
						completed.put(entry.getKey(), status);
						break;
				}
			}
		} finally {
			p.readUnlockQuests();
		}
		lew.writeShort((short) started.size());
		for (Entry<Short, QuestEntry> startedQuest : started.entrySet()) {
			lew.writeShort(startedQuest.getKey().shortValue());
			lew.writeLengthPrefixedString(startedQuest.getValue().getData());
		}
		lew.writeShort((short) completed.size());
		for (Entry<Short, QuestEntry> completedQuest : completed.entrySet()) {
			lew.writeShort(completedQuest.getKey().shortValue());
			lew.writeLong(TimeTool.unixToWindowsTime(completedQuest.getValue().getCompletionTime()));
		}

		lew.writeShort((short) 0);
		lew.writeShort((short) (coupleRings.size()));
		for (Ring ring : coupleRings) {
			lew.writeInt(ring.getPartnerCharId());
			lew.writePaddedAsciiString(Player.getNameFromId(ring.getPartnerCharId()), 13);
			lew.writeLong(ring.getUniqueId());
			lew.writeLong(ring.getPartnerRingId());
		}
		lew.writeShort((short) friendshipRings.size());
		for (Ring ring : friendshipRings) {
			lew.writeInt(ring.getPartnerCharId());
			lew.writePaddedAsciiString(Player.getNameFromId(ring.getPartnerCharId()), 13);
			lew.writeLong(ring.getUniqueId());
			lew.writeLong(ring.getPartnerRingId());
			lew.writeInt(ring.getDataId());
		}
		lew.writeShort((short) 0); //possibly wedding ring

		for (int i = 0; i < 5; i++)
			lew.writeInt(ROCK_MAPS[i]);
		for (int i = 0; i < 10; i++)
			lew.writeInt(VIP_MAPS[i]);
		lew.writeInt(0);
	}

	public static byte[] writeServerMessage(byte type, String message, byte channel, boolean megaEar) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter((type == 3 ? 7 : type == 4 ? 6 : 5) + message.length());
		lew.writeShort(ClientSendOps.SERVER_MESSAGE);
		lew.writeByte(type);
		if (type == 4) {
			lew.writeBool(true);
		}
		lew.writeLengthPrefixedString(message);
		if (type == 3) {
			lew.writeByte((byte) (channel - 1));
			lew.writeBool(megaEar);
		}
		return lew.getBytes();
	}

	private CommonPackets() {
		//uninstantiable...
	}
}
