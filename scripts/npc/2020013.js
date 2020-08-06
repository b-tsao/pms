/* Pedro
    Pirate 3rd job advancement
    El Nath: Chief's Residence (211000001)


    Custom Quest 100100, 100102


    Created by Matthew Prince of MapleRoyals.com - matt@mapleroyals.com
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
            if (!(cm.getJob().equals(MapleJob.BRAWLER) ||
                cm.getJob().equals(MapleJob.GUNSLINGER))) {
                cm.sendOk("And...Who might you be?");
                cm.dispose();
                return;
            }
            if (cm.getQuestStatus(100102).equals(MapleQuestStatus.Status.COMPLETED)) {
                cm.sendNext("Indeed, you have proven to be worthy of the strength I will now bestow upon you.");
            } else if (cm.getQuestStatus(100102).equals(MapleQuestStatus.Status.STARTED)) {
                cm.sendOk("Go and find me the #rNecklace of Wisdom#k which is hidden on the Holy Ground at the Snowfield.");
                cm.dispose();
            } else if (cm.getQuestStatus(100100).equals(MapleQuestStatus.Status.COMPLETED)) {
                cm.sendNext("I was right, your strength is truly excellent.");
            } else if (cm.getQuestStatus(100100).equals(MapleQuestStatus.Status.COMPLETED)) {
                cm.sendOk("Well, well. Now go and see #bKyrin#k. She will show you the way.");
                cm.dispose();
            } else if ((cm.getJob().equals(MapleJob.BRAWLER) ||
                cm.getJob().equals(MapleJob.GUNSLINGER)) &&
                cm.getLevel() >= 70 && 
                cm.getChar().getRemainingSp() <= (cm.getLevel() - 70) * 3) {
                cm.sendNext("You are a strong one.");
            } else {
                cm.sendOk("Your time has yet to come...");
                cm.dispose();
            }
        } else if (status == 1) {
            if (cm.getQuestStatus(100102).equals(MapleQuestStatus.Status.COMPLETED)) {
                if (cm.getJob().equals(MapleJob.BRAWLER)) {
                    cm.changeJob(MapleJob.MARAUDER);
                    cm.getChar().gainAp(5);
                    cm.gainItem(4031058, -1)
                    cm.gainItem(4031059, -1)
                    cm.sendOk("You are now a #bMarauder#k!");
                    cm.dispose();
                } else if (cm.getJob().equals(MapleJob.GUNSLINGER)) {
                    cm.changeJob(MapleJob.OUTLAW);
                    cm.getChar().gainAp(5);
                    cm.gainItem(4031058, -1)
                    cm.gainItem(4031059, -1)
                    cm.sendOk("You are now an #bOutlaw#k!");
                    cm.dispose();
                }
            } else if (cm.getQuestStatus(100100).equals(MapleQuestStatus.Status.COMPLETED)) {
                cm.sendAcceptDecline("Is your mind ready to undertake the final test?");
            } else {
                cm.sendAcceptDecline("But I can make you even stronger. Although you will have to prove not only your strength but your knowledge. Are you ready for the challenge?");
            }
        } else if (status == 2) {
            if (cm.getQuestStatus(100101).equals(MapleQuestStatus.Status.COMPLETED)) {
                cm.startQuest(100102);
                cm.sendOk("Go and find me the #rNecklace of Wisdom#k which is hidden on the Holy Ground at the Snowfield.");
                cm.dispose();
            } else {
                cm.startQuest(100100);
                cm.sendOk("Well, well. Now go and see #bKyrin#k. She will show you the way.");
                cm.dispose();
            }
        }
    }
}
