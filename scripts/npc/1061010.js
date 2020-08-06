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


function start() {
    var eim = cm.getChar().getEventInstance();
    if (eim != null) {
    	eim.stopEventTimer();
        if (cm.getPlayer().getMapId() == 108010201) {
            cm.warp(100040106, 0);
        } else if (cm.getPlayer().getMapId() == 108010301) {
            cm.warp(105070001, 0);
        } else if (cm.getPlayer().getMapId() == 108010401) {
            cm.warp(107000402, 0);
        } else if (cm.getPlayer().getMapId() == 108010101) {
            cm.warp(105040305, 0);
        } else if (cm.getPlayer().getMapId() == 108010501) {
            cm.warp(105070001, 0);
        } else {
            cm.sendOk("INCORRECT MAP.");
        }
	cm.dispose();
    } else {
        cm.sendOk("ERROR - EIM IS CONSIDERED NULL WHEN IT SHOULDN'T BE! PERHAPS YOU ARE ON THE WRONG MAP?");
    }
}

function action(mode, type, selection) {
}
