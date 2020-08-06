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
				qm.sendNext("Pia asked me to go to the thicket around the beach and take out Mano. I'm to bring back Rainbow-colored Snail Shell. So you get to stay here while I do all the work for you again? Don't you think this is a bit too much? I might use all of the Rainbow-colored Snail Shell for myself!");
		} else if (status == 1) {
			qm.sendNextPrev("Rainbow-colored Snail Shell wasn't an item that makes dreams come true. It turns people who use it into snails. Phew... learned a good lesson not to believe in rumors, otherwise I would be the one crawling about.");
		} else if (status == 2) {
			qm.gainMeso(120000);
			qm.gainFame(3);
			qm.gainExp(56500);
			qm.forceCompleteQuest();
			qm.dispose();
		}
	}
}