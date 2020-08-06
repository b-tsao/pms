/* 
Gachaphon EXP 
Rev 1: By Matt of MapleRoyals.com (matt@mapleroyals.com)
*/

importPackage(net.sf.odinms.client);

var status = 0;
var itemchance = Math.floor(Math.random()*135);
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
				cm.sendNext("Welcome to #bEXP Gachapon#k.\r\nI give things to people if they give me cardboard tickets~!\r\nThe tickets look like this: #v5220000#");
			}
		else if (status == 1) {
			if (cm.haveItem(5220000) && cm.canHold(1000000) && cm.canHold(2000000) && cm.canHold(3990000) && cm.canHold(4000000)) {
			cm.sendYesNo("I see you have a ticket of mine, do you wish to use it?");
			}
			else if (!cm.haveItem(5220000)) {
			cm.sendOk("You dont have any #bgachapon tickets#k.");
			cm.dispose();
			}
		}
		else if (status == 2) {
			cm.gainItem(5220000, -1);
			if ((itemchance >= 1) && (itemchance <= 2)) { // Chance = 1
			cm.gainItem(2370000, 1); // Song of 
                        cm.gachNotice(cm.getC(), 2370000);
			}
                        if ((itemchance >= 3) && (itemchance <= 5)) { // Chance = 2
			cm.gainItem(2370001, 1); // Stories of
			}
                        if ((itemchance >= 7) && (itemchance <= 9)) { // Chance = 3
			cm.gainItem(2370002, 1); // Travels of
			}
                        if ((itemchance >= 10) && (itemchance <= 14)) { // Chance = 4
			cm.gainItem(2370003, 1); // Writ 10
			}
                        if ((itemchance >= 15) && (itemchance <= 20)) { // Chance = 6
			cm.gainItem(2370004, 1); // Writ 9
			}
                        if ((itemchance >= 21) && (itemchance <= 29)) { // Chance = 7
			cm.gainItem(2370005, 1); // Writ 8
			}
                        if ((itemchance >= 30) && (itemchance <= 39)) { // Chance = 9
			cm.gainItem(2370006, 1); // Writ 7
			}
                        if ((itemchance >= 40) && (itemchance <= 50)) { // Chance = 10
			cm.gainItem(2370007, 1); // Writ 6
			}
                        if ((itemchance >= 51) && (itemchance <= 61)) { // Chance = 10
			cm.gainItem(2370008, 1); // Writ 5
			}
                        if ((itemchance >= 62) && (itemchance <= 73)) { // Chance = 12
			cm.gainItem(2370009, 1); // Writ 4
			}
                        if ((itemchance >= 74) && (itemchance <= 89)) { // Chance = 15
			cm.gainItem(2370010, 1); // Writ 3
			}
                        if ((itemchance >= 90) && (itemchance <= 109)) { // Chance = 19
			cm.gainItem(2370011, 1); // Writ 2
			}
                        if ((itemchance >= 110) && (itemchance <= 135)) { // Chance = 25
			cm.gainItem(2370012, 1); // Writ 2
			}
                }
	}
}
