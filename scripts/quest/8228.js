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
			qm.sendNext("May I see that Coded Communique quickly?");
		} else if (status == 1) {
			qm.sendNext("The Hyper Glyph's work to no-avail on this. Maybe bring it #bto other experts in the town#k about this type of material?");
		} else if (status == 2) {	
			qm.forceStartQuest();
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
			qm.sendNext("..Ah yes it is you, traveler. Thank you for helping me.");
		} else if (status == 1) {
			qm.sendNext("What have you brought me?");
		} else if (status == 2) {	
			qm.gainItem(4032018, 1);
			qm.forceCompleteQuest(8228);
			qm.dispose();
		}
	}
}