//Ridley Boss ETC Exchange

importPackage(net.sf.odinms.client);



var status = 0;
var itemchance = Math.floor(Math.random()*5+1);


function start() {
	status = -1;
	action(1, 0, 0);
}


function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 2 && mode == 0) {
			cm.sendOk("See you next time, when you try your luck here~!");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
		 cm.sendSimple("BZZT~ I am #bT-1337#k! If you get me some fuel to help recharge myself I will give you something special!\r\n\r\n#b#L0##i4032022# Diferium Fuel Cell#l\r\n#L1##i4032021# Triferium Fuel Cell#k#l");
			}
		else if (status == 1) {
			if (selection == 0) {
			if (cm.haveItem(4032022)) {
			cm.gainItem(4032022, -1);
			if (itemchance == 1) {
			cm.gainItem(2022341, 1);
			}
			else if (itemchance == 2) {
			cm.gainItem(2022344, 1);
			}
			else if (itemchance == 3) {
			cm.gainItem(2022342, 1);
			}
			else if (itemchance == 4) {
			cm.gainItem(2022339, 1);
			}
			else if (itemchance == 5) {
			cm.gainItem(2022338, 1);
			}
			cm.dispose();
			}
			else if (!cm.haveItem(4032022)) {
			cm.sendOk("You dont have a #bDiferium Fuel Cell#k.");
			cm.dispose();
			}
			}
			else if (selection == 1) {
			if (cm.haveItem(4032021)) {
			cm.gainItem(4032021, -1);
			if (itemchance == 1) {
			cm.gainItem(2022341, 1);
			}
			else if (itemchance == 2) {
			cm.gainItem(2022344, 1);
			}
			else if (itemchance == 3) {
			cm.gainItem(2022342, 1);
			}
			else if (itemchance == 4) {
			cm.gainItem(2022339, 1);
			}
			else if (itemchance == 5) {
			cm.gainItem(2022338, 1);
			}
			cm.dispose();
			}
			else if (!cm.haveItem(4032021)) {
			cm.sendOk("You dont have a #bTriferium Fuel Cell#k.");
			cm.dispose();
			}
		}
		}
	}
}