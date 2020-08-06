function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status == 0 && mode == 0) {
			cm.dispose();
			return;
		} else if (status <= 2 && mode == 0) {
			cm.sendNext("You must have some business to take care of here.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
				cm.sendSimple("I can take you places for a ticket! \r\n\r\n#L0##bLet's go to Kerning City!#l");
		} else if (status == 1) {
			if (selection == 0) {
				cm.sendYesNo("Alright then, would you like to head over to Kerning right now?");
			} else {
				cm.sendSimple("Ok, I can't force you.")
			}
		} else if (status == 2) {
			if (cm.haveItem(4031713)) {
				cm.gainItem(4031713,-1);
				cm.warp(103000000);
				cm.dispose();
			} else {
             cm.sendNext("You need a ticket!");
				cm.dispose();
			}
		} 
	}
}
