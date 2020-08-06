/**
 * @Author: Moogra
 */

var status;
var treeRandom = Math.floor(Math.random()*14+1);
function start() {
 	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.sendOk("Hmm... I guess you still have things to do here?");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
			if (status == 0) {
				if(cm.getPlayer().getMapId() > 209000000 && cm.getPlayer().getMapId() < 209000016) {
				cm.sendAcceptDecline("Do you want to stop decorating this beautiful tree?");
				} else {
				cm.sendNext("Do you want to decorate our beautiful tree?");
				}
		} else if (status == 1) {
			if (cm.getPlayer().getMapId() > 209000000 && cm.getPlayer().getMapId() < 209000016) {
				cm.warp(209000000);
				cm.dispose();
			} else {
				cm.warp(209000000 + treeRandom);
				cm.dispose();
			}
		}
	}
}