/* Author: Xterminator (Modified by RMZero213)
	NPC Name: 		Roger
	Map(s): 		Maple Road : Lower level of the Training Camp (2)
	Description: 		Quest - Roger's Apple
*/
var status = -1;
function start(mode, type, selection){
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
			qm.sendNext("What is this that you brought me...?");
		} else if (status == 1) {
			qm.sendNextPrev("How'd you get this.");
		} else if (status == 2) {
			qm.gainExp(5000);
			qm.forceCompleteQuest(2124);
			qm.dispose();
		}
	}
}