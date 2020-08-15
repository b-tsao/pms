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
 * (Reactor 2202002)
 *   Hidden Street: Abandoned Tower<Stage 2> (Map 922010200)
 *   Hidden Street: Abandoned Tower<Stage 3> (Map 922010300)
 *   Hidden Street: Abandoned Tower<Stage 5> (Map 922010500)
 *
 * Ludibrium Party Quest drop passes in second stage, and fifth stage, spawn Bloctopus in third stage.
 *
 * @author Neuro
 */

switch (map.getId()) {
    case 922010300:
        map.setNoSpawn(false);
        for (let amount = 0; amount < 3; amount++) {
            reactor.spawnMob(9300171);
        }
        break;
    default:
        reactor.dropItems(0, 0, 0, 4001022, 1000000);
}