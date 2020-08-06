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

package argonms.game.loading.mob;

/**
 *
 * @author GoldenKevin
 */
public class Skill {
	private short skill;
	private byte level;
	private short effectDelay;

	public void setSkill(short id) {
		this.skill = id;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public void setEffectDelay(short delay) {
		this.effectDelay = delay;
	}

	public short getSkill() {
		return skill;
	}

	public byte getLevel() {
		return level;
	}

	public short getEffectDelay() {
		return effectDelay;
	}

	@Override
	public String toString() {
		return "Id=" + skill + ", Level=" + level;
	}
}
