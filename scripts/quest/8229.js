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
			qm.sendNext("Did you figure anything out about the Communique?");
		} else if (status == 1) {
			qm.sendNext("You figured out how to translate it?! You must be back to #bJack#k!");
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
			qm.sendNext("Hey there! What'd you bring back to me?!");
		} else if (status == 1) {
			qm.sendNext("You really are something special, " + qm.getPlayer().getName());
		} else if (status == 2) {	
			qm.gainExp(50000*8);
			qm.forceCompleteQuest(8229);
			qm.dispose();
		}
	}
}