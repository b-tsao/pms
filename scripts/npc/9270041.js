function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.sendNext("I am here for a long time. Please talk to me again when you change your mind.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("Hello I can fly you to Singapore for a cheap price of #b20,000 mesos#k. Would you like to go to Singapore now?");
		} else if (status == 1) {
			if(cm.getMeso() < 20000) {
				cm.sendOk("I don't think you have enough money...");
			} else {
				cm.gainMeso(-20000);
				cm.warp(540010000);
			}
			cm.dispose();
		}
	}
}