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

package argonms.game.net.external.handler;

import argonms.common.character.Skills;
import argonms.common.character.inventory.Inventory;
import argonms.common.character.inventory.Inventory.InventoryType;
import argonms.common.loading.item.ItemDataLoader;
import argonms.common.loading.item.ItemEffectsData;
import argonms.common.net.external.CheatTracker;
import argonms.common.net.external.ClientSendOps;
import argonms.common.util.input.LittleEndianReader;
import argonms.common.util.output.LittleEndianByteArrayWriter;
import argonms.game.character.GameCharacter;
import argonms.game.net.external.GameClient;
import argonms.game.net.external.GamePackets;

/**
 *
 * @author GoldenKevin
 */
public final class PlayerMiscHandler {
	private static final int
		BINDING_CHANGE_KEY_MAPPING = 0,
		BINDING_CHANGE_AUTO_HP_POT = 1,
		BINDING_CHANGE_AUTO_MP_POT = 2
	;

	public static void handleChair(LittleEndianReader packet, GameClient gc) {
		short chairId = packet.readShort();
		GameCharacter p = gc.getPlayer();
		if (chairId == -1) {
			//cancel chair (item or map bench)
			if (p.getItemChair() != 0) {
				p.setItemChair(0);
				p.getMap().sendToAll(writeShowChair(gc.getPlayer().getId(), 0));
			} else {
				p.setMapChair((short) 0);
			}
			gc.getSession().send(writeRiseFromChair());
		} else {
			//sit on map bench
			if (p.getMap().isChairOccupied(chairId)) {
				gc.getSession().send(writeRiseFromChair());
				return;
			}
			p.setMapChair(chairId);
			gc.getSession().send(writeSitOnChair(chairId));
		}
	}

	public static void handleItemChair(LittleEndianReader packet, GameClient gc) {
		int itemId = packet.readInt();
		GameCharacter p = gc.getPlayer();
		if (!p.getInventory(InventoryType.SETUP).hasItem(itemId, 1)) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to use chair without owning the item");
			return;
		}
		p.setItemChair(itemId);
		p.getMap().sendToAll(writeShowChair(gc.getPlayer().getId(), itemId));
		gc.getSession().send(GamePackets.writeEnableActions());
	}

	public static void handleReplenishHpMp(LittleEndianReader packet, GameClient gc) {
		long now = System.currentTimeMillis();
		GameCharacter p = gc.getPlayer();
		packet.skip(4);
		short hp = packet.readShort();
		short mp = packet.readShort();
		//TODO: use MP recovery and HP recovery skill levels to determine if the
		//proper amount of HP/MP is being recovered
		if (p.getHp() == 0 || hp > 400 || mp > 1000 || (hp > 0 && mp > 0)) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to replenish too much HP/MP at once");
			return;
		}

		//nobody uses fast regen hacks to regenerate health at only double their
		//expected rate - usually the period is less than one second. give them
		//the benefit of the doubt and let latency be assumed to be the culprit
		//as long as regeneration rate is less than double the expected rate
		if (hp > 0) {
			CheatTracker ct = CheatTracker.get(gc);
			long last = ct.getLoggedTime("hpr");
			ct.logTime("hpr", now);
			//it's not documented in the skill description, but having at least
			//level 1 Improved HP Recovery will double recover rate to 5 seconds
			if (now - last < 10000 / 2 && (p.getSkillLevel(Skills.IMPROVED_HP_RECOVERY) == 0 || now - last < 5000 / 2)) {
				ct.suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to replenish HP too rapidly (" + ((now - last) / 1000.0) + " seconds)");
				return;
			}
			p.gainHp(hp);
		}
		if (mp > 0) {
			CheatTracker ct = CheatTracker.get(gc);
			long last = ct.getLoggedTime("mpr");
			ct.logTime("mpr", now);
			if (now - last < 10000 / 2) {
				ct.suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to replenish MP too rapidly (" + ((now - last) / 1000.0) + " seconds)");
				return;
			}
			p.gainMp(mp);
		}
	}

	public static void handleEmote(LittleEndianReader packet, GameClient gc) {
		GameCharacter p = gc.getPlayer();
		int emote = packet.readInt();
		if (emote > 7) { //cash emotes
			int itemid = 5159992 + emote;
			if (p.getInventory(InventoryType.CASH).hasItem(itemid, 1)) {
				CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to use cash shop emote without owning the item");
				return;
			}
		}
		p.getMap().sendToAll(writeExpressionChange(p, emote), p);
	}

	public static void handleBindingChange(LittleEndianReader packet, GameClient gc) {
		GameCharacter p = gc.getPlayer();
		int actionType = packet.readInt();
		switch (actionType) {
			case BINDING_CHANGE_KEY_MAPPING:
				for (int i = packet.readInt(); i > 0; --i) {
					byte key = (byte) packet.readInt();
					byte type = packet.readByte();
					int action = packet.readInt();
					p.bindKey(key, type, action);
				}
				break;
			case BINDING_CHANGE_AUTO_HP_POT: {
				int itemId = packet.readInt();
				if (itemId == 0) {
					p.setAutoHpPot(0);
					return;
				}

				Inventory equippedInv = p.getInventory(Inventory.InventoryType.EQUIPPED);
				//check for	potion item pouch
				if (!equippedInv.hasItem(1812002, 1)) {
					CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to bind pet auto potion without equip");
					return;
				}

				ItemEffectsData e = ItemDataLoader.getInstance().getEffect(itemId);
				if (e == null || e.getHpRecover() == 0 && e.getHpRecoverPercent() == 0) {
					CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to bind invalid potion to pet auto potion");
					return;
				}
				p.setAutoHpPot(itemId);
				break;
			}
			case BINDING_CHANGE_AUTO_MP_POT: {
				int itemId = packet.readInt();
				if (itemId == 0) {
					p.setAutoMpPot(0);
					return;
				}

				Inventory equippedInv = p.getInventory(Inventory.InventoryType.EQUIPPED);
				//check for	potion item pouch
				if (!equippedInv.hasItem(1812003, 1)) {
					CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to bind pet auto potion without equip");
					return;
				}

				ItemEffectsData e = ItemDataLoader.getInstance().getEffect(itemId);
				if (e == null || e.getMpRecover() == 0 && e.getMpRecoverPercent() == 0) {
					CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to bind invalid potion to pet auto potion");
					return;
				}
				p.setAutoMpPot(itemId);
				break;
			}
		}
	}

	private static byte[] writeSitOnChair(short chairId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(5);
		lew.writeShort(ClientSendOps.CHAIR);
		lew.writeBool(true);
		lew.writeShort(chairId);
		return lew.getBytes();
	}

	private static byte[] writeRiseFromChair() {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(3);
		lew.writeShort(ClientSendOps.CHAIR);
		lew.writeBool(false);
		return lew.getBytes();
	}

	private static byte[] writeShowChair(int pId, int itemId) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.ITEM_CHAIR);
		lew.writeInt(pId);
		lew.writeInt(itemId);
		return lew.getBytes();
	}

	private static byte[] writeExpressionChange(GameCharacter p, int expression) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.FACIAL_EXPRESSION);
		lew.writeInt(p.getId());
		lew.writeInt(expression);
		return lew.getBytes();
	}

	private PlayerMiscHandler() {
		//uninstantiable...
	}
}
