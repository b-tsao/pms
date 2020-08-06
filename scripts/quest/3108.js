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
			qm.sendNext("(Sculptures are beautiful.... As is made clear in the ice, but ice seems rather worn down. look around the periphery closer look at the statue.)");
		} else if (status == 1) {
			qm.sendNextPrev("(There seems to be a lot of big footprints around the statue... along with broken pieces)");
		} else if (status == 2) {
			qm.gainExp(1200);
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {
}