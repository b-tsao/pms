importPackage(net.sf.odinms.client);


var status = 0;


function start() {
    status = -1;
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }else if (mode == 0){
        cm.dispose();
		return;		
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		if (cm.getQuestStatus(2050).equals


(MapleQuestStatus.Status.STARTED))
		cm.warp (101000100, 0);
		cm.dispose();
		if (cm.getQuestStatus(2051).equals


(MapleQuestStatus.Status.STARTED))
		cm.warp (101000102, 0);
		cm.dispose();
		}
	}
}