/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
					   Matthias Butz <matze@odinms.de>
					   Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
-- Odin JavaScript ---------------------------------------------------------------------------
	Tangyoon - Nautilus Cook
-- By ---------------------------------------------------------------------------------------------
	xQuasar
**/
importPackage(net.sf.odinms.client);
function start() {
	if (cm.getQuestStatus(2180).equals(MapleQuestStatus.Status.STARTED) && cm.haveItem(4031847,1)) {
		cm.gainItem(4031848, 1);
		cm.gainItem(4031847, -1);
	} else if (cm.getQuestStatus(2180).equals(MapleQuestStatus.Status.STARTED) && cm.haveItem(4031848,1)) {
		cm.gainItem(4031849, 1);
		cm.gainItem(4031848, -1);
	} else if (cm.getQuestStatus(2180).equals(MapleQuestStatus.Status.STARTED) && cm.haveItem(4031849,1)) {
		cm.gainItem(4031850, 1);
		cm.gainItem(4031849, -1);
	} if (cm.getQuestStatus(2180).equals(MapleQuestStatus.Status.STARTED) && !cm.haveItem(4031847) && !cm.haveItem(4031848) && !cm.haveItem(4031849) && !cm.haveItem(4031850)) {
		cm.sendOk("MOOOOO....");
	} else {
		cm.sendOk("MOOOOO....");
	}
	cm.dispose();
}

function action(mode, selection, type) {
cm.dispose();
}