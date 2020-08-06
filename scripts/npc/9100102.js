// Perion Gachaphon

importPackage(net.sf.odinms.client);



var status = 0;
var itemchance = Math.floor(Math.random()*238+1);
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
				cm.sendNext("I am #bPerion#k gachapon.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
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
			cm.gainItem(2002006, itemamount);
			}
			else if ((itemchance >= 11) && (itemchance <= 20)) {
			cm.gainItem(2000001, itemamount);
			}
			else if ((itemchance >= 21) && (itemchance <= 30)) {
			cm.gainItem(2000000, itemamount);
			}
			else if ((itemchance >= 31) && (itemchance <= 38)) {
			cm.gainItem(2000002, itemamount);
			}
			else if ((itemchance >= 39) && (itemchance <= 47)) {
			cm.gainItem(2002010, itemamount);
			}
			else if ((itemchance >= 48) && (itemchance <= 55)) {
			cm.gainItem(1092001, 1, true, true);
			}
			else if ((itemchance >= 56) && (itemchance <= 61)) {
			cm.gainItem(1002757, 1, true, true);
			}
			else if ((itemchance >= 61) && (itemchance <= 65)) {
			cm.gainItem(1402000, 1, true, true);
			}
			else if (itemchance == 66) {
			cm.gainItem(2022179, 1, true, true);
			}
			else if (itemchance == 67) {
			cm.gainItem(2044305, 1, true, true);
			}
			else if (itemchance == 69) {
			cm.gainItem(2044104, 1, true, true);
			}
			else if (itemchance == 70) {
			cm.gainItem(2044105, 1, true, true);
			}
			else if (itemchance == 71) {
			cm.gainItem(1092000, 1, true, true);
			}
			else if (itemchance == 72) {
			cm.gainItem(1092000, 1, true, true);
			}
			else if (itemchance == 73) {
			cm.gainItem(2040804, 1, true, true);
			}
			else if (itemchance == 74) {
			cm.gainItem(2340000, 1, true, true);
			cm.gachNotice(cm.getC(), 2340000);
			}
			else if (itemchance == 75) {
			cm.gainItem(1092002, 1, true, true);
			}
			else if (itemchance == 76) {
			cm.gainItem(1092002, 1, true, true);
			}
			else if (itemchance == 77) {
			cm.gainItem(1092003, 1, true, true);
			}
			else if (itemchance == 78) {
			cm.gainItem(1092003, 1, true, true);
			}
			else if (itemchance == 79) {
			cm.gainItem(1092004, 1, true, true);
			}	
			else if (itemchance == 80) {
			cm.gainItem(1092004, 1, true, true);
			}	
			else if (itemchance == 81) {
			cm.gainItem(1092005, 1, true, true);
			}
			else if (itemchance == 82) {
			cm.gainItem(1092005, 1, true, true);
			}
			else if (itemchance == 83) {
			cm.gainItem(1092006, 1, true, true);
			}	
			else if (itemchance == 84) {
			cm.gainItem(1092006, 1, true, true);
			}
			else if (itemchance == 85) {
			cm.gainItem(1092007, 1, true, true);
			}
			else if (itemchance == 86) {
			cm.gainItem(1092007, 1, true, true);
			}
			else if (itemchance == 87) {
			cm.gainItem(1302063, 1, true, true);
			}	
			else if (itemchance == 88) {
			cm.gainItem(1002006, 1, true, true);
			}
			else if (itemchance == 89) {
			cm.gainItem(3010012, 1, true, true);
			}
			else if (itemchance == 90) {
			cm.gainItem(2040929, 1, true, true);
			}
			else if (itemchance == 91) {
			cm.gainItem(2040930, 1, true, true);
			}
			else if (itemchance == 92) {
			cm.gainItem(2040933, 1, true, true);
			}
			else if (itemchance == 93) {
			cm.gainItem(1312017, 1, true, true);
			}
			else if (itemchance == 94) {
			cm.gainItem(2040419, 1, true, true);
			}
			else if (itemchance == 95) {
			cm.gainItem(2040417, 1, true, true);
			}
			else if (itemchance == 96) {
			cm.gainItem(1322006, 1, true, true);
			}
			else if (itemchance == 97) {
			cm.gainItem(1322006, 1, true, true);
			}
			else if (itemchance == 98) {
			cm.gainItem(2040530, 1, true, true);
			}
			else if (itemchance == 99) {
			cm.gainItem(1322031, 1, true, true);
			}
			else if (itemchance == 100) {
			cm.gainItem(1322031, 1, true, true);
			}
			else if (itemchance == 101) {
			cm.gainItem(1432015, 1, true, true);
			}
			else if (itemchance == 102) {
			cm.gainItem(1432009, 1, true, true);
			}
			else if (itemchance == 103) {
			cm.gainItem(1432013, 1, true, true);
			}
			else if (itemchance == 104) {
			cm.gainItem(1322062, 1, true, true);
			cm.gachNotice(cm.getC(), 1322062);
			}
			else if (itemchance == 105) {
			cm.gainItem(1432046, 1, true, true);
			}
			else if (itemchance == 106) {
			cm.gainItem(1432016, 1, true, true);
			}
			else if (itemchance == 107) {
			cm.gainItem(1432039, 1, true, true);
			cm.gachNotice(cm.getC(), 1432039);
			}
			else if (itemchance == 108) {
			cm.gainItem(1432017, 1, true, true);
			}
			else if (itemchance == 109) {
			cm.gainItem(1432018, 1, true, true);
			cm.gachNotice(cm.getC(), 1432018);
			}
			else if (itemchance == 110) {
			cm.gainItem(1442013, 1, true, true);
			}
			else if (itemchance == 111) {
			cm.gainItem(1442029, 1, true, true);
			}
			else if (itemchance == 112) {
			cm.gainItem(1442014, 1, true, true);
			}
			else if (itemchance == 113) {
			cm.gainItem(1442011, 1, true, true);
			}
			else if (itemchance == 114) {
			cm.gainItem(1442061, 1, true, true);
			}
			else if (itemchance == 115) {
			cm.gainItem(1442065, 1, true, true);
			}
			else if (itemchance == 116) {
			cm.gainItem(1442065, 1, true, true);
			}
			else if (itemchance == 117) {
			cm.gainItem(1442015, 1, true, true);
			}
			else if (itemchance == 118) {
			cm.gainItem(1442028, 1, true, true);
			}
			else if (itemchance == 119) {
			cm.gainItem(1442055, 1, true, true);
			}
			else if (itemchance == 120) {
			cm.gainItem(1442016, 1, true, true);
			}
			else if (itemchance == 121) {
			cm.gainItem(1442025, 1, true, true);
			}
			else if (itemchance == 122) {
			cm.gainItem(2044111, 1, true, true);
			}
			else if (itemchance == 123) {
			cm.gainItem(2044111, 1, true, true);
			}
			else if (itemchance == 124) {
			cm.gainItem(2044112, 1, true, true);
			}
			else if (itemchance == 125) {
			cm.gainItem(2044112, 1, true, true);
			}
			else if (itemchance == 126) {
			cm.gainItem(2044113, 1, true, true);
			}
			else if (itemchance == 127) {
			cm.gainItem(2044113, 1, true, true);
			}
			else if (itemchance == 128) {
			cm.gainItem(1122003, 1, true, true);
			}
			else if (itemchance == 129) {
			cm.gainItem(1122003, 1, true, true);
			}
			else if (itemchance == 130) {
			cm.gainItem(1442026, 1, true, true);
			}
			else if (itemchance == 131) {
			cm.gainItem(1312017, 1, true, true);
			}
			else if (itemchance == 132) {
			cm.gainItem(1312017, 1, true, true);
			}
			else if (itemchance == 133) {
			cm.gainItem(1082146, 1, true, true);
			}
			else if (itemchance == 134) {
			cm.gainItem(1082146, 1, true, true);
			}
			else if (itemchance == 135) {
			cm.gainItem(1442057, 1, true, true);
			cm.gachNotice(cm.getC(), 1442057);
			}
			else if (itemchance == 136) {
			cm.gainItem(1312012, 1, true, true);
			}
			else if (itemchance == 137) {
			cm.gainItem(1312030, 1, true, true);
			}
			else if (itemchance == 138) {
			cm.gainItem(1072132, 1, true, true);
			}
			else if (itemchance == 139) {
			cm.gainItem(1072133, 1, true, true);
			}
			else if (itemchance == 140) {
			cm.gainItem(1302024, 1, true, true);
			}
			else if (itemchance == 141) { 
			cm.gainItem(1302057, 1, true, true);
			}
			else if (itemchance == 142) { 
			cm.gainItem(1302080, 1, true, true);
			}
			else if (itemchance == 143) { 
			cm.gainItem(1302049, 1, true, true);
			}
			else if (itemchance == 144) { 
			cm.gainItem(1322051, 1, true, true);
			}
			else if (itemchance == 145) { 
			cm.gainItem(1322027, 1, true, true);
			}
			else if (itemchance == 146) { 
			cm.gainItem(1322025, 1, true, true);
			}
			else if (itemchance == 147) {
			cm.gainItem(1402029, 1, true, true);
			}
			else if (itemchance == 148) { 
			cm.gainItem(1402013, 1, true, true);
			}
			else if (itemchance == 149) { 
			cm.gainItem(1402045, 1, true, true);
			}
			else if (itemchance == 150) { 
			cm.gainItem(1050011, 1, true, true);
			}
			else if (itemchance == 151) { 
			cm.gainItem(1050011, 1, true, true);
			}
			else if (itemchance == 152) { 
			cm.gainItem(1050022, 1, true, true);
			}
			else if (itemchance == 153) { 
			cm.gainItem(1050022, 1, true, true);
			}
			else if (itemchance == 154) { 
			cm.gainItem(1051016, 1, true, true);
			}
			else if (itemchance == 155) { 
			cm.gainItem(1051016, 1, true, true);
			}
			else if (itemchance == 156) { 
			cm.gainItem(1050083, 1, true, true);
			}
			else if (itemchance == 157) { 
			cm.gainItem(1051080, 1, true, true);
			}
			else if (itemchance == 158) { 
			cm.gainItem(2040914, 1, true, true);
			}
			else if (itemchance == 159) { 
			cm.gainItem(2040016, 1, true, true);
			}
			else if (itemchance == 160) { 
			cm.gainItem(2040016, 1, true, true);
			}
			else if (itemchance == 161) { 
			cm.gainItem(2040017, 1, true, true);
			}
			else if (itemchance == 162) { 
			cm.gainItem(2040017, 1, true, true);
			}
			else if (itemchance == 163) { 
			cm.gainItem(2040027, 1, true, true);
			}
			else if (itemchance == 164) { 
			cm.gainItem(2040029, 1, true, true);
			}
			else if (itemchance == 165) { 
			cm.gainItem(2040031, 1, true, true);
			}
			else if (itemchance == 166) { 
			cm.gainItem(2040200, 1, true, true);
			}
			else if (itemchance == 167) { 
			cm.gainItem(2040201, 1, true, true);
			}
			else if (itemchance == 168) { 
			cm.gainItem(2040202, 1, true, true);
			}
			else if (itemchance == 169) { 
			cm.gainItem(2040317, 1, true, true);
			}
			else if (itemchance == 170) { 
			cm.gainItem(2040318, 1, true, true);
			}
			else if (itemchance == 171) { 
			cm.gainItem(2040418, 1, true, true);
			}
			else if (itemchance == 172) { 
			cm.gainItem(2040419, 1, true, true);
			}
			else if (itemchance == 173) { 
			cm.gainItem(2040501, 1, true, true);
			}
			else if (itemchance == 174) { 
			cm.gainItem(2040502, 1, true, true);
			}
			else if (itemchance == 175) { 
			cm.gainItem(2040532, 1, true, true);
			}
			else if (itemchance == 176) { 
			cm.gainItem(2040534, 1, true, true);
			}
			else if (itemchance == 177) { 
			cm.gainItem(2040625, 1, true, true);
			}
			else if (itemchance == 178) { 
			cm.gainItem(2040627, 1, true, true);
			}
			else if (itemchance == 179) { 
			cm.gainItem(2040701, 1, true, true);
			}
			else if (itemchance == 180) { 
			cm.gainItem(2040701, 1, true, true);
			}
			else if (itemchance == 181) { 
			cm.gainItem(2040702, 1, true, true);
			}
			else if (itemchance == 182) { 
			cm.gainItem(2040702, 1, true, true);
			}
			else if (itemchance == 183) { 
			cm.gainItem(2040704, 1, true, true);
			}
			else if (itemchance == 184) { 
			cm.gainItem(2040705, 1, true, true);
			}
			else if (itemchance == 185) { 
			cm.gainItem(2040801, 1, true, true);
			}
			else if (itemchance == 186) { 
			cm.gainItem(2040802, 1, true, true);
			}
			else if (itemchance == 187) { 
			cm.gainItem(2040804, 1, true, true);
			}
			else if (itemchance == 188) { 
			cm.gainItem(2040805, 1, true, true);
			}
			else if (itemchance == 189) { 
			cm.gainItem(2040915, 1, true, true);
			}
			else if (itemchance == 190) { 
			cm.gainItem(2040931, 1, true, true);
			}
			else if (itemchance == 191) { 
			cm.gainItem(2040933, 1, true, true);
			}
			else if (itemchance == 192) { 
			cm.gainItem(2041013, 1, true, true);
			}
			else if (itemchance == 193) { 
			cm.gainItem(2041014, 1, true, true);
			}			
			else if (itemchance == 194) { 
			cm.gainItem(2041019, 1, true, true);
			}			
			else if (itemchance == 195) { 
			cm.gainItem(2041020, 1, true, true);
			}			
			else if (itemchance == 196) { 
			cm.gainItem(2043001, 1, true, true);
			}			
			else if (itemchance == 197) { 
			cm.gainItem(2043002, 1, true, true);
			}			
			else if (itemchance == 198) { 
			cm.gainItem(2043101, 1, true, true);
			}			
			else if (itemchance == 199) { 
			cm.gainItem(2043102, 1, true, true);
			}			
			else if (itemchance == 200) { 
			cm.gainItem(2043201, 1, true, true);
			}
			else if (itemchance == 201) { 
			cm.gainItem(2043201, 1, true, true);
			}
			else if (itemchance == 202) { 
			cm.gainItem(2043202, 1, true, true);
			}
			else if (itemchance == 203) { 
			cm.gainItem(2043202, 1, true, true);
			}
			else if (itemchance == 204) { 
			cm.gainItem(2044001, 1, true, true);
			}
			else if (itemchance == 205) { 
			cm.gainItem(2044002, 1, true, true);
			}
			else if (itemchance == 206) { 
			cm.gainItem(2044101, 1, true, true);
			}
			else if (itemchance == 207) { 
			cm.gainItem(2044102, 1, true, true);
			}
			else if (itemchance == 208) { 
			cm.gainItem(2044201, 1, true, true);
			}
			else if (itemchance == 209) { 
			cm.gainItem(2044202, 1, true, true);
			}
			else if (itemchance == 210) { 
			cm.gainItem(2044301, 1, true, true);
			}
			else if (itemchance == 211) { 
			cm.gainItem(2044401, 1, true, true);
			}
			else if (itemchance == 212) { 
			cm.gainItem(2044402, 1, true, true);
			}
			else if (itemchance == 213) { 
			cm.gainItem(2048010, 1, true, true);
			}
			else if (itemchance == 214) { 
			cm.gainItem(2048012, 1, true, true);
			}
			else if (itemchance == 215) { 
			cm.gainItem(1002080, 1, true, true);
			}
			else if (itemchance == 216) { 
			cm.gainItem(1002585, 1, true, true);
			}
			else if (itemchance == 217) { 
			cm.gainItem(1082146, 1, true, true);
			}
			else if (itemchance == 218) { 
			cm.gainItem(1442019, 1, true, true);
			}
			else if (itemchance == 219) { 
			cm.gainItem(1432007, 1, true, true);
			}
			else if (itemchance == 220) { 
			cm.gainItem(1432010, 1, true, true);
			}
			else if (itemchance == 221) { 
			cm.gainItem(1402017, 1, true, true);
			}
			else if (itemchance == 222) { 
			cm.gainItem(1302012, 1, true, true);
			}
			else if (itemchance == 223) { 
			cm.gainItem(1312032, 1, true, true);
			}
			else if (itemchance == 224) { 
			cm.gainItem(1302063, 1, true, true);
			}
			else if (itemchance == 225) { 
			cm.gainItem(1302020, 1, true, true);
			}
			else if (itemchance == 226) { 
			cm.gainItem(1302030, 1, true, true);
			}
			else if (itemchance == 227) { 
			cm.gainItem(1302064, 1, true, true);
			}
			else if (itemchance == 228) { 
			cm.gainItem(1442024, 1, true, true);
			}
			else if (itemchance == 229) { 
			cm.gainItem(1442051, 1, true, true);
			}
			else if (itemchance == 230) { 
			cm.gainItem(1432012, 1, true, true);
			}
			else if (itemchance == 231) { 
			cm.gainItem(1432040, 1, true, true);
			}
			else if (itemchance == 232) { 
			cm.gainItem(1412011, 1, true, true);
			}
			else if (itemchance == 233) { 
			cm.gainItem(1422014, 1, true, true);
			}
			else if (itemchance == 234) { 
			cm.gainItem(1422029, 1, true, true);
			}
			else if (itemchance == 235) { 
			cm.gainItem(1402039, 1, true, true);
			}
			else if (itemchance == 236) {
			cm.gainItem(2022282, 1, true, true);
			cm.gachNotice(cm.getC(), 2022282);
			}
			else if (itemchance == 237) {
			cm.gainItem(3010008, 1, true, true);
			cm.gachNotice(cm.getC(), 3010008);
			}
			else if (itemchance == 238) {
			cm.gainItem(3010007, 1, true, true);
			cm.gachNotice(cm.getC(), 3010007);
			}
			else if (itemchance == 239) {
			cm.gainItem(1302100, 1, true, true);
			cm.gachNotice(cm.getC(), 1302100);
			}
			cm.dispose();

		}
	}
}