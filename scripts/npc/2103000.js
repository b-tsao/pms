importPackage(net.sf.odinms.client);
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getQuestStatus(3900).equals(MapleQuestStatus.Status.STARTED)) {
	cm.forceCompleteQuest(3900);
	cm.gainExp(300);
	cm.playerMessage(5, "You drank from the oasis.");
    } else {
	cm.playerMessage(5, "What a pretty Oasis...");
	}
    cm.dispose();
}