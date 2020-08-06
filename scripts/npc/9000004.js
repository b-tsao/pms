importPackage(net.sf.odinms.client);

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 1) {
			cm.sendOk("Make up your mind and visit me again.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
		if (cm.getJob().equals(MapleJob.BISHOP)) {
					cm.teachSkill(2321004, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.FP_ARCHMAGE)) {
					cm.teachSkill(2121004, 0, 10);
				cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.IL_ARCHMAGE)) {
					cm.teachSkill(2221004, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.BOWMASTER)) {
					cm.teachSkill(3121003, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.CROSSBOWMASTER)) {
					cm.teachSkill(3221003, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.NIGHTLORD)) {
					//cm.teachSkill(4121007, 0, 10); 
					cm.teachSkill(4121003, 0, 10);
					cm.teachSkill(4121004, 0, 10);
					cm.teachSkill(4121008, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.HERO)) {
					cm.teachSkill(1121010, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.DARKKNIGHT)) {
					cm.teachSkill(1321003, 0, 10); 
					cm.teachSkill(1320008, 0, 25);
					cm.teachSkill(1320009, 0, 25);
					cm.teachSkill(1321002, 0, 10);
					cm.teachSkill(1320006, 0, 10);
					cm.teachSkill(1321007, 0, 10);
					cm.teachSkill(1320005, 0, 10);
					cm.teachSkill(1321001, 0, 10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.BUCCANEER)) {
					cm.teachSkill(5121009,0,10);
			        cm.teachSkill(5121007,0,10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
				} else if (cm.getJob().equals(MapleJob.PALADIN)) {
					cm.teachSkill(1221007,0,10);
			        cm.teachSkill(1220010,0,10);
					cm.teachSkill(1221011,0,10);
					cm.sendOk("Your skills should now be refunded.");
					cm.dispose();
					

				
				} else {
				cm.sendOk("You are not qualified to speak to me.");
		cm.dispose(); 
				}
				}
		}
}