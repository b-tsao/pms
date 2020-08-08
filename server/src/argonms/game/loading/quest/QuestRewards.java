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

package argonms.game.loading.quest;

import argonms.common.character.PlayerJob;
import argonms.common.character.QuestEntry;
import argonms.common.character.inventory.Inventory;
import argonms.common.character.inventory.Inventory.InventoryType;
import argonms.common.character.inventory.InventorySlot;
import argonms.common.character.inventory.InventoryTools;
import argonms.common.character.inventory.InventoryTools.UpdatedSlots;
import argonms.common.loading.item.ItemDataLoader;
import argonms.common.net.external.ClientSession;
import argonms.common.net.external.CommonPackets;
import argonms.common.util.Rng;
import argonms.common.util.TimeTool;
import argonms.common.util.collections.Pair;
import argonms.game.GameServer;
import argonms.game.character.GameCharacter;
import argonms.game.character.inventory.ItemTools;
import argonms.game.net.external.GamePackets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author GoldenKevin
 */
public class QuestRewards {
	private final List<QuestItemStats> items;
	private final Map<Short, Byte> questChanges;
	private final List<Short> jobs;
	private final List<SkillReward> skillChanges;
	private int sumItemProbs;
	private short minLevel;
	private int giveExp;
	private short nextQuest;
	private int giveMesos;
	private int giveBuff;
	private short givePetTameness;
	private short givePetSkill;
	private short giveFame;
	private long endDate;

	protected QuestRewards() {
		items = new ArrayList<QuestItemStats>();
		questChanges = new HashMap<Short, Byte>();
		jobs = new ArrayList<Short>();
		skillChanges = new ArrayList<SkillReward>();
	}

	protected void addRewardItem(QuestItemStats item) {
		sumItemProbs += item.getProb();
		items.add(item);
	}

	protected void addQuestToChange(short questId, byte state) {
		questChanges.put(Short.valueOf(questId), Byte.valueOf(state));
	}

	protected void addJob(short jobId) {
		jobs.add(Short.valueOf(jobId));
	}

	protected void addRewardSkill(SkillReward reward) {
		skillChanges.add(reward);
	}

	protected void setMinLevel(short level) {
		minLevel = level;
	}

	protected void setRewardExp(int exp) {
		giveExp = exp;
	}

	protected void setRewardQuest(short questId) {
		nextQuest = questId;
	}

	protected void setRewardMoney(int mesos) {
		giveMesos = mesos;
	}

	protected void setRewardBuff(int itemId) {
		giveBuff = itemId;
	}

	protected void setRewardPetTameness(short tameness) {
		givePetTameness = tameness;
	}

	protected void setRewardPetSkill(short petSkillId) {
		givePetSkill = petSkillId;
	}

	protected void setRewardFame(short pop) {
		giveFame = pop;
	}

	protected void setEndDate(int idate) {
		endDate = TimeTool.intDateToCalendar(idate).getTimeInMillis();
	}

	protected void setRepeatInterval(int interval) {
		//you know what? I don't care about this value since
		//it takes too much effort to check in giveRewards. T.T
	}

	protected SkillReward getSkillReward(int skillId) {
		for (SkillReward sr : skillChanges)
			if (sr.skillId == skillId)
				return sr;
		return null;
	}

	private boolean applicableItem(GameCharacter p, QuestItemStats item) {
		return item.jobMatch(p.getJob()) && item.genderMatch(p.getGender()) && item.notExpired();
	}

