/**
 * Third Eos Rock (NPC 2040027)
 * Ludibrium: Eos Tower 41st Floor (Map 221021700)
 *
 * Teleports floor 41 to either 71 or 1 at Eos Tower.
 *
 * @author joemama
 */

if (player.hasItem(4001020) {
	let selection = npc.askMenu("You can use #bEos Rock Scroll#k to activate #bThird Eos Rock#k. Which of these rocks would you like to teleport to?"
		+ "#L0# First Eos Rock - 41st floor#l\r\n"
		+ "#L1# Third Eos Rock - 1st floor#l");
	if (selection == 0) {
		selection = npc.askYesNo("You can use #bEos Rock Scroll#k to activate #bThird Eos Rock#k. Will you teleport to #bSecond Eos Rock#k at the 71st Floor?"");
		cm.gainItem(4001020, -1);
		npc.makeEvent([player, 221022900]);
	} else if (selection == 1) {
		selection = npc.askYesNo("You can use #bEos Rock Scroll#k to activate #bThird Eos Rock#k. Will you teleport to #bFourth Eos Rock#k at the 1st Floor?");
		cm.gainItem(4001020, -1);
		npc.makeEvent([player, 221020000]);
	}
	if (selection == 1) {
		break;
	} else if (selection == 0) {
		npc.sayNext("Come back if you're tired of walking.");
	}
else {
	npc.sayNext("There's a rock that will enable you to teleport to either the #bSecond Eos Rock#k or #bFourth Eos Rock#k, but it cannot be activated without the scroll."")
}