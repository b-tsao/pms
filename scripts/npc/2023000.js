var fromMap = new Array(211000000,220000000,240000000);
var toMap = new Array(211040200,220050300,240030000);
var location;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if(mode == -1) {
		cm.dispose();
} else {
	if(mode == 0) {
		cm.dispose();
		cm.sendOk("Think about it some more!");
		return;
	}
	if(mode == 1) {
		status++;
	}
}
	if(status == 0) {
		switch(cm.getChar().getMapId()) {
			case fromMap[0]:
				location = 0;
				break;
			case fromMap[1]:
				location = 1;
				break;
			case fromMap[2]:
				location = 2;
				break;
		}
		cm.sendYesNo("Hello there! This Taxi will take you from #m"+cm.getChar().getMapId()+"# to #b#m"+toMap[location]+"##k instantly! The transportation fee of #b45000 meso#k may seem expensive, but not that much when you want to easily transport through danger zones! Would you like to go?");
	} else if(status == 1) {
	if(cm.getMeso() < 45000) {
			cm.sendNext("You don't seem to have enough mesos. I am terribly sorry, but I cannot help you unless you pay up. Bring in the mesos by hunting more and come back when you have enough.");
			cm.dispose();
		} else{
			cm.warp(toMap[location]);
			cm.gainMeso(-45000[location]);
			cm.dispose();
		}
	}
}
