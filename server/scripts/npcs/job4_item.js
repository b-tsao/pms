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
        npc.sayNext("You need #b#t4031343##k and #b#t4031344##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        npc.sayNext("This forest and the dragons that inhabit it protect herrings, which protect the eggs of the dragons who protet the forest. But we think we need to protect ourselves. They're fairly dangerous. I've also heard of a mighty dragon known as Horntail, somewhere outside of town. His power is said to be unrivaled...");
        npc.sayNext("At any rate, I heard that those merchants who sell order sheets in the other town sells #b#t4031348##k, which can help the owner of the sheet. We might as well protect our town and Minar forest with the order sheet.");
        npc.askAccept("Can you get me the order sheet for our town?\r\n Then I'll give you what you're looking for.");
        npc.startQuest(6905);
    }
    if (player.getJob() > 200 && player.getJob() < 300 && player.isQuestActive(6914)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        npc.sayNext("You need #b#t4031511##k and #b#t4031512##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        npc.sayNext("This forest and the dragons that inhabit it protect herrings, which protect the eggs of the dragons who protet the forest. But we think we need to protect ourselves. They're fairly dangerous. I've also heard of a mighty dragon known as Horntail, somewhere outside of town. His power is said to be unrivaled...");
        npc.sayNext("At any rate, I heard that those merchants who sell order sheets in the other town sells #b#t4031348##k, which can help the owner of the sheet. We might as well protect our town and Minar forest with the order sheet.");
        npc.askAccept("Can you get me the order sheet for our town?\r\n Then I'll give you what you're looking for.");
        npc.startQuest(6915);
    }
    if (player.getJob() > 300 && player.getJob() < 400 && player.isQuestActive(6924)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        npc.sayNext("You need #b#t4031343##k and #b#t4031344##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        npc.sayNext("This forest and the dragons that inhabit it protect herrings, which protect the eggs of the dragons who protet the forest. But we think we need to protect ourselves. They're fairly dangerous. I've also heard of a mighty dragon known as Horntail, somewhere outside of town. His power is said to be unrivaled...");
        npc.sayNext("At any rate, I heard that those merchants who sell order sheets in the other town sells #b#t4031348##k, which can help the owner of the sheet. We might as well protect our town and Minar forest with the order sheet.");
        npc.askAccept("Can you get me the order sheet for our town?\r\n Then I'll give you what you're looking for.");
        npc.startQuest(6925);
    }
    if (player.getJob() > 400 && player.getJob() < 500 && player.isQuestActive(6934)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        npc.sayNext("You need #b#t4031517##k and #b#t4031518##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        npc.sayNext("This forest and the dragons that inhabit it protect herrings, which protect the eggs of the dragons who protet the forest. But we think we need to protect ourselves. They're fairly dangerous. I've also heard of a mighty dragon known as Horntail, somewhere outside of town. His power is said to be unrivaled...");
        npc.sayNext("At any rate, I heard that those merchants who sell order sheets in the other town sells #b#t4031348##k, which can help the owner of the sheet. We might as well protect our town and Minar forest with the order sheet.");
        npc.askAccept("Can you get me the order sheet for our town?\r\n Then I'll give you what you're looking for.");
        npc.startQuest(6935);
    }
    if (player.getJob() > 500 && player.getJob() < 600 && player.isQuestActive(6944)) {
        npc.sayNext("I heard about you from the 4th Job Instructor in the Forest of the Priest. You want the 4th job advancement right?");
        npc.sayNext("You need #b#tt4031511##k and #b#t4031512##k. If you do one thing for our town, I'll give you what you want. Can you prove you're a hero?");
        npc.sayNext("This forest and the dragons that inhabit it protect herrings, which protect the eggs of the dragons who protet the forest. But we think we need to protect ourselves. They're fairly dangerous. I've also heard of a mighty dragon known as Horntail, somewhere outside of town. His power is said to be unrivaled...");
        npc.sayNext("At any rate, I heard that those merchants who sell order sheets in the other town sells #b#t4031348##k, which can help the owner of the sheet. We might as well protect our town and Minar forest with the order sheet.");
        npc.askAccept("Can you get me the order sheet for our town?\r\n Then I'll give you what you're looking for.");
        npc.startQuest(6945);
    }
} else if ((player.isQuestActive(6905) || player.isQuestActive(6915) || player.isQuestActive(6925) || player.isQuestActive(6935) || player.isQuestActive(6945))) {
    if (player.hasItem(4031348, 1)) {
        npc.say("You finally got the #b#t4031348##k!");
    }
} else {
    npc.say("monkas");
}