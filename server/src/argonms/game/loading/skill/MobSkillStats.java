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

package argonms.game.loading.skill;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author GoldenKevin
 */
public class MobSkillStats {
	private final Map<Byte, MobSkillEffectsData> levels;
	private int animationTime;

	protected MobSkillStats() {
		levels = new HashMap<Byte, MobSkillEffectsData>();
	}

	protected void addLevel(byte level, MobSkillEffectsData effect) {
		levels.put(Byte.valueOf(level), effect);
	}

	protected void setDelay(int delay) {
		animationTime = delay;
	}

	public MobSkillEffectsData getLevel(byte level) {
		return levels.get(Byte.valueOf(level));
	}

	public int getDelay() {
		return animationTime;
	}
}
