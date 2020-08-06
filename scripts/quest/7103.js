var status = -1;

function start(mode, type, selection) {
	qm.dispose();
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
			if(qm.haveItem(4031172)) {
				qm.gainItem(1102057, 1);
				qm.gainExp(78000);
				qm.gainMeso(150000);
				qm.forceCompleteQuest(7103);
				qm.dispose();
			} else {
				qm.sendOk("You don't have a Ludibrum Medal..");
				qm.dispose();
			}
		} else {
			qm.dispose();
		}
	}
}