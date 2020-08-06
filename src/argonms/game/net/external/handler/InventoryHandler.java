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

import argonms.common.GlobalConstants;
import argonms.common.character.inventory.Equip;
import argonms.common.character.inventory.Inventory;
import argonms.common.character.inventory.Inventory.InventoryType;
import argonms.common.character.inventory.InventorySlot;
import argonms.common.character.inventory.InventorySlot.ItemType;
import argonms.common.character.inventory.InventoryTools;
import argonms.common.loading.item.ItemDataLoader;
import argonms.common.loading.item.ItemEffectsData;
import argonms.common.net.external.CheatTracker;
import argonms.common.net.external.ClientSendOps;
import argonms.common.net.external.CommonPackets;
import argonms.common.util.input.LittleEndianReader;
import argonms.common.util.output.LittleEndianByteArrayWriter;
import argonms.game.GameServer;
import argonms.game.character.GameCharacter;
import argonms.game.field.MapEntity.EntityType;
import argonms.game.field.entity.ItemDrop;
import argonms.game.net.external.GameClient;
import argonms.game.net.external.GamePackets;
import java.util.Set;

/**
 *
 * @author GoldenKevin
 */
public final class InventoryHandler {
	public static void handleItemMove(LittleEndianReader packet, GameClient gc) {
		/*int time = */packet.readInt();
		InventoryType type = InventoryType.valueOf(packet.readByte());
		short src = packet.readShort();
		short dst = packet.readShort();
		short qty = packet.readShort();
		GameCharacter p = gc.getPlayer();
		if (src < 0 && dst > 0) { //unequip
			InventoryTools.unequip(p.getInventory(InventoryType.EQUIPPED), p.getInventory(InventoryType.EQUIP), src, dst);
			gc.getSession().send(CommonPackets.writeInventoryMoveItem(InventoryType.EQUIP, src, dst, (byte) 1));
			p.equipChanged((Equip) p.getInventory(InventoryType.EQUIP).get(dst), false, true);
			p.getMap().sendToAll(GamePackets.writeUpdateAvatar(p), p);
			if (p.getChatRoom() != null)
				GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendChatroomPlayerLookUpdate(p, p.getChatRoom().getRoomId());
		} else if (dst < 0) { //equip
			short[] result = InventoryTools.equip(p.getInventory(InventoryType.EQUIP), p.getInventory(InventoryType.EQUIPPED), src, dst);
			if (result != null) {
				gc.getSession().send(CommonPackets.writeInventoryMoveItem(InventoryType.EQUIP, src, dst, (byte) 2));
				if (result.length == 0 && p.getInventory(InventoryType.EQUIP).get(src) != null || result.length == 2 && src != result[1]) {
					//swapped out an equip
					p.equipChanged((Equip) p.getInventory(InventoryType.EQUIP).get(src), false, true);
				}
				if (result.length == 2) {
					//swapped out an additional equip
					gc.getSession().send(CommonPackets.writeInventoryMoveItem(InventoryType.EQUIP, result[0], result[1], (byte) 1));
					p.equipChanged((Equip) p.getInventory(InventoryType.EQUIP).get(result[1]), false, true);
				}
				p.equipChanged((Equip) p.getInventory(InventoryType.EQUIPPED).get(dst), true, true);
				p.getMap().sendToAll(GamePackets.writeUpdateAvatar(p), p);
				if (p.getChatRoom() != null)
					GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendChatroomPlayerLookUpdate(p, p.getChatRoom().getRoomId());
			} else {
				gc.getSession().send(CommonPackets.writeInventoryNoChange());
			}
		} else if (dst == 0) { //drop
			Inventory inv = p.getInventory(src >= 0 ? type : InventoryType.EQUIPPED);
			InventorySlot item = inv.get(src);
			if (item.getType() == ItemType.PET) {
				byte petSlot = p.indexOfPet(item.getUniqueId());
				p.setPetItemIgnores(item.getUniqueId(), null);
				if (petSlot != -1)
					p.removePet(petSlot, (byte) 0);
			}
			InventorySlot toDrop;
			short newQty = (short) (item.getQuantity() - qty);
			if (newQty == 0 || InventoryTools.isRechargeable(item.getDataId()) || InventoryTools.isCashItem(item.getDataId())) {
				inv.remove(src);
				toDrop = item;
				gc.getSession().send(CommonPackets.writeInventoryClearSlot(type, src));
				if (src < 0) {
					//dropped an equipped equip
					p.equipChanged((Equip) toDrop, false, true);
					p.getMap().sendToAll(GamePackets.writeUpdateAvatar(p), p);
					if (p.getChatRoom() != null)
						GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendChatroomPlayerLookUpdate(p, p.getChatRoom().getRoomId());
				}
			} else {
				item.setQuantity(newQty);
				toDrop = item.clone();
				toDrop.setQuantity(qty);
				gc.getSession().send(CommonPackets.writeInventoryDropItem(type, src, newQty));
			}
			ItemDrop d = new ItemDrop(toDrop);
			p.getMap().drop(d, 0, p, ItemDrop.PICKUP_ALLOW_ALL, p.getId(), !ItemDataLoader.getInstance().canDrop(toDrop.getDataId()));
		} else { //move item
			Inventory inv = p.getInventory(type);
			InventorySlot move = inv.get(src);
			InventorySlot replace = inv.get(dst);
			short slotMax = ItemDataLoader.getInstance().getSlotMax(move.getDataId());
			if (notStackable(move, replace, slotMax)) { //swap
				exchange(p, type, src, dst);
			} else { //merge!
				short srcQty = move.getQuantity();
				short dstQty = replace.getQuantity();
				int total = srcQty + dstQty;
				if (total > slotMax) { //exchange quantities
					short rest = (short) (total - slotMax);
					move.setQuantity(rest);
					replace.setQuantity(slotMax);
					gc.getSession().send(CommonPackets.writeInventoryMoveItemShiftQuantities(type, src, rest, dst, slotMax));
				} else { //combine
					replace.setQuantity((short) total);
					inv.remove(src);
					gc.getSession().send(CommonPackets.writeInventoryMoveItemCombineQuantities(type, src, dst, (short) total));
				}
			}
		}
	}

