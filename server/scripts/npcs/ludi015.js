/**
 * Second Eos Rock (NPC 2040027)
 * Ludibrium: Eos Tower 71st Floor (Map 221022900)
 *
 * Teleports floor 71 to either 100 or 41 at Eos Tower.
 *
 * @author joemama
 */

let floors = [221024400, 221021700]

if (player.hasItem(4001020, 1)) {
	let selection = npc.askMenu("You can use #bEos Rock Scroll#k to activate #bSecond Eos Rock#k. Which of these rocks would you like to teleport to?"
		+ "#L0# First Eos Rock - 100th floor#l\r\n"
		+ "#L1# Third Eos Rock - 41st floor#l");
	if (selection == 0) {
		selection = npc.askYesNo("You can use #bEos Rock Scroll#k to activate #bSecond Eos Rock#k. Will you teleport to #bFirst Eos Rock#k at the 100th Floor?");
		player.loseItem(4001020, 1);
		player.changeMap(221024400);
	} else if (selection == 1) {
		selection = npc.askYesNo("You can use #bEos Rock Scroll#k to activate #bSecond Eos Rock#k. Will you teleport to #bThird Eos Rock#k at the 41st Floor?");
		player.loseItem(4001020, 1);
		player.changeMap(221021700);
	} else {
		npc.sayNext("Come back if you're tired of walking.");
	}
} else {
	npc.sayNext("There's a rock that will enable you to teleport to either the #bFirst Eos Rock#k or #bThird Eos Rock#k, but it cannot be activated without the scroll.");
}