var status = 0;

function start() {
    if (cm.getQuestStatus(3923).equals(net.sf.odinms.client.MapleQuestStatus.Status.STARTED)) {

        if (!cm.haveItem(4031578)) {
		 cm.gainItem(4031578, 1);
		} else {
		cm.sendNext("You seem to have these already...");
		}
    } else {
		cm.sendNext("...");
	}
    cm.dispose();
}
function action(mode, type, selection){
	
}