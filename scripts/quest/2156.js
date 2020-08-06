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
			qm.sendNext("Did you get the shell!?");
		} else if (status == 1) {
			if(qm.getPlayer().getMorphState() == 2210006) {
			qm.sendNext("...hey wait a minute...");
			status++;
			} else {
			qm.sendNext("NO WAY! Thank you friend! It must've been difficult to defeat Mano for this!!!");
			}
		} else if (status == 2) {
			qm.gainExp(7500*8);
			qm.gainFame(3);
			qm.forceCompleteQuest(2156);
			qm.gainItem(2210006, -1);
			qm.dispose();
		} else if (status == 3) {
			qm.sendOk("You jerk I wanted to use that shell! *sniff*");
			qm.gainExp(10000*8);
			qm.gainFame(-1);
			qm.forceCompleteQuest(2156);
			qm.dispose();
		}
}
}