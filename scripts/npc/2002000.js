/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/* Rupi
Happyville Warp NPC
*/
var status;

function start() {
	status = 0;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
		cm.dispose();
		return;
    }
    if (status == 1) {
		if (cm.getPlayer().getMapId() != 209000000) {
			cm.sendAcceptDecline("I can take you to Happyville if you want!");
		} else {
			cm.sendAcceptDecline("Did you want to go back to Henesys?");
		}
    } else if (status == 2) {
		if (cm.getPlayer().getMapId() != 209000000) {
			cm.warp(209000000);
			cm.dispose();
		} else {
			cm.warp(100000000);
			cm.dispose();
		}
	}
}