/* 
Gachaphon Perion
Rev 1: By Matt of MapleRoyals.com (matt@mapleroyals.com)
Rev 2: By Chris of MapleRoyals.com (chris@mapleroyals.com)
Rev 3: By Chris of MapleRoyals.com (chris@mapleroyals.com)
*/

importPackage(net.sf.odinms.client);

var status = 0;
var itemchance = Math.floor(Math.random()*350+1);
var itemamount = Math.floor(Math.random()*50+1);
var energyamount = Math.floor(Math.random()*10+1);
var onyxamount = Math.floor(Math.random()*3+1);

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
				cm.sendNext("Welcome to #bNLC Gachapon#k.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
			}
		else if (status == 1) {
			if (cm.haveItem(5220000) && cm.canHold(1000000) && cm.canHold(2000000) && cm.canHold(3990000) && cm.canHold(4000000)) {
			cm.sendYesNo("I see you have a ticket of mine, do you wish to use it?");
			}
			else if (!cm.haveItem(5220000)) {
			cm.sendOk("You don't have any #bgachapon tickets#k.");
			cm.dispose();
			}
			else {
			cm.sendOk("You need to make some inventory space.");
			cm.dispose();
			}
		}
		else if (status == 2) {
			cm.gainItem(5220000, -1);
			if ((itemchance >= 1) && (itemchance <= 20)) { // Chance = 19
			cm.gainItem(2000004, itemamount); // Elixir
			}
			else if ((itemchance >= 21) && (itemchance <= 40)) { // Chance = 19
			cm.gainItem(2020012, itemamount); // Melting Cheese
			}
			else if ((itemchance >= 41) && (itemchance <= 50)) { // Chance = 9
			cm.gainItem(2000005, itemamount); // Power Elixir
			}
			else if (itemchance == 51) {
			cm.gainItem(2040002, 1);//scroll for helm def 10%
			} 
			else if (itemchance == 52) {
			cm.gainItem(2040005, 1);//scroll for helm hp 10%
			} 
			else if (itemchance == 53) {
			cm.gainItem(2040018, 1);//scroll for helm acc 10%
			} 
			else if (itemchance == 54) {
			cm.gainItem(2040026, 1);//scroll for helm int 10%
			} 
			else if (itemchance == 55) {
			cm.gainItem(2040031, 1);//scroll for helm dex 10%
			} 
			else if (itemchance == 56) {
			cm.gainItem(2040107, 1);//scroll for face avoid 10%
			} 
			else if (itemchance == 57) {
			cm.gainItem(2040202, 1);//scroll for eye acc 10%
			} 
			else if (itemchance == 58) {
			cm.gainItem(2040207, 1);//scroll for eye int 10%
			} 
			else if (itemchance == 59) {
			cm.gainItem(2040302, 1);//scroll for earring int 10%
			} 
			else if (itemchance == 60) {
			cm.gainItem(2040312, 1);//scroll for earring def 10%
			} 
			else if (itemchance == 61) {
			cm.gainItem(2040318, 1);//scroll for earring dex 10%
			} 
			else if (itemchance == 62) {
			cm.gainItem(2040323, 1);//scroll for earring luk 10%
			} 
			else if (itemchance == 63) {
			cm.gainItem(2040328, 1);//scroll for earring hp 10%
			} 
			else if (itemchance == 64) {
			cm.gainItem(2040402, 1);//scroll for top def 10%
			} 
			else if (itemchance == 65) {
			cm.gainItem(2040414, 1);//scroll for top luk 100%
			} 
			else if (itemchance == 66) {
			cm.gainItem(2040419, 1);//scroll for top str 10%
			}
			else if (itemchance == 67) {
			cm.gainItem(2040422, 1);//scroll for top hp 10%
			}
			else if (itemchance == 68) {
			cm.gainItem(2040427, 1);//scroll for top luk 10%
			}
			else if (itemchance == 69) {
			cm.gainItem(2040502, 1);//scroll for overall dex 10%
			}
			else if (itemchance == 70) {
			cm.gainItem(2040505, 1);//scroll for overall def 10%
			}
			else if (itemchance == 71) {
			cm.gainItem(2040514, 1);//scroll for overall int 10%
			}
			else if (itemchance == 72) {
			cm.gainItem(2040517, 1);//scroll for overall luk 10%
			}
			else if (itemchance == 73) {
			cm.gainItem(2040534, 1);//scroll for overall str 10%
			}
			else if (itemchance == 74) {
			cm.gainItem(2040602, 1);//scroll for bottom def 10%
			}
			else if (itemchance == 75) {
			cm.gainItem(2040614, 1);//scroll for bottom dex 100%
			}
			else if (itemchance == 76) {
			cm.gainItem(2040619, 1);//scroll for bottom jump 10%
			}
			else if (itemchance == 77) {
			cm.gainItem(2040622, 1);//scroll for bottom hp 10%
			}
			else if (itemchance == 78) {
			cm.gainItem(2040627, 1);//scroll for bottom dex 10%
			}
			else if (itemchance == 79) {
			cm.gainItem(2040702, 1);//scroll for shoes dex 10%
			}
			else if (itemchance == 80) {
			cm.gainItem(2040705, 1);//scroll for shoes jump 10%
			}
			else if (itemchance == 81) {
			cm.gainItem(2040708, 1);//scroll for shoes speed 10%
			}
			else if (itemchance == 82) {
			cm.gainItem(2040802, 1);//scroll gloves for dex 10%
			}
			else if (itemchance == 83) {
			cm.gainItem(2040805, 1);//scroll gloves for att 10%
			}
			else if (itemchance == 84) {
			cm.gainItem(2040818, 1);//scroll gloves for m.att 10%
			}
			else if (itemchance == 85) {
			cm.gainItem(2040825, 1);//scroll gloves for hp 10%
			}
			else if (itemchance == 86) {
			cm.gainItem(2040902, 1);//scroll shield def 10%
			}
			else if (itemchance == 87) {
			cm.gainItem(2040915, 1);//scroll shield att 10%
			}
			else if (itemchance == 88) {
			cm.gainItem(2040920, 1);//scroll shield m.att 10%
			}
			else if (itemchance == 89) {
			cm.gainItem(2040925, 1);//scroll shield for luk 10%
			}
			else if (itemchance == 90) {
			cm.gainItem(2040928, 1);//scroll shield for hp 10%
			}
			else if (itemchance == 91) {
			cm.gainItem(2040933, 1);//scroll shield for str 10%
			}
			else if (itemchance == 92) {
			cm.gainItem(2041002, 1);//scroll cape for m.def 10%
			}
			else if (itemchance == 93) {
			cm.gainItem(2041005, 1);//scroll cape for def 10%
			}
			else if (itemchance == 94) {
			cm.gainItem(2041008, 1);//scroll cape for hp 10%
			}
			else if (itemchance == 95) {
			cm.gainItem(2041011, 1);//scroll cape for mp 10%
			}
			else if (itemchance == 96) {
			cm.gainItem(2041014, 1);//scroll cape for str 10%
			}
			else if (itemchance == 97) {
			cm.gainItem(2041017, 1);//scroll cape for int 10%
			}
			else if (itemchance == 98) {
			cm.gainItem(2041020, 1);//scroll cape for dex 10%
			}
			else if (itemchance == 99) {
			cm.gainItem(2041023, 1);//scroll cape for luk 10%
			}
			else if (itemchance == 100) {
			cm.gainItem(2043002, 1);//scroll one-handed sword for att 10%
			}
			else if (itemchance == 101) {
			cm.gainItem(2043010, 1);//scroll one-handed sword for m.att 100%
			}
			else if (itemchance == 102) {
			cm.gainItem(2043019, 1);//scroll one-handed sword for acc 10%
			}
			else if (itemchance == 103) {
			cm.gainItem(2043102, 1);//scroll one-handed axe for att 10%
			}
			else if (itemchance == 104) {
			cm.gainItem(2043114, 1);//scroll one-handed axe for acc 10%
			}
			else if (itemchance == 105) {
			cm.gainItem(2043202, 1);//scroll one-handed bw for att 10%
			}
			else if (itemchance == 106) {
			cm.gainItem(2043214, 1);//scroll one-handed bw for acc 10%
			}
			else if (itemchance == 107) {
			cm.gainItem(2043302, 1);//scroll dagger for att 10%
			}
			else if (itemchance == 108) {
			cm.gainItem(2043702, 1);//scroll wand for m.att 10%
			}
			else if (itemchance == 109) {
			cm.gainItem(2043802, 1);//scroll staff for m.att 10%
			}
			else if (itemchance == 110) {
			cm.gainItem(2044002, 1);//scroll two-handed sword for att 10%
			}
			else if (itemchance == 111) {
			cm.gainItem(2044014, 1);//scroll two-handed sword for acc 10%
			}
			else if (itemchance == 112) {
			cm.gainItem(2044102, 1);//scroll two-handed axe for att 10%
			}
			else if (itemchance == 113) {
			cm.gainItem(2044114, 1);//scroll two-handed axe for acc 10%
			}
			else if (itemchance == 114) {
			cm.gainItem(2044202, 1);//scroll two-handed bw for att 10%
			}
			else if (itemchance == 115) {
			cm.gainItem(2044214, 1);//scroll two-handed bw for acc 10%
			}
			else if (itemchance == 116) {
			cm.gainItem(2044302, 1);//scroll spear for att 10%
			}
			else if (itemchance == 117) {
			cm.gainItem(2044314, 1);//scroll spear for acc 10%
			}
			else if (itemchance == 118) {
			cm.gainItem(2044402, 1);//scroll polearm for att 10%
			}
			else if (itemchance == 119) {
			cm.gainItem(2044414, 1);//scroll polearm for acc 10%
			}
			else if (itemchance == 120) {
			cm.gainItem(2044502, 1);//scroll bow for att 10%
			}
			else if (itemchance == 121) {
			cm.gainItem(2044602, 1);//scroll xbow for att 10%
			}
			else if (itemchance == 122) {
			cm.gainItem(2044702, 1);//scroll claw for att 10%
			}
			else if (itemchance == 123) {
			cm.gainItem(2044802, 1);//scroll knuckler for att 10%
			}
			else if (itemchance == 124) {
			cm.gainItem(2044809, 1);//scroll knuckler for acc 10%
			}
			else if (itemchance == 125) {
			cm.gainItem(2044902, 1);//scroll gun for att 10%
			}
			else if (itemchance == 126) {
			cm.gainItem(2048010, 1);//scroll pet equip str 60%
			}
			else if (itemchance == 127) {
			cm.gainItem(2048011, 1);//scroll pet equip int 60%
			}
			else if (itemchance == 128) {
			cm.gainItem(2048012, 1);//scroll pet equip dex 60%
			}
			else if (itemchance == 129) {
			cm.gainItem(2048013, 1);//scroll pet equip luk 60%
			}
			else if (itemchance == 130) {
			cm.gainItem(4032015, 1);//TAO OF SHADOWS      
			}
			else if ((itemchance >= 131) && (itemchance <= 134)) { 
			cm.gainItem(2061001, itemamount);//bronze arrow for xbow
			}
			else if ((itemchance >= 135) && (itemchance <= 138)) { 
			cm.gainItem(2060001, itemamount);//bronze arrow for bow
			}
			else if (itemchance == 139) {
			cm.gainItem(2070005, 1);//steely 
			}
			else if (itemchance == 140) {
			cm.gainItem(2330004, 1);//Shiny Bullet
			}
			else if ((itemchance >= 141) && (itemchance <= 147)) { 
			cm.gainItem(2002023, itemamount);//Ginger Ale
			}
			else if ((itemchance >= 148) && (itemchance <= 152)) { 
			cm.gainItem(2030020, itemamount);//Return scroll to NLC
			}
			else if (itemchance == 153) {
			cm.gainItem(3010041, 1); //SKULL THRONE
			cm.gachNotice(cm.getC(), 3010041);
			}
			else if ((itemchance >= 154) && (itemchance <= 156)) { 
			cm.gainItem(1032013, 1, true, true);//Red-hearted earrings
			}
			else if ((itemchance >= 157) && (itemchance <= 159)) { 
			cm.gainItem(1032015, 1, true, true);//Metal Silver Earrings
			}
			else if ((itemchance >= 160) && (itemchance <= 162)) { 
			cm.gainItem(1032025, 1, true, true);//Leaf Earrings
			}
			else if ((itemchance >= 163) && (itemchance <= 166)) { 
			cm.gainItem(1002036, 1, true, true);//Green Jester
			}
			else if ((itemchance >= 167) && (itemchance <= 170)) { 
			cm.gainItem(1002044, 1, true, true);//Mithril Koif
			}
			else if ((itemchance >= 171) && (itemchance <= 174)) { 
			cm.gainItem(1002046, 1, true, true);//Red Bone Helm
			}
			else if ((itemchance >= 175) && (itemchance <= 178)) { 
			cm.gainItem(1002073, 1, true, true);//Red Wizardy 
			}
			else if ((itemchance >= 179) && (itemchance <= 182)) { 
			cm.gainItem(1002063, 1, true, true);//Blue baseball Cap
			}
			else if ((itemchance >= 183) && (itemchance <= 186)) { 
			cm.gainItem(1002089, 1, true, true);//Green Bamboo
			}
			else if ((itemchance >= 184) && (itemchance <= 187)) { 
			cm.gainItem(1002126, 1, true, true);//Green Ghetto Beanie
			}
			else if ((itemchance >= 188) && (itemchance <= 191)) { 
			cm.gainItem(1002185, 1, true, true);//Dark Pilfer
			}
			else if ((itemchance >= 192) && (itemchance <= 195)) { 
			cm.gainItem(1002366, 1, true, true);//Dark Oriental Fury Hat
			}
			else if ((itemchance >= 196) && (itemchance <= 199)) { 
			cm.gainItem(1102004, 1, true, true);//Dark Napolean
			}
			else if (itemchance == 200) {
			cm.gainItem(4032016, 1, true, true);//TAO OF SIGHT
			}
			else if (itemchance == 201) {
			cm.gainItem(4032017, 1, true, true);//TAO OF HARMONY
			}
			else if ((itemchance >= 202) && (itemchance <= 204)) { 
			cm.gainItem(1102034, 1, true, true);//Blue Giles Cape
			}
			else if ((itemchance >= 205) && (itemchance <= 208)) { 
			cm.gainItem(1040000, 1, true, true);//Yellow Jangoon Armor
			}
			else if ((itemchance >= 209) && (itemchance <= 212)) { 
			cm.gainItem(1040043, 1, true, true);//Red Pao
			}
			else if ((itemchance >= 213) && (itemchance <= 216)) { 
			cm.gainItem(1040099, 1, true, true);//Oaker Scorpio
			}
			else if ((itemchance >= 217) && (itemchance <= 219)) { 
			cm.gainItem(1041101, 1, true, true);//Blue Mystique
			}
			else if ((itemchance >= 220) && (itemchance <= 223)) { 
			cm.gainItem(1082071, 1, true, true);//Aqua Brace
			}
			else if ((itemchance >= 224) && (itemchance <= 227)) { 
			cm.gainItem(1082083, 1, true, true);//Dark Willow 
			}
			else if ((itemchance >= 228) && (itemchance <= 230)) { 
			cm.gainItem(1082104, 1, true, true);//Mithril Husk
			}
			else if ((itemchance >= 231) && (itemchance <= 233)) { 
			cm.gainItem(1082119, 1, true, true);//Purple Larceny
			}
			else if ((itemchance >= 234) && (itemchance <= 236)) { 
			cm.gainItem(1082133, 1, true, true);//Red Sage
			}
			else if (itemchance == 237) {
			cm.gainItem(1082145, 1, true, true);//YWG
			}
			else if (itemchance == 238) {
			cm.gainItem(1082146, 1, true, true);//RWG
			}
			else if (itemchance == 239) {
			cm.gainItem(1082147, 1, true, true);//BlWG
			}
			else if (itemchance == 240) {
			cm.gainItem(1082148, 1, true, true);//RWG
			}
			else if (itemchance == 241) {
			cm.gainItem(4031750, 1, true, true);//DARK MATTER
			}
			else if (itemchance == 242) {
			cm.gainItem(1082150, 1, true, true);//GWG
			}
			else if ((itemchance >= 243) && (itemchance <= 244)) { 
			cm.gainItem(1082159, 1, true, true);//Blue Arcina
			}
			else if ((itemchance >= 245) && (itemchance <= 248)) { 
			cm.gainItem(1050021, 1, true, true);//Blue Crusader Chainmail
			}
			else if ((itemchance >= 249) && (itemchance <= 252)) { 
			cm.gainItem(1050022, 1, true, true);//Dark Crusader Chainmail 
			}
			else if ((itemchance >= 253) && (itemchance <= 256)) { 
			cm.gainItem(1050025, 1, true, true);//White Doros Robe
			}
			else if ((itemchance >= 257) && (itemchance <= 260)) { 
			cm.gainItem(1050031, 1, true, true);//White Chaos Robe
			}
			else if ((itemchance >= 261) && (itemchance <= 264)) { 
			cm.gainItem(1050075, 1, true, true);//Red Pris
			}
			else if ((itemchance >= 265) && (itemchance <= 267)) { 
			cm.gainItem(1050104, 1, true, true);//Red Varuna
			}
			else if (itemchance == 268) {
			cm.gainItem(1050127, 1, true, true);//Black Bath Towel
			}
			else if ((itemchance >= 269) && (itemchance <= 273)) { 
			cm.gainItem(1051015, 1, true, true);//Blood Fitted Mail
			}
			else if ((itemchance >= 274) && (itemchance <= 277)) { 
			cm.gainItem(1060000, 1, true, true);//Black Jangoon Pants
			}
			else if ((itemchance >= 278) && (itemchance <= 281)) { 
			cm.gainItem(1061047, 1, true, true);//Red Amoria Skirt 
			}
			else if ((itemchance >= 282) && (itemchance <= 285)) { 
			cm.gainItem(1061071, 1, true, true);//Dark Shadow Pants
			}
			else if (itemchance == 286) {
			cm.gainItem(1051140, 1, true, true);//Yellow Bath Towel
			}
			else if ((itemchance >= 287) && (itemchance <= 289)) { 
			cm.gainItem(1092008, 1, true, true);//Pan Lid
			}
			else if ((itemchance >= 290) && (itemchance <= 294)) { 
			cm.gainItem(1092003, 1, true, true);//Stolen Fence
			}
			else if ((itemchance >= 295) && (itemchance <= 298)) { 
			cm.gainItem(1092045, 1, true, true);//Maple Magician Shield
			}
			else if ((itemchance >= 299) && (itemchance <= 302)) { 
			cm.gainItem(1092046, 1, true, true);//Maple Warrior Shield
			}
			else if ((itemchance >= 303) && (itemchance <= 306)) { 
			cm.gainItem(1092047, 1, true, true);//Maple Theif Shield
			}
			else if ((itemchance >= 307) && (itemchance <= 309)) { 
			cm.gainItem(1092023, 1, true, true);//Steel Aquila Shield
			}
			else if (itemchance == 310) {
			cm.gainItem(1092022, 1, true, true);//Palette
			}
			else if ((itemchance >= 311) && (itemchance <= 313)) { 
			cm.gainItem(1072168, 1, true, true);//Red Snowshoes
			}
			else if ((itemchance >= 314) && (itemchance <= 316)) { 
			cm.gainItem(1072169, 1, true, true);//Blue Snowshoes
			}
			else if ((itemchance >= 317) && (itemchance <= 319)) { 
			cm.gainItem(1072170, 1, true, true);//Green Snowshoes
			}
			else if ((itemchance >= 320) && (itemchance <= 322)) { 
			cm.gainItem(1072171, 1, true, true);//Black Snowshoes
			}
			else if ((itemchance >= 323) && (itemchance <= 324)) { 
			cm.gainItem(1072338, 1, true, true);//Purple Snowshoes
			}
			else if ((itemchance >= 325) && (itemchance <= 327)) { 
			cm.gainItem(1072108, 1, true, true);//Black Green-Lined Shoes
			}
			else if ((itemchance >= 328) && (itemchance <= 330)) { 
			cm.gainItem(1072123, 1, true, true);//Green Steel-Tip Boots
			}
			else if (itemchance == 331) {
			cm.gainItem(1302024, 1, true, true);//Newspaper Sword
			}
			else if (itemchance == 332) {
			cm.gainItem(1302026, 1, true, true);//Black Umbrella
			}
			else if ((itemchance >= 333) && (itemchance <= 337)) { 
			cm.gainItem(1302012, 1, true, true);//Red Katana
			}
			else if (itemchance == 338) {
			cm.gainItem(1382045, 1, true, true);//Ele staff 
			}
			else if (itemchance == 339) {
			cm.gainItem(1382046, 1, true, true);//Ele staff 
			}
			else if (itemchance == 340) {
			cm.gainItem(1382047, 1, true, true);//Ele staff 
			}
			else if (itemchance == 341) {
			cm.gainItem(1382048, 1, true, true);//Ele staff 
			}
			else if (itemchance == 342) {
			cm.gainItem(1382049, 1, true, true);//Ele staff 
			}
			else if (itemchance == 343) {
			cm.gainItem(1382050, 1, true, true);//Ele staff 
			}
			else if ((itemchance >= 344) && (itemchance <= 348)) { 
			cm.gainItem(1382028, 1, true, true);//Dark Ritual
			}
			else if ((itemchance >= 349) && (itemchance <= 354)) { 
			cm.gainItem(1382036, 1, true, true);//Dragon Staff
			}
			else if (itemchance == 355) {
			cm.gainItem(1382051, 1, true, true);//Ele staff 
			}
			else if (itemchance == 356) {
			cm.gainItem(1382052, 1, true, true);//Ele staff 
			}
			else if ((itemchance >= 357) && (itemchance <= 365)) { 
			cm.gainItem(1372004, 1, true, true);//Ice wand
			}
			else if (itemchance == 366) {
			cm.gainItem(1372035, 1, true, true);//Ele staff 
			}
			else if (itemchance == 367) {
			cm.gainItem(1372036, 1, true, true);//Ele staff 
			}
			else if (itemchance == 368) {
			cm.gainItem(1372037, 1, true, true);//Ele staff 
			}
			else if (itemchance == 369) {
			cm.gainItem(1372038, 1, true, true);//Ele staff 
			}
			else if (itemchance == 370) {
			cm.gainItem(1372039, 1, true, true);//Ele staff 
			}
			else if (itemchance == 371) {
			cm.gainItem(1372040, 1, true, true);//Ele staff 
			}
			else if ((itemchance >= 372) && (itemchance <= 380)) { 
			cm.gainItem(1332014, 1, true, true);//Gephart
			}
			else if (itemchance == 381) {
			cm.gainItem(1372041, 1, true, true);//Ele staff 
			}
			else if (itemchance == 382) {
			cm.gainItem(1372042, 1, true, true);//Ele staff 
			}
			else if (itemchance == 383) {
			cm.gainItem(1372007, 1, true, true);//Cromi
			}
			else if (itemchance == 384) {
			cm.gainItem(4031751, 1, true, true);//VORTICULAR GYRO
			}
			else if (itemchance == 385) {
			cm.gainItem(4031752, 1, true, true);//BLINKING DINGBAT
			}
			else if (itemchance == 386) {
			cm.gainItem(4031753, 1, true, true);//ZETA RESIDUE
			}
			else if (itemchance == 387) {
			cm.gainItem(4031754, 1, true, true);//BLACK VERSAL MATERIAL
			}
			else if (itemchance == 388) {
			cm.gainItem(4031755, 1, true, true);//TARU TOTEM
			}
			else if (itemchance == 389) {
			cm.gainItem(4031756, 1, true, true);//MYSTIC ASTROLABE
			}
			else if (itemchance == 340) {
			cm.gainItem(4031757, 1, true, true);//ANTELLION RELIC
			}
			else if (itemchance == 341) {
			cm.gainItem(4031758, 1, true, true);//NARICIAN JEWEL
			}
			else if (itemchance == 342) {
			cm.gainItem(4031759, 1, true, true);//SUBANI ANKH
			}
			else if (itemchance == 343) {
			cm.gainItem(4031913, 1, true, true);//STONE TIGER HEAD
			}
			else if (itemchance == 344) {
			cm.gainItem(4031914, 1, true, true);//TYPHOON CREST 
			}
			else if (itemchance == 345) {
			cm.gainItem(4031915, 1, true, true);//LEFAY JEWEL
			}
			else if (itemchance == 346) {
			cm.gainItem(4031916, 1, true, true);//PHARAOHS WRAPPINGS
			}
			else if (itemchance == 347) {
			cm.gainItem(4031917, 1, true, true);//CRYSTAL SHARD
			}
			else if (itemchance == 348) {
			cm.gainItem(4032026, 1, true, true);//RIDLEYS STONE
			}
			else if (itemchance == 349) {
			cm.gainItem(4032021, 1, true, true);//TRIFERIUM FUEL CELL
			}
			else if (itemchance == 350) {
			cm.gainItem(4032022 , 1, true, true);//DIFERIUM FUEL CELL
			}
			else if (itemchance == 351) {
			cm.gainItem(4031936 , 1, true, true);//TARU SPIRIT FEATHER
			}
			cm.dispose();
		}
	}
}