	private void giveItem(GameCharacter p, int itemId, short quantity, int period) {
		InventoryType type = InventoryTools.getCategory(itemId);
		Inventory inv = p.getInventory(InventoryTools.getCategory(itemId));
		InventorySlot slot = InventoryTools.makeItemWithId(itemId);
		//period is stored in minutes for quests
		if (period != 0) {
			//if this ever becomes false, InventoryTools.slotsNeeded(),
			//InventoryTools.addToInventory(Inventory, InventorySlot, int, boolean),
			//InventoryHandler.handleItemMove(), InventoryHandler.notStackable(),
			//MiniroomHandler.tradeSetItems(), and NpcMiniroomHandler.handleNpcStorageAction()
			//need to be generalized from cash items to items with expirations
			assert ItemDataLoader.getInstance().getSlotMax(itemId) == 1;
			slot.setExpiration(System.currentTimeMillis() + (period * 1000 * 60));
		}
		UpdatedSlots changedSlots = InventoryTools.addToInventory(inv, slot, quantity, true);
		ClientSession<?> ses = p.getClient().getSession();
		short pos;
		for (Short s : changedSlots.modifiedSlots) {
			pos = s.shortValue();
			slot = inv.get(pos);
			assert period == 0;
			ses.send(CommonPackets.writeInventoryUpdateSlotQuantity(type, pos, slot));
		}
		for (Short s : changedSlots.addedOrRemovedSlots) {
			pos = s.shortValue();
			slot = inv.get(pos);
			if (period != 0) {
				slot.setUniqueId(p.generateTransientUniqueIdForQuestItem());
				p.onExpirableItemAdded(slot);
			}
			ses.send(CommonPackets.writeInventoryAddSlot(type, pos, slot));
		}
		p.itemCountChanged(itemId);
		ses.send(GamePackets.writeShowItemGainFromQuest(itemId, quantity));
	}

	private void takeItem(GameCharacter p, int itemId, short quantity) {
		InventoryType type = InventoryTools.getCategory(itemId);
		Inventory inv = p.getInventory(InventoryTools.getCategory(itemId));
		if (quantity == 0)
			quantity = (short) -InventoryTools.getAmountOfItem(inv, itemId);
		UpdatedSlots changedSlots = InventoryTools.removeFromInventory(p, itemId, -quantity);
		ClientSession<?> ses = p.getClient().getSession();
		short pos;
		for (Short s : changedSlots.modifiedSlots) {
			pos = s.shortValue();
			ses.send(CommonPackets.writeInventoryUpdateSlotQuantity(type, pos, inv.get(pos)));
		}
		for (Short s : changedSlots.addedOrRemovedSlots) {
			pos = s.shortValue();
			ses.send(CommonPackets.writeInventoryClearSlot(type, pos));
		}
		p.itemCountChanged(itemId);
		ses.send(GamePackets.writeShowItemGainFromQuest(itemId, quantity));
	}

	private boolean awardItems(GameCharacter p, int selection) {
		boolean findRandomItem = (sumItemProbs > 0);
		int selectableItemIndex = 0;
		int random = findRandomItem ? Rng.getGenerator().nextInt(sumItemProbs) : 0, runningProbs = 0;

		List<Pair<Pair<Integer, Short>, Integer>> itemsToGain = new ArrayList<Pair<Pair<Integer, Short>, Integer>>();
		List<Pair<Integer, Short>> itemsToLose = new ArrayList<Pair<Integer, Short>>();
		//fails if there are multiple rewards of the same itemid, but that never happens!
		Map<InventoryType, Integer> netEmptySlotRemovals = new EnumMap<InventoryType, Integer>(InventoryType.class);
		netEmptySlotRemovals.put(InventoryType.EQUIP, Integer.valueOf(0));
		netEmptySlotRemovals.put(InventoryType.USE, Integer.valueOf(0));
		netEmptySlotRemovals.put(InventoryType.SETUP, Integer.valueOf(0));
		netEmptySlotRemovals.put(InventoryType.ETC, Integer.valueOf(0));
		netEmptySlotRemovals.put(InventoryType.CASH, Integer.valueOf(0));

		for (QuestItemStats item : items) {
			boolean applicable = applicableItem(p, item);
			if (item.getProb() != 0 && applicable) {
				if (item.getProb() == -1) {
					//items List better keep the order of the item rewards in
					//Quest.wz/Act.img...
					if (selectableItemIndex != selection)
						applicable = false;
					selectableItemIndex++;
				} else {
					if (findRandomItem && random < (runningProbs += item.getProb()))
						//use this item - leave give = true and don't look for more random items
						findRandomItem = false;
					else
						//don't give this item
						applicable = false;
				}
			}
			if (applicable) {
				short quantity = item.getCount();
				InventoryType type = InventoryTools.getCategory(item.getItemId());

				Pair<Integer, Short> idAndQty = new Pair<Integer, Short>(Integer.valueOf(item.getItemId()), Short.valueOf(quantity));
				if (quantity > 0) {
					itemsToGain.add(new Pair<Pair<Integer, Short>, Integer>(idAndQty, Integer.valueOf(item.getPeriod())));
					netEmptySlotRemovals.put(type, Integer.valueOf(netEmptySlotRemovals.get(type).intValue() + InventoryTools.slotsNeeded(p.getInventory(type), item.getItemId(), quantity, false)));
				} else {
					itemsToLose.add(idAndQty);
					netEmptySlotRemovals.put(type, Integer.valueOf(netEmptySlotRemovals.get(type).intValue() - InventoryTools.slotsFreed(p.getInventory(type), item.getItemId(), -quantity)));
				}
			}
		}

		for (Map.Entry<InventoryType, Integer> netEmptySlotChange : netEmptySlotRemovals.entrySet())
			if (p.getInventory(netEmptySlotChange.getKey()).freeSlots() < netEmptySlotChange.getValue().intValue())
				return false;

		for (Pair<Integer, Short> itemToLose : itemsToLose)
			takeItem(p, itemToLose.left.intValue(), itemToLose.right.shortValue());
		for (Pair<Pair<Integer, Short>, Integer> itemToGain : itemsToGain)
			giveItem(p, itemToGain.left.left.intValue(), itemToGain.left.right.shortValue(), itemToGain.right.intValue());
		return true;
	}

