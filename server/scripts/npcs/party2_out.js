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

/**
 * Sgt. Anderson (NPC 2040047)
 *   Hidden Street: Abandoned Tower <Stage 1> (Map 922010100),
 *   Hidden Street: Abandoned Tower <Stage 2> (Map 922010200),
 *   Hidden Street: Abandoned Tower <Stage 3> (Map 922010300),
 *   Hidden Street: Abandoned Tower <Stage 4> (Map 922010400),
 *   Hidden Street: Abandoned Tower <Stage 5> (Map 922010500),
 *   Hidden Street: Abandoned Tower <Stage 6> (Map 922010600),
 *   Hidden Street: Abandoned Tower <Stage 7> (Map 922010700),
 *   Hidden Street: Abandoned Tower <Stage 8> (Map 922010800),
 *   Hidden Street: Abandoned Tower <Determine to adventure> (Map 922011100),
 *   Hidden Street: Abandoned Tower <Bonus> (Map 922011000),
 *   Hidden Street: Abandoned Tower <End of Journey> (Map 922010000)
 *
 * Exit NPC to forfeit the Ludibrium party quest.
 *
 * @author Neuro
 */

//TODO: GMS-like conversation
if (map.getId() == 922010000) {
	npc.sayNext("See you next time.");
	player.changeMap(221024500, "mid00"); //TODO: shouldn't this be a random portal in Kerning?
	player.loseItem(4001022);
} else {
	let str;
	if (map.getId() == 922011000)
		str = "Are you ready to leave this map?";
	else
		str = "Once you leave the map, you'll have to restart the whole quest if you want to try it again.  Do you still want to leave this map?";
	if (npc.askYesNo(str) == 1)
		player.changeMap(922010000, "st00");
}