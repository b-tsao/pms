// Ellinia Gachaphon

importPackage(net.sf.odinms.client);

var status = 0;
var itemchance = Math.floor(Math.random()*241+1);
var itemamount = Math.floor(Math.random()*50+1);
var allcure = Math.floor(Math.random()*5+1);


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
				cm.sendNext("I am #bMushroom Shrine#k gachapon.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
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
			cm.gainItem(2022015, itemamount);
			}
			else if ((itemchance >= 11) && (itemchance <= 20)) {
			cm.gainItem(2100004, 1, true, true);
			}
			else if ((itemchance >= 21) && (itemchance <= 30)) {
			cm.gainItem(2022017, itemamount);
			}
			else if ((itemchance >= 31) && (itemchance <= 40)) {
			cm.gainItem(2000005, itemamount);
			}
			else if ((itemchance >= 41) && (itemchance <= 45)) {
			cm.gainItem(2022178, allcure);
			}
			else if ((itemchance >= 46) && (itemchance <= 50)) {
			cm.gainItem(2001002, itemamount);
			}
			else if ((itemchance >= 51) && (itemchance <= 55)) {
			cm.gainItem(2001001, itemamount);
			}
			else if ((itemchance >= 56) && (itemchance <= 61)) {
			cm.gainItem(1032015, 1, true, true);
			}
			else if ((itemchance >= 62) && (itemchance <= 65)) {
			cm.gainItem(1002577, 1, true, true);
			}
			else if (itemchance == 66) {
			cm.gainItem(1050018, 1, true, true);
			}
			else if (itemchance == 67) {
			cm.gainItem(1051017, 1, true, true);
			}
			else if (itemchance == 69) {
			cm.gainItem(2040025, 1, true, true);
			}
			else if (itemchance == 70) {
			cm.gainItem(2040029, 1, true, true);
			}
			else if (itemchance == 71) {
			cm.gainItem(2040318, 1, true, true);
			}
			else if (itemchance == 72) {
			cm.gainItem(2040533, 1, true, true);
			}
			else if (itemchance == 73) {
			cm.gainItem(2040531, 1, true, true);
			}
			else if (itemchance == 74) {
			cm.gainItem(2040625, 1, true, true);
			}
			else if (itemchance == 75) {
			cm.gainItem(2049100, 1, true, true);
			cm.gachNotice(cm.getC(), 2049100);
			}
			else if (itemchance == 76) {
			cm.gainItem(1472054, 1, true, true);
			}
			else if (itemchance == 77) {
			cm.gainItem(1082149, 1, true, true);
			cm.gachNotice(cm.getC(), 1082149);
			}
			else if (itemchance == 78) {
			cm.gainItem(1102084, 1, true, true);
			cm.gachNotice(cm.getC(), 1102084);
			}
			else if (itemchance == 79) {
			cm.gainItem(2040804, 1, true, true);
			}	
			else if (itemchance == 80) {
			cm.gainItem(2040817, 1, true, true);
			}	
			else if (itemchance == 81) {
			cm.gainItem(2040914, 1, true, true);
			}
			else if (itemchance == 82) {
			cm.gainItem(1032028, 1, true, true);
			cm.gachNotice(cm.getC(), 1032028);
			}
			else if (itemchance == 83) {
			cm.gainItem(1022060, 1, true, true);
			cm.gachNotice(cm.getC(), 1022060);
			}	
			else if (itemchance == 84) {
			cm.gainItem(2044704, 1, true, true);
			}
			else if (itemchance == 85) {
			cm.gainItem(2044705, 1, true, true);
			}
			else if (itemchance == 86) {
			cm.gainItem(2040008, 1, true, true);
			}
			else if (itemchance == 87) {
			cm.gainItem(2040008, 1, true, true);
			}	
			else if (itemchance == 88) {
			cm.gainItem(2040009, 1, true, true);
			}
			else if (itemchance == 89) {
			cm.gainItem(2040009, 1, true, true);
			}
			else if (itemchance == 90) {
			cm.gainItem(2040012, 1, true, true);
			}
			else if (itemchance == 91) {
			cm.gainItem(2040013, 1, true, true);
			}
			else if (itemchance == 92) {
			cm.gainItem(2040014, 1, true, true);
			}
			else if (itemchance == 93) {
			cm.gainItem(2040104, 1, true, true);
			}
			else if (itemchance == 94) {
			cm.gainItem(2040103, 1, true, true);
			}
			else if (itemchance == 95) {
			cm.gainItem(2040015, 1, true, true);
			}
			else if (itemchance == 96) {
			cm.gainItem(2040009, 1, true, true);
			}
			else if (itemchance == 97) {
			cm.gainItem(2040103, 1, true, true);
			}
			else if (itemchance == 98) {
			cm.gainItem(2040103, 1, true, true);
			}
			else if (itemchance == 99) {
			cm.gainItem(2040008, 1, true, true);
			}
			else if (itemchance == 100) {
			cm.gainItem(2040104, 1, true, true);
			}
			else if (itemchance == 101) {
			cm.gainItem(2040104, 1, true, true);
			}
			else if (itemchance == 102) {
			cm.gainItem(2040108, 1, true, true);
			}
			else if (itemchance == 103) {
			cm.gainItem(2040109, 1, true, true);
			}
			else if (itemchance == 104) {
			cm.gainItem(2040203, 1, true, true);
			}
			else if (itemchance == 105) {
			cm.gainItem(2040204, 1, true, true);
			}
			else if (itemchance == 106) {
			cm.gainItem(2040208, 1, true, true);
			}
			else if (itemchance == 107) {
			cm.gainItem(2040209, 1, true, true);
			}
			else if (itemchance == 108) {
			cm.gainItem(2040322, 1, true, true);
			}
			else if (itemchance == 109) {
			cm.gainItem(2040320, 1, true, true);
			}
			else if (itemchance == 110) {
			cm.gainItem(2040304, 1, true, true);
			}
			else if (itemchance == 111) {
			cm.gainItem(2040305, 1, true, true);
			}
			else if (itemchance == 112) {
			cm.gainItem(2040306, 1, true, true);
			}
			else if (itemchance == 113) {
			cm.gainItem(2040307, 1, true, true);
			}
			else if (itemchance == 114) {
			cm.gainItem(2040404, 1, true, true);
			}
			else if (itemchance == 115) {
			cm.gainItem(2040404, 1, true, true);
			}
			else if (itemchance == 116) {
			cm.gainItem(2040404, 1, true, true);
			}
			else if (itemchance == 117) {
			cm.gainItem(2040405, 1, true, true);
			}
			else if (itemchance == 118) {
			cm.gainItem(2040405, 1, true, true);
			}
			else if (itemchance == 119) {
			cm.gainItem(2040406, 1, true, true);
			}
			else if (itemchance == 120) {
			cm.gainItem(2040407, 1, true, true);
			}
			else if (itemchance == 121) {
			cm.gainItem(2040408, 1, true, true);
			}
			else if (itemchance == 122) {
			cm.gainItem(2040408, 1, true, true);
			}
			else if (itemchance == 123) {
			cm.gainItem(2040409, 1, true, true);
			}
			else if (itemchance == 124) {
			cm.gainItem(2040409, 1, true, true);
			}
			else if (itemchance == 125) {
			cm.gainItem(2040410, 1, true, true);
			}
			else if (itemchance == 126) {
			cm.gainItem(2040411, 1, true, true);
			}
			else if (itemchance == 127) {
			cm.gainItem(2040508, 1, true, true);
			}
			else if (itemchance == 128) {
			cm.gainItem(2040509, 1, true, true);
			}
			else if (itemchance == 129) {
			cm.gainItem(2040510, 1, true, true);
			}
			else if (itemchance == 130) {
			cm.gainItem(2040510, 1, true, true);
			}
			else if (itemchance == 131) {
			cm.gainItem(2040511, 1, true, true);
			}
			else if (itemchance == 132) {
			cm.gainItem(2040511, 1, true, true);
			}
			else if (itemchance == 133) {
			cm.gainItem(2040518, 1, true, true);
			}
			else if (itemchance == 134) {
			cm.gainItem(2040519, 1, true, true);
			}
			else if (itemchance == 135) {
			cm.gainItem(2040520, 1, true, true);
			}
			else if (itemchance == 136) {
			cm.gainItem(2040521, 1, true, true);
			}
			else if (itemchance == 137) {
			cm.gainItem(2040604, 1, true, true);
			}
			else if (itemchance == 138) {
			cm.gainItem(2040604, 1, true, true);
			}
			else if (itemchance == 139) {
			cm.gainItem(2040605, 1, true, true);
			}
			else if (itemchance == 140) {
			cm.gainItem(2040605, 1, true, true);
			}
			else if (itemchance == 141) { 
			cm.gainItem(2040606, 1, true, true);
			}
			else if (itemchance == 142) { 
			cm.gainItem(2040607, 1, true, true);
			}
			else if (itemchance == 143) { 
			cm.gainItem(2040608, 1, true, true);
			}
			else if (itemchance == 144) { 
			cm.gainItem(2040608, 1, true, true);
			}
			else if (itemchance == 145) { 
			cm.gainItem(2040609, 1, true, true);
			}
			else if (itemchance == 146) { 
			cm.gainItem(2040609, 1, true, true);
			}
			else if (itemchance == 147) {
			cm.gainItem(2040610, 1, true, true);
			}
			else if (itemchance == 148) { 
			cm.gainItem(2040611, 1, true, true);
			}
			else if (itemchance == 149) { 
			cm.gainItem(2040624, 1, true, true);
			}
			else if (itemchance == 150) { 
			cm.gainItem(2040626, 1, true, true);
			}
			else if (itemchance == 151) { 
			cm.gainItem(2040712, 1, true, true);
			}
			else if (itemchance == 152) { 
			cm.gainItem(2040713, 1, true, true);
			}
			else if (itemchance == 153) { 
			cm.gainItem(2040714, 1, true, true);
			}
			else if (itemchance == 154) { 
			cm.gainItem(2040715, 1, true, true);
			}
			else if (itemchance == 155) { 
			cm.gainItem(2040716, 1, true, true);
			}
			else if (itemchance == 156) { 
			cm.gainItem(2040717, 1, true, true);
			}
			else if (itemchance == 157) { 
			cm.gainItem(2040808, 1, true, true);
			}
			else if (itemchance == 158) { 
			cm.gainItem(2040809, 1, true, true);
			}
			else if (itemchance == 159) { 
			cm.gainItem(2040812, 1, true, true);
			}
			else if (itemchance == 160) { 
			cm.gainItem(2040812, 1, true, true);
			}
			else if (itemchance == 161) { 
			cm.gainItem(2040813, 1, true, true);
			}
			else if (itemchance == 162) { 
			cm.gainItem(2040813, 1, true, true);
			}
			else if (itemchance == 163) { 
			cm.gainItem(2040906, 1, true, true);
			}
			else if (itemchance == 164) { 
			cm.gainItem(2040815, 1, true, true);
			}
			else if (itemchance == 165) { 
			cm.gainItem(2040814, 1, true, true);
			}
			else if (itemchance == 166) { 
			cm.gainItem(2040810, 1, true, true);
			}
			else if (itemchance == 167) { 
			cm.gainItem(2040811, 1, true, true);
			}
			else if (itemchance == 168) { 
			cm.gainItem(2040907, 1, true, true);
			}
			else if (itemchance == 169) { 
			cm.gainItem(2040916, 1, true, true);
			}
			else if (itemchance == 170) { 
			cm.gainItem(2040917, 1, true, true);
			}
			else if (itemchance == 171) { 
			cm.gainItem(2040921, 1, true, true);
			}
			else if (itemchance == 172) { 
			cm.gainItem(2040922, 1, true, true);
			}
			else if (itemchance == 173) { 
			cm.gainItem(2041035, 1, true, true);
			}
			else if (itemchance == 174) { 
			cm.gainItem(2041034, 1, true, true);
			}
			else if (itemchance == 175) { 
			cm.gainItem(2041036, 1, true, true);
			}
			else if (itemchance == 176) { 
			cm.gainItem(2041037, 1, true, true);
			}
			else if (itemchance == 177) { 
			cm.gainItem(2041038, 1, true, true);
			}
			else if (itemchance == 178) { 
			cm.gainItem(2041039, 1, true, true);
			}
			else if (itemchance == 179) { 
			cm.gainItem(2041040, 1, true, true);
			}
			else if (itemchance == 180) { 
			cm.gainItem(2041041, 1, true, true);
			}
			else if (itemchance == 181) { 
			cm.gainItem(2043004, 1, true, true);
			}
			else if (itemchance == 182) { 
			cm.gainItem(2043005, 1, true, true);
			}
			else if (itemchance == 183) { 
			cm.gainItem(2043006, 1, true, true);
			}
			else if (itemchance == 184) { 
			cm.gainItem(2043007, 1, true, true);
			}
			else if (itemchance == 185) { 
			cm.gainItem(2043104, 1, true, true);
			}
			else if (itemchance == 186) { 
			cm.gainItem(2043105, 1, true, true);
			}
			else if (itemchance == 187) { 
			cm.gainItem(2043204, 1, true, true);
			}
			else if (itemchance == 188) { 
			cm.gainItem(2043205, 1, true, true);
			}
			else if (itemchance == 189) { 
			cm.gainItem(2043304, 1, true, true);
			}
			else if (itemchance == 190) { 
			cm.gainItem(2043305, 1, true, true);
			}
			else if (itemchance == 191) { 
			cm.gainItem(2043704, 1, true, true);
			}
			else if (itemchance == 192) { 
			cm.gainItem(2043705, 1, true, true);
			}
			else if (itemchance == 193) { 
			cm.gainItem(2043804, 1, true, true);
			}
			else if (itemchance == 194) { 
			cm.gainItem(2043805, 1, true, true);
			}
			else if (itemchance == 195) { 
			cm.gainItem(2044004, 1, true, true);
			}			
			else if (itemchance == 196) { 
			cm.gainItem(2044005, 1, true, true);
			}			
			else if (itemchance == 197) { 
			cm.gainItem(2044104, 1, true, true);
			}			
			else if (itemchance == 198) { 
			cm.gainItem(2044105, 1, true, true);
			}			
			else if (itemchance == 199) { 
			cm.gainItem(2044104, 1, true, true);
			}			
			else if (itemchance == 200) { 
			cm.gainItem(2044105, 1, true, true);
			}
			else if (itemchance == 201) { 
			cm.gainItem(2044205, 1, true, true);
			}
			else if (itemchance == 202) { 
			cm.gainItem(2044204, 1, true, true);
			}
			else if (itemchance == 203) { 
			cm.gainItem(2044304, 1, true, true);
			}
			else if (itemchance == 204) { 
			cm.gainItem(2044305, 1, true, true);
			}
			else if (itemchance == 205) { 
			cm.gainItem(2044404, 1, true, true);
			}
			else if (itemchance == 206) { 
			cm.gainItem(2044405, 1, true, true);
			}
			else if (itemchance == 207) { 
			cm.gainItem(2044504, 1, true, true);
			}
			else if (itemchance == 208) { 
			cm.gainItem(2044505, 1, true, true);
			}
			else if (itemchance == 209) { 
			cm.gainItem(2044604, 1, true, true);
			}
			else if (itemchance == 210) { 
			cm.gainItem(2044605, 1, true, true);
			}
			else if (itemchance == 211) { 
			cm.gainItem(2044803, 1, true, true);
			}
			else if (itemchance == 212) { 
			cm.gainItem(2044804, 1, true, true);
			}
			else if (itemchance == 213) { 
			cm.gainItem(2044903, 1, true, true);
			}
			else if (itemchance == 214) { 
			cm.gainItem(2044904, 1, true, true);
			}
			else if (itemchance == 215) { 
			cm.gainItem(2040028, 1, true, true);
			}
			else if (itemchance == 216) { 
			cm.gainItem(2040030, 1, true, true);
			}
			else if (itemchance == 217) { 
			cm.gainItem(2040531, 1, true, true);
			}
			else if (itemchance == 218) { 
			cm.gainItem(2040533, 1, true, true);
			}
			else if (itemchance == 219) { 
			cm.gainItem(2040626, 1, true, true);
			}
			else if (itemchance == 220) { 
			cm.gainItem(2040932, 1, true, true);
			}
			else if (itemchance == 221) { 
			cm.gainItem(1002394, 1, true, true);
			}
			else if (itemchance == 222) { 
			cm.gainItem(1082146, 1, true, true);
			}
			else if (itemchance == 223) { 
			cm.gainItem(1082147, 1, true, true);
			}
			else if (itemchance == 224) { 
			cm.gainItem(1082150, 1, true, true);
			}
			else if (itemchance == 225) { 
			cm.gainItem(1002080, 1, true, true);
			}
			else if (itemchance == 226) { 
			cm.gainItem(1002081, 1, true, true);
			}
			else if (itemchance == 227) { 
			cm.gainItem(1002082, 1, true, true);
			}
			else if (itemchance == 228) { 
			cm.gainItem(1050070, 1, true, true);
			}
			else if (itemchance == 229) { 
			cm.gainItem(1050070, 1, true, true);
			}
			else if (itemchance == 230) { 
			cm.gainItem(1382037, 1, true, true);
			}
			else if (itemchance == 231) { 
			cm.gainItem(1382015, 1, true, true);
			}
			else if (itemchance == 232) { 
			cm.gainItem(1382035, 1, true, true);
			}
			else if (itemchance == 233) { 
			cm.gainItem(1102040, 1, true, true);
			}
			else if (itemchance == 234) { 
			cm.gainItem(1040100, 1, true, true);
			}
			else if (itemchance == 235) { 
			cm.gainItem(1040100, 1, true, true);
			}
			else if (itemchance == 236) {
			cm.gainItem(1060089, 1, true, true);
			}
			else if (itemchance == 237) {
			cm.gainItem(1051078, 1, true, true);
			}
			else if (itemchance == 238) {
			cm.gainItem(1050063, 1, true, true);
			}
			else if (itemchance == 239) {
			cm.gainItem(1051064, 1, true, true);
			}
			else if (itemchance == 240) {
			cm.gainItem(1051064, 1, true, true);
			}
			else if (itemchance == 241) {
			cm.gainItem(1050063, 1, true, true);
			}
			else if (itemchance == 242) {
			cm.gainItem(1051078, 1, true, true);
			}

			cm.dispose();
		}
	}
}