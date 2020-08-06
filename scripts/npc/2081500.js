/* 4th Job Advancer for Pirates
 */

importPackage(net.sf.odinms.client);

var status = 0;
var job;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (cm.getLevel() < 120) {
                cm.sendNext("Sorry, but you have to be at least level 120 to make 4th Job Advancement.");
                status = 98;
            } else if (cm.getLevel() >=120) {
                status = 101;
                cm.sendNext("Hello, so you want to do your 4th Job Advancement?");
            }
        } else if (status == 102) {
            if (cm.getJob().equals(net.sf.odinms.client.MapleJob.MARAUDER)) {
                status = 111;
                cm.sendYesNo("Congratulations on reaching such a high level. Do you want to Job Advance now?");
            } else if (cm.getJob().equals(net.sf.odinms.client.MapleJob.OUTLAW)) {
                status = 114;
                cm.sendYesNo("Congratulations on reaching such a high level. Do you want to Job Advance now?");
            }
        } else if (status == 112) {
            cm.changeJob(MapleJob.BUCCANEER);
			cm.gainItem(2280003,1);
            cm.teachSkill(5121004,0,10);
            cm.teachSkill(5121005,0,10);
            cm.teachSkill(5121010,0,10);
            cm.teachSkill(5121001,0,10);
            cm.teachSkill(5121002,0,10);
            cm.teachSkill(5121003,0,10);
			cm.teachSkill(5121009,0,10);
			cm.teachSkill(5121007,0,10);
            cm.teachSkill(5221000,0,10);
            cm.teachSkill(5121000,0,10);
            cm.sendOk("There you go. Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
        } else if (status == 115) {
            cm.changeJob(MapleJob.CORSAIR);
			cm.gainItem(2280003,1);
            cm.teachSkill(5220001,0,10);
            cm.teachSkill(5220011,0,10);
            cm.teachSkill(5220002,0,10);
            cm.teachSkill(5121000,0,10);
            cm.teachSkill(5221003,0,10);
            cm.teachSkill(5221004,0,10);
            cm.teachSkill(5221009,0,10);
            cm.teachSkill(5221006,0,10);
            cm.teachSkill(5221007,0,10);
            cm.teachSkill(5221008,0,10);
            cm.sendOk("There you go. Hope you enjoy it. I've given you a full set of skills as well, aren't you lucky!");
        }
    }
}