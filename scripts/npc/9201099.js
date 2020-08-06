//Mo
importPackage(net.sf.odinms.client);
function start() {
    if (cm.getQuestStatus(8224).equals(MapleQuestStatus.Status.COMPLETED)) {
		cm.sendOk("You could make yourself of some use....");
    } else {
    	cm.sendOk("What do #bYOU#k want...?");
	cm.dispose();
    }
}

function action(mode, type, selection) {
    cm.dispose();
}
