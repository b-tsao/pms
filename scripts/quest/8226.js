/* Author: Xterminator (Modified by RMZero213)
	NPC Name: 		Roger
	Map(s): 		Maple Road : Lower level of the Training Camp (2)
	Description: 		Quest - Roger's Apple
*/
var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			qm.sendNext("I have a new task for you.");
		} else if (status == 1) {
			qm.sendNext("#bElder Wraiths#k have been plaguing this area for a long time. These souls were not always violent and vicious. I believe with a proper burial they will return to normal... I need you to get 50 #i4032010# for me to do this. This will not be an easy task to undergo, be aware.");
		} else if (status == 2) {
			qm.forceStartQuest();
			qm.sendOk("Thank you for this help");
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			if (qm.getPlayer().getHp() <= 1) {
				qm.sendNext("Hey, your HP is not fully recovered yet. Did you take all the Roger's Apple that I gave you? Are you sure?");
				qm.dispose();
			} else {
				qm.sendNext("I have found the keystone.");}
		
		} else if (status == 1) {
			qm.sendNextPrev("Here is your reward");
			} else if (status == 2) {
			qm.gainExp(85000);
			qm.forceCompleteQuest(8222);
			qm.dispose();
		}
	}
}