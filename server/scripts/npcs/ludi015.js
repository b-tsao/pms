/**
 * Second Eos Rock (NPC 2040027)
 * Ludibrium: Eos Tower 71st Floor (Map 221022900)
 *
 * Teleports floor 71 to either 100 or 41 at Eos Tower.
 *
 * @author joemama
 */

if (player.hasItem(4001020, 1)) {
	let selection = npc.askMenu("You can use #bEos Rock Scroll#k to activate #bSecond Eos Rock#k. Which of these rocks would you like to teleport to?\r\n"
		+ "#L0# First Eos Rock - 100th floor#l\r\n"
		+ "#L1# Third Eos Rock - 41st floor#l");
	let prompt;
	let map;
	if (selection == 0) {
		prompt = "You can use #bEos Rock Scroll#k to activate #bSecond Eos Rock#k. Will you teleport to #bFirst Eos Rock#k at the 100th Floor?";
		map = 221024400;
	} else if (selection == 1) {
		prompt = "You can use #bEos Rock Scroll#k to activate #bSecond Eos Rock#k. Will you teleport to #bThird Eos Rock#k at the 41st Floor?";
		map = 221021700
	}
	selection = npc.askYesNo(prompt);
	if (selection == 1) {
		player.loseItem(4001020, 1);
		player.changeMap(map);
	} else {
		npc.sayNext("Come back if you're tired of walking.");
	}
} else {
	npc.sayNext("There's a rock that will enable you to teleport to either the #bFirst Eos Rock#k or #bThird Eos Rock#k, but it cannot be activated without the scroll.");
}