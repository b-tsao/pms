/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010-2013  GoldenKevin
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package kvjcompiler.skill.structure;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import kvjcompiler.Converter;
import kvjcompiler.Effects;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class SkillEffect implements IStructure {
	private enum EffectType {
		Integer,
		Short,
		Byte,
		Point,
		Summon
	}
	
	private class Effect {
		public EffectType t;
		public byte e;
		public int i;
		public short s;
		public byte b;
		public Point p;
		
		public Effect(EffectType type, byte effect, int value) {
			t = type;
			e = effect;
			i = value;
		}
		
		public Effect(EffectType type, byte effect, short value) {
			t = type;
			e = effect;
			s = value;
		}
		
		public Effect(EffectType type, byte effect, byte value) {
			t = type;
			e = effect;
			b = value;
		}
		
		public Effect(EffectType type, byte effect, Point value) {
			t = type;
			e = effect;
			p = value;
		}
		
		public Effect(EffectType type, byte effect, byte value1, int value2) {
			t = type;
			e = effect;
			b = value1;
			i = value2;
		}
	}
	
	private final List<Effect> effects;

	public SkillEffect() {
		this.effects = new LinkedList<Effect>();
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("mpCon")) {
			effects.add(new Effect(EffectType.Short, Effects.MP_CONSUME, Short.parseShort(value)));
		} else if (key.equals("hpCon")) {
			effects.add(new Effect(EffectType.Short, Effects.HP_CONSUME, Short.parseShort(value)));
		} else if (key.equals("time")) {
			effects.add(new Effect(EffectType.Integer, Effects.DURATION, Integer.parseInt(value) * 1000));
		} else if (key.equals("x")) { //grr, mobskills has one with a value of 50000. that would work with unsigned short, but java doesn't have that...
			effects.add(new Effect(EffectType.Integer, Effects.X, Integer.parseInt(value)));
		} else if (key.equals("y")) {
			effects.add(new Effect(EffectType.Integer, Effects.Y, Integer.parseInt(value)));
		} else if (key.equals("z")) {
			if (Converter.isNumber(value))
				effects.add(new Effect(EffectType.Integer, Effects.Z, Integer.parseInt(value)));
		} else if (key.equals("damage")) {
			effects.add(new Effect(EffectType.Short, Effects.DAMAGE, Short.parseShort(value)));
		} else if (key.equals("lt")) {
			int comma = value.indexOf(',');
			effects.add(new Effect(EffectType.Point, Effects.LT, new Point(Integer.parseInt(value.substring(0, comma)), Integer.parseInt(value.substring(comma + 1, value.length())))));
		} else if (key.equals("rb")) {
			int comma = value.indexOf(',');
			effects.add(new Effect(EffectType.Point, Effects.RB, new Point(Integer.parseInt(value.substring(0, comma)), Integer.parseInt(value.substring(comma + 1, value.length())))));
		} else if (key.equals("mobCount")) {
			effects.add(new Effect(EffectType.Byte, Effects.MOB_COUNT, Byte.parseByte(value)));
		} else if (key.equals("prop")) {
			effects.add(new Effect(EffectType.Short, Effects.PROP, Short.parseShort(value)));
		} else if (key.equals("mastery")) {
			effects.add(new Effect(EffectType.Byte, Effects.MASTERY, Byte.parseByte(value)));
		} else if (key.equals("cooltime") || key.equals("interval")) {
			effects.add(new Effect(EffectType.Short, Effects.COOLTIME, Short.parseShort(value)));
		} else if (key.equals("range")) {
			effects.add(new Effect(EffectType.Short, Effects.RANGE, Short.parseShort(value)));
		} else if (key.equals("pad")) {
			effects.add(new Effect(EffectType.Short, Effects.WATK, Short.parseShort(value)));
		} else if (key.equals("pdd")) {
			effects.add(new Effect(EffectType.Short, Effects.WDEF, Short.parseShort(value)));
		} else if (key.equals("mad")) {
			effects.add(new Effect(EffectType.Short, Effects.MATK, Short.parseShort(value)));
		} else if (key.equals("mdd")) {
			effects.add(new Effect(EffectType.Short, Effects.MDEF, Short.parseShort(value)));
		} else if (key.equals("acc")) {
			effects.add(new Effect(EffectType.Short, Effects.ACCY, Short.parseShort(value)));
		} else if (key.equals("eva")) {
			effects.add(new Effect(EffectType.Short, Effects.AVOID, Short.parseShort(value)));
		} else if (key.equals("hp")) {
			effects.add(new Effect(EffectType.Short, Effects.HP, Short.parseShort(value)));
		} else if (key.equals("mp")) {
			effects.add(new Effect(EffectType.Short, Effects.MP, Short.parseShort(value)));
		} else if (key.equals("speed")) {
			effects.add(new Effect(EffectType.Short, Effects.SPEED, Short.parseShort(value)));
		} else if (key.equals("jump")) {
			effects.add(new Effect(EffectType.Short, Effects.JUMP, Short.parseShort(value)));
		} else if (key.equals("attackCount")) {
			effects.add(new Effect(EffectType.Byte, Effects.ATTACK_COUNT, Byte.parseByte(value)));
		} else if (key.equals("bulletCount")) {
			effects.add(new Effect(EffectType.Byte, Effects.BULLET_COUNT, Byte.parseByte(value)));
		} else if (key.equals("itemCon")) {
			effects.add(new Effect(EffectType.Integer, Effects.ITEM_CONSUME, Integer.parseInt(value)));
		} else if (key.equals("itemConNo")) {
			effects.add(new Effect(EffectType.Byte, Effects.ITEM_CONSUME_COUNT, Byte.parseByte(value)));
		} else if (key.equals("bulletConsume")) {
			effects.add(new Effect(EffectType.Short, Effects.BULLET_CONSUME, Short.parseShort(value)));
		} else if (key.equals("moneyCon")) {
			effects.add(new Effect(EffectType.Short, Effects.MONEY_CONSUME, Short.parseShort(value)));
		} else if (key.equals("morph")) {
			effects.add(new Effect(EffectType.Integer, Effects.MORPH, Integer.parseInt(value)));
		} else if (key.equals("limit")) {
			effects.add(new Effect(EffectType.Short, Effects.LIMIT, Short.parseShort(value)));
		} else if (key.equals("summonEffect")) {
			effects.add(new Effect(EffectType.Byte, Effects.SUMMON_EFFECT, Byte.parseByte(value)));
		} else if (Converter.isNumber(key)) {
			//as of GMS v0.62, Shout level 4's prop is glitched, but we can't
			//check that here, so we'll have to make a special case in the Kvj
			//parser. :(
			effects.add(new Effect(EffectType.Summon, Effects.SUMMON, Byte.parseByte(key), Integer.parseInt(value)));
		}
	}

	@Override
	public int size() {
		int sum = 0;
		for (Effect e : effects) {
			switch (e.t) {
				case Integer:
					sum += Size.INT + Size.BYTE;
					break;
				case Short:
					sum += Size.SHORT + Size.BYTE;
					break;
				case Byte:
					sum += Size.BYTE + Size.BYTE;
					break;
				case Point:
					sum += Size.BYTE + 2 * Size.SHORT;
					break;
				case Summon:
					sum += Size.BYTE + Size.BYTE + Size.INT;
					break;
			}
		}
		return sum;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		for (Effect e : effects) {
			switch (e.t) {
				case Integer:
					lew.writeByte(e.e).writeInt(e.i);
					break;
				case Short:
					lew.writeByte(e.e).writeShort(e.s);
					break;
				case Byte:
					lew.writeByte(e.e).writeByte(e.b);
					break;
				case Point:
					lew.writeByte(e.e).writeShort((short) e.p.x).writeShort((short) e.p.y);
					break;
				case Summon:
					lew.writeByte(e.e).writeByte(e.b).writeInt(e.i);
					break;
			}
		}
	}
}
