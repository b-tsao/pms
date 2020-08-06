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

package argonms.game.field;

import argonms.common.loading.StatusEffectsData.EffectSource;
import argonms.common.loading.StatusEffectsData.MonsterStatusEffectsData;

/**
 *
 * @author GoldenKevin
 */
public class MonsterStatusEffectValues {
	private final MonsterStatusEffectsData e;
	private final short mod;

	public MonsterStatusEffectValues(MonsterStatusEffectsData e, short mod) {
		this.e = e;
		this.mod = mod;
	}

	public int getSource() {
		return e.getDataId();
	}

	public byte getLevelWhenCast() {
		return e.getLevel();
	}

	public EffectSource getSourceType() {
		return e.getSourceType();
	}

	public short getModifier() {
		return mod;
	}

	public MonsterStatusEffectsData getEffectsData() {
		return e;
	}
}
