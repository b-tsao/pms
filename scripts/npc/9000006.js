//Gachaphon

importPackage(net.sf.odinms.client);



var status = 0;
var chance1 = Math.floor(Math.random()*200+1);
var chance2 = Math.floor(Math.random()*50);
var chance3 = (Math.floor(Math.random()*20)+1);
var chance4 = Math.floor(Math.random()*2+1);
var itemchance = chance1 + chance2 + chance3 * chance4;
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
				cm.sendNext("I am #bHenesys#k gachapon.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
			}
		else if (status == 1) {
			if (cm.haveItem(5220000)) {
			cm.sendYesNo("I see you have a ticket of mine, do you wish to use it?");
			}
			else if (!cm.haveItem(5220000)) {
			cm.sendOk("You dont have any #bgachapon tickets#k.");
			cm.dispose();
			}
		}
		else if (status == 2) {
			cm.gainItem(5220000, -1);
			if ((itemchance >= 1) && (itemchance <= 20)) {
			cm.gainItem(2000001, itemamount);
			}
			else if ((itemchance >= 21) && (itemchance <= 40)) {
			cm.gainItem(2000003, itemamount);
			}
			else if ((itemchance >= 41) && (itemchance <= 50)) {
			cm.gainItem(2000004, itemamount);
			}
			else if ((itemchance >= 51) && (itemchance <= 60)) {
			cm.gainItem(1302024, 1);
			}
			else if ((itemchance >= 61) && (itemchance <= 70)) {
			cm.gainItem(2040705, 1);
			}
			else if (itemchance == 71) {
			cm.gainItem(3010001, 1);
			}
			else if (itemchance == 72) {
			cm.gainItem(1082002, 1);
			}
			else if (itemchance == 73) {
			cm.gainItem(1442023, 1);
			}
			else if (itemchance == 74) {
			cm.gainItem(1452045, 1);
			}
			else if (itemchance == 75) {
			cm.gainItem(1452023, 1);
			}
			else if (itemchance == 76) {
			cm.gainItem(1452018, 1);
			}
			else if (itemchance == 77) {
			cm.gainItem(1452020, 1);
			}
			else if (itemchance == 78) {
			cm.gainItem(2040326, 1);
			}
			else if (itemchance == 79) {
			cm.gainItem(2040327, 1);
			}	
			else if (itemchance == 80) {
			cm.gainItem(2040421, 1);
			}	
			else if (itemchance == 81) {
			cm.gainItem(2040422, 1);
			}
			else if (itemchance == 82) {
			cm.gainItem(2040618, 1);
			}
			else if (itemchance == 83) {
			cm.gainItem(1312012, 1);
			}	
			else if (itemchance == 84) {
			cm.gainItem(1322051, 1);
			}
			else if (itemchance == 85) {
			cm.gainItem(1322063, 1);
			}
			else if (itemchance == 86) {
			cm.gainItem(1432009, 1);
			}
			else if (itemchance == 87) {
			cm.gainItem(2040623, 1);
			}	
			else if (itemchance == 88) {
			cm.gainItem(2040621, 1);
			}
			else if (itemchance == 89) {
			cm.gainItem(2040824, 1);
			}
			else if (itemchance == 90) {
			cm.gainItem(2040926, 1);
			}
			else if (itemchance == 91) {
			cm.gainItem(2040927, 1);
			}
			else if (itemchance == 92) {
			cm.gainItem(2022179, 1);
			}
			else if (itemchance == 93) {
			cm.gainItem(1122001, 1);
			}
			else if (itemchance == 94) {
			cm.gainItem(2061001, 1);
			}
			else if (itemchance == 95) {
			cm.gainItem(2061002, 1);
			}
			else if (itemchance == 96) {
			cm.gainItem(2060001, 1);
			}
			else if (itemchance == 97) {
			cm.gainItem(2060002, 1);
			}
			else if (itemchance == 98) {
			cm.gainItem(1102040, 1);
			}
			else if (itemchance == 99) {
			cm.gainItem(1102041, 1);
			}
			else if (itemchance == 100) {
			cm.gainItem(1082149, 1);
			}
			else if (itemchance == 101) {
			cm.gainItem(1462009, 1);
			}
			else if (itemchance == 102) {
			cm.gainItem(1462046, 1);
			}
			else if (itemchance == 103) {
			cm.gainItem(1462021, 1);
			}
			else if (itemchance == 104) {
			cm.gainItem(1462016, 1);
			}
			else if (itemchance == 105) {
			cm.gainItem(1462021, 1);
			}
			else if (itemchance == 106) {
			cm.gainItem(1462016, 1);
			}
			else if (itemchance == 107) {
			cm.gainItem(1442061, 1);
			}
			else if (itemchance == 108) {
			cm.gainItem(1462017, 1);
			}
			else if (itemchance == 109) {
			cm.gainItem(1462017, 1);
			}
			else if (itemchance == 110) {
			cm.gainItem(1452004, 1);
			}
			else if (itemchance == 111) {
			cm.gainItem(1452004, 1);
			}
			else if (itemchance == 112) {
			cm.gainItem(1452005, 1);
			}
			else if (itemchance == 113) {
			cm.gainItem(1452005, 1);
			}
			else if (itemchance == 114) {
			cm.gainItem(1452005, 1);
			}
			else if (itemchance == 115) {
			cm.gainItem(1452006, 1);
			}
			else if (itemchance == 116) {
			cm.gainItem(1452006, 1);
			}
			else if (itemchance == 117) {
			cm.gainItem(1452007, 1);
			}
			else if (itemchance == 118) {
			cm.gainItem(1452007, 1);
			}
			else if (itemchance == 119) {
			cm.gainItem(1452008, 1);
			}
			else if (itemchance == 120) {
			cm.gainItem(1452008, 1);
			}
			else if (itemchance == 121) {
			cm.gainItem(1452009, 1);
			}
			else if (itemchance == 122) {
			cm.gainItem(1452009, 1);
			}
			else if (itemchance == 123) {
			cm.gainItem(1452010, 1);
			}
			else if (itemchance == 124) {
			cm.gainItem(1452010, 1);
			}
			else if (itemchance == 125) {
			cm.gainItem(1452011, 1);
			}
			else if (itemchance == 126) {
			cm.gainItem(1452011, 1);
			}
			else if (itemchance == 127) {
			cm.gainItem(1452012, 1);
			}
			else if (itemchance == 128) {
			cm.gainItem(1452012, 1);
			}
			else if (itemchance == 129) {
			cm.gainItem(1462015, 1);
			}
			else if (itemchance == 130) {
			cm.gainItem(1462015, 1);
			}
			else if (itemchance == 131) {
			cm.gainItem(1462004, 1);
			}
			else if (itemchance == 132) {
			cm.gainItem(1462004, 1);
			}
			else if (itemchance == 133) {
			cm.gainItem(1462005, 1);
			}
			else if (itemchance == 134) {
			cm.gainItem(1462006, 1);
			}
			else if (itemchance == 135) {
			cm.gainItem(1462006, 1);
			}
			else if (itemchance == 136) {
			cm.gainItem(1462007, 1);
			}
			else if (itemchance == 137) {
			cm.gainItem(1462007, 1);
			}
			else if (itemchance == 138) {
			cm.gainItem(1462008, 1);
			}
			else if (itemchance == 139) {
			cm.gainItem(1462008, 1);
			}
			else if (itemchance == 140) {
			cm.gainItem(1302021, 1);
			}
			else if (itemchance == 141) { 
			cm.gainItem(1462009, 1);
			}
			else if (itemchance == 142) { 
			cm.gainItem(1050052, 1);
			}
			else if (itemchance == 143) { 
			cm.gainItem(1050052, 1);
			}
			else if (itemchance == 144) { 
			cm.gainItem(1050076, 1);
			}
			else if (itemchance == 145) { 
			cm.gainItem(1050076, 1);
			}
			else if (itemchance == 146) { 
			cm.gainItem(1050072, 1);
			}
			else if (itemchance == 147) {
			cm.gainItem(1050052, 1);
			}
			else if (itemchance == 148) { 
			cm.gainItem(1050072, 1);
			}
			else if (itemchance == 149) { 
			cm.gainItem(1050077, 1);
			}
			else if (itemchance == 150) { 
			cm.gainItem(1050077, 1);
			}
			else if (itemchance == 151) { 
			cm.gainItem(1050051, 1);
			}
			else if (itemchance == 152) { 
			cm.gainItem(1050051, 1);
			}
			else if (itemchance == 153) { 
			cm.gainItem(1050058, 1);
			}
			else if (itemchance == 154) { 
			cm.gainItem(1050059, 1);
			}
			else if (itemchance == 155) { 
			cm.gainItem(1050059, 1);
			}
			else if (itemchance == 156) { 
			cm.gainItem(1050060, 1);
			}
			else if (itemchance == 157) { 
			cm.gainItem(1050060, 1);
			}
			else if (itemchance == 158) { 
			cm.gainItem(1050061, 1);
			}
			else if (itemchance == 159) { 
			cm.gainItem(1050061, 1);
			}
			else if (itemchance == 160) { 
			cm.gainItem(1050061, 1);
			}
			else if (itemchance == 161) { 
			cm.gainItem(1050061, 1);
			}
			else if (itemchance == 162) { 
			cm.gainItem(1050062, 1);
			}
			else if (itemchance == 163) { 
			cm.gainItem(1050062, 1);
			}
			else if (itemchance == 164) { 
			cm.gainItem(1050063, 1);
			}
			else if (itemchance == 165) { 
			cm.gainItem(1050063, 1);
			}
			else if (itemchance == 166) { 
			cm.gainItem(1050064, 1);
			}
			else if (itemchance == 167) { 
			cm.gainItem(1050064, 1);
			}
			else if (itemchance == 168) { 
			cm.gainItem(2040027, 1);
			}
			else if (itemchance == 169) { 
			cm.gainItem(2040029, 1);
			}
			else if (itemchance == 170) { 
			cm.gainItem(2040031, 1);
			}
			else if (itemchance == 171) { 
			cm.gainItem(2040200, 1);
			}
			else if (itemchance == 172) { 
			cm.gainItem(2040200, 1);
			}
			else if (itemchance == 173) { 
			cm.gainItem(2040317, 1);
			}
			else if (itemchance == 174) { 
			cm.gainItem(2040318, 1);
			}
			else if (itemchance == 175) { 
			cm.gainItem(2040418, 1);
			}
			else if (itemchance == 176) { 
			cm.gainItem(2040419, 1);
			}
			else if (itemchance == 177) { 
			cm.gainItem(2040501, 1);
			}
			else if (itemchance == 178) { 
			cm.gainItem(2040502, 1);
			}
			else if (itemchance == 179) { 
			cm.gainItem(2040532, 1);
			}
			else if (itemchance == 180) { 
			cm.gainItem(2049100, 1);
			}
			else if (itemchance == 181) { 
			cm.gainItem(3010009, 1);
			}
			else if (itemchance == 182) { 
			cm.gainItem(2040534, 1);
			}
			else if (itemchance == 183) { 
			cm.gainItem(2040625, 1);
			}
			else if (itemchance == 184) { 
			cm.gainItem(2040627, 1);
			}
			else if (itemchance == 185) { 
			cm.gainItem(2040704, 1);
			}
			else if (itemchance == 186) { 
			cm.gainItem(2040705, 1);
			}
			else if (itemchance == 187) { 
			cm.gainItem(2040804, 1);
			}
			else if (itemchance == 188) { 
			cm.gainItem(2040805, 1);
			}
			else if (itemchance == 189) { 
			cm.gainItem(2040805, 1);
			}
			else if (itemchance == 190) { 
			cm.gainItem(2041013, 1);
			}
			else if (itemchance == 191) { 
			cm.gainItem(2041014, 1);
			}
			else if (itemchance == 192) { 
			cm.gainItem(2041014, 1);
			}
			else if (itemchance == 193) { 
			cm.gainItem(2041019, 1);
			}
			else if (itemchance == 194) { 
			cm.gainItem(2041020, 1);
			}
			else if (itemchance == 195) { 
			cm.gainItem(2044501, 1);
			}
			else if (itemchance == 196) { 
			cm.gainItem(2044501, 1);
			}
			else if (itemchance == 197) { 
			cm.gainItem(2044502, 1);
			}
			else if (itemchance == 198) { 
			cm.gainItem(2044502, 1);
			}
			else if (itemchance == 199) { 
			cm.gainItem(2048010, 1);
			}
			else if (itemchance == 200) { 
			cm.gainItem(2048012, 1);
			}
			else if (itemchance == 201) { 
			cm.gainItem(1002081, 1);
			}
			else if (itemchance == 202) { 
			cm.gainItem(1002081, 1);
			}
			else if (itemchance == 203) { 
			cm.gainItem(1002081, 1);
			}
			else if (itemchance == 204) { 
			cm.gainItem(1452052, 1);
			}
			else if (itemchance == 205) { 
			cm.gainItem(1452025, 1);
			}
			else if (itemchance == 206) { 
			cm.gainItem(1452025, 1);
			}
			else if (itemchance == 207) { 
			cm.gainItem(1302037, 1);
			}
			else if (itemchance == 208) { 
			cm.gainItem(1442018, 1);
			}
			else if ((itemchance >= 209) && (itemchance <= 215)) { 
			cm.gainItem(2060001, 1);
			}
			else if ((itemchance >= 216) && (itemchance <= 221)) { 
			cm.gainItem(1082002, 1);
			}
			else if ((itemchance >= 222) && (itemchance <= 228)) { 
			cm.gainItem(2040326, 1);
			}
			else if ((itemchance >= 228) && (itemchance <= 240)) { 
			cm.gainItem(1432009, 1);
			}
			else if (itemchance == 241) { 
			cm.gainItem(1442023, 1);
			}
			else if (itemchance == 242) {
				cm.gainItem(01452021, 1);
			}
			else if (itemchance == 243) {
				cm.gainItem(1452022, 1);
			}
			else if (itemchance == 244) {
				cm.gainItem(1452016, 1);
			}
			else if (itemchance == 245) {
				cm.gainItem(1462014, 1);
			}
			else if (itemchance == 246) {
				cm.gainItem(1462019, 1);
			}
			else if (itemchance == 247) {
				cm.gainItem(1462040, 1);
			}
			cm.dispose();
		}
	}
}