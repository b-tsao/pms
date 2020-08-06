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
			qm.sendNext("Hey! We gotta talk and be fast. There's an even deeper part of Masteria called Crimsonwood Keep... this is astounding. I need your help, my brother found it, #bJack#k. Please go look for him.");
		} else if (status == 1) {			
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
			qm.sendNext("My brother John must've sent you! I am #bJack Barricade#k.");
		} else if (status == 1) {	
			qm.gainExp(175000*8);
			qm.gainItem(3992040, 1);
			qm.forceCompleteQuest(8219);
			qm.dispose();
		}
	}
}