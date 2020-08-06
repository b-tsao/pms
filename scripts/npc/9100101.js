// Ellinia Gachaphon

importPackage(net.sf.odinms.client);

var status = 0;
var itemchance = Math.floor(Math.random()*193+1);
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
				cm.sendNext("I am #bEllinia#k gachapon.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
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
			cm.gainItem(2000006, itemamount);
			}
			else if ((itemchance >= 11) && (itemchance <= 20)) {
			cm.gainItem(2100001, 1, true, true);
			}
			else if ((itemchance >= 21) && (itemchance <= 30)) {
			cm.gainItem(2002007, itemamount);
			}
			else if ((itemchance >= 31) && (itemchance <= 38)) {
			cm.gainItem(2002010, itemamount);
			}
			else if ((itemchance >= 39) && (itemchance <= 47)) {
			cm.gainItem(2000004, itemamount);
			}
			else if ((itemchance >= 48) && (itemchance <= 55)) {
			cm.gainItem(2000003, itemamount);
			}
			else if ((itemchance >= 56) && (itemchance <= 61)) {
			cm.gainItem(1050009, 1, true, true);
			}
			else if ((itemchance >= 61) && (itemchance <= 65)) {
			cm.gainItem(2022027, 1, true, true);
			}
			else if (itemchance == 66) {
			cm.gainItem(2022179, 1, true, true);
			cm.gachNotice(cm.getC(), 2022179);
			}
			else if (itemchance == 67) {
			cm.gainItem(2022273, 1, true, true);
			cm.gachNotice(cm.getC(), 2022273);
			}
			else if (itemchance == 69) {
			cm.gainItem(1122006, 1, true, true);
			}
			else if (itemchance == 70) {
			cm.gainItem(1122006, 1, true, true);
			}
			else if (itemchance == 71) {
			cm.gainItem(2040024, 1, true, true);
			}
			else if (itemchance == 72) {
			cm.gainItem(2040024, 1, true, true);
			}
			else if (itemchance == 73) {
			cm.gainItem(2040026, 1, true, true);
			}
			else if (itemchance == 74) {
			cm.gainItem(2040026, 1, true, true);
			}
			else if (itemchance == 75) {
			cm.gainItem(3010004, 1, true, true);
			}
			else if (itemchance == 76) {
			cm.gainItem(3010004, 1, true, true);
			}
			else if (itemchance == 77) {
			cm.gainItem(2049000, 1, true, true);
			}
			else if (itemchance == 78) {
			cm.gainItem(2049000, 1, true, true);
			}
			else if (itemchance == 79) {
			cm.gainItem(1102042, 1, true, true);
			cm.gachNotice(cm.getC(), 1102042);
			}	
			else if (itemchance == 80) {
			cm.gainItem(1102086, 1, true, true);
			cm.gachNotice(cm.getC(), 1102086);
			}	
			else if (itemchance == 81) {
			cm.gainItem(2100002, 1, true, true);
			}
			else if (itemchance == 82) {
			cm.gainItem(2100002, 1, true, true);
			}
			else if (itemchance == 83) {
			cm.gainItem(2100002, 1, true, true);
			}	
			else if (itemchance == 84) {
			cm.gainItem(1082148, 1, true, true);
			}
			else if (itemchance == 85) {
			cm.gainItem(1082148, 1, true, true);
			}
			else if (itemchance == 86) {
			cm.gainItem(1372039, 1, true, true);
			cm.gachNotice(cm.getC(), 1372039);
			}
			else if (itemchance == 87) {
			cm.gainItem(1372040, 1, true, true);
			cm.gachNotice(cm.getC(), 1372040);
			}	
			else if (itemchance == 88) {
			cm.gainItem(1372041, 1, true, true);
			cm.gachNotice(cm.getC(), 1372041);
			}
			else if (itemchance == 89) {
			cm.gainItem(1372042, 1, true, true);
			cm.gachNotice(cm.getC(), 1372042);
			}
			else if (itemchance == 90) {
			cm.gainItem(1050055, 1, true, true);
			}
			else if (itemchance == 91) {
			cm.gainItem(1062000, 1, true, true);
			}
			else if (itemchance == 92) {
			cm.gainItem(1382050, 1, true, true);
			}
			else if (itemchance == 93) {
			cm.gainItem(1302028, 1, true, true);
			}
			else if (itemchance == 94) {
			cm.gainItem(1372002, 1, true, true);
			}
			else if (itemchance == 95) {
			cm.gainItem(1002033, 1, true, true);
			}
			else if (itemchance == 96) {
			cm.gainItem(1372038, 1, true, true);
			}
			else if (itemchance == 97) {
			cm.gainItem(1372037, 1, true, true);
			}
			else if (itemchance == 98) {
			cm.gainItem(1092022, 1, true, true);
			}
			else if (itemchance == 99) {
			cm.gainItem(1092022, 1, true, true);
			}
			else if (itemchance == 100) {
			cm.gainItem(1032032, 1, true, true);
			cm.gachNotice(cm.getC(), 1032032);
			}
			else if (itemchance == 101) {
			cm.gainItem(1372017, 1, true, true);
			}
			else if (itemchance == 102) {
			cm.gainItem(1372011, 1, true, true);
			}
			else if (itemchance == 103) {
			cm.gainItem(1372031, 1, true, true);
			}
			else if (itemchance == 104) {
			cm.gainItem(1372033, 1, true, true);
			}
			else if (itemchance == 105) {
			cm.gainItem(1372015, 1, true, true);
			}
			else if (itemchance == 106) {
			cm.gainItem(1372009, 1, true, true);
			}
			else if (itemchance == 107) {
			cm.gainItem(1050009, 1, true, true);
			}
			else if (itemchance == 108) {
			cm.gainItem(1050025, 1, true, true);
			}
			else if (itemchance == 109) {
			cm.gainItem(1050025, 1, true, true);
			}
			else if (itemchance == 110) {
			cm.gainItem(1050003, 1, true, true);
			}
			else if (itemchance == 111) {
			cm.gainItem(1050003, 1, true, true);
			}
			else if (itemchance == 112) {
			cm.gainItem(1050027, 1, true, true);
			}
			else if (itemchance == 113) {
			cm.gainItem(1050027, 1, true, true);
			}
			else if (itemchance == 114) {
			cm.gainItem(1050029, 1, true, true);
			}
			else if (itemchance == 115) {
			cm.gainItem(1050029, 1, true, true);
			}
			else if (itemchance == 116) {
			cm.gainItem(1050031, 1, true, true);
			}
			else if (itemchance == 117) {
			cm.gainItem(1050031, 1, true, true);
			}
			else if (itemchance == 118) {
			cm.gainItem(1050039, 1, true, true);
			}
			else if (itemchance == 119) {
			cm.gainItem(1050039, 1, true, true);
			}
			else if (itemchance == 120) {
			cm.gainItem(1051026, 1, true, true);
			}
			else if (itemchance == 121) {
			cm.gainItem(1051026, 1, true, true);
			}
			else if (itemchance == 122) {
			cm.gainItem(1050056, 1, true, true);
			}
			else if (itemchance == 123) {
			cm.gainItem(1050056, 1, true, true);
			}
			else if (itemchance == 124) {
			cm.gainItem(1050055, 1, true, true);
			}
			else if (itemchance == 125) {
			cm.gainItem(1050055, 1, true, true);
			}
			else if (itemchance == 126) {
			cm.gainItem(1051046, 1, true, true);
			}
			else if (itemchance == 127) {
			cm.gainItem(1051046, 1, true, true);
			}
			else if (itemchance == 128) {
			cm.gainItem(1051047, 1, true, true);
			}
			else if (itemchance == 129) {
			cm.gainItem(1051047, 1, true, true);
			}
			else if (itemchance == 130) {
			cm.gainItem(2040817, 1, true, true);
			}
			else if (itemchance == 131) {
			cm.gainItem(1082145, 1, true, true);
			}
			else if (itemchance == 132) {
			cm.gainItem(1082175, 1, true, true);
			}
			else if (itemchance == 133) {
			cm.gainItem(2040024, 1, true, true);
			}
			else if (itemchance == 134) {
			cm.gainItem(2040025, 1, true, true);
			}
			else if (itemchance == 135) {
			cm.gainItem(2040026, 1, true, true);
			}
			else if (itemchance == 136) {
			cm.gainItem(2040205, 1, true, true);
			}
			else if (itemchance == 137) {
			cm.gainItem(2040206, 1, true, true);
			}
			else if (itemchance == 138) {
			cm.gainItem(2040301, 1, true, true);
			}
			else if (itemchance == 139) {
			cm.gainItem(2040302, 1, true, true);
			}
			else if (itemchance == 140) {
			cm.gainItem(2040305, 1, true, true);
			}
			else if (itemchance == 141) { 
			cm.gainItem(2040321, 1, true, true);
			}
			else if (itemchance == 142) { 
			cm.gainItem(2040323, 1, true, true);
			}
			else if (itemchance == 143) { 
			cm.gainItem(2040513, 1, true, true);
			}
			else if (itemchance == 144) { 
			cm.gainItem(2040514, 1, true, true);
			}
			else if (itemchance == 145) { 
			cm.gainItem(2040516, 1, true, true);
			}
			else if (itemchance == 146) { 
			cm.gainItem(2040517, 1, true, true);
			}
			else if (itemchance == 147) {
			cm.gainItem(2040816, 1, true, true);
			}
			else if (itemchance == 148) { 
			cm.gainItem(2040817, 1, true, true);
			}
			else if (itemchance == 149) { 
			cm.gainItem(2040919, 1, true, true);
			}
			else if (itemchance == 150) { 
			cm.gainItem(2040920, 1, true, true);
			}
			else if (itemchance == 151) { 
			cm.gainItem(2040924, 1, true, true);
			}
			else if (itemchance == 152) { 
			cm.gainItem(2040925, 1, true, true);
			}
			else if (itemchance == 153) { 
			cm.gainItem(2041016, 1, true, true);
			}
			else if (itemchance == 154) { 
			cm.gainItem(2041017, 1, true, true);
			}
			else if (itemchance == 155) { 
			cm.gainItem(2041022, 1, true, true);
			}
			else if (itemchance == 156) { 
			cm.gainItem(2041023, 1, true, true);
			}
			else if (itemchance == 157) { 
			cm.gainItem(2043008, 1, true, true);
			}
			else if (itemchance == 158) { 
			cm.gainItem(2043009, 1, true, true);
			}
			else if (itemchance == 159) { 
			cm.gainItem(2043701, 1, true, true);
			}
			else if (itemchance == 160) { 
			cm.gainItem(2043702, 1, true, true);
			}
			else if (itemchance == 161) { 
			cm.gainItem(2043801, 1, true, true);
			}
			else if (itemchance == 162) { 
			cm.gainItem(2043802, 1, true, true);
			}
			else if (itemchance == 163) { 
			cm.gainItem(2048011, 1, true, true);
			}
			else if (itemchance == 164) { 
			cm.gainItem(2048013, 1, true, true);
			}
			else if (itemchance == 165) { 
			cm.gainItem(1002083, 1, true, true);
			}
			else if (itemchance == 166) { 
			cm.gainItem(1002083, 1, true, true);
			}
			else if (itemchance == 167) { 
			cm.gainItem(1002586, 1, true, true);
			}
			else if (itemchance == 168) { 
			cm.gainItem(1002586, 1, true, true);
			}
			else if (itemchance == 169) { 
			cm.gainItem(1382015, 1, true, true);
			}
			else if (itemchance == 170) { 
			cm.gainItem(1382011, 1, true, true);
			}
			else if (itemchance == 171) { 
			cm.gainItem(1382014, 1, true, true);
			}
			else if (itemchance == 172) { 
			cm.gainItem(1382007, 1, true, true);
			}
			else if (itemchance == 173) { 
			cm.gainItem(1382053, 1, true, true);
			}
			else if (itemchance == 174) { 
			cm.gainItem(1382010, 1, true, true);
			}
			else if (itemchance == 175) { 
			cm.gainItem(1382008, 1, true, true);
			}
			else if (itemchance == 176) { 
			cm.gainItem(1382016, 1, true, true);
			}
			else if (itemchance == 177) { 
			cm.gainItem(1382037, 1, true, true);
			}
			else if (itemchance == 178) { 
			cm.gainItem(1372012, 1, true, true);
			}
			else if (itemchance == 179) { 
			cm.gainItem(1372012, 1, true, true);
			}
			else if (itemchance == 180) { 
			cm.gainItem(1372012, 1, true, true);
			}
			else if (itemchance == 181) { 
			cm.gainItem(1372008, 1, true, true);
			}
			else if (itemchance == 182) { 
			cm.gainItem(1372035, 1, true, true);
			}
			else if (itemchance == 183) { 
			cm.gainItem(1372036, 1, true, true);
			}
			else if (itemchance == 184) { 
			cm.gainItem(1372037, 1, true, true);
			}
			else if (itemchance == 185) { 
			cm.gainItem(1372038, 1, true, true);
			}
			else if (itemchance == 186) { 
			cm.gainItem(1372034, 1, true, true);
			}
			else if (itemchance == 187) { 
			cm.gainItem(1382009, 1, true, true);
			}
			else if (itemchance == 188) { 
			cm.gainItem(1382012, 1, true, true);
			}
			else if (itemchance == 189) { 
			cm.gainItem(1382039, 1, true, true);
			}
			else if (itemchance == 190) { 
			cm.gainItem(1372004, 1, true, true);
			}
			else if (itemchance == 191) { 
			cm.gainItem(1372000, 1, true, true);
			}
			else if (itemchance == 192) { 
			cm.gainItem(1372000, 1, true, true);
			}
			else if (itemchance == 193) { 
			cm.gainItem(1050023, 1, true, true);
			}
			else if (itemchance == 194) { 
			cm.gainItem(3010019, 1, true, true);
			cm.gachNotice(cm.getC(), 3010019);
			}

			cm.dispose();
		}
	}
}