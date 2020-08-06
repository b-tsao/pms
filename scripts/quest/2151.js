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
			qm.sendNext("Hey there! I don't really remember much about the #rrumor#k...");
		} else if (status == 1) {
			qm.sendNextPrev("I remember as a kid I saw #rit#k but I thought it was my imagination...");
		} else if (status == 2) {
			qm.sendAcceptDecline("You may want to consider asking others.");
		} else if (status == 3) {
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {
}