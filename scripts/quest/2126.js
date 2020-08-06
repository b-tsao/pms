
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
		if (status == 0) { {
		qm.sendNext("What is this?");
		} else if (status == 1) {
			qm.sendNextPrev("This is from a dog called Moppie?");
		} else if (status == 2) {
			qm.forceCompleteQuest();
			qm.dispose();
		}
	}
}