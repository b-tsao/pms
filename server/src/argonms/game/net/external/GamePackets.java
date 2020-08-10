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

package argonms.game.net.external;

import argonms.common.GlobalConstants;
import argonms.common.character.BuddyListEntry;
import argonms.common.character.PlayerStatusEffect;
import argonms.common.character.QuestEntry;
import argonms.common.character.Skills;
import argonms.common.character.inventory.Inventory.InventoryType;
import argonms.common.character.inventory.InventorySlot;
import argonms.common.character.inventory.InventorySlot.ItemType;
import argonms.common.character.inventory.InventoryTools;
import argonms.common.character.inventory.Pet;
import argonms.common.character.inventory.Ring;
import argonms.common.field.MonsterStatusEffect;
import argonms.common.loading.StatusEffectsData;
import argonms.common.net.external.ClientSendOps;
import argonms.common.net.external.CommonPackets;
import argonms.common.net.external.PacketSubHeaders;
import argonms.common.util.Rng;
import argonms.common.util.TimeTool;
import argonms.common.util.output.LittleEndianByteArrayWriter;
import argonms.common.util.output.LittleEndianWriter;
import argonms.game.character.BuddyList;
import argonms.game.character.Chatroom;
import argonms.game.character.ClientUpdateKey;
import argonms.game.character.GameCharacter;
import argonms.game.character.GuildList;
import argonms.game.character.PartyList;
import argonms.game.character.PlayerStatusEffectValues;
import argonms.game.character.StatusEffectTools;
import argonms.game.character.inventory.ItemTools;
import argonms.game.character.inventory.StorageInventory;
import argonms.game.field.MonsterStatusEffectValues;
import argonms.game.field.entity.ItemDrop;
import argonms.game.field.entity.Miniroom;
import argonms.game.field.entity.Miniroom.MiniroomType;
import argonms.game.field.entity.Mist;
import argonms.game.field.entity.Mob;
import argonms.game.field.entity.MysticDoor;
import argonms.game.field.entity.Npc;
import argonms.game.field.entity.PlayerNpc;
import argonms.game.field.entity.PlayerSkillSummon;
import argonms.game.field.entity.Reactor;
import argonms.game.field.movement.LifeMovementFragment;
import argonms.game.loading.shop.NpcShop;
import argonms.game.loading.shop.NpcShop.ShopSlot;
import argonms.game.net.external.handler.BuddyListHandler;
import argonms.game.net.external.handler.GuildListHandler;
import argonms.game.net.external.handler.PartyListHandler;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author GoldenKevin
 */
public final class GamePackets {
	private static final Map<ClientUpdateKey, Number> EMPTY_STATUPDATE = Collections.emptyMap();

