/*
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

package argonms.game.field.movement;

import argonms.common.util.output.LittleEndianWriter;
import java.awt.Point;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author GoldenKevin
 */
public class AbsoluteLifeMovement implements PositionChangedMovementFragment, FootholdChangedMovementFragment, StanceChangedMovementFragment {
	private final byte type;
	private final Point position;
	private final Point pixelsPerSecond;
	private final short foothold;
	private final byte stance;
	private final short duration;

	public AbsoluteLifeMovement(byte type, Point position, Point wobble, short foothold, byte stance, short duration) {
		this.type = type;
		this.position = position;
		this.pixelsPerSecond = wobble;
		this.foothold = foothold;
		this.stance = stance;
		this.duration = duration;
	}

	@Override
	public void serialize(LittleEndianWriter lew) {
		lew.writeByte(type);
		lew.writePos(position);
		lew.writePos(pixelsPerSecond);
		lew.writeShort(foothold);
		lew.writeByte(stance);
		lew.writeShort(duration);
	}

	@Override
	public Set<UpdatedEntityInfo> updatedStats() {
		return EnumSet.of(UpdatedEntityInfo.POSITION, UpdatedEntityInfo.FOOTHOLD, UpdatedEntityInfo.STANCE);
	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public short getFoothold() {
		return foothold;
	}

	@Override
	public byte getStance() {
		return stance;
	}
}