	public short giveRewards(GameCharacter p, int selection) {
		//some requirements are repeated in the act data - we might as well recheck them
		if (endDate != 0 && System.currentTimeMillis() >= endDate)
			return -QuestEntry.QUEST_ACTION_ERROR_EXPIRED;
		if (minLevel != 0 && p.getLevel() < minLevel
				|| !jobs.isEmpty() && !jobs.contains(Short.valueOf(p.getJob())) && !PlayerJob.isGameMaster(p.getJob()))
			return -QuestEntry.QUEST_ACTION_ERROR_UNKNOWN;
		if (!awardItems(p, selection))
			return -QuestEntry.QUEST_ACTION_ERROR_INVENTORY_FULL;
		for (Entry<Short, Byte> entry : questChanges.entrySet()) {
			switch (entry.getValue().byteValue()) {
				case QuestEntry.STATE_STARTED:
					if (p.isQuestInactive(entry.getKey().shortValue()))
						p.localStartQuest(entry.getKey().shortValue());
					break;
				case QuestEntry.STATE_COMPLETED:
					if (p.isQuestStarted(entry.getKey().shortValue()))
						p.localCompleteQuest(entry.getKey().shortValue(), -1);
					break;
			}
		}
		for (SkillReward skill : skillChanges)
			skill.applyTo(p);
		if (giveExp != 0)
			p.gainExp((int) Math.min((long) giveExp * GameServer.getVariables().getExpRate(), Integer.MAX_VALUE), false, true);
		if (giveMesos != 0)
			if (giveMesos > 0)
				p.gainMesos((int) Math.min((long) giveMesos * GameServer.getVariables().getMesoRate(), Integer.MAX_VALUE), true);
			else
				p.gainMesos(giveMesos, true);
		if (giveBuff != 0)
			ItemTools.useItem(p, giveBuff);
		if (givePetTameness != 0) {
			//TODO: WHICH PET DO WE APPLY THIS TO?
		}
		if (givePetSkill != 0) {
			//TODO: WHICH PET DO WE APPLY THIS TO?
		}
		if (giveFame != 0)
			p.gainFame(giveFame, true);
		return nextQuest;
	}

	protected static class SkillReward {
		private final List<Short> compatibleJobs;
		private int skillId;
		private byte currentLevel, masterLevel;
		private boolean onlyMasterLevel;

		protected SkillReward(int skillId, byte skillLevel, byte masterLevel, boolean onlyMasterLevel) {
			this.compatibleJobs = new ArrayList<Short>();
			this.skillId = skillId;
			this.currentLevel = skillLevel;
			this.masterLevel = masterLevel;
			this.onlyMasterLevel = onlyMasterLevel;
		}

		protected void addApplicableJob(short jobId) {
			compatibleJobs.add(Short.valueOf(jobId));
		}

		protected void applyTo(GameCharacter p) {
			if (!compatibleJobs.contains(Short.valueOf(p.getJob())))
				return;
			p.setSkillLevel(skillId, currentLevel, masterLevel, onlyMasterLevel);
		}
	}
}
