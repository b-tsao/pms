var status = -1;

function start(mode, type, selection) {
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
			qm.sendNext("Are you ready to be sent to the secret group's area?");
		} else if (status == 1) {
			qm.sendNextPrev("I have warned you.");
		} else if (status == 2) {
			qm.warp(260000200);
			qm.forceCompleteQuest(2127);
			qm.dispose();
		}
	}
}