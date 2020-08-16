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

package argonms.game.script.binding;

import argonms.common.character.inventory.Equip;
import argonms.common.character.inventory.InventorySlot;
import argonms.common.character.inventory.InventorySlot.ItemType;
import argonms.common.character.inventory.InventoryTools;
import argonms.common.util.Rng;
import argonms.game.GameServer;
import argonms.game.character.GameCharacter;
import argonms.game.field.entity.ItemDrop;
import argonms.game.field.entity.Mob;
import argonms.game.field.entity.Reactor;
import argonms.game.loading.mob.MobDataLoader;
import argonms.game.net.external.GameClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author GoldenKevin
 */
public class ScriptReactor extends PlayerScriptInteraction {
	private final Reactor reactor;

	public ScriptReactor(Reactor reactor, GameClient client, Scriptable globalScope) {
		super(client, globalScope);
		this.reactor = reactor;
	}

	//TODO: some way to drop items only if the player has a specific quest active
	public void dropItems(int mesosMin, int mesosMax, int mesoChance, int... itemsAndChances) {
		Random generator = Rng.getGenerator();
		List<ItemDrop> drops;
		int multiplier = GameServer.getVariables().getMesoRate();
		//TODO: should we multiply mesoChance by drop rate?
		if (mesoChance == 0 || generator.nextInt(1000000) >= mesoChance) {
			drops = new ArrayList<ItemDrop>(itemsAndChances.length / 2);
		} else {
			drops = new ArrayList<ItemDrop>(1 + itemsAndChances.length / 2);
			int mesos = (generator.nextInt(mesosMax - mesosMin + 1) + mesosMin);
			drops.add(new ItemDrop((int) Math.min((long) mesos * multiplier, Integer.MAX_VALUE)));
		}
		multiplier = GameServer.getVariables().getDropRate();
		for (int i = 0; i + 1 < itemsAndChances.length; i+= 2) {
			if (generator.nextInt(1000000) < ((long) itemsAndChances[i + 1] * multiplier)) {
				InventorySlot item = InventoryTools.makeItemWithId(itemsAndChances[i]);
				if (item.getType() == ItemType.EQUIP)
					InventoryTools.randomizeStats((Equip) item);
				drops.add(new ItemDrop(item));
			}
		}
		GameCharacter p = getClient().getPlayer();
		p.getMap().drop(drops, reactor, ItemDrop.PICKUP_ALLOW_OWNER, p.getId(), p.getId());
	}
	
	public Object spawnMob(int mobId) {
		GameCharacter p = getClient().getPlayer();
		Mob mob = new Mob(MobDataLoader.getInstance().getMobStats(mobId), p.getMap());
		mob.setPosition(reactor.getPosition());
		p.getMap().spawnMonster(mob);
		return Context.javaToJS(new ScriptMob(mob), globalScope);
	}

	public Object spawnMob(int mobId, boolean faceRight) {
		GameCharacter p = getClient().getPlayer();
		Mob mob = new Mob(MobDataLoader.getInstance().getMobStats(mobId), p.getMap(), faceRight ? (byte) 4 : (byte) 5);
		mob.setPosition(reactor.getPosition());
		p.getMap().spawnMonster(mob);
		return Context.javaToJS(new ScriptMob(mob), globalScope);
	}
}