	/**
	 * 
	 * @param stats THIS MAP MUST BE SORTED! Preferably pass an EnumMap.
	 * @param itemReaction
	 * @return
	 */
	public static byte[] writeUpdatePlayerStats(Map<ClientUpdateKey, ?> stats, boolean is) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.PLAYER_STAT_UPDATE);
		lew.writeBool(is);
		int updateMask = 0;
		for (ClientUpdateKey key : stats.keySet())
			updateMask |= key.intValue();
		lew.writeInt(updateMask);
		for (Entry<ClientUpdateKey, ?> statupdate : stats.entrySet()) {
			switch (statupdate.getKey()) {
				case LEVEL: //unsigned
					lew.writeByte((byte) ((Number) statupdate.getValue()).shortValue());
					break;
				case SKIN:
				case JOB:
				case STR:
				case DEX:
				case INT:
				case LUK:
				case HP:
				case MAXHP:
				case MP:
				case MAXMP:
				case AVAILABLEAP:
				case AVAILABLESP:
					lew.writeShort(((Number) statupdate.getValue()).shortValue());
					break;
				case FACE:
				case HAIR:
				case EXP:
				case FAME:
				case MESO:
					lew.writeInt(((Number) statupdate.getValue()).intValue());
					break;
				case PET: { //TODO: there's no reason why one update key should have three bits set. figure out what each bit means?
					Pet[] pets = (Pet[]) statupdate.getValue();
					for (int i = 0; i < 3; i++)
						lew.writeLong(pets[i] == null ? 0 : pets[i].getUniqueId());
					lew.writeByte((byte) 0);
					break;
				}
			}
		}
		return lew.getBytes();
	}

	public static byte[] writeEnableActions() {
		return writeUpdatePlayerStats(EMPTY_STATUPDATE, true);
	}

	public static byte[] writeUseSkill(Map<PlayerStatusEffect, Short> stats, int skillId, int duration, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.FIRST_PERSON_APPLY_STATUS_EFFECT);
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		for (Short statupdate : stats.values()) {
			lew.writeShort(statupdate.shortValue());
			lew.writeInt(skillId);
			lew.writeInt(duration);
		}
		lew.writeShort((short) 0);
		lew.writeShort(delay);
		lew.writeByte((byte) 0); //# of times skill was cast

		return lew.getBytes();
	}
	
	public static byte[] writeMountSkill(Map<PlayerStatusEffect, Short> stats, int skillId, int itemId, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.FIRST_PERSON_APPLY_STATUS_EFFECT);
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		for (Short statupdate : stats.values()) {
			lew.writeShort(statupdate.shortValue());
			lew.writeInt(itemId);
			lew.writeInt(skillId);
		}
		lew.writeInt(0);
		lew.writeShort(delay);
		lew.writeByte((byte) 0); //# of times skill was cast

		return lew.getBytes();
	}

	public static byte[] writeUsePirateSkill(Map<PlayerStatusEffect, Short> stats, int skillId, int duration, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.FIRST_PERSON_APPLY_STATUS_EFFECT);
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		lew.writeShort((short) 0);
		for (Entry<PlayerStatusEffect, Short> statupdate : stats.entrySet()) {
			lew.writeShort(statupdate.getValue().shortValue());
			lew.writeShort((short) 0);
			lew.writeInt(skillId);
			lew.writeInt(0);
			switch (statupdate.getKey()) {
				case DASH_SPEED:
				case DASH_JUMP:
					break;
				default:
					lew.writeByte((byte) 0);
					break;
			}
			lew.writeShort((short) duration);
		}
		lew.writeShort(delay);
		lew.writeByte((byte) 0); //# of times skill was cast

		return lew.getBytes();
	}

	public static byte[] writeGiveDebuff(Map<PlayerStatusEffect, Short> stats, short skillId, short skillLevel, int duration, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.FIRST_PERSON_APPLY_STATUS_EFFECT);
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		for (Short statupdate : stats.values()) {
			lew.writeShort(statupdate.shortValue());
			lew.writeShort(skillId);
			lew.writeShort(skillLevel);
			lew.writeInt(duration);
		}
		lew.writeShort((short) 0);
		lew.writeShort(delay);
		lew.writeByte((byte) 1); //# of times skill was cast

		return lew.getBytes();
	}

	public static byte[] writeUseItem(Map<PlayerStatusEffect, Short> stats, int itemId, int duration) {
		return writeUseSkill(stats, -itemId, duration, (short) 0);
	}

	public static byte[] writeCancelStatusEffect(Set<PlayerStatusEffect> stats) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(19);

		lew.writeShort(ClientSendOps.FIRST_PERSON_CANCEL_STATUS_EFFECT);
		long updateMask = 0;
		for (PlayerStatusEffect key : stats)
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		lew.writeByte((byte) 0);

		return lew.getBytes();
	}

	public static byte[] writeCooldown(int skill, short seconds) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8);
		lew.writeShort(ClientSendOps.COOLDOWN);
		lew.writeInt(skill);
		lew.writeShort(seconds);
		return lew.getBytes();
	}

	public static byte[] writeUpdateSkillLevel(int skillid, byte level, byte masterlevel) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(18);

		lew.writeShort(ClientSendOps.SKILL_ENTRY_UPDATE);
		lew.writeBool(true);
		lew.writeShort((short) 1);
		lew.writeInt(skillid);
		lew.writeInt(level);
		lew.writeInt(masterlevel);
		lew.writeBool(true);

		return lew.getBytes();
	}

	private static void writeRings(LittleEndianWriter lew, Map<Short, InventorySlot> equippedInv) {
		Ring coupleRing = null, friendshipRing = null, weddingRing = null;
		synchronized(equippedInv) {
			for (InventorySlot item : equippedInv.values())
				if (item.getType() == ItemType.RING)
					if (InventoryTools.isCoupleRing(item.getDataId()))
						coupleRing = (Ring) item;
					else if (InventoryTools.isFriendshipRing(item.getDataId()))
						friendshipRing = (Ring) item;
					else if (InventoryTools.isWeddingRing(item.getDataId()))
						weddingRing = (Ring) item;
		}
		if (coupleRing != null) {
			lew.writeByte((byte) 1);
			lew.writeLong(coupleRing.getUniqueId());
			lew.writeLong(coupleRing.getPartnerRingId());
			lew.writeInt(coupleRing.getDataId());
		} else {
			lew.writeByte((byte) 0);
		}
		if (friendshipRing != null) {
			lew.writeByte((byte) 1);
			lew.writeLong(friendshipRing.getUniqueId());
			lew.writeLong(friendshipRing.getPartnerRingId());
			lew.writeInt(friendshipRing.getDataId());
		} else {
			lew.writeByte((byte) 0);
		}
		lew.writeByte((byte) 0); //possibly wedding ring
	}

	public static byte[] writeUpdateAvatar(GameCharacter p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.UPDATE_AVATAR);
		lew.writeInt(p.getId());
		lew.writeBool(true);
		CommonPackets.writeAvatar(lew, p, false);
		writeRings(lew, p.getInventory(InventoryType.EQUIPPED).getAll());
		return lew.getBytes();
	}

	public static byte[] writeShowPetLevelUp(GameCharacter p, byte petSlot) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9);

		lew.writeShort(ClientSendOps.THIRD_PERSON_VISUAL_EFFECT);
		lew.writeInt(p.getId());
		lew.writeByte(StatusEffectTools.PET_LVL_UP);
		lew.writeByte((byte) 0);
		lew.writeByte(petSlot);

		return lew.getBytes();
	}

	public static byte[] writeBuffMapVisualEffect(GameCharacter p, byte effectType, int skillId, byte skillLevel, byte direction) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(direction != 3 ? 13 : 12);

		lew.writeShort(ClientSendOps.THIRD_PERSON_VISUAL_EFFECT);
		lew.writeInt(p.getId());
		lew.writeByte(effectType);
		lew.writeInt(skillId);
		if (skillLevel != -1)
			lew.writeByte(skillLevel);
		if (direction != -1)
			lew.writeByte(direction);

		return lew.getBytes();
	}

	private static byte[] writeShowThirdPersonEffect(GameCharacter p, byte effectId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);
		lew.writeShort(ClientSendOps.THIRD_PERSON_VISUAL_EFFECT);
		lew.writeInt(p.getId());
		lew.writeByte(effectId);
		return lew.getBytes();
	}

	public static byte[] writeShowLevelUp(GameCharacter p) {
		return writeShowThirdPersonEffect(p, StatusEffectTools.LEVEL_UP);
	}

	public static byte[] writeShowJobChange(GameCharacter p) {
		return writeShowThirdPersonEffect(p, StatusEffectTools.JOB_ADVANCEMENT);
	}

	public static byte[] writeShowQuestEffect(GameCharacter p) {
		return writeShowThirdPersonEffect(p, StatusEffectTools.QUEST);
	}

	public static byte[] writeBuffMapEffect(GameCharacter p, Map<PlayerStatusEffect, Short> stats) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.THIRD_PERSON_APPLY_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		for (Entry<PlayerStatusEffect, Short> statupdate : stats.entrySet()) {
			lew.writeShort(statupdate.getValue().shortValue());
			if (statupdate.getKey() == PlayerStatusEffect.MORPH)
				lew.writeByte((byte) 0);
		}
		lew.writeByte((byte) 0);
		lew.writeShort((short) 0);

		return lew.getBytes();
	}
	
	public static byte[] writeBuffMapMountEffect(GameCharacter p, Map<PlayerStatusEffect, Short> stats, int skillId, int itemId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.THIRD_PERSON_APPLY_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		for (Entry<PlayerStatusEffect, Short> statupdate : stats.entrySet()) {
			lew.writeShort(statupdate.getValue().shortValue());
			lew.writeInt(itemId);
			lew.writeInt(skillId);
		}
		lew.writeInt(0x2D4DFC2A);
		lew.writeShort((short) 0);

		return lew.getBytes();
	}

	public static byte[] writeBuffMapPirateEffect(GameCharacter p, Map<PlayerStatusEffect, Short> stats, int skillId, int duration) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.THIRD_PERSON_APPLY_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		lew.writeShort((short) 0);
		for (Entry<PlayerStatusEffect, Short> statupdate : stats.entrySet()) {
			lew.writeShort(statupdate.getValue().shortValue());
			lew.writeShort((short) 0);
			lew.writeInt(skillId);
			lew.writeInt(0);
			switch (statupdate.getKey()) {
				case DASH_SPEED:
				case DASH_JUMP:
					break;
				default:
					lew.writeByte((byte) 0);
					break;
			}
			lew.writeShort((short) duration);
		}
		lew.writeShort((short) 0);

		return lew.getBytes();
	}

	public static byte[] writeBuffMapChargeEffect(GameCharacter p, Map<PlayerStatusEffect, Short> stats, int skillId, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(30);

		lew.writeShort(ClientSendOps.THIRD_PERSON_APPLY_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		lew.writeInt(skillId);
		lew.writeShort((short) 0);
		lew.writeShort(delay);

		return lew.getBytes();
	}

	public static byte[] writeBuffMapShadowStarsEffect(GameCharacter p, Map<PlayerStatusEffect, Short> stats, int itemId, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(30);

		lew.writeShort(ClientSendOps.THIRD_PERSON_APPLY_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		lew.writeInt(itemId);
		lew.writeShort((short) 0);
		lew.writeShort(delay);

		return lew.getBytes();
	}

	public static byte[] writeDebuffMapEffect(GameCharacter p, Map<PlayerStatusEffect, Short> stats, short skillId, short skillLevel, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.THIRD_PERSON_APPLY_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats.keySet())
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);
		for (Entry<PlayerStatusEffect, Short> statupdate : stats.entrySet()) {
			if (statupdate.getKey() == PlayerStatusEffect.POISON)
				lew.writeShort(statupdate.getValue().shortValue());
			lew.writeShort(skillId);
			lew.writeShort(skillLevel);
		}
		lew.writeShort((short) 0);
		lew.writeShort(delay);

		return lew.getBytes();
	}

	public static byte[] writeCancelStatusEffectMapEffect(GameCharacter p, Set<PlayerStatusEffect> stats) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(22);

		lew.writeShort(ClientSendOps.THIRD_PERSON_CANCEL_STATUS_EFFECT);
		lew.writeInt(p.getId());
		long updateMask = 0;
		for (PlayerStatusEffect key : stats)
			updateMask |= key.longValue();
		lew.writeLong(0);
		lew.writeLong(updateMask);

		return lew.getBytes();
	}

	public static byte[] writeShowItemGain(int itemid, int quantity) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(20);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_INVENTORY);
		lew.writeByte((byte) 0);
		lew.writeInt(itemid);
		lew.writeInt(quantity);
		lew.writeInt(0);
		lew.writeInt(0);

		return lew.getBytes();
	}

	public static byte[] writeShowMesoGain(int gain) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_INVENTORY);
		lew.writeByte((byte) 1);
		lew.writeInt(gain);
		lew.writeShort((short) 0);

		return lew.getBytes();
	}

	private static byte[] writeShowInventoryStatus(byte mode) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(12);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_INVENTORY);
		lew.writeByte(mode);
		lew.writeInt(0);
		lew.writeInt(0);

		return lew.getBytes();
	}

	public static byte[] writeShowInventoryFull() {
		return writeShowInventoryStatus((byte) 0xFF);
	}

	public static byte[] writeShowInventoryUnavailable() {
		return writeShowInventoryStatus((byte) 0xFE);
	}

	public static byte[] writeQuestForfeit(short questId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(16);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_QUEST);
		lew.writeShort(questId);
		lew.writeByte(QuestEntry.STATE_NOT_STARTED);
		lew.writeShort((short) 0);
		lew.writeLong(0);

		return lew.getBytes();
	}

	public static byte[] writeQuestProgress(short questId, String data) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8 + data.length());

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_QUEST);
		lew.writeShort(questId);
		lew.writeByte(QuestEntry.STATE_STARTED);
		lew.writeLengthPrefixedString(data);

		return lew.getBytes();
	}

	public static byte[] writeQuestComplete(short questId, long completionTime) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(14);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_QUEST);
		lew.writeShort(questId);
		lew.writeByte(QuestEntry.STATE_COMPLETED);
		lew.writeLong(TimeTool.unixToWindowsTime(completionTime));

		return lew.getBytes();
	}

	public static byte[] writeItemExpired(int itemId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_EXPIRE);
		lew.writeInt(itemId);

		return lew.getBytes();
	}

	public static byte[] writeShowExpGain(int gain, boolean white, boolean fromQuest) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(!fromQuest ? 20 : 21);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(PacketSubHeaders.STATUS_INFO_EXP);
		lew.writeBool(white);
		lew.writeInt(gain);
		lew.writeBool(fromQuest);
		lew.writeByte((byte) 0);
		lew.writeShort((short) 0);
		lew.writeLong(0);
		if (fromQuest)
			lew.writeByte((byte) 0);

		return lew.getBytes();
	}

	/**
	 * 
	 * @param gain
	 * @param pointType must be one of the following values from
	 * PacketSubHeaders: STATUS_INFO_FAME, STATUS_INFO_MESOS,
	 * STATUS_INFO_GUILD_POINTS
	 * @return
	 */
	public static byte[] writeShowPointsGainFromQuest(int gain, byte pointType) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);

		lew.writeShort(ClientSendOps.SHOW_STATUS_INFO);
		lew.writeByte(pointType);
		lew.writeInt(gain);

		return lew.getBytes();
	}

	public static byte[] writeShowQuestReqsFulfilled(short questId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.SHOW_QUEST_COMPLETION);
		lew.writeShort(questId);

		return lew.getBytes();
	}

	public static byte[] writeShowItemGainFromQuest(int itemid, int quantity) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(12);

		lew.writeShort(ClientSendOps.FIRST_PERSON_VISUAL_EFFECT);
		lew.writeByte(StatusEffectTools.ITEM_GAIN);
		lew.writeByte((byte) 1); //Number of different items (itemid and amount gets repeated)
		lew.writeInt(itemid);
		lew.writeInt(quantity);

		return lew.getBytes();
	}

	public static byte[] writeShowPetLevelUp(byte petSlot) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(5);

		lew.writeShort(ClientSendOps.FIRST_PERSON_VISUAL_EFFECT);
		lew.writeByte(StatusEffectTools.PET_LVL_UP);
		lew.writeByte((byte) 0);
		lew.writeByte(petSlot);

		return lew.getBytes();
	}

	/**
	 *
	 * @param p
	 * @param effectType a const from StatusEffectTools.
	 * @param skillId
	 * @return
	 */
	public static byte[] writeSelfVisualEffect(byte effectType, int skillId, byte skillLevel, byte direction) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8);

		lew.writeShort(ClientSendOps.FIRST_PERSON_VISUAL_EFFECT);
		lew.writeByte(effectType);
		lew.writeInt(skillId);
		lew.writeByte(skillLevel);
		if (direction != -1)
			lew.writeByte(direction);

		return lew.getBytes();
	}

	public static byte[] writeSelfCharmEffect(short charmsLeft, short daysLeft) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8);

		lew.writeShort(ClientSendOps.FIRST_PERSON_VISUAL_EFFECT);
		lew.writeByte(StatusEffectTools.SAFETY_CHARM);
		lew.writeBool(true);
		lew.writeByte((byte) charmsLeft);
		lew.writeByte((byte) daysLeft);

		return lew.getBytes();
	}

	public static byte[] writeShowSelfQuestEffect() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(3);

		lew.writeShort(ClientSendOps.FIRST_PERSON_VISUAL_EFFECT);
		lew.writeByte(StatusEffectTools.QUEST);

		return lew.getBytes();
	}

	public static byte[] writeQuestStartSuccess(short questId, int npcId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11);

		lew.writeShort(ClientSendOps.QUEST_ACTION);
		lew.writeByte(QuestEntry.QUEST_ACTION_SUCCESS);
		lew.writeShort(questId);
		lew.writeInt(npcId);
		lew.writeShort((short) 0);

		return lew.getBytes();
	}

	public static byte[] writeQuestCompleteSuccess(short questId, int npcId, short nextQuest) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11);

		lew.writeShort(ClientSendOps.QUEST_ACTION);
		lew.writeByte(QuestEntry.QUEST_ACTION_SUCCESS);
		lew.writeShort(questId);
		lew.writeInt(npcId);
		lew.writeShort(nextQuest);

		return lew.getBytes();
	}

	public static byte[] writeQuestActionError(short questId, byte errorType) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9);

		lew.writeShort(ClientSendOps.QUEST_ACTION);
		lew.writeByte(errorType);
		lew.writeShort(questId);
		lew.writeInt(0);

		return lew.getBytes();
	}

	public static byte[] writeChangeMap(int mapid, byte spawnPoint, GameCharacter p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(26);

		lew.writeShort(ClientSendOps.CHANGE_MAP);
		lew.writeInt(p.getClient().getChannel() - 1);
		lew.writeShort((short) 2);
		lew.writeShort((short) 0);
		lew.writeInt(mapid);
		lew.writeByte(spawnPoint);
		lew.writeShort(p.getHp()); // hp (???)
		lew.writeBool(false);
		//long questMask = 0x1FFFFFFFFFFFFFFL;
		long questMask = TimeTool.unixToWindowsTime(System.currentTimeMillis());
		lew.writeLong(questMask);

		return lew.getBytes();
	}

	public static byte[] writeChangeMap(int mapid, Point position, GameCharacter p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(26);

		lew.writeShort(ClientSendOps.CHANGE_MAP);
		lew.writeInt(p.getClient().getChannel() - 1);
		lew.writeShort((short) 2);
		lew.writeShort((short) 0);
		lew.writeInt(mapid);
		lew.writeByte(MysticDoor.OUT_OF_TOWN_PORTAL_ID);
		lew.writeShort(p.getHp()); // hp (???)
		lew.writeBool(true);
		lew.writeInt(position.x);
		lew.writeInt(position.y);
		//long questMask = 0x1FFFFFFFFFFFFFFL;
		long questMask = TimeTool.unixToWindowsTime(System.currentTimeMillis());
		lew.writeLong(questMask);

		return lew.getBytes();
	}

	private static void writeMapEntryStatusEffectValue(LittleEndianWriter lew, PlayerStatusEffect key, PlayerStatusEffectValues v) {
		//perhaps it would be more concise if we didn't use a switch-case, and just use some conditionals if there are patterns.
		switch (key) {
			default: //give no value at all
				break;
			case SPEED:
				if (v.getSourceType() == StatusEffectsData.EffectSource.PLAYER_SKILL &&
						(v.getSource() == Skills.SIN_HASTE || v.getSource() == Skills.DIT_HASTE || v.getSource() == Skills.GM_HASTE || v.getSource() == Skills.SUPER_GM_HASTE))
					break;
				//fallthrough for non-haste sources
			case COMBO:
			case JUMP:
				lew.writeByte((byte) v.getModifier());
				break;
			// case HOMING_BEACON: //all non-debuff 5th byte keys
			case MORPH:
			// case RECOVERY:
			// case MAPLE_WARRIOR:
			// case POWER_STANCE:
			// case MANA_REFLECTION:
				lew.writeShort(v.getModifier());
				break;
			case SEDUCE: //all debuffs besides slow (glitch in global, SLOW doesn't display properly and if you try, it error 38s)
			case STUN:
			case SEAL:
			case DARKNESS:
			case WEAKNESS:
			case CURSE:
				lew.writeShort((short) v.getSource());
				lew.writeShort(v.getModifier());
				break;
			case CHARGE:
				lew.writeInt(v.getSource());
				break;
			case SHADOW_STARS:
				lew.writeInt(v.getModifier());
				break;
			case POISON:
				lew.writeShort(v.getModifier());
				lew.writeShort((short) v.getSource());
				lew.writeShort(v.getLevelWhenCast());
				break;
		}
	}

	private static void writeBuddyListEntry(LittleEndianWriter lew, BuddyListEntry entry) {
		lew.writeInt(entry.getId());
		lew.writePaddedAsciiString(entry.getName(), 13);
		lew.writeByte(entry.getStatus());
		switch (entry.getStatus()) {
			case BuddyListEntry.STATUS_HALF_OPEN:
				lew.writeShort((short) 0);
				lew.writeByte((byte) 0xF0);
				lew.writeByte((byte) 0xB2);
				break;
			case BuddyListEntry.STATUS_INVITED:
				lew.writeByte((byte) 0x1F);
				//intentional fallthrough to lew.writeInt(buddy.getChannel() - 1);
			case BuddyListEntry.STATUS_MUTUAL:
				//I believe that client recognizes -1 as offline (no channel)
				//and 20 as cash shop
				lew.writeInt(entry.getChannel() - 1);
				break;
		}
	}

	public static byte[] writeBuddyList(byte op, BuddyList bList) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.BUDDY_LIST);
		lew.writeByte(op);
		lew.writeByte((byte) bList.getBuddies().size());
		for (BuddyListEntry buddy : bList.getBuddies())
			writeBuddyListEntry(lew, buddy);
		for (int i = 0; i < bList.getCapacity(); i++)
			lew.writeInt(0);

		return lew.getBytes();
	}

	public static byte[] writeBuddyInvite(int from, String fromName) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(32 + fromName.length());

		lew.writeShort(ClientSendOps.BUDDY_LIST);
		lew.writeByte(BuddyListHandler.INVITE_RECEIVED);
		lew.writeInt(from);
		lew.writeLengthPrefixedString(fromName);

		writeBuddyListEntry(lew, new BuddyListEntry(from, fromName, BuddyListEntry.STATUS_INVITED));

		return lew.getBytes();
	}

	public static byte[] writeBuddyLoggedIn(BuddyListEntry entry) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(12);

		lew.writeShort(ClientSendOps.BUDDY_LIST);
		lew.writeByte(BuddyListHandler.BUDDY_LOGGED_IN);
		lew.writeInt(entry.getId());
		lew.writeByte((byte) 0);
		lew.writeInt(entry.getChannel() - 1);

		return lew.getBytes();
	}

	public static byte[] writeBuddyCapacityUpdate(short capacity) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.BUDDY_LIST);
		lew.writeByte(BuddyListHandler.CAPACITY_CHANGE);
		lew.writeByte((byte) capacity);

		return lew.getBytes();
	}

	public static byte[] writeSimpleBuddyListMessage(byte opCode) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.BUDDY_LIST);
		lew.writeByte(opCode);
		lew.writeByte((byte) 0);

		return lew.getBytes();
	}

	private static void writePartyList(PartyList party, LittleEndianWriter lew, boolean leaving) {
		party.lockRead();
		try {
			PartyList.Member[] partyMembers = party.getAllMembers();

			for (PartyList.Member member : partyMembers)
				lew.writeInt(member.getPlayerId());

			for (PartyList.Member member : partyMembers)
				lew.writePaddedAsciiString(member.getName(), 13);

			for (PartyList.Member member : partyMembers)
				lew.writeInt(member.getJob());

			for (PartyList.Member member : partyMembers)
				lew.writeInt(member.getLevel());

			for (PartyList.Member member : partyMembers)
				lew.writeInt(member.getChannel() - 1);

			lew.writeInt(party.getLeader());

			for (PartyList.Member member : partyMembers)
				lew.writeInt(member.getMapId());

			if (leaving) {
				lew.writeBytes(new byte[4 * 4 * 6]);
			} else {
				for (PartyList.Member member : partyMembers) {
					lew.writeInt(member.getDoorTown());
					lew.writeInt(member.getDoorTarget());
					Point pos = member.getDoorPosition();
					lew.writeInt(pos.x);
					lew.writeInt(pos.y);
				}
			}
		} finally {
			party.unlockRead();
		}
	}

	public static byte[] writePartyCreated(int partyId, MysticDoor leaderDoor) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(PartyListHandler.PARTY_CREATED);
		lew.writeInt(partyId);
		if (leaderDoor == null) {
			lew.writeInt(GlobalConstants.NULL_MAP);
			lew.writeInt(GlobalConstants.NULL_MAP);
			lew.writeInt(0);
		} else {
			if (!leaderDoor.isInTown())
				leaderDoor = leaderDoor.getComplement();
			lew.writeInt(leaderDoor.getMapId());
			lew.writeInt(leaderDoor.getComplement().getMapId());
			lew.writePos(leaderDoor.getComplement().getPosition());
		}

		return lew.getBytes();
	}

	public static byte[] writePartyDisbanded(int partyId, int leader) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(16);

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(PartyListHandler.LEFT_PARTY);
		lew.writeInt(partyId);
		lew.writeInt(leader);
		lew.writeBool(false);
		lew.writeInt(0);

		return lew.getBytes();
	}

	public static byte[] writePartyMemberLeft(PartyList party, int playerId, String playerName, boolean expelled) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(PartyListHandler.LEFT_PARTY);
		lew.writeInt(party.getId());
		lew.writeInt(playerId);
		lew.writeBool(true);
		lew.writeBool(expelled);
		lew.writeLengthPrefixedString(playerName);
		writePartyList(party, lew, false);

		return lew.getBytes();
	}

	public static byte[] writePartyMemberJoined(PartyList party, String playerName) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(PartyListHandler.JOINED_PARTY);
		lew.writeInt(party.getId());
		lew.writeLengthPrefixedString(playerName);
		writePartyList(party, lew, false);

		return lew.getBytes();
	}

	public static byte[] writePartyList(PartyList party) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(PartyListHandler.SILENT_LIST_UPDATE);
		lew.writeInt(party.getId());
		writePartyList(party, lew, false);

		return lew.getBytes();
	}

	public static byte[] writePartyChangeLeader(int newLeader, boolean partyQuestForcedChange) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(PartyListHandler.LEADER_CHANGED);
		lew.writeInt(newLeader);
		lew.writeBool(partyQuestForcedChange);

		return lew.getBytes();
	}

	public static byte[] writeSimplePartyListMessage(byte opCode) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(3);

		lew.writeShort(ClientSendOps.PARTY_LIST);
		lew.writeByte(opCode);

		return lew.getBytes();
	}

	public static byte[] writePartyMemberHpUpdate(int playerId, int hp, int maxHp) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(14);

		lew.writeShort(ClientSendOps.UPDATE_PARTY_MEMBER_HP);
		lew.writeInt(playerId);
		lew.writeInt(hp);
		lew.writeInt(maxHp);

		return lew.getBytes();
	}

	public static byte[] writeSimpleGuildListMessage(byte opCode) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(3);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(opCode);

		return lew.getBytes();
	}

	public static byte[] writeGuildContract(PartyList party, String guildName, String creatorName, boolean isCreator) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(isCreator ? 3 : (11 + creatorName.length() + guildName.length()));

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.GUILD_CONTRACT);
		if (!isCreator) {
			lew.writeInt(party.getId());
			lew.writeLengthPrefixedString(creatorName);
			lew.writeLengthPrefixedString(guildName);
		}

		return lew.getBytes();
	}

	private static void writeGuildListEntry(LittleEndianWriter lew, GuildList.Member member) {
		lew.writePaddedAsciiString(member.getName(), 13);
		lew.writeInt(member.getJob());
		lew.writeInt(member.getLevel());
		lew.writeInt(member.getRank());
		lew.writeInt(Math.min(member.getChannel(), 1));
		lew.writeInt(member.getSignature());
		lew.writeInt(member.getAllianceRank());
	}

	public static byte[] writeGuildList(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.LIST);
		lew.writeBool(true);
		lew.writeInt(guild.getId());
		lew.writeLengthPrefixedString(guild.getName());
		for (byte i = 0; i < 5; i++)
			lew.writeLengthPrefixedString(guild.getTitle(i));

		GuildList.Member[] members = guild.getAllMembers();
		lew.writeByte((byte) members.length);
		for (GuildList.Member member : members)
			lew.writeInt(member.getPlayerId());
		for (GuildList.Member member : members)
			writeGuildListEntry(lew, member);

		lew.writeInt(guild.getCapacity());
		lew.writeShort(guild.getEmblemBackground());
		lew.writeByte(guild.getEmblemBackgroundColor());
		lew.writeShort(guild.getEmblemDesign());
		lew.writeByte(guild.getEmblemDesignColor());
		lew.writeLengthPrefixedString(guild.getNotice());
		lew.writeInt(guild.getGp());
		lew.writeInt(guild.getAllianceId());

		return lew.getBytes();
	}

	public static byte[] writeGuildClear() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.LIST);
		lew.writeBool(false);

		return lew.getBytes();
	}

	public static byte[] writeGuildMemberJoined(GuildList guild, GuildList.Member member) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(48);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.JOINED_GUILD);
		lew.writeInt(guild.getId());
		lew.writeInt(member.getPlayerId());
		writeGuildListEntry(lew, member);

		return lew.getBytes();
	}

	public static byte[] writeGuildMemberLeft(GuildList guild, int playerId, String playerName, boolean expelled) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(13 + playerName.length());

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(expelled ? GuildListHandler.EXPELLED_FROM_GUILD : GuildListHandler.LEFT_GUILD);
		lew.writeInt(guild.getId());
		lew.writeInt(playerId);
		lew.writeLengthPrefixedString(playerName);

		return lew.getBytes();
	}

	public static byte[] writeGuildDisbanded(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.DISBANDED_GUILD);
		lew.writeInt(guild.getId());
		lew.writeBool(true);

		return lew.getBytes();
	}

	public static byte[] writeGuildCapacityChanged(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.CAPACITY_CHANGED);
		lew.writeInt(guild.getId());
		lew.writeByte(guild.getCapacity());

		return lew.getBytes();
	}

	public static byte[] writeGuildMemberLevelJobUpdate(GuildList guild, GuildList.Member member) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(19);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.LEVEL_JOB_CHANGED);
		lew.writeInt(guild.getId());
		lew.writeInt(member.getPlayerId());
		lew.writeInt(member.getLevel());
		lew.writeInt(member.getJob());

		return lew.getBytes();
	}

	public static byte[] writeGuildMemberLoggedIn(GuildList guild, GuildList.Member member) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(12);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.CHANNEL_CHANGE);
		lew.writeInt(guild.getId());
		lew.writeInt(member.getPlayerId());
		lew.writeByte((byte) Math.min(member.getChannel(), 1));

		return lew.getBytes();
	}

	public static byte[] writeGuildChangeRankTitles(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.RANK_TITLES_CHANGED);
		lew.writeInt(guild.getId());
		for (byte i = 0; i < 5; i++)
			lew.writeLengthPrefixedString(guild.getTitle(i));

		return lew.getBytes();
	}

	public static byte[] writeGuildMemberChangeRank(GuildList guild, GuildList.Member member) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(12);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.RANK_CHANGED);
		lew.writeInt(guild.getId());
		lew.writeInt(member.getPlayerId());
		lew.writeByte(member.getRank());

		return lew.getBytes();
	}

	public static byte[] writeGuildChangeEmblem(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(13);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.EMBLEM_CHANGED);
		lew.writeInt(guild.getId());
		lew.writeShort(guild.getEmblemBackground());
		lew.writeByte(guild.getEmblemBackgroundColor());
		lew.writeShort(guild.getEmblemDesign());
		lew.writeByte(guild.getEmblemDesignColor());

		return lew.getBytes();
	}

	public static byte[] writeGuildChangeNotice(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9 + guild.getNotice().length());

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.NOTICE_CHANGED);
		lew.writeInt(guild.getId());
		lew.writeLengthPrefixedString(guild.getNotice());

		return lew.getBytes();
	}

	public static byte[] writeGuildChangeGp(GuildList guild) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11);

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.GUILD_GP_CHANGED);
		lew.writeInt(guild.getId());
		lew.writeInt(guild.getGp());

		return lew.getBytes();
	}

	public static byte[] writeShowGuildRankings(int npcId, Collection<GuildList> guilds) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.GUILD_LIST);
		lew.writeByte(GuildListHandler.SHOW_GUILD_RANK_BOARD);
		lew.writeInt(npcId);
		lew.writeInt(guilds.size());
		for (GuildList guild : guilds) {
			lew.writeLengthPrefixedString(guild.getName());
			lew.writeInt(guild.getGp());
			lew.writeInt(guild.getEmblemDesign());
			lew.writeInt(guild.getEmblemDesignColor());
			lew.writeInt(guild.getEmblemBackground());
			lew.writeInt(guild.getEmblemBackgroundColor());
		}

		return lew.getBytes();
	}

	public static byte[] writeUpdateGuildName(GameCharacter p, String newName) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(8 + newName.length());

		lew.writeShort(ClientSendOps.UPDATE_GUILD_MEMBERSHIP);
		lew.writeInt(p.getId());
		lew.writeLengthPrefixedString(newName);

		return lew.getBytes();
	}

	public static byte[] writeUpdateGuildEmblem(GameCharacter p, GuildList g) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(12);

		lew.writeShort(ClientSendOps.UPDATE_GUILD_EMBLEM);
		lew.writeInt(p.getId());
		lew.writeShort(g.getEmblemBackground());
		lew.writeByte(g.getEmblemBackgroundColor());
		lew.writeShort(g.getEmblemDesign());
		lew.writeByte(g.getEmblemDesignColor());

		return lew.getBytes();
	}

	public static byte[] writeSpawnPortal(MysticDoor door) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(14);

		lew.writeShort(ClientSendOps.SPAWN_PORTAL);
		lew.writeInt(door.getMapId());
		lew.writeInt(door.getComplement().getMapId());
		lew.writePos(door.getComplement().getPosition());

		return lew.getBytes();
	}

	public static byte[] writeRemovePortal() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);

		lew.writeShort(ClientSendOps.SPAWN_PORTAL);
		lew.writeInt(GlobalConstants.NULL_MAP);
		lew.writeInt(GlobalConstants.NULL_MAP);

		return lew.getBytes();
	}

	public static byte[] writeShowPlayer(GameCharacter p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.SHOW_PLAYER);
		lew.writeInt(p.getId());
		lew.writeLengthPrefixedString(p.getName());

		GuildList guild = p.getGuild();
		if (guild != null) {
			lew.writeLengthPrefixedString(guild.getName());
			lew.writeShort(guild.getEmblemBackground());
			lew.writeByte(guild.getEmblemBackgroundColor());
			lew.writeShort(guild.getEmblemDesign());
			lew.writeByte(guild.getEmblemDesignColor());
		} else {
			lew.writeLengthPrefixedString("");
			lew.writeShort((short) 0);
			lew.writeByte((byte) 0);
			lew.writeShort((short) 0);
			lew.writeByte((byte) 0);
		}
		lew.writeInt(0);
		lew.writeInt(1);

		long updateMask = 0;
		Map<PlayerStatusEffect, PlayerStatusEffectValues> statusEffects = new TreeMap<PlayerStatusEffect, PlayerStatusEffectValues>(new Comparator<PlayerStatusEffect>() {
			//sort by value order (i.e. 5,6,7,8,1,2,3,4), then by mask (i.e. enum order)
			@Override
			public int compare(PlayerStatusEffect k1, PlayerStatusEffect k2) {
				int diff = k1.getValueOrder() - k2.getValueOrder();
				if (diff == 0) //if k1 and k2 share the same value order
					//sort by enum order (which should be smallest to biggest)
					diff = k1.compareTo(k2); //also equivalent to ((int) (k1.longValue() - k2.longValue()))
				return diff;
			}
		});
		statusEffects.putAll(p.getAllEffects());
		for (PlayerStatusEffect key : statusEffects.keySet())
			updateMask |= key.longValue();
		//no idea why we have to do it, but make the 4th byte (in a 64-bit little endian integer) = 0xF8
		lew.writeLong(updateMask & 0xFFFFFFFF00FFFFFFL | 0x00000000F8000000L);
		for (Entry<PlayerStatusEffect, PlayerStatusEffectValues> effect : statusEffects.entrySet())
			writeMapEntryStatusEffectValue(lew, effect.getKey(), effect.getValue());
		lew.writeInt(0);
		//write the 4th byte here
		//TODO: causes player with Dash on to look like he's using Energy Charge
		lew.writeByte((byte) ((updateMask & 0x00000000FF000000L) >> 24));
		lew.writeByte((byte) 0);
		lew.writeInt(0);

		int CHAR_MAGIC_SPAWN = Rng.getGenerator().nextInt();
		lew.writeInt(CHAR_MAGIC_SPAWN);
		lew.writeShort((short) 0);
		lew.writeLong(0);
		lew.writeInt(CHAR_MAGIC_SPAWN);
		lew.writeShort((short) 0);
		lew.writeLong(0);
		lew.writeInt(CHAR_MAGIC_SPAWN);
		lew.writeShort((short) 0);
		//TODO: should we only check if a player has an equipped
		//mount, and not if they have monster riding on?
		if (p.isEffectActive(PlayerStatusEffect.MONSTER_RIDING)) {
			/*TamingMob mount = p.getEquippedMount();
			if (mount != null) {
				lew.writeInt(mount.getDataId());
				lew.writeInt(mount.getSkillId());
				lew.writeInt(CHAR_MAGIC_SPAWN);
			} else {*/
				lew.writeInt(1932000);
				lew.writeInt(5221006);
				lew.writeInt(CHAR_MAGIC_SPAWN);
			//}
		} else {
			lew.writeInt(0);
			lew.writeInt(0);
			lew.writeInt(CHAR_MAGIC_SPAWN);
		}

		lew.writeLong(0);
		lew.writeInt(CHAR_MAGIC_SPAWN);
		lew.writeLong(0);
		lew.writeInt(0);
		lew.writeShort((short) 0);
		lew.writeInt(CHAR_MAGIC_SPAWN);
		lew.writeInt(0);
		lew.writeShort(p.getJob()); // 40 01?
		CommonPackets.writeAvatar(lew, p, false);
		lew.writeInt(0);
		lew.writeInt(p.getItemEffect());
		lew.writeInt(p.getItemChair());
		lew.writePos(p.getPosition());
		lew.writeByte(p.getStance());
		lew.writeShort(p.getFoothold());
		lew.writeByte((byte) 0);
		Pet[] pets = p.getPets();
		for (int i = 0; i < 3 && pets[i] != null; i++) {
			Pet pet = pets[i];
			lew.writeByte((byte) 1);
			lew.writeInt(pet.getDataId());
			lew.writeLengthPrefixedString(pet.getName());
			lew.writeLong(pet.getUniqueId());
			lew.writePos(pet.getPosition());
			lew.writeByte(pet.getStance());
			lew.writeInt(pet.getFoothold());
		}
		lew.writeByte((byte) 0);
		lew.writeShort((short) 1);
		lew.writeInt(0);
		lew.writeInt(0);
		lew.writeShort((short) 0);
		Miniroom room = p.getMiniRoom();
		if (room != null && room.isVisible() && room.positionOf(p) == 0)
			writeMiniroomBalloon(lew, room);
		else
			lew.writeByte(MiniroomType.NONE.byteValue());
		lew.writeByte((byte) 0);
		writeRings(lew, p.getInventory(InventoryType.EQUIPPED).getAll());
		lew.writeByte((byte) 0);
		return lew.getBytes();
	}

	public static byte[] writeRemovePlayer(GameCharacter p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(6);

		lew.writeShort(ClientSendOps.REMOVE_PLAYER);
		lew.writeInt(p.getId());

		return lew.getBytes();
	}

	public static byte[] writePlayerNpcLook(PlayerNpc pnpc) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(22);
		lew.writeShort(ClientSendOps.PLAYER_NPC);
		lew.writeBool(true);
		lew.writeInt(pnpc.getDataId());
		lew.writeLengthPrefixedString(pnpc.getPlayerName());
		CommonPackets.writeAvatar(lew, pnpc.getGender(), pnpc.getSkinColor(), pnpc.getEyes(),
				true, pnpc.getHair(), pnpc.getEquips(), new Pet[3]);
		return lew.getBytes();
	}

	public static byte[] writeShowPet(Pet pet, int owner, byte slot, boolean removeExisting, boolean hasLabelRing, boolean hasQuoteRing) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(32 + pet.getName().length());
		lew.writeShort(ClientSendOps.TOGGLE_PET);
		lew.writeInt(owner);
		lew.writeByte(slot);
		lew.writeBool(true);
		lew.writeBool(removeExisting);
		lew.writeInt(pet.getDataId());
		lew.writeLengthPrefixedString(pet.getName());
		lew.writeLong(pet.getUniqueId());
		lew.writePos(pet.getPosition());
		lew.writeByte(pet.getStance());
		lew.writeShort(pet.getFoothold());
		lew.writeBool(hasLabelRing);
		lew.writeBool(hasQuoteRing);
		return lew.getBytes();
	}

	public static byte[] writeRemovePet(int owner, byte slot, byte message) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9);
		lew.writeShort(ClientSendOps.TOGGLE_PET);
		lew.writeInt(owner);
		lew.writeByte(slot);
		lew.writeBool(false);
		lew.writeByte(message);
		return lew.getBytes();
	}

	public static byte[] writePetItemIgnore(GameCharacter p, byte slot, Pet pet, int[] ignore) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(16 + 4 * ignore.length);
		lew.writeShort(ClientSendOps.PET_ITEM_IGNORE);
		lew.writeInt(p.getId());
		lew.writeByte(slot);
		lew.writeLong(pet.getUniqueId());
		lew.writeByte((byte) ignore.length);
		for (int itemId : ignore)
			lew.writeInt(itemId);
		return lew.getBytes();
	}

	public static byte[] writePetFoodResponse(GameCharacter p, byte slot, boolean positive, boolean hasQuoteRing) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.PET_RESPONSE);
		lew.writeInt(p.getId());
		lew.writeByte(slot);
		lew.writeBool(true);
		lew.writeBool(positive);
		lew.writeBool(hasQuoteRing);
		return lew.getBytes();
	}

	public static byte[] writeShowSummon(PlayerSkillSummon summon, byte animation) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(25);

		lew.writeShort(ClientSendOps.SHOW_SUMMON);
		lew.writeInt(summon.getOwner());
		lew.writeInt(summon.getId());
		lew.writeInt(summon.getSkillId());
		lew.writeByte(summon.getSkillLevel());
		lew.writePos(summon.getPosition());
		lew.writeByte((byte) 3); //perhaps one of these are health? stance?
		lew.writeByte((byte) 0);
		lew.writeByte((byte) 0);
		lew.writeByte(summon.getSummonType());
		lew.writeBool(!summon.isPuppet());
		lew.writeByte(animation);

		return lew.getBytes();
	}

	public static byte[] writeRemoveSummon(PlayerSkillSummon summon, byte animation) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11);

		lew.writeShort(ClientSendOps.REMOVE_SUMMON);
		lew.writeInt(summon.getOwner());
		lew.writeInt(summon.getId());
		lew.writeByte(animation);

		return lew.getBytes();
	}

	private static void writeMonsterData(LittleEndianWriter lew, Mob monster, boolean newSpawn, byte effect) {
		lew.writeInt(monster.getId());
		lew.writeByte(monster.getControlStatus());
		lew.writeInt(monster.getDataId());

		Map<MonsterStatusEffect, MonsterStatusEffectValues> stats = monster.getAllEffects();
		int updateMask = 0;
		for (MonsterStatusEffect key : stats.keySet())
			updateMask |= key.intValue();
		lew.writeInt(updateMask);
		for (MonsterStatusEffectValues statupdate : stats.values()) {
			lew.writeShort(statupdate.getModifier());
			switch (statupdate.getSourceType()) {
				case MOB_SKILL:
					lew.writeShort((short) statupdate.getSource());
					lew.writeShort(statupdate.getLevelWhenCast());
					break;
				case PLAYER_SKILL:
					lew.writeInt(statupdate.getSource());
					break;
			}
			lew.writeShort((short) 0);
		}

		lew.writePos(monster.getPosition());
		lew.writeByte(monster.getStance());
		lew.writeShort((short) 0);
		lew.writeShort(monster.getFoothold());
		if (effect > 0) {
			lew.writeByte(effect);
			lew.writeByte((byte) 0);
			lew.writeShort((short) 0);
		}
		lew.writeShort((short) (newSpawn ? -2 : -1));
		lew.writeInt(0);
	}

	/**
	 * Spawn an monster without making the client animate the monster for itself.
	 * @param monster
	 * @param newSpawn
	 * @param effect
	 * @return
	 */
	public static byte[] writeShowMonster(Mob monster, boolean newSpawn, byte effect) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.SHOW_MONSTER);
		writeMonsterData(lew, monster, newSpawn, effect);
		return lew.getBytes();
	}

	/**
	 * This will only make the monster disappear if it was spawned by sending
	 * writeShowMonster.
	 * @param monster
	 * @param animation
	 * @return
	 */
	public static byte[] writeRemoveMonster(Mob m, byte animation) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);
		lew.writeShort(ClientSendOps.REMOVE_MONSTER);
		lew.writeInt(m.getId());
		lew.writeByte(animation);
		return lew.getBytes();
	}

	/**
	 * Spawn an monster and make the client animate it. The client will not be
	 * physically affected by it (i.e. it can't hit or be hurt by the monster)
	 * unless writeShowMonster is also sent.
	 * @param monster
	 * @param aggro
	 * @return
	 */
	public static byte[] writeShowAndControlMonster(Mob monster, boolean aggro) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.CONTROL_MONSTER);
		lew.writeByte((byte) (monster.isVisible() ? aggro ? 2 : 1 : 0));
		if (monster.isVisible())
			writeMonsterData(lew, monster, false, (byte) 0);
		else
			lew.writeInt(monster.getId());
		return lew.getBytes();
	}

	public static byte[] writeStopControlMonster(Mob monster) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);
		lew.writeShort(ClientSendOps.CONTROL_MONSTER);
		lew.writeByte((byte) 0);
		lew.writeInt(monster.getId());
		return lew.getBytes();
	}

	public static byte[] writeMonsterBuff(Mob monster, Map<MonsterStatusEffect, Short> stats, short skillId, short skillLevel, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);

		lew.writeShort(ClientSendOps.APPLY_MONSTER_STATUS_EFFECT);
		lew.writeInt(monster.getId());
		int updateMask = 0;
		for (MonsterStatusEffect key : stats.keySet())
			updateMask |= key.intValue();

		lew.writeInt(updateMask);
		for (Short statupdate : stats.values()) {
			lew.writeShort(statupdate.shortValue());
			lew.writeShort(skillId);
			lew.writeShort(skillLevel);
			lew.writeShort((short) 0);
		}
		lew.writeShort(delay); //delay in ms
		lew.writeBool(true);

		return lew.getBytes();
	}

	public static byte[] writeMonsterDebuff(Mob monster, Map<MonsterStatusEffect, Short> stats, int skillId, short delay) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);

		lew.writeShort(ClientSendOps.APPLY_MONSTER_STATUS_EFFECT);
		lew.writeInt(monster.getId());
		int updateMask = 0;
		for (MonsterStatusEffect key : stats.keySet())
			updateMask |= key.intValue();

		lew.writeInt(updateMask);
		for (Short statupdate : stats.values()) {
			lew.writeShort(statupdate.shortValue());
			lew.writeInt(skillId);
			lew.writeShort((short) 0);
		}
		lew.writeShort(delay); //delay in ms
		lew.writeBool(true);

		return lew.getBytes();
	}

	public static byte[] writeMonsterCancelStatusEffect(Mob monster, Set<MonsterStatusEffect> stats) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);

		lew.writeShort(ClientSendOps.CANCEL_MONSTER_STATUS_EFFECT);
		lew.writeInt(monster.getId());
		int updateMask = 0;
		for (MonsterStatusEffect key : stats)
			updateMask |= key.intValue();

		lew.writeInt(updateMask);
		lew.writeBool(true);

		return lew.getBytes();
	}

	private static void writeNpcData(LittleEndianWriter lew, Npc npc) {
		lew.writeInt(npc.getId());
		lew.writeInt(npc.getDataId());
		lew.writeShort((short) npc.getPosition().x);
		lew.writeShort(npc.getCy());
		lew.writeByte(npc.getStance());
		lew.writeShort(npc.getFoothold());
		lew.writeShort(npc.getRx0());
		lew.writeShort(npc.getRx1());
		lew.writeBool(true);
	}

	/**
	 * Spawn an NPC without making the client animate the NPC for itself.
	 * @param npc
	 * @return
	 */
	public static byte[] writeShowNpc(Npc npc) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(22);
		lew.writeShort(ClientSendOps.SHOW_NPC);
		writeNpcData(lew, npc);
		return lew.getBytes();
	}

	/**
	 * This will only make the NPC disappear if it was spawned by sending
	 * writeShowNpc.
	 * @param npc
	 * @return
	 */
	public static byte[] writeRemoveNpc(Npc npc) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(6);
		lew.writeShort(ClientSendOps.REMOVE_NPC);
		lew.writeInt(npc.getId());
		return lew.getBytes();
	}

	/**
	 * Spawn an NPC and make the client animate it.
	 * @param npc
	 * @return
	 */
	public static byte[] writeShowAndControlNpc(Npc npc) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(22);
		lew.writeShort(ClientSendOps.CONTROL_NPC);
		lew.writeByte((byte) 1);
		writeNpcData(lew, npc);
		return lew.getBytes();
	}

	/**
	 * This will only make the NPC disappear if it was spawned by sending
	 * writeShowAndControlNpc.
	 * @param npc
	 * @return
	 */
	public static byte[] writeStopControlAndRemoveNpc(Npc npc) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(22);
		lew.writeShort(ClientSendOps.CONTROL_NPC);
		lew.writeByte((byte) 0);
		lew.writeInt(npc.getId());
		return lew.getBytes();
	}

	public static byte[] writeNpcShop(GameCharacter customer, int npcId, NpcShop shop) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.NPC_SHOP);
		lew.writeInt(npcId);
		List<ShopSlot> items = shop.allItems();
		Map<Integer, Double> rechargeables = shop.nonBuyableRechargeables();
		lew.writeShort((short) (items.size() + rechargeables.size()));
		for (ShopSlot item : items) {
			lew.writeInt(item.itemId);
			lew.writeInt(item.price);
			if (!InventoryTools.isRechargeable(item.itemId))
				lew.writeShort(item.quantity);
			else
				lew.writeDouble(shop.rechargeCost(item.itemId));
			lew.writeShort(ItemTools.getPersonalSlotMax(customer, item.itemId));
		}
		//lets client know the max quantity of a stack of ammo, for recharging
		for (Map.Entry<Integer, Double> rechargeable : rechargeables.entrySet()) {
			int itemId = rechargeable.getKey().intValue();
			lew.writeInt(itemId);
			lew.writeInt(0);
			lew.writeDouble(rechargeable.getValue().doubleValue());
			lew.writeShort(ItemTools.getPersonalSlotMax(customer, itemId));
		}

		return lew.getBytes();
	}

	public static byte[] writeNpcStorage(int npcId, StorageInventory storage) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.NPC_STORAGE);
		lew.writeByte((byte) 0x15);
		lew.writeInt(npcId);
		lew.writeByte((byte) storage.getMaxSlots());
		lew.writeInt(storage.getBitfield(true, EnumSet.of(InventoryType.EQUIP, InventoryType.USE, InventoryType.SETUP, InventoryType.ETC, InventoryType.CASH), true));
		lew.writeInt(0);
		lew.writeInt(storage.getMesos());
		InventorySlot[] items = storage.getStartingItems();
		lew.writeShort((short) 0);
		lew.writeByte((byte) items.length);
		for (InventorySlot item : items)
			CommonPackets.writeItemInfo(lew, item, true, false);
		lew.writeShort((short) 0);
		lew.writeByte((byte) 0);

		return lew.getBytes();
	}

	public static byte[] writeShowItemDrop(ItemDrop drop, byte animation, byte pickupAllow) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.SHOW_ITEM_DROP);
		lew.writeByte(animation);
		lew.writeInt(drop.getId());
		lew.writeByte(drop.getDropType());
		lew.writeInt(drop.getDataId());
		lew.writeInt(drop.getOwner());
		lew.writeByte(pickupAllow);
		lew.writePos(drop.getPosition());
		lew.writeInt(drop.getMob()); //when this is 0, i.e. a player drop, anyone can pick it up, overriding pickupAllow. TODO: but then how do reactors work?
		if (animation != ItemDrop.SPAWN_ANIMATION_NONE) {
			lew.writePos(drop.getSourcePos());
			lew.writeShort((short) 0);
		}

		if (drop.getDropType() == ItemDrop.ITEM)
			CommonPackets.writeItemExpire(lew, drop.getItemExpire(), true);
		lew.writeBool(true); //allow pet item pickup?

		return lew.getBytes();
	}

	public static byte[] writeRemoveItemDrop(ItemDrop d, byte animation) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.REMOVE_ITEM_DROP);
		lew.writeByte(animation);
		lew.writeInt(d.getId());
		if (animation > ItemDrop.DESTROY_ANIMATION_NONE) {
			lew.writeInt(d.getOwner());
			if (animation == ItemDrop.DESTROY_ANIMATION_PET_LOOTED)
				lew.writeByte(d.getPetSlot());
		}

		return lew.getBytes();
	}

	public static byte[] writeShowMist(Mist mist) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(41);

		lew.writeShort(ClientSendOps.SHOW_MIST);
		lew.writeInt(mist.getId());
		lew.writeInt(mist.getMistType());
		lew.writeInt(mist.getOwner());
		lew.writeInt(mist.getSkillId());
		lew.writeByte(mist.getSkillLevel());
		lew.writeShort(mist.getSkillDelay());
		Rectangle box = mist.getBox();
		lew.writeInt(box.x);
		lew.writeInt(box.y);
		lew.writeInt(box.x + box.width);
		lew.writeInt(box.y + box.height);
		lew.writeInt(0);

		return lew.getBytes();
	}

	public static byte[] writeRemoveMist(Mist mist) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(6);

		lew.writeShort(ClientSendOps.REMOVE_MIST);
		lew.writeInt(mist.getId());

		return lew.getBytes();
	}

	public static byte[] writeShowMysticDoor(MysticDoor d, byte animation) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11);

		lew.writeShort(ClientSendOps.SHOW_DOOR);
		lew.writeByte(animation);
		lew.writeInt(d.getId());
		lew.writePos(d.getPosition());

		return lew.getBytes();
	}

	public static byte[] writeRemoveMysticDoor(MysticDoor d, byte animation) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);

		lew.writeShort(ClientSendOps.REMOVE_DOOR);
		lew.writeByte(animation);
		lew.writeInt(d.getId());

		return lew.getBytes();
	}

	public static byte[] writeTriggerReactor(Reactor reactor) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(15);

		lew.writeShort(ClientSendOps.HIT_REACTOR);
		lew.writeInt(reactor.getId());
		lew.writeByte(reactor.getStateId());
		lew.writePos(reactor.getPosition());
		lew.writeShort(reactor.getStance());
		lew.writeBool(false);
		lew.writeByte((byte) 5); // frame delay, set to 5 since there doesn't appear to be a fixed formula for it

		return lew.getBytes();
	}

	public static byte[] writeShowReactor(Reactor reactor) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(16);

		lew.writeShort(ClientSendOps.SHOW_REACTOR);
		lew.writeInt(reactor.getId());
		lew.writeInt(reactor.getDataId());
		lew.writeByte(reactor.getStateId());
		lew.writePos(reactor.getPosition());
		lew.writeBool(false);

		return lew.getBytes();
	}

	public static byte[] writeRemoveReactor(Reactor reactor) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11);

		lew.writeShort(ClientSendOps.REMOVE_REACTOR);
		lew.writeInt(reactor.getId());
		lew.writeByte(reactor.getStateId());
		lew.writePos(reactor.getPosition());

		return lew.getBytes();
	}

	public static void writeSerializedMovements(LittleEndianByteArrayWriter lew, List<LifeMovementFragment> moves) {
		lew.writeByte((byte) moves.size());
		for (LifeMovementFragment move : moves)
			move.serialize(lew);
	}

	/**
	 * Error message to the client if a user cannot warp to another area.
	 * @param type Message to be sent. Possible values :<br>
	 *             0x01 (1) - You cannot move that channel. Please try again later.
	 *             0x02 (2) - You cannot go into the cash shop. Please try again later.
	 *             0x03 (3) - The Item-Trading shop is currently unavailable, please try again later.
	 *             0x04 (4) - You cannot go into the trade shop, due to the limitation of user count.
	 *             0x05 (5) - You do not meet the minimum level requirement to access the Trade Shop.
	 * @return 
	 */
	public static byte[] writeServerMigrateFailed(byte msg) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter ();
		lew.writeShort(ClientSendOps.BLOCK_MIGRATE);
		lew.writeByte(msg);
		return lew.getBytes();
	}

	public static byte[] writePrivateChatMessage(byte type, String name, String message) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7
				+ name.length() + message.length());

		lew.writeShort(ClientSendOps.PRIVATE_CHAT);
		lew.writeByte(type);
		lew.writeLengthPrefixedString(name);
		lew.writeLengthPrefixedString(message);

		return lew.getBytes();
	}

	public static byte[] writeWhisperMessage(String name, String message, byte srcCh) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9
				+ name.length() + message.length());

		lew.writeShort(ClientSendOps.WHISPER);
		lew.writeByte((byte) 0x12);
		lew.writeLengthPrefixedString(name);
		lew.writeShort((short) (srcCh - 1));
		lew.writeLengthPrefixedString(message);

		return lew.getBytes();
	}

	public static byte[] writeSpouseChatMessage(String name, String message) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(6
				+ name.length() + message.length());

		lew.writeShort(ClientSendOps.SPOUSE_CHAT);
		lew.writeLengthPrefixedString(name);
		lew.writeLengthPrefixedString(message);

		return lew.getBytes();
	}

	public static byte[] writeWhisperMessge(String name, String message, byte channel) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(9 + message.length());
		lew.writeShort(ClientSendOps.WHISPER);
		lew.writeByte((byte) 0x12); //???
		lew.writeLengthPrefixedString(name);
		lew.writeShort((short) (channel - 1));
		lew.writeLengthPrefixedString(message);
		return lew.getBytes();
	}

	public static byte[] writeTipMessage(String message) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(5 + message.length());
		lew.writeShort(ClientSendOps.TIP_MESSAGE);
		lew.writeByte((byte) 0xFF);
		lew.writeLengthPrefixedString(message);
		return lew.getBytes();
	}

	public static byte[] writeMapEffect(byte type, String name) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(5 + name.length());
		lew.writeShort(ClientSendOps.MAP_EFFECT);
		lew.writeByte(type);
		lew.writeLengthPrefixedString(name);
		return lew.getBytes();
	}

	public static byte[] writeClock() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);
		lew.writeShort(ClientSendOps.CLOCK);
		lew.writeByte((byte) 1);

		Calendar now = TimeTool.currentDateTime();
		lew.writeByte((byte) now.get(Calendar.HOUR_OF_DAY));
		lew.writeByte((byte) now.get(Calendar.MINUTE));
		lew.writeByte((byte) now.get(Calendar.SECOND));

		return lew.getBytes();
	}

	public static byte[] writeTimer(int seconds) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(7);
		lew.writeShort(ClientSendOps.CLOCK);
		lew.writeByte((byte) 2);
		lew.writeInt(seconds);
		return lew.getBytes();
	}

	public static byte[] writeShipEffect(short effect) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);
		lew.writeShort(ClientSendOps.SHIP);
		lew.writeShort(effect);
		return lew.getBytes();
	}

	public static byte[] writeTradeInvite(String inviter, int tradeEntId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10 + inviter.length());
		lew.writeShort(ClientSendOps.MINIROOM_ACT);
		lew.writeByte(Miniroom.ACT_INVITE);
		lew.writeByte((byte) 3);
		lew.writeLengthPrefixedString(inviter);
		lew.writeInt(tradeEntId);
		return lew.getBytes();
	}

	public static byte[] writeInviteFail(byte code, String name) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(6 + name.length());
		lew.writeShort(ClientSendOps.MINIROOM_ACT);
		lew.writeByte(Miniroom.ACT_DECLINE);
		lew.writeByte(code);
		lew.writeLengthPrefixedString(name);
		return lew.getBytes();
	}

	public static void writeMiniroomBalloon(LittleEndianWriter lew, Miniroom room) {
		lew.writeByte(room.getMiniroomType().byteValue());
		lew.writeInt(room.getId());
		lew.writeLengthPrefixedString(room.getMessage());
		if (room.getMiniroomType() != MiniroomType.HIRED_MERCHANT)
			lew.writeBool(room.getPassword() != null);
		lew.writeByte(room.getStyle());
		lew.writeByte(room.getAmountOfPlayers());
		lew.writeByte(room.getMaxPlayers());
		if (room.getMiniroomType() != MiniroomType.HIRED_MERCHANT)
			lew.writeBool(room.gameInProgress());
	}

	public static byte[] writeChatroomAvatar(byte op, byte position, Chatroom.Avatar avatar, boolean entered) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
		lew.writeShort(ClientSendOps.MESSENGER_ACT);
		lew.writeByte(Chatroom.ACT_OPEN);
		lew.writeByte(position);
		CommonPackets.writeAvatar(lew, avatar.getGender(), avatar.getSkin(), avatar.getEyes(), true, avatar.getHair(), avatar.getEquips(), new Pet[3]);
		lew.writeLengthPrefixedString(avatar.getName());
		lew.writeByte((byte) (avatar.getChannel() - 1));
		lew.writeBool(entered);
		return lew.getBytes();
	}

	public static byte[] writeChatroomJoin(byte position) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);
		lew.writeShort(ClientSendOps.MESSENGER_ACT);
		lew.writeByte(Chatroom.ACT_JOIN);
		lew.writeByte(position);
		return lew.getBytes();
	}

	public static byte[] writeChatroomClear(byte position) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);
		lew.writeShort(ClientSendOps.MESSENGER_ACT);
		lew.writeByte(Chatroom.ACT_EXIT);
		lew.writeByte(position);
		return lew.getBytes();
	}

	public static byte[] writeChatroomInvite(String from, int roomId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(11 + from.length());

		lew.writeShort(ClientSendOps.MESSENGER_ACT);
		lew.writeByte(Chatroom.ACT_INVITE);
		lew.writeLengthPrefixedString(from);
		lew.writeByte((byte) 0);
		lew.writeInt(roomId);
		lew.writeByte((byte) 0);

		return lew.getBytes();
	}

	public static byte[] writeChatroomInviteResponse(byte op, String invitee, boolean joined) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(6 + invitee.length());
		lew.writeShort(ClientSendOps.MESSENGER_ACT);
		lew.writeByte(op);
		lew.writeLengthPrefixedString(invitee);
		lew.writeBool(joined);
		return lew.getBytes();
	}

	public static byte[] writeChatroomText(String text) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(5 + text.length());
		lew.writeShort(ClientSendOps.MESSENGER_ACT);
		lew.writeByte(Chatroom.ACT_CHAT);
		lew.writeLengthPrefixedString(text);
		return lew.getBytes();
	}

	/*public static byte[] writeEnterCs(Player p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.CS_OPEN);
		writeCharData(lew, p);
		lew.writeByte((byte) 1);
		lew.writeLengthPrefixedString(p.getClient().getAccountName());
		lew.writeInt(0);
		lew.writeShort((short) 322); //amount of items in this packet.
		for (int sn = 10101048; sn <= 10101197; sn++) { //150 consecutive items with modifier 512
			lew.writeInt(sn);
			lew.writeShort(0x200);
			lew.write(1);
		}
		//170 more items that I can't see a clear pattern through.
		lew.writeBytes(getAdditionalCashItems());
		//No idea what this is, definitely not an sn.
		lew.writeBytes(HexTool.getByteArrayFromHexString("00 08 00 00 00 37 00 31 00 38 00 31 00 00 00 00 00 18 00 0E 00 0F 00 0C 06 38 02 14 00 08 80 B6 03 67 00 69 00 6E 00 49 00 70 00 00 00 00 00 00 00 06 00 04 00 13 00 0E 06 A8 01 14 00 D8 9F CD 03 33 00 2E 00 33 00 31 00 2E 00 32 00 33 00 35 00 2E 00 32 00 32 00 34 00 00 00 00 00 00 00 00 00 04 00 0A 00 15 01 0C 06 0E 00 00 00 62 00 65 00 67 00 69 00 6E 00 49 00 01 00 00 00 00 00 00 00 C5 FD FD 02 01 00 00 00 00 00 00 00 05 FE FD 02 01 00 00 00 00 00 00 00 13 FE FD 02 01 00 00 00 00 00 00 00 22 4A CB 01 01 00 00 00 00 00 00 00 C2 FD FD 02 01 00 00 00 01 00 00 00 C5 FD FD 02 01 00 00 00 01 00 00 00 05 FE FD 02 01 00 00 00 01 00 00 00 13 FE FD 02 01 00 00 00 01 00 00 00 22 4A CB 01 01 00 00 00 01 00 00 00 C2 FD FD 02 02 00 00 00 00 00 00 00 C5 FD FD 02 02 00 00 00 00 00 00 00 05 FE FD 02 02 00 00 00 00 00 00 00 13 FE FD 02 02 00 00 00 00 00 00 00 22 4A CB 01 02 00 00 00 00 00 00 00 C2 FD FD 02 02 00 00 00 01 00 00 00 C5 FD FD 02 02 00 00 00 01 00 00 00 05 FE FD 02 02 00 00 00 01 00 00 00 13 FE FD 02 02 00 00 00 01 00 00 00 22 4A CB 01 02 00 00 00 01 00 00 00 C2 FD FD 02 03 00 00 00 00 00 00 00 C5 FD FD 02 03 00 00 00 00 00 00 00 05 FE FD 02 03 00 00 00 00 00 00 00 13 FE FD 02 03 00 00 00 00 00 00 00 22 4A CB 01 03 00 00 00 00 00 00 00 C2 FD FD 02 03 00 00 00 01 00 00 00 C5 FD FD 02 03 00 00 00 01 00 00 00 05 FE FD 02 03 00 00 00 01 00 00 00 13 FE FD 02 03 00 00 00 01 00 00 00 22 4A CB 01 03 00 00 00 01 00 00 00 C2 FD FD 02 04 00 00 00 00 00 00 00 C5 FD FD 02 04 00 00 00 00 00 00 00 05 FE FD 02 04 00 00 00 00 00 00 00 13 FE FD 02 04 00 00 00 00 00 00 00 22 4A CB 01 04 00 00 00 00 00 00 00 C2 FD FD 02 04 00 00 00 01 00 00 00 C5 FD FD 02 04 00 00 00 01 00 00 00 05 FE FD 02 04 00 00 00 01 00 00 00 13 FE FD 02 04 00 00 00 01 00 00 00 22 4A CB 01 04 00 00 00 01 00 00 00 C2 FD FD 02 05 00 00 00 00 00 00 00 C5 FD FD 02 05 00 00 00 00 00 00 00 05 FE FD 02 05 00 00 00 00 00 00 00 13 FE FD 02 05 00 00 00 00 00 00 00 22 4A CB 01 05 00 00 00 00 00 00 00 C2 FD FD 02 05 00 00 00 01 00 00 00 C5 FD FD 02 05 00 00 00 01 00 00 00 05 FE FD 02 05 00 00 00 01 00 00 00 13 FE FD 02 05 00 00 00 01 00 00 00 22 4A CB 01 05 00 00 00 01 00 00 00 C2 FD FD 02 06 00 00 00 00 00 00 00 C5 FD FD 02 06 00 00 00 00 00 00 00 05 FE FD 02 06 00 00 00 00 00 00 00 13 FE FD 02 06 00 00 00 00 00 00 00 22 4A CB 01 06 00 00 00 00 00 00 00 C2 FD FD 02 06 00 00 00 01 00 00 00 C5 FD FD 02 06 00 00 00 01 00 00 00 05 FE FD 02 06 00 00 00 01 00 00 00 13 FE FD 02 06 00 00 00 01 00 00 00 22 4A CB 01 06 00 00 00 01 00 00 00 C2 FD FD 02 07 00 00 00 00 00 00 00 C5 FD FD 02 07 00 00 00 00 00 00 00 05 FE FD 02 07 00 00 00 00 00 00 00 13 FE FD 02 07 00 00 00 00 00 00 00 22 4A CB 01 07 00 00 00 00 00 00 00 C2 FD FD 02 07 00 00 00 01 00 00 00 C5 FD FD 02 07 00 00 00 01 00 00 00 05 FE FD 02 07 00 00 00 01 00 00 00 13 FE FD 02 07 00 00 00 01 00 00 00 22 4A CB 01 07 00 00 00 01 00 00 00 C2 FD FD 02 08 00 00 00 00 00 00 00 C5 FD FD 02 08 00 00 00 00 00 00 00 05 FE FD 02 08 00 00 00 00 00 00 00 13 FE FD 02 08 00 00 00 00 00 00 00 22 4A CB 01 08 00 00 00 00 00 00 00 C2 FD FD 02 08 00 00 00 01 00 00 00 C5 FD FD 02 08 00 00 00 01 00 00 00 05 FE FD 02 08 00 00 00 01 00 00 00 13 FE FD 02 08 00 00 00 01 00 00 00 22 4A CB 01 08 00 00 00 01 00 00 00 C2 FD FD 02 00 00 A3 00 26 71 0F 00 F3 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 23 00 00 00 FF FF FF FF 0F 00 00 00 BD 68 32 01 BD 68 32 01 10 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 54 71 0F 00 F4 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 BD 68 32 01 BD 68 32 01 10 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 58 71 0F 00 F5 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 32 00 00 00 FF FF FF FF 0F 00 00 00 BD 68 32 01 BD 68 32 01 10 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 B5 E6 0F 00 F8 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 FF FF FF FF 0F 00 00 00 BD 68 32 01 BD 68 32 01 10 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 FD 4A 0F 00 FC 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 14 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 29 71 0F 00 FD 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 23 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 E5 DE 0F 00 FF 20 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 0B D1 10 00 00 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 23 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 1C F9 19 00 01 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 49 4B 4C 00 02 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 63 72 4C 00 03 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 76 72 4C 00 04 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 32 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 77 72 4C 00 05 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 FF FF FF FF 0F 00 00 00 BE 68 32 01 BE 68 32 01 0B 00 00 00 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 FC 4A 0F 00 06 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 BF 68 32 01 BF 68 32 01 0B 00 00 00 0C 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F6 4B 0F 00 07 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 FF FF FF FF 0F 00 00 00 BF 68 32 01 BF 68 32 01 0B 00 00 00 0C 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 20 4E 00 3A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 00 00 00 00 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 79 72 4C 00 3B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 42 BC 4E 00 3C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 0A 31 10 00 3D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 54 72 4C 00 3E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 69 0F 00 3F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 78 4B 0F 00 40 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 A8 F8 19 00 41 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 09 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 21 A6 1B 00 42 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B4 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 84 5C 10 00 43 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 98 34 10 00 44 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 98 0F 00 45 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 75 83 10 00 46 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 18 0A 10 00 47 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 0B 31 10 00 48 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 61 72 4C 00 49 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 4B BC 4E 00 4A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 39 70 4D 00 4B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 10 47 4E 00 4C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 C8 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B3 E6 0F 00 4D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 73 34 10 00 4E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5C 98 0F 00 4F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 DF 82 10 00 50 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 57 4B 4C 00 51 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B8 34 10 00 52 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 48 94 0F 00 53 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 A9 7E 10 00 54 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 96 0D 10 00 55 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 3C 95 4E 00 56 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 E9 F8 19 00 57 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 1C D1 10 00 58 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 3F 71 0F 00 59 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 F5 4B 0F 00 5A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B9 82 10 00 5B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B7 D0 10 00 5C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 98 0F 00 5D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 51 4B 4C 00 5E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 0F 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 C0 DE 0F 00 5F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 10 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 E3 4E 00 60 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B4 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 D0 27 4E 00 61 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 E8 94 50 00 62 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 26 71 0F 00 63 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B1 3E 52 00 64 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 D6 D0 10 00 65 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 6B 71 0F 00 66 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 71 4B 0F 00 67 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 8B F8 10 00 68 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 A9 F8 19 00 69 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 3D 6A 32 01 3D 6A 32 01 12 00 00 00 14 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 89 83 4F 00 6A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 BE 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 C5 F7 10 00 6B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5F 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 02 20 4E 00 6C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 F0 F8 4D 00 6D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 43 BC 4E 00 6E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 10 31 10 00 6F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 71 72 4C 00 70 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 71 0F 00 71 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 A9 57 10 00 72 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 08 20 4E 00 73 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 08 00 00 00 0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 4B 4C 00 74 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 6A 5C 10 00 75 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 AB 34 10 00 76 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 E9 94 50 00 77 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 47 BC 4E 00 78 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 CA 2C 10 00 79 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 60 72 4C 00 7A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 4A BC 4E 00 7B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 38 70 4D 00 7C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 7A C0 4C 00 7D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0B 00 00 00 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B0 CD 4F 00 7E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 AA 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 D1 E6 0F 00 7F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 CB 34 10 00 80 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 40 98 0F 00 81 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 32 83 10 00 82 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 90 0D 10 00 83 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 C3 2C 10 00 84 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 42 98 0F 00 85 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 FD 09 10 00 86 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B5 DE 0F 00 87 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0D 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 24 A6 1B 00 88 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 B4 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 62 E6 0F 00 89 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 EF D0 10 00 8A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5B 98 0F 00 8B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 CA 4A 0F 00 8C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 DC D0 10 00 8D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 4E 98 0F 00 8E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 3C 46 0F 00 8F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 4A 4B 4C 00 90 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 9D E6 0F 00 91 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 0F 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 4B 4B 4C 00 92 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 D3 F8 19 00 93 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 F2 D0 10 00 94 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 3C 71 0F 00 95 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 67 4B 0F 00 96 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 EE D0 10 00 97 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 58 71 0F 00 98 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 45 4C 0F 00 99 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 8E F8 10 00 9A 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 2D 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 95 F8 19 00 9B 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 E9 69 32 01 E9 69 32 01 11 00 00 00 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 3A 95 4E 00 9C 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 09 20 4E 00 9D 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 46 BC 4E 00 9E 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 50 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F 0A 10 00 9F 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 31 10 00 A0 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7A 72 4C 00 A1 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7A 71 0F 00 A2 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3D 70 4D 00 A3 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 77 5C 10 00 A4 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F9 23 4E 00 A5 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 08 00 00 00 0A 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 10 47 4E 00 A6 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 BE 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 19 5C 10 00 A7 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 7F 34 10 00 A8 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 34 98 0F 00 A9 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 11 31 10 00 AA 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 57 98 0F 00 AB 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 45 BC 4E 00 AC 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF 09 10 00 AD 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 AD DE 0F 00 AE 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 70 4B 0F 00 AF 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0B 00 00 00 0D 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 64 4D 00 B0 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 F0 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 AF E6 0F 00 B1 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 98 34 10 00 B2 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 31 98 0F 00 B3 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 77 83 10 00 B4 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1B D1 10 00 B5 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 58 98 0F 00 B6 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DE 82 10 00 B7 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FE 09 10 00 B8 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 8A E6 0F 00 B9 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0D 00 00 00 0F 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 A6 1B 00 BA 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 BE 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 24 F9 19 00 BB 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 D7 D0 10 00 BC 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 57 71 0F 00 BD 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 EC 4A 0F 00 BE 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E3 D0 10 00 BF 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 64 98 0F 00 C0 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F3 4A 0F 00 C1 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 A9 F8 10 00 C2 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 8D E6 0F 00 C3 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 0F 00 00 00 11 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 89 83 4F 00 C4 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 8C 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 28 F9 19 00 C5 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 D1 10 00 C6 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 58 BF 0F 00 C7 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 55 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 B0 3E 52 00 C8 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 46 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E2 D0 10 00 C9 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4F 71 0F 00 CA 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 28 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 77 4B 0F 00 CB 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2A F8 10 00 CC 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 37 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 13 F9 19 00 CD 21 9A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 19 00 00 00 FF FF FF FF 0F 00 00 00 EA 69 32 01 EA 69 32 01 11 00 00 00 13 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 27 00 00 00"));

		return lew.getBytes();
	}

	public static byte[] writeEnterMts(Player p) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();

		lew.writeShort(ClientSendOps.MTS_OPEN);
		writeCharData(lew, p);
		lew.writeLengthPrefixedString(p.getClient().getAccountName());
		lew.writeInt(5000);
		lew.write(HexTool.getByteArrayFromHexString("0A 00 00 00 64 00 00 00 18 00 00 00 A8 00 00 00 B0 ED 4E 3C FD 68 C9 01"));

		return lew.getBytes();
	}*/

	public static byte[] writeShowHide() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.GM);
		lew.writeByte(PacketSubHeaders.GM_HIDE);
		lew.writeBool(true);

		return lew.getBytes();
	}

	public static byte[] writeStopHide() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(4);

		lew.writeShort(ClientSendOps.GM);
		lew.writeByte(PacketSubHeaders.GM_HIDE);
		lew.writeBool(false);

		return lew.getBytes();
	}

	private GamePackets() {
		//uninstantiable...
	}
}
