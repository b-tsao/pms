//Nautilus Gachaphon

importPackage(net.sf.odinms.client);



var status = 0;
var itemchance = Math.floor(Math.random()*159+1);
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
				cm.sendNext("I am #bNautilus#k gachapon.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
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
			cm.gainItem(1492002, 1, true, true);
			}
			else if ((itemchance >= 56) && (itemchance <= 61)) {
			cm.gainItem(2001002, 1, itemamount);
			}
			else if ((itemchance >= 62) && (itemchance <= 65)) {
			cm.gainItem(2001001, 1, itemamount);
			}
			else if (itemchance == 66) {
			cm.gainItem(1492001, 1, true, true);
			}
			else if (itemchance == 67) {
			cm.gainItem(1492002, 1, true, true);
			}
			else if (itemchance == 69) {
			cm.gainItem(1492003, 1, true, true);
			}
			else if (itemchance == 70) {
			cm.gainItem(1492004, 1, true, true);
			}
			else if (itemchance == 71) {
			cm.gainItem(1492005, 1, true, true);
			}
			else if (itemchance == 72) {
			cm.gainItem(1492006, 1, true, true);
			}
			else if (itemchance == 73) {
			cm.gainItem(1492007, 1, true, true);
			}
			else if (itemchance == 74) {
			cm.gainItem(1492008, 1, true, true);
			}
			else if (itemchance == 75) {
			cm.gainItem(1492009, 1, true, true);
			}
			else if (itemchance == 76) {
			cm.gainItem(1492010, 1, true, true);
			}
			else if (itemchance == 77) {
			cm.gainItem(1492011, 1, true, true);
			}
			else if (itemchance == 78) {
			cm.gainItem(1492012, 1, true, true);
			}
			else if (itemchance == 79) {
			cm.gainItem(1482000, 1, true, true);
			}	
			else if (itemchance == 80) {
			cm.gainItem(1482001, 1, true, true);
			}	
			else if (itemchance == 81) {
			cm.gainItem(1482002, 1, true, true);
			}
			else if (itemchance == 82) {
			cm.gainItem(1482003, 1, true, true);
			}
			else if (itemchance == 83) {
			cm.gainItem(1482004, 1, true, true);
			}	
			else if (itemchance == 84) {
			cm.gainItem(1482005, 1, true, true);
			}
			else if (itemchance == 85) {
			cm.gainItem(1482006, 1, true, true);
			}
			else if (itemchance == 86) {
			cm.gainItem(1482007, 1, true, true);
			}
			else if (itemchance == 87) {
			cm.gainItem(1482008, 1, true, true);
			}	
			else if (itemchance == 88) {
			cm.gainItem(1482009, 1, true, true);
			}
			else if (itemchance == 89) {
			cm.gainItem(1482010, 1, true, true);
			}
			else if (itemchance == 90) {
			cm.gainItem(1482011, 1, true, true);
			}
			else if (itemchance == 91) {
			cm.gainItem(1482012, 1, true, true);
			}
			else if (itemchance == 92) {
			cm.gainItem(2043211, 1, true, true);
			}
			else if (itemchance == 93) {
			cm.gainItem(2043212, 1, true, true);
			}
			else if (itemchance == 94) {
			cm.gainItem(2043213, 1, true, true);
			}
			else if (itemchance == 95) {
			cm.gainItem(2002010, 12, true, true);
			}
			else if (itemchance == 96) {
			cm.gainItem(2002010, 2, true, true);
			}
			else if (itemchance == 97) {
			cm.gainItem(2002010, 6, true, true);
			}
			else if (itemchance == 98) {
			cm.gainItem(2040324, 1, true, true);
			}
			else if (itemchance == 99) {
			cm.gainItem(2040326, 1, true, true);
			}
			else if (itemchance == 100) {
			cm.gainItem(2040325, 1, true, true);
			}
			else if (itemchance == 101) {
			cm.gainItem(2040324, 1, true, true);
			}
			else if (itemchance == 102) {
			cm.gainItem(2040326, 1, true, true);
			}
			else if (itemchance == 103) {
			cm.gainItem(2040325, 1, true, true);
			}
			else if (itemchance == 104) {
			cm.gainItem(1422011, 1, true, true);
			}
			else if (itemchance == 105) {
			cm.gainItem(1422030, 1, true, true);
			}
			else if (itemchance == 106) {
			cm.gainItem(1422031, 1, true, true);
			}
			else if (itemchance == 107) {
			cm.gainItem(2044211, 1, true, true);
			}
			else if (itemchance == 108) {
			cm.gainItem(2044212, 1, true, true);
			}
			else if (itemchance == 109) {
			cm.gainItem(2044213, 1, true, true);
			}
			else if (itemchance == 110) {
			cm.gainItem(2044807, 1, true, true);
			}
			else if (itemchance == 111) {
			cm.gainItem(2044808, 1, true, true);
			}
			else if (itemchance == 112) {
			cm.gainItem(2044809, 1, true, true);
			}
			else if (itemchance == 113) {
			cm.gainItem(1122004, 1, true, true);
			}
			else if (itemchance == 114) {
			cm.gainItem(1122010, 1, true, true);
			cm.gachNotice(cm.getC(), 1122010);
			}
			else if (itemchance == 115) {
			cm.gainItem(1012098, 1, true, true);
			cm.gachNotice(cm.getC(), 1012098);
			}
			else if (itemchance == 116) {
			cm.gainItem(1012103, 1, true, true);
			cm.gachNotice(cm.getC(), 1012103);
			}
			else if (itemchance == 117) {
			cm.gainItem(1012102, 1, true, true);
			cm.gachNotice(cm.getC(), 1012102);
			}
			else if (itemchance == 118) {
			cm.gainItem(1012101, 1, true, true);
			cm.gachNotice(cm.getC(), 1012101);
			}
			else if (itemchance == 119) {
			cm.gainItem(1052101, 1, true, true);
			}
			else if (itemchance == 120) {
			cm.gainItem(1052104, 1, true, true);
			}
			else if (itemchance == 121) {
			cm.gainItem(1052107, 1, true, true);
			}
			else if (itemchance == 122) {
			cm.gainItem(1052110, 1, true, true);
			}
			else if (itemchance == 123) {
			cm.gainItem(1052113, 1, true, true);
			}
			else if (itemchance == 124) {
			cm.gainItem(1052116, 1, true, true);
			}
			else if (itemchance == 125) {
			cm.gainItem(1052119, 1, true, true);
			}
			else if (itemchance == 126) {
			cm.gainItem(1052122, 1, true, true);
			}
			else if (itemchance == 127) {
			cm.gainItem(1052125, 1, true, true);
			}
			else if (itemchance == 128) {
			cm.gainItem(2330005, 1, true, true);
			cm.gachNotice(cm.getC(), 2330005);
			}
			else if (itemchance == 129) {
			cm.gainItem(2040027, 1, true, true);
			}
			else if (itemchance == 130) {
			cm.gainItem(2040029, 1, true, true);
			}
			else if (itemchance == 131) {
			cm.gainItem(2040031, 1, true, true);
			}
			else if (itemchance == 132) {
			cm.gainItem(2040317, 1, true, true);
			}
			else if (itemchance == 133) {
			cm.gainItem(2040318, 1, true, true);
			}
			else if (itemchance == 134) {
			cm.gainItem(2040418, 1, true, true);
			}
			else if (itemchance == 135) {
			cm.gainItem(2040419, 1, true, true);
			}
			else if (itemchance == 136) {
			cm.gainItem(2040501, 1, true, true);
			}
			else if (itemchance == 137) {
			cm.gainItem(2040532, 1, true, true);
			}
			else if (itemchance == 138) {
			cm.gainItem(2040534, 1, true, true);
			}
			else if (itemchance == 139) {
			cm.gainItem(2040625, 1, true, true);
			}
			else if (itemchance == 140) {
			cm.gainItem(2040704, 1, true, true);
			}
			else if (itemchance == 141) { 
			cm.gainItem(2040705, 1, true, true);
			}
			else if (itemchance == 142) { 
			cm.gainItem(2040804, 1, true, true);
			}
			else if (itemchance == 143) { 
			cm.gainItem(2040805, 1, true, true);
			}
			else if (itemchance == 144) { 
			cm.gainItem(2041013, 1, true, true);
			}
			else if (itemchance == 145) { 
			cm.gainItem(2041014, 1, true, true);
			}
			else if (itemchance == 146) { 
			cm.gainItem(2041019, 1, true, true);
			}
			else if (itemchance == 147) { 
			cm.gainItem(2041020, 1, true, true);
			}
			else if (itemchance == 148) { 
			cm.gainItem(2044801, 1, true, true);
			}
			else if (itemchance == 149) { 
			cm.gainItem(2044802, 1, true, true);
			}
			else if (itemchance == 150) { 
			cm.gainItem(2044901, 1, true, true);
			}
			else if (itemchance == 151) { 
			cm.gainItem(2044902, 1, true, true);
			}
			else if (itemchance == 152) { 
			cm.gainItem(2048010, 1, true, true);
			}
			else if (itemchance == 153) { 
			cm.gainItem(2048012, 1, true, true);
			}
			else if (itemchance == 154) { 
			cm.gainItem(2332000, 1, true, true);
			}
			else if (itemchance == 155) { 
			cm.gainItem(2331000, 1, true, true);
			}
			else if (itemchance == 156) { 
			cm.gainItem(1002082, 1, true, true);
			}
			else if (itemchance == 157) { 
			cm.gainItem(1002587, 1, true, true);
			}
			else if (itemchance == 158) { 
			cm.gainItem(1082147, 1, true, true);
			}
			else if (itemchance == 159) { 
			cm.gainItem(1302029, 1, true, true);
			}
			else if (itemchance == 160) { 
			cm.gainItem(3010013, 1, true, true);
			cm.gachNotice(cm.getC(), 3010013);
			}
			
			cm.dispose();

		}
	}
}