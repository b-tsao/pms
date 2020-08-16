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
 * in01-in05 [custom]
 *   Hidden Street: Darkness in the Tower (Map 922010401),
 *   Hidden Street: Darkness in the Tower (Map 922010402),
 *   Hidden Street: Darkness in the Tower (Map 922010403),
 *   Hidden Street: Darkness in the Tower (Map 922010404),
 *   Hidden Street: Darkness in the Tower (Map 922010405),
 *
 * Ludibrium PQ Darkness in the Tower portals.
 * Overridden from script-less portals so entry leads to instanced map.
 *
 * @author Neuro
 */

portal.playSoundEffect();
if (map.getId() > 922010200 && map.getId() < 922010300) {
    player.changeMap(portal.getEvent("party2").getVariable("party2stage2"), "st00");
} else if (map.getId() == 922010400) {
    player.changeMap(portal.getEvent("party2").getVariable("party2darkness" + (portal.getId() - 1)), "st00");
} else if (map.getId() > 922010400 && map.getId() < 922010500) {
    player.changeMap(portal.getEvent("party2").getVariable("party2stage4"), "in0" + (map.getId() % 10));
} else if (map.getId() == 922010500) {
    player.changeMap(portal.getEvent("party2").getVariable("party2portal" + (portal.getId() - 1)), "st00");
} else if (map.getId() > 922010500 && map.getId() < 922010600) {
    player.changeMap(portal.getEvent("party2").getVariable("party2stage5"), "in0" + (map.getId() % 10));
}