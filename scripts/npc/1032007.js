/* Author: Xterminator
	NPC Name: 		Joel
	Map(s): 		Victoria Road : Ellinia Station (101000300)
	Description: 		Ellinia Ticketing Usher
*/
var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
	if (status >= 0 && mode == 0) {
		cm.sendNext("You must have some business to take care of here, right?");
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		cm.sendSimple("Hello, I'm in charge of selling tickets for the ship ride to Orbis Station of Ossyria. The ride to Orbis takes off every 15 minutes, beginning on the hour, and it'll cost you #b5000 mesos#k. Are you sure you want to purchase a #bTicket to Orbis (Regular)#k?" +
                 "#k\r\n#L10#Warp right to Orbis for 5000 mesos" +
				 "#k\r\n#L20#Buy Ticket for 5000 mesos");
	} else if (selection == 20) {
		if (cm.getMeso() < 5000) {
			cm.sendNext("Are you sure you have #b5000 mesos#k? If so, then I urge you to check your etc. inventory, and see if it's full or not.");
			cm.dispose();
		} else {
			cm.gainMeso(-5000);
			cm.gainItem(4031045, 1);
			cm.dispose();
			}		
	} else if (selection == 10) {
		if (cm.getMeso() < 5000) {
			cm.sendNext("Are you sure you have #b5000 mesos#k? If so, then I urge you to check your etc. inventory, and see if it's full or not.");
			cm.dispose();
		} else {
			cm.gainMeso(-5000);
			cm.warp(200000100);
			cm.dispose();
			}	
		}
	}
}