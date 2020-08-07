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

package argonms.common.character.inventory;

import argonms.game.field.Positionable;
import java.awt.Point;

/**
 *
 * @author GoldenKevin
 */
public class Pet extends InventorySlot implements Positionable {
	private String name;
	private byte level;
	private short closeness;
	private byte fullness;

	private Point pos;
	private byte stance;
	private short foothold;

	/**
	 * After this method is called, the name of the newly created pet will still
	 * be null so set it with a call to the method
	 * setName(StringDataLoader.getInstance().getItemNameFromId(getItemId()))
	 * @param itemid
	 */
	public Pet(int itemid) {
		super(itemid);

		//set all the default stats except for name, because StringDataLoader
		//might not be available (and if it was, it would be too expensive).
		this.level = 1;
		this.closeness = 0;
		this.fullness = 100;
	}

	public Pet(int id, String name, byte lvl, short cn, byte fn) {
		super(id);

		this.name = name;
		this.level = lvl;
		this.closeness = cn;
		this.fullness = fn;
	}

	@Override
	public ItemType getType() {
		return ItemType.PET;
	}

	@Override
	public byte getTypeByte() {
		return InventorySlot.PET;
	}

	@Override
	public short getQuantity() {
		return 1;
	}

	@Override
	public void setQuantity(short newValue) {
		throw new UnsupportedOperationException("Cannot change quantity of a pet.");
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public void setCloseness(short closeness) {
		this.closeness = closeness;
	}

	public void setFullness(byte fullness) {
		this.fullness = fullness;
	}

	@Override
	public void setPosition(Point pos) {
		this.pos = pos;
	}

	@Override
	public void setStance(byte stance) {
		this.stance = stance;
	}

	@Override
	public void setFoothold(short foothold) {
		this.foothold = foothold;
	}

	public String getName() {
		return name;
	}

	public byte getLevel() {
		return level;
	}

	public short getCloseness() {
		return closeness;
	}

	public byte getFullness() {
		return fullness;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() >= getExpiration();
	}

	@Override
	public Point getPosition() {
		return pos;
	}

	@Override
	public byte getStance() {
		return stance;
	}

	@Override
	public short getFoothold() {
		return foothold;
	}

	@Override
	public Pet clone() {
		Pet copy = new Pet(getDataId());
		copy.setExpiration(getExpiration());
		copy.setUniqueId(getUniqueId());
		copy.setOwner(getOwner());
		copy.setFlag(getFlag());

		copy.setName(getName());
		copy.setLevel(getLevel());
		copy.setCloseness(getCloseness());
		copy.setFullness(getFullness());

		return copy;
	}
}
