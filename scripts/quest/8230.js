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
			qm.sendNext("To get into the keep we're going to need to find someone who can help us do that. ");
		} else if (status == 1) {
			qm.sendNext("Do you know of #banyone who knows something about the Keep?#k");
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
			qm.sendNext("Did you find anything of use?");
		} else if (status == 1) {	
			qm.sendOk("Impressive! You got this keystone from #bLukan#k?");
			qm.forceCompleteQuest(8230);
			qm.dispose();
		}
	}
}