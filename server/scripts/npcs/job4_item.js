/* 
6905 = warrior 4031343, 4031344
6915 = magician 4031511, 4031512
6925 = bowman 4031343, 4031344
6935 = thief 4031518, 4031517
6945 = pirate 4031511, 4031512
*/

if ((!player.isQuestActive(6905) || !player.isQuestActive(6915) || !player.isQuestActive(6925) || !player.isQuestActive(6935) || !player.isQuestActive(6945))) {
    if (player.getJob() > 100 && player.getJob() < 200 && player.isQuestActive(6904)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        let selection = npc.askAccept("You need #b#t4031343##k and #b#t4031344##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        switch (selection) {
            case 0:
                npc.say("Please come back if you ever change your mind.");
                break;
            case 1:
                player.startQuest(6905, npc.getNpcId());
                break;
        }
    }
    if (player.getJob() > 200 && player.getJob() < 300 && player.isQuestActive(6914)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        let selection = npc.askAccept("You need #b#t4031511##k and #b#t4031512##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        switch (selection) {
            case 0:
                npc.say("Please come back if you ever change your mind.");
                break;
            case 1:
                player.startQuest(6915, npc.getNpcId());
                break;
        }
    }
    if (player.getJob() > 300 && player.getJob() < 400 && player.isQuestActive(6924)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        let selection = npc.askAccept("You need #b#t4031343##k and #b#t4031344##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        switch (selection) {
            case 0:
                npc.say("Please come back if you ever change your mind.");
                break;
            case 1:
                player.startQuest(6925, npc.getNpcId());
                break;
        }
    }
    if (player.getJob() > 400 && player.getJob() < 500 && player.isQuestActive(6934)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        let selection = npc.askAccept("You need #b#t4031517##k and #b#t4031518##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        switch (selection) {
            case 0:
                npc.say("Please come back if you ever change your mind.");
                break;
            case 1:
                player.startQuest(6935, npc.getNpcId());
                break;
        }
    }
    if (player.getJob() > 500 && player.getJob() < 600 && player.isQuestActive(6944)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        let selection = npc.askAccept("You need #b#tt4031511##k and #b#t4031512##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        switch (selection) {
            case 0:
                npc.say("Please come back if you ever change your mind.");
                break;
            case 1:
                player.startQuest(6945, npc.getNpcId());
                break;
        }
    }
} else if (player.getLevel() < 120) {
    npc.say("Hello young one. If you are ever in need of the #bStar#k and the #bPentagon#k speak to me.");
    } else {
        npc.say("You've grown much since I first saw you. Remember to use your strength for the greater good.")
    }
