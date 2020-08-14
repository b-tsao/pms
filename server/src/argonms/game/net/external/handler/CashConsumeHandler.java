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

import argonms.common.character.inventory.Inventory;
import argonms.common.character.inventory.InventorySlot;
import argonms.common.character.inventory.InventoryTools;
import argonms.common.character.inventory.Pet;
import argonms.common.loading.item.ItemDataLoader;
import argonms.common.net.external.CheatTracker;
import argonms.common.net.external.ClientSendOps;
import argonms.common.net.external.CommonPackets;
import argonms.common.util.input.LittleEndianReader;
import argonms.common.util.output.LittleEndianByteArrayWriter;
import argonms.game.GameServer;
import argonms.game.character.GameCharacter;
import argonms.game.character.inventory.PetTools;
import argonms.game.net.external.GameClient;
import argonms.game.net.external.GamePackets;
import argonms.game.net.external.handler.ChatHandler.TextStyle;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author GoldenKevin
 */
public class CashConsumeHandler {
	private static void handleHiredMerchant(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle hired merchant
	}

	private static void handleVipTeleportRock(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle VIP teleport rock
	}

	private static void handleStatReset(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle AP/SP reset
	}

	private static void handleEquipModifier(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle equip tagger/sealer
	}

	private static void handleMegaphone(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle megaphone
		switch (itemId / 1000 % 10) {
			case 0: // Cheap megaphone
			case 3: // Heart megaphone
			case 4: // Skull megaphone
				// Doesn't work client side
				break;
			case 1: // Megaphone
				if (p.getLevel() >= 10) {
					p.getMap().sendToAll(ChatHandler.writeStyledChat(TextStyle.LIGHT_BLUE_TEXT_WHITE_BG, p, packet.readLengthPrefixedString(), false));
                } else {
                	p.getClient().getSession().send(CommonPackets.writeServerMessage(TextStyle.RED_TEXT_CLEAR_BG.byteValue(), "You must be above level 10 to use this item!", (byte) -1, false));
                }
				break;
			case 2: // Super megaphone
				GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendWorldWideMessage(TextStyle.PINK_TEXT_WHITE_BG.byteValue(), p.getName() + " : " + packet.readLengthPrefixedString(), p.getClient().getChannel(), packet.readBool());
				break;
			case 5: // Maple TV
//				int tvType = itemId % 10;
//                boolean megassenger = false;
//                boolean ear = false;
//                String victimName = null;
//                byte[] victimLook = null;
//                if (tvType != 1) {
//                    if (tvType >= 3) {
//                        megassenger = true;
//                        if (tvType == 3) {
//                            packet.readByte();
//                        }
//                        ear = packet.readBool();
//                    } else if (tvType != 2) {
//                        packet.readByte();
//                    }
//                    if (tvType != 4) {
//                    	victimName = packet.readLengthPrefixedString();
//                        victimLook = GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().getCharLook(victimName, false, true);
//                    }
//                }
//                List<String> messages = new LinkedList<>();
//                StringBuilder builder = new StringBuilder();
//                for (int i = 0; i < 5; i++) {
//                    String message = packet.readLengthPrefixedString();
//                    if (megassenger) {
//                        builder.append(" ").append(message);
//                    }
//                    messages.add(message);
//                }
//                packet.readInt();
//                if (megassenger) {
//                	GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendWorldWideMessage(TextStyle.PINK_TEXT_WHITE_BG.byteValue(), p.getName() + " : " + builder.toString(), p.getClient().getChannel(), ear);
//                }
//                if (GameServer.getVariables().getMapleTV() == null) {
//                	GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendWorldWide(CommonPackets.enableTV());
//                	LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter();
//            		CommonPackets.writeAvatar(lew, p, false);
//            		int duration = 0;
//            		switch (tvType) {
//	        	        case 1:
//	        	        case 4:
//	        	        	duration = 30000;
//	        	            break;
//	        	        case 2:
//	        	        case 5:
//	        	        	duration = 60000;
//	        	        	break;
//	        	        default:
//	        	        	duration = 15000;
//	        	        	break;
//	        		}
//                	GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendWorldWide(CommonPackets.sendTV((byte) (tvType <= 2 ? tvType : tvType - 3), p.getName(), lew.getBytes(), victimName, victimLook, messages, duration));
//                	Scheduler.getInstance().runAfterDelay(new Runnable() {
//        				@Override
//        				public void run() {
//        					GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendWorldWide(CommonPackets.removeTV());
//        				}
//        			}, duration);
//                } else {
//                	p.getClient().getSession().send(CommonPackets.writeServerMessage(TextStyle.RED_TEXT_CLEAR_BG.byteValue(), "MapleTV is already in use.", (byte) -1, false));
//                }
				break;
			case 6: // Item megaphone
				break;
		}
	}

	private static void handleBanner(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle message to map
	}

	private static void handleNoteItem(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle send note
	}

	private static void handleCongratulatorySong(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle send music change
	}

	private static void handleMapEffect(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle map effect
	}

	private static void handlePlayerShopPermit(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: handle create shop
	}

	private static void handlePetNameTag(LittleEndianReader packet, GameCharacter p) {
		String name = packet.readLengthPrefixedString();
		name = name.substring(0, Math.min(13, name.length()));
		Pet pet = p.getPets()[0];
		if (pet != null) {
			pet.setName(name);
			PetTools.updatePet(p, pet);
			p.getMap().sendToAll(writePetNameChange(p, (byte) 0, name, PetTools.hasLabelRing(p, (byte) 0)));
		}
	}

