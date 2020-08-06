
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
			qm.sendNext("I can't believe it, someone else heard about it?");
		} else if (status == 1) {
			qm.sendNextPrev("Tae Gong, who's in the middle of the ocean fishing on a raft, was concerned that he couldn't gather up #t4000166# for his wife. I made a deal with him to gather up #b20 #t4000166#s#k for him and deliver them to his wife #bGrandma Yeon#k at the Korean Folk Town.\n\n#t4000166# #b#c4000166##k/20");
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