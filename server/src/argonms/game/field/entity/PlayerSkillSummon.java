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

package argonms.game.field.entity;

import argonms.game.character.GameCharacter;
import argonms.game.field.AbstractEntity;
import argonms.game.loading.skill.PlayerSkillEffectsData;
import argonms.game.loading.skill.SkillDataLoader;
import argonms.game.loading.skill.SkillStats;
import argonms.game.net.external.GamePackets;
import java.awt.Point;

/**
 *
 * @author GoldenKevin
 */
public class PlayerSkillSummon extends AbstractEntity {
	private final byte summonType; //0 = stationary, 1 = follow, 2/4 = only tele follow, 3 = bird follow
	private final int ownerEid;
	private final int skillId;
	private final byte skillLevel;
	private short remHp;

	public PlayerSkillSummon(GameCharacter p, PlayerSkillEffectsData skill, Point initialPos, byte stance) {
		setPosition(initialPos);
		setStance(stance);
		ownerEid = p.getId();
		skillId = skill.getDataId();
		skillLevel = skill.getLevel();
		remHp = -1;
		summonType = SkillDataLoader.getInstance().getSkill(skillId).getSummonType();
	}

	public PlayerSkillSummon(int pId, int skillId, byte skillLevel, Point pos, byte stance) {
		setPosition(pos);
		setStance(stance);
		SkillStats skill = SkillDataLoader.getInstance().getSkill(skillId);
		ownerEid = pId;
		this.skillId = skillId;
		this.skillLevel = skillLevel;
		remHp = -1;
		summonType = skill.getSummonType();
	}

	public byte getSummonType() {
		return summonType;
	}

	public int getOwner() {
		return ownerEid;
	}

	public int getSkillId() {
		return skillId;
	}

	public byte getSkillLevel() {
		return skillLevel;
	}

	public void setHp(short hp) {
		remHp = hp;
	}

	public boolean hurt(int loss) {
		remHp -= Math.min(loss, remHp);
		return remHp == 0;
	}

	public boolean isPuppet() {
		return remHp >= 0;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.SUMMON;
	}

	@Override
	public boolean isAlive() {
		return remHp != 0;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public byte[] getShowNewSpawnMessage() {
		return GamePackets.writeShowSummon(this, (byte) 0);
	}

	@Override
	public byte[] getShowExistingSpawnMessage() {
		return GamePackets.writeShowSummon(this, (byte) 1);
	}

	@Override
	public byte[] getDestructionMessage() {
		return GamePackets.writeRemoveSummon(this, (byte) 1);
	}
}
