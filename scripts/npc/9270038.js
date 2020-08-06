function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.sendNext("I see, if you are staying longer you should take a tour of the haunted Ghost Ship!");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("Hello I can fly you back to Kerning for a cheap price of \r\n\#b20,000 mesos#k. Would you like to go to Kerning now?");
		} else if (status == 1) {
			if(cm.getMeso() < 20000) {
				cm.sendOk("I don't think you have enough money...");
			} else {
				cm.gainMeso(-20000);
				cm.warp(103000000);
			}
			cm.dispose();
		}
	}
}