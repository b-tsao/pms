//Showa (M) Gachaphon

importPackage(net.sf.odinms.client);



var status = 0;
var itemchance = Math.floor(Math.random()*145+1);
var itemamount = Math.floor(Math.random()*50+1);


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
				cm.sendNext("I am #bSpa (Male)#k gachapon.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
			}
		else if (status == 1) {
		if (cm.haveItem(5220000) && cm.canHold(1000000) && cm.canHold(2000000) && cm.canHold(3990000) && cm.canHold(4000000)) {
			cm.sendYesNo("I see you have a ticket of mine, do you wish to use it?");
			}
			else if (!cm.haveItem(5220000)) {
			cm.sendOk("You dont have any #bgachapon tickets#k.");
			cm.dispose();
			}
			else {
			cm.sendOk("You need to make some inventory space.");
			cm.dispose();
			}
		}
		else if (status == 2) {
			cm.gainItem(5220000, -1);
			if ((itemchance >= 1) && (itemchance <= 10)) {
			cm.gainItem(2000000, itemamount);
			}
			else if ((itemchance >= 11) && (itemchance <= 20)) {
			cm.gainItem(2000001, itemamount);
			}
			else if ((itemchance >= 21) && (itemchance <= 30)) {
			cm.gainItem(2000002, itemamount);
			}
			else if ((itemchance >= 31) && (itemchance <= 40)) {
			cm.gainItem(2000006, itemamount);
			}
			else if ((itemchance >= 41) && (itemchance <= 45)) {
			cm.gainItem(2000003, itemamount);
			}
			else if ((itemchance >= 46) && (itemchance <= 50)) {
			cm.gainItem(2000004, itemamount);
			}
			else if ((itemchance >= 51) && (itemchance <= 55)) {
			cm.gainItem(1302022, 1, true, true);
			}
			else if ((itemchance >= 56) && (itemchance <= 61)) {
			cm.gainItem(2001002, 1, itemamount);
			}
			else if ((itemchance >= 62) && (itemchance <= 65)) {
			cm.gainItem(2001001, 1, itemamount);
			}
			else if (itemchance == 66) {
			cm.gainItem(2022002, 1, true, true);
			}
			else if (itemchance == 67) {
			cm.gainItem(2022002, 1, true, true);
			}
			else if (itemchance == 69) {
			cm.gainItem(1012076, 1, true, true);
			cm.gachNotice(cm.getC(), 1012076);
			}
			else if (itemchance == 70) {
			cm.gainItem(2022178, 1, true, true);
			}
			else if (itemchance == 71) {
			cm.gainItem(2040316, 1, true, true);
			}
			else if (itemchance == 72) {
			cm.gainItem(2040532, 1, true, true);
			}
			else if (itemchance == 73) {
			cm.gainItem(1402014, 1, true, true);
			}
			else if (itemchance == 74) {
			cm.gainItem(1402044, 1, true, true);
			}
			else if (itemchance == 75) {
			cm.gainItem(1402029, 1, true, true);
			}
			else if (itemchance == 76) {
			cm.gainItem(1442061, 1, true, true);
			}
			else if (itemchance == 77) {
			cm.gainItem(2044311, 1, true, true);
			}
			else if (itemchance == 78) {
			cm.gainItem(2044312, 1, true, true);
			}
			else if (itemchance == 79) {
			cm.gainItem(2048010, 1, true, true);
			}	
			else if (itemchance == 80) {
			cm.gainItem(2048011, 1, true, true);
			}	
			else if (itemchance == 81) {
			cm.gainItem(2048012, 1, true, true);
			}
			else if (itemchance == 82) {
			cm.gainItem(2048013, 1, true, true);
			}
			else if (itemchance == 83) {
			cm.gainItem(2044313, 1, true, true);
			}	
			else if (itemchance == 84) {
			cm.gainItem(2100006, 1, true, true);
			}
			else if (itemchance == 85) {
			cm.gainItem(2100007, 1, true, true);
			}
			else if (itemchance == 86) {
			cm.gainItem(1082179, 1, true, true);
			}
			else if (itemchance == 87) {
			cm.gainItem(1050127, 1, true, true);
			}	
			else if (itemchance == 88) {
			cm.gainItem(1051140, 1, true, true);
			}
			else if (itemchance == 89) {
			cm.gainItem(1050018, 1, true, true);
			}
			else if (itemchance == 90) {
			cm.gainItem(1051017, 1, true, true);
			}
			else if (itemchance == 91) {
			cm.gainItem(1102082, 1, true, true);
			cm.gachNotice(cm.getC(), 1102082);
			}
			else if (itemchance == 92) {
			cm.gainItem(1102085, 1, true, true);
			}
			else if (itemchance == 93) {
			cm.gainItem(2040320, 1, true, true);
			}
			else if (itemchance == 94) {
			cm.gainItem(2040508, 1, true, true);
			}
			else if (itemchance == 95) {
			cm.gainItem(1472054, 1, true, true);
			}
			else if (itemchance == 96) {
			cm.gainItem(2040531, 1, true, true);
			}
			else if (itemchance == 97) {
			cm.gainItem(1332030, 1, true, true);
			}
			else if (itemchance == 98) {
			cm.gainItem(2040028, 1, true, true);
			}
			else if (itemchance == 99) {
			cm.gainItem(2040030, 1, true, true);
			}
			else if (itemchance == 100) {
			cm.gainItem(2040318, 1, true, true);
			}
			else if (itemchance == 101) {
			cm.gainItem(2040323, 1, true, true);
			}
			else if (itemchance == 102) {
			cm.gainItem(2040418, 1, true, true);
			}
			else if (itemchance == 103) {
			cm.gainItem(2040419, 1, true, true);
			}
			else if (itemchance == 104) {
			cm.gainItem(2040501, 1, true, true);
			}
			else if (itemchance == 105) {
			cm.gainItem(2040531, 1, true, true);
			}
			else if (itemchance == 106) {
			cm.gainItem(2040533, 1, true, true);
			}
			else if (itemchance == 107) {
			cm.gainItem(2040625, 1, true, true);
			}
			else if (itemchance == 108) {
			cm.gainItem(2040626, 1, true, true);
			}
			else if (itemchance == 109) {
			cm.gainItem(2040627, 1, true, true);
			}
			else if (itemchance == 110) {
			cm.gainItem(2040810, 1, true, true);
			}
			else if (itemchance == 111) {
			cm.gainItem(2040930, 1, true, true);
			}
			else if (itemchance == 112) {
			cm.gainItem(2040932, 1, true, true);
			}
			else if (itemchance == 113) {
			cm.gainItem(2041020, 1, true, true);
			}
			else if (itemchance == 114) {
			cm.gainItem(2043001, 1, true, true);
			}
			else if (itemchance == 115) {
			cm.gainItem(2043002, 1, true, true);
			}
			else if (itemchance == 116) {
			cm.gainItem(2043101, 1, true, true);
			}
			else if (itemchance == 117) {
			cm.gainItem(2043101, 1, true, true);
			}
			else if (itemchance == 118) {
			cm.gainItem(2043201, 1, true, true);
			}
			else if (itemchance == 119) {
			cm.gainItem(2043201, 1, true, true);
			}
			else if (itemchance == 120) {
			cm.gainItem(2043202, 1, true, true);
			}
			else if (itemchance == 121) {
			cm.gainItem(2043202, 1, true, true);
			}
			else if (itemchance == 122) {
			cm.gainItem(2044001, 1, true, true);
			}
			else if (itemchance == 123) {
			cm.gainItem(2044002, 1, true, true);
			}
			else if (itemchance == 124) {
			cm.gainItem(2044601, 1, true, true);
			}
			else if (itemchance == 125) {
			cm.gainItem(2044602, 1, true, true);
			}
			else if (itemchance == 126) {
			cm.gainItem(2044701, 1, true, true);
			}
			else if (itemchance == 127) {
			cm.gainItem(2044702, 1, true, true);
			}
			else if (itemchance == 128) {
			cm.gainItem(2044901, 1, true, true);
			}
			else if (itemchance == 129) {
			cm.gainItem(2044902, 1, true, true);
			}
			else if (itemchance == 130) {
			cm.gainItem(2048011, 1, true, true);
			}
			else if (itemchance == 131) {
			cm.gainItem(1012079, 1, true, true);
			cm.gachNotice(cm.getC(), 1012079);
			}
			else if (itemchance == 132) {
			cm.gainItem(1002418, 1, true, true);
			}
			else if (itemchance == 133) {
			cm.gainItem(1002418, 1, true, true);
			}
			else if (itemchance == 134) {
			cm.gainItem(1002391, 1, true, true);
			}
			else if (itemchance == 135) {
			cm.gainItem(1082150, 1, true, true);
			}
			else if (itemchance == 136) {
			cm.gainItem(1302031, 1, true, true);
			}
			else if (itemchance == 137) {
			cm.gainItem(1312014, 1, true, true);
			}
			else if (itemchance == 138) {
			cm.gainItem(1452053, 1, true, true);
			}
			else if (itemchance == 139) {
			cm.gainItem(1012110, 1, true, true);
			cm.gachNotice(cm.getC(), 1012110);
			}
			else if (itemchance == 140) {
			cm.gainItem(1032020, 1, true, true);
			}
			else if (itemchance == 141) { 
			cm.gainItem(1032021, 1, true, true);
			}
			else if (itemchance == 142) { 
			cm.gainItem(1032022, 1, true, true);
			}
			else if (itemchance == 143) { 
			cm.gainItem(1032023, 1, true, true);
			}
			else if (itemchance == 144) { 
			cm.gainItem(1032020, 1, true, true);
			}
			else if (itemchance == 145) { 
			cm.gainItem(1092047, 1, true, true);
			}
			else if (itemchance == 146) { 
			cm.gainItem(3010018, 1, true, true);
			cm.gachNotice(cm.getC(), 3010018);
			}
			cm.dispose();

		}
	}
}