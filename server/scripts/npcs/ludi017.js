/**
 * Fourth Eos Rock (NPC 2040027)
 * Ludibrium: Eos Tower 1st Floor (Map 221020000)
 *
 * Teleports floor 1 to  at Eos Tower.
 *
 * @author joemama
 */

if (player.hasItem(4001020) {
	let selection = npc.askYesNo("You can use #bEos Rock Scroll#k to activate #bFourth Eos Rock#k. Will you teleport to #bThird Eos Rock#k at the 41st floor?");
	if (selection == 0) {
		npc.sayNext("Come back whenever you're tired of walking.");
	} else if (selection == 1) {
		cm.gainItem(4001020, -1);
		npc.makeEvent([player, 221021700]);
	}
else {
	npc.sayNext("There's a rock that will enable you to teleport to #bThird Eos Rock#k, but it cannot be activated without the scroll."")
}