	public static void handleReturnScroll(LittleEndianReader packet, GameClient gc) {
		/*int time = */packet.readInt();
		short slot = packet.readShort();
		int itemId = packet.readInt();

		GameCharacter p = gc.getPlayer();
		Inventory inv = p.getInventory(InventoryType.USE);
		InventorySlot changed = inv.get(slot);
		if (changed == null || changed.getDataId() != itemId || changed.getQuantity() < 1) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to use nonexistent map return scroll");
			return;
		}
		changed = InventoryTools.takeFromInventory(inv, slot, (short) 1);
		if (changed != null)
			gc.getSession().send(CommonPackets.writeInventoryUpdateSlotQuantity(InventoryType.USE, slot, changed));
		else
			gc.getSession().send(CommonPackets.writeInventoryClearSlot(InventoryType.USE, slot));
		p.itemCountChanged(itemId);
		//TODO: packet edit check for valid map (can't use victoria island scrolls in orbis e.g.)

		ItemEffectsData e = ItemDataLoader.getInstance().getEffect(itemId);
		if (e.getMoveTo() != 0) {
			if (e.getMoveTo() == GlobalConstants.NULL_MAP)
				p.changeMap(p.getMap().getReturnMap());
			else
				p.changeMap(e.getMoveTo());
		}
	}

	public static void handleUpgradeScroll(LittleEndianReader packet, GameClient gc) {
		/*int time = */packet.readInt();
		short scrollSlot = packet.readShort();
		short equipSlot = packet.readShort();
		boolean useWhiteScroll = (packet.readShort() == 2);
		boolean legendarySpirit = packet.readBool();

		GameCharacter p = gc.getPlayer();
		InventorySlot scroll = p.getInventory(InventoryType.USE).get(scrollSlot);
		if (scroll == null || scroll.getQuantity() < 1) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to scroll equip with nonexistent scroll");
			return;
		}
		Equip equip;
		if (!legendarySpirit)
			equip = (Equip) p.getInventory(InventoryType.EQUIPPED).get(equipSlot);
		else
			equip = (Equip) p.getInventory(InventoryType.EQUIP).get(equipSlot);
		if (equip == null) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to scroll nonexistent equip");
			return;
		}
		if ((scroll.getDataId() / 100) % 100 < 90 && (scroll.getDataId() / 100) % 100 != (equip.getDataId() / 10000) % 100) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to scroll equip with incompatible scroll");
			return;
		}
		short whiteScrollSlot = 0;
		if (useWhiteScroll) {
			Set<Short> whiteScrollSlots = p.getInventory(InventoryType.USE).getItemSlots(2340000);
			if (whiteScrollSlots.isEmpty() || p.getInventory(InventoryType.USE).get(whiteScrollSlot = whiteScrollSlots.iterator().next().shortValue()).getQuantity() < 1) {
				CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to scroll equip with nonexistent white scroll");
				return;
			}
		}

		p.equipChanged(equip, false, false); //temporarily take equip off
		//perform scroll effects
		byte result = InventoryTools.scrollEquip(scroll, equip, useWhiteScroll);

		if (result == -1) { //no upgrade slots
			p.equipChanged(equip, true, true); //put equip back on

			gc.getSession().send(CommonPackets.writeInventoryNoChange());
			if (legendarySpirit)
				p.getMap().sendToAll(writeScrollResult(p.getId(), result, false, legendarySpirit));
		} else {
			//remove scrolls
			scroll = InventoryTools.takeFromInventory(p.getInventory(InventoryType.USE), scrollSlot, (short) 1);
			InventorySlot whiteScroll = null;
			if (useWhiteScroll)
				whiteScroll = InventoryTools.takeFromInventory(p.getInventory(InventoryType.USE), whiteScrollSlot, (short) 1);

			//update equips
			boolean cursed;
			if (result == -2) { //cursed
				result = 0; //indicates general failure
				cursed = true;

				InventoryTools.takeFromInventory(p.getInventory(!legendarySpirit ? InventoryType.EQUIPPED : InventoryType.EQUIP), equipSlot, (short) 1);
				equip = null; //leave equip off since we permanantly lost it
			} else { //success (result == 1) or non-cursed fail (result == 0)
				cursed = false;

				p.equipChanged(equip, true, true); //put equip back on
			}

			gc.getSession().send(CommonPackets.writeInventoryUpdateEquipFromScroll(scrollSlot, whiteScrollSlot, equipSlot, scroll, whiteScroll, equip));
			p.getMap().sendToAll(writeScrollResult(p.getId(), result, cursed, legendarySpirit));
		}
	}

	public static void handleMesoDrop(LittleEndianReader packet, GameClient gc) {
		/*int time = */packet.readInt();
		int amount = packet.readInt();
		GameCharacter p = gc.getPlayer();
		if (amount < 0) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.CERTAIN_PACKET_EDITING, "Tried to drop negative mesos");
			return;
		}

		if (!p.gainMesos(-amount, false, true)) {
			gc.getSession().send(GamePackets.writeEnableActions());
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to drop nonexistent mesos");
			return;
		}
		ItemDrop d = new ItemDrop(amount);
		p.getMap().drop(d, 0, p, ItemDrop.PICKUP_ALLOW_ALL, p.getId(), false);
	}

	public static void handlePetMapItemPickUp(LittleEndianReader packet, GameClient gc) {
		long uniqueId = packet.readLong();
		/*byte mode = */packet.readByte();
		packet.readInt(); //?
		/*Point pos = */packet.readPos();
		int eid = packet.readInt();
		GameCharacter p = gc.getPlayer();
		byte petSlot = p.indexOfPet(uniqueId);
		if (petSlot == -1) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to use nonexistent pet to loot map item drop");
			return;
		}

		//TODO: Synchronize on the item (!d.isAlive and GameMap.pickUpDrop are
		//not thread safe if two players try picking it up at the exact same time).
		ItemDrop d = (ItemDrop) p.getMap().getEntityById(EntityType.DROP, eid);
		if (d == null || !d.isAlive()) {
			gc.getSession().send(CommonPackets.writeInventoryNoChange());
			gc.getSession().send(GamePackets.writeShowInventoryFull());
			return;
		}

		//don't let pet pick up anything dropped by its owner from inventory
		if (d.getMob() == 0 && d.getOwner() == p.getId())
			return;

		//check for meso magnet and item pouch
		Inventory equippedInv = p.getInventory(InventoryType.EQUIPPED);
		if (d.getDropType() == ItemDrop.MESOS && !equippedInv.hasItem(1812000, 1) || d.getDropType() == ItemDrop.ITEM && !equippedInv.hasItem(1812001, 1)) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to use nonexistent pet equip to loot map item drop");
			return;
		}

		p.getMap().pickUpDrop(d, p, petSlot);
	}

	public static void handleMapItemPickUp(LittleEndianReader packet, GameClient gc) {
		/*byte mode = */packet.readByte();
		packet.readInt(); //?
		/*Point pos = */packet.readPos();
		int eid = packet.readInt();
		GameCharacter p = gc.getPlayer();
		//TODO: Synchronize on the item (!d.isAlive and GameMap.pickUpDrop are
		//not thread safe if two players try picking it up at the exact same time).
		ItemDrop d = (ItemDrop) p.getMap().getEntityById(EntityType.DROP, eid);
		if (d == null || !d.isAlive()) {
			gc.getSession().send(CommonPackets.writeInventoryNoChange());
			gc.getSession().send(GamePackets.writeShowInventoryFull());
			return;
		}
		p.getMap().pickUpDrop(d, p, (byte) -1);
	}

	private static boolean notStackable(InventorySlot src, InventorySlot dst, short slotMax) {
		return dst == null || src.getDataId() != dst.getDataId() || slotMax == 1
				|| InventoryTools.isThrowingStar(src.getDataId())
				|| InventoryTools.isBullet(src.getDataId())
				|| InventoryTools.isCashItem(dst.getDataId());
	}

	private static void exchange(GameCharacter p, InventoryType type, short src, short dst) {
		Inventory inv = p.getInventory(type);
		InventorySlot item1 = inv.remove(src);
		InventorySlot item2 = inv.remove(dst);
		inv.put(dst, item1);
		if (item2 != null)
			inv.put(src, item2);
		p.getClient().getSession().send(CommonPackets.writeInventoryMoveItem(type, src, dst, (byte) -1));
	}

	private static byte[] writeScrollResult(int playerId, byte success, boolean cursed, boolean legendarySpirit) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10);
		lew.writeShort(ClientSendOps.SHOW_SCROLL_EFFECT);
		lew.writeInt(playerId);
		lew.writeByte(success);
		lew.writeBool(cursed);
		lew.writeBool(legendarySpirit);
		lew.writeByte((byte) 0);
		return lew.getBytes();
	}

	private InventoryHandler() {
		//uninstantiable...
	}
}
