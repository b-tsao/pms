/**
 * Third Eos Rock (NPC 2040027)
 * Ludibrium: Eos Tower 41st Floor (Map 221021700)
 *
 * Teleports floor 41 to either 71 or 1 at Eos Tower.
 *
 * @author joemama
 */

if (player.hasItem(4001020, 1)) {
	let selection = npc.askMenu("You can use #b#t4001020##k to activate #Third Eos Rock#k. Which of these rocks would you like to teleport to?\r\n"
		+ "#L0# #bFirst Eos Rock - 100th floor#l\r\n"
		+ "#L1# #bThird Eos Rock - 41st floor#l");
	let prompt;
	let map;
	if (selection == 0) {
		prompt = "You can use #b#t4001020##k to activate #Third Eos Rock#k. Will you teleport to #Second Eos Rock#k at the 71st Floor?";
		map = 221022900;
	} else if (selection == 1) {
		prompt = "You can use #b#t4001020##k to activate #Third Eos Rock#k. Will you teleport to #Fourth Eos Rock#k at the 1st Floor?";
		map = 221020000;
	}
	selection = npc.askYesNo(prompt);
	if (selection == 1) {
		player.loseItem(4001020, 1);
		player.changeMap(map);
	} else {
		npc.sayNext("Come back if you're tired of walking.");
	}
} else {
	npc.sayNext("There's a rock that will enable you to teleport to either the #bSecond Eos Rock#k or #bFourth Eos Rock#k, but it cannot be activated without the scroll.");
}