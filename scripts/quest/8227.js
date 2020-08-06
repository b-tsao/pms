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
			qm.sendNext("I need you to be a messenger for me. Please deliver this #i4032032# to Jack.");
		} else if (status == 1) {	
			qm.gainItem(4032032, 1);
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {

}