	private static void handleFreeMarketSearch(LittleEndianReader packet, GameCharacter p) {
		//TODO: Owl of Minerva
	}

	private static void handleMesoBag(LittleEndianReader packet, GameCharacter p) {
		//TODO: give mesos
	}

	private static void handlePetSpecialFood(GameCharacter p, int itemId) {
		List<Integer> consumableBy = ItemDataLoader.getInstance().getEffect(itemId).getPetsConsumable();
		byte petSlot = -1;
		Pet[] pets = p.getPets();
		for (byte i = 0; i < 3 && pets[i] != null; i++)
			if (consumableBy.contains(pets[i].getDataId()) && (petSlot == -1 || pets[i].getFullness() < pets[petSlot].getFullness()))
				petSlot = i;
		if (petSlot == -1)
			return;

		Pet pet = pets[petSlot];
		PetTools.gainFullness(pet, ItemDataLoader.getInstance().getPetFullnessRecover(itemId));
		PetTools.gainCloseness(p, petSlot, pet, 100);
		PetTools.updatePet(p, pet);
		p.getMap().sendToAll(GamePackets.writePetFoodResponse(p, petSlot, true, PetTools.hasQuoteRing(p, petSlot)));
	}

	private static void handleTemporaryPlayerEffect(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: player effect
	}

	private static void handleMorphItem(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: morph effect
	}

	private static void handlePortableDelivery(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: send package delivery
	}

	private static void handleChalkboard(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: chalkboard
	}

	private static void handleSuperMegaphoneWithAvatar(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: super megaphone with avatar
		List<String> lines = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            lines.add(packet.readLengthPrefixedString());
        }
        GameServer.getChannel(p.getClient().getChannel()).getCrossServerInterface().sendWorldWide(GamePackets.writeAvatarMega(p, p.getClient().getChannel(), itemId, lines, packet.readBool()));
	}

	private static void handleCharacterRefactor(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: character name change/character transfer
	}

	private static void handlePortableGeneralShop(LittleEndianReader packet, GameCharacter p, int itemId) {
		//TODO: portable general shop
	}

	public static void handleCashItem(LittleEndianReader packet, GameClient gc) {
		short slot = packet.readShort();
		int itemId = packet.readInt();

		GameCharacter p = gc.getPlayer();
		Inventory inv = p.getInventory(Inventory.InventoryType.CASH);
		InventorySlot changed = inv.get(slot);
		if (changed == null || changed.getDataId() != itemId || changed.getQuantity() < 1) {
			CheatTracker.get(gc).suspicious(CheatTracker.Infraction.POSSIBLE_PACKET_EDITING, "Tried to use nonexistent cash consume");
			return;
		}
		changed = InventoryTools.takeFromInventory(inv, slot, (short) 1);
		if (changed != null)
			gc.getSession().send(CommonPackets.writeInventoryUpdateSlotQuantity(Inventory.InventoryType.CASH, slot, changed));
		else
			gc.getSession().send(CommonPackets.writeInventoryClearSlot(Inventory.InventoryType.CASH, slot));
		p.itemCountChanged(itemId);

		switch (itemId / 10000) {
			case 503:
				handleHiredMerchant(packet, p, itemId);
				break;
			case 504:
				handleVipTeleportRock(packet, p, itemId);
				break;
			case 505:
				handleStatReset(packet, p, itemId);
				break;
			case 506:
				handleEquipModifier(packet, p, itemId);
				break;
			case 507:
				handleMegaphone(packet, p, itemId);
				break;
			case 508:
				handleBanner(packet, p, itemId);
				break;
			case 509:
				handleNoteItem(packet, p, itemId);
				break;
			case 510:
				handleCongratulatorySong(packet, p, itemId);
				break;
			case 512:
				handleMapEffect(packet, p, itemId);
				break;
			case 514:
				handlePlayerShopPermit(packet, p, itemId);
				break;
			case 517:
				handlePetNameTag(packet, p);
				break;
			case 520:
				handleMesoBag(packet, p);
				break;
			case 523:
				handleFreeMarketSearch(packet, p);
				break;
			case 524:
				handlePetSpecialFood(p, itemId);
				break;
			case 528:
				handleTemporaryPlayerEffect(packet, p, itemId);
				break;
			case 530:
				handleMorphItem(packet, p, itemId);
				break;
			case 533:
				handlePortableDelivery(packet, p, itemId);
				break;
			case 537:
				handleChalkboard(packet, p, itemId);
				break;
			case 539:
				handleSuperMegaphoneWithAvatar(packet, p, itemId);
				break;
			case 540:
				handleCharacterRefactor(packet, p, itemId);
				break;
			case 545:
				handlePortableGeneralShop(packet, p, itemId);
				break;
		}
	}

	private static byte[] writePetNameChange(GameCharacter p, byte slot, String newName, boolean hasLabelRing) {
		LittleEndianByteArrayWriter lew = new LittleEndianByteArrayWriter(10 + newName.length());

		lew.writeShort(ClientSendOps.PET_NAME_CHANGE);
		lew.writeInt(p.getId());
		lew.writeByte(slot);
		lew.writeLengthPrefixedString(newName);
		lew.writeBool(hasLabelRing);

		return lew.getBytes();
	}
}
