/**
 * First Eos Rock (NPC 2040024)
 * Ludibrium: Eos Tower 100th Floor (Map 221024400)
 *
 * Teleports floor 100 to 71 at Eos Tower.
 *
 * @author joemama
 */

if (player.hasItem(4001020, 1)) {
	let selection = npc.askYesNo("You can use #bEos Rock Scroll#k to activate #bFirst Eos Rock#k. Will you teleport to #bSecond Eos Rock#k at the 71st floor?");
	if (selection == 0) {
		npc.sayNext("Come back if you're tired of walking.");
	} else if (selection == 1) {
		cm.gainItem(4001020, -1);
		npc.makeEvent([player, 221024400]);
	}
} else {
	npc.sayNext("There's a rock that will enable you to teleport to #bSecond Eos Rock#k, but it cannot be activated without the scroll.");
}