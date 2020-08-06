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

/*
-- JavaScript -----------------
Muirhat - Nautilus' Port
-- Created By --
Cody/Cyndicate
-- Function --
No specific function, useless text.
-- GMS LIKE AND HORRIBLE--
V2 by Moogra
-- SHORTENED BY 3 TIMES -- 
V3 by Vintage
--Actual usage..--
*/
importPackage(net.sf.odinms.client);
var status;

function start() {
 	status = -1;
	action(1, 0, 0);
}
function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.sendOk("Hmm... I guess you still have things to do here?");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
			if (status == 0) {
				if (cm.getQuestStatus(2175).equals(MapleQuestStatus.Status.STARTED)) {
				cm.warp(912000000);
				cm.dispose();
				} else {
				cm.sendOk("The Black Magician won't get away with messing with #bThe Nautilus!#k");
				}
		}
	}
}