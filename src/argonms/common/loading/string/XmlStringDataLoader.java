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

package argonms.common.loading.string;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import argonms.common.util.XmlStreamReader;

/**
 *
 * @author Neuro
 */
public class XmlStringDataLoader extends StringDataLoader {
	private final String dataPath;

	protected XmlStringDataLoader(String wzPath) {
		this.dataPath = wzPath;
	}

	@Override
	public boolean loadAll() {
		String dir = dataPath + "String.wz" + File.separatorChar;
		XmlStreamReader reader;
		Integer key;
		String str;
		try {
			for (String s : new String[] { "Cash", "Consume", "Ins", "Pet" }) {
				/* TODO
				 * Cash has msgIn
				 * Pet has descD
				 * */
				reader = new XmlStreamReader(new FileInputStream(new File(dir + s + ".img.xml")));
				reader.next();
				for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
					key = Integer.valueOf(reader.get("name"));
					for (boolean childHasNext = reader.in(); childHasNext; childHasNext = reader.next()) {
						str = reader.get("value");
						switch (reader.get("name")) {
							case "name":
								itemNames.put(key, str);
								break;
							case "desc":
								itemDescs.put(key, str);
								break;
							case "msg":
								itemMsgs.put(key, str);
								break;
						}
					}
					reader.out();
				}
				reader.close();
			}
			
			reader = new XmlStreamReader(new FileInputStream(new File(dir + "Eqp.img.xml")));
			reader.next(); // Eqp.img
			reader.in(); // Eqp
			for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
				for (boolean childHasNext = reader.in(); childHasNext; childHasNext = reader.next()) {
					key = Integer.valueOf(reader.get("name"));
					reader.in();
					str = reader.get("value");
					itemNames.put(key, str);
					reader.out();
				}
				reader.out();
			}
			reader.close();
			
			reader = new XmlStreamReader(new FileInputStream(new File(dir + "Etc.img.xml")));
			reader.next(); // Etc.img
			reader.in(); // Etc
			for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
				key = Integer.valueOf(reader.get("name"));
				reader.in();
				str = reader.get("value");
				itemNames.put(key, str);
				reader.next();
				str = reader.get("value");
				itemDescs.put(key, str);
				reader.out();
			}
			reader.out();
			// Extra Etcs
			for (boolean hasNext = reader.next(); hasNext; hasNext = reader.next()) {
				key = Integer.valueOf(reader.get("name"));
				reader.in();
				str = reader.get("value");
				itemNames.put(key, str);
				reader.next();
				str = reader.get("value");
				itemDescs.put(key, str);
				reader.out();
			}
			reader.close();
			
			reader = new XmlStreamReader(new FileInputStream(new File(dir + "Map.img.xml")));
			reader.next(); // Map.img
			for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
				for (boolean childHasNext = reader.in(); childHasNext; childHasNext = reader.next()) {
					key = Integer.valueOf(reader.get("name"));
					for (boolean childChildHasNext = reader.in(); childChildHasNext; childChildHasNext = reader.next()) {
						str = reader.get("value");
						switch (reader.get("name")) {
							case "streetName":
								streetNames.put(key, str);
								break;
							case "mapName":
								mapNames.put(key, str);
								break;
							case "mapDesc":
								// TODO
								break;
							case "help0":
								// TODO
								break;
							case "help1":
								// TODO
								break;
						}
					}
					reader.out();
				}
				reader.out();
			}
			reader.close();
			
			reader = new XmlStreamReader(new FileInputStream(new File(dir + "Mob.img.xml")));
			reader.next(); // Mob.img
			for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
				key = Integer.valueOf(reader.get("name"));
				reader.in();
				str = reader.get("value");
				mobNames.put(key, str);
				reader.out();
			}
			reader.close();
			
			reader = new XmlStreamReader(new FileInputStream(new File(dir + "Npc.img.xml")));
			reader.next(); // Npc.img
			for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
				key = Integer.valueOf(reader.get("name"));
				for (boolean childHasNext = reader.in(); childHasNext; childHasNext = reader.next()) {
					str = reader.get("value");
					switch (reader.get("name")) {
						case "name":
							npcNames.put(key, str);
							break;
						// TODO many other options (n0, n1, ..., d0, d1, ..., s0, s1, ...
					}
				}
				reader.out();
			}
			reader.close();
			
			reader = new XmlStreamReader(new FileInputStream(new File(dir + "Skill.img.xml")));
			reader.next(); // Skill.img
			for (boolean hasNext = reader.in(); hasNext; hasNext = reader.next()) {
				key = Integer.valueOf(reader.get("name"));
				for (boolean childHasNext = reader.in(); childHasNext; childHasNext = reader.next()) {
					str = reader.get("value");
					switch (reader.get("name")) {
						case "bookName":
						case "name":
							skillNames.put(key, str);
							break;
						// TODO many other options desc, h1, h2, h3, ...
					}
				}
				reader.out();
			}
			reader.close();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
}
