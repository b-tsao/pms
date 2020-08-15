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
 * next00 [custom]
 *   Hidden Street: Abandoned Tower <Stage 1> (Map 922010100),
 *   Hidden Street: Abandoned Tower <Stage 2> (Map 922010200),
 *   Hidden Street: Abandoned Tower <Stage 3> (Map 922010300),
 *   Hidden Street: Abandoned Tower <Stage 4> (Map 922010400),
 *   Hidden Street: Abandoned Tower <Stage 5> (Map 922010500),
 *   Hidden Street: Abandoned Tower <Stage 6> (Map 922010600),
 *   Hidden Street: Abandoned Tower <Stage 7> (Map 922010700),
 *   Hidden Street: Abandoned Tower <Stage 8> (Map 922010800),
 *   Hidden Street: A Crack on the Wall (Map 922010900)
 *
 * Ludibrium PQ portals.
 * Overridden from script-less portals so entry may be blocked when a stage is
 * not cleared.
 *
 * @author Neuro
 */

let stage = (map.getId() - 922010000) / 100;
let clear = portal.getEvent("party2").getVariable(stage + "stageclear");
if (clear != null && clear) {
	portal.playSoundEffect();
	player.changeMap(portal.getEvent("party2").getVariable("party2stage" + stage + 1), "st00");
} else {
	portal.block();
}