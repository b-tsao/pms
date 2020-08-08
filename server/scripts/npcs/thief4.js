/**
 * Hellin: 4th Job - Thief Instructor (NPC 2081400)
 * Leafre: Forest of the Priest (Map 240010501)
 *
 * Thief forth job advancement NPC.
 *
 * @author joemama
 */

function advanceJob() {
	if (player.getJob() == 411)	
		npc.sayNext("You have officially been anointed as a #bNightlord#k from this point forward.");
	else if (player.getJob() == 421) 
		npc.sayNext("You have officially been anointed as a #bShadower#k from this point forward.");
	player.setJob(player.getJob() + 1);
	player.gainSp(1);
	player.gainAp(5);
	player.gainEquipInventorySlots(4);
	player.gainUseInventorySlots(4);
	npc.sayNext("I've expanded your both your equipment and use slots as well as given you some SP and AP to get you started.");
	npc.sayNext("You started your journey as a simple adventurer... but you have grown so much since then. You possesss great strength, willpower and courage.");
	npc.sayNext("If one who possesses all of these qualities cannot be called a legend, then who can?");
	npc.sayNext("A legend is not born, but is created through struggle. Accept your destiny, and lead Maple World to a brighter future.");
}

if (player.getJob() == 412 || player.getJob() == 422) {
	npc.sayNext("How are your journeys as a master thief? Please use your strength for the good of the Maple World. Not everyone is as capable and fortunate as you.");
} else if ((player.getJob() == 411 || player.getJob() == 421) && player.getLevel() >= 120 && player.isQuestCompleted(6934)) {
	let selection = npc.askYesNo("You must have trained your whole life for this. Are you prepared for your 4th job advancement?");
	if (selection == 1) {
		if (player.getJob() != 411 && player.getJob() != 421 || player.getLevel() < 120 || !player.isQuestCompleted(6934)) {
			npc.logSuspicious("Tried to select NPC option that was not given");
		}
		advanceJob();
	} else {
		npc.sayNext("Take your time. This is not something you should take lightly. Come talk to me once you're ready for your destiny.");
	}
} else {
	npc.say("Hmm... It seems like there is nothing I can help you with.... for now.");
}
