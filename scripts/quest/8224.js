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
			qm.sendNext("You! Traveler! I require your assistance in collecting some seeds...");
		} else if (status == 1) {
			qm.sendNextPrev("There are many possessed phantom trees around this area, and I require you to bring me #b50#k of their seeds so I can look into this manifestation of evil and look how to fix it!");
			} else if (status == 2) {
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {

}