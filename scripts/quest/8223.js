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
			qm.sendNext("Do you really think charging the inner sanctum of the Keep is a smart idea..?");
		} else if (status == 1) {
			qm.sendNext("I refuse to be part of this pointless suicide mission. If you can prove to me you can hold your own and fight the inner army of the Keep, I will help you out and give you the #bKeystone#k to get in...");
		} else if (status == 2) {	
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {
}