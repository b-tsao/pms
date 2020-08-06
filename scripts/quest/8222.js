/* Author: Xterminator (Modified by RMZero213)
	NPC Name: 		Roger
	Map(s): 		Maple Road : Lower level of the Training Camp (2)
	Description: 		Quest - Roger's Apple
*/
var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			qm.sendNext("Traveler, do you know anything about #bStormbreaker#k warriors?");
		} else if (status == 1) {
			qm.sendNext("I would love nothing more than to #bDESTROY#k these Stormbreakers... Please collect me 10 of their badges #i4032006#.");
		} else if (status == 2) {
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			qm.sendNext("You have defeated and collected the badges for me?! Thank you!");
		} else if (status == 1) {
			qm.gainExp(85000 * 8);
			qm.forceCompleteQuest(8222);
			qm.dispose();
		}
	}
}