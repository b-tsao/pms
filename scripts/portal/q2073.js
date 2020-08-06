importPackage(net.sf.odinms.client);
function enter(pi) {
	if (pi.getQuestStatus(2073).equals(MapleQuestStatus.Status.STARTED)){
	pi.warp(900000000);
	}
	else if(pi.getQuestStatus(2073).equals(MapleQuestStatus.Status.COMPLETED)) { 
	pi.playerMessage(5, "Utah thinks you smell funny");
	} else {
		pi.playerMessage(5, "A weird force is blocking you from entering..");
	}
}
