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
			qm.sendNext("I'll give you the Barricades' Marker of Heroism at one cost. I need some items... ");
		} else if (status == 1) {
			qm.sendOk("If you can collect these items I will give it to you. \r\n\ 1 Power Crystal #i4004000# \r\n\ 10 Gold Ores #i4010006#\r\n\ 4 Typhon Feathers #i4032005#");
			qm.forceStartQuest();
			qm.dispose();
		}
	}
}

function end(mode, type, selection) {

}