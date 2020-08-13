/**
 * Irene: Ticketing Usher (NPC 9270041)
 * Victoria Road: Kerning City (Map 103000000)
 *
 * Sells tickets to Singapore and teleports player from Kerning City to the
 * waiting room on the airplane to Singapore.
 *
 * @author joemama
 */

let COST = 20000;

function admit(str, map) {
	let selection = npc.askYesNo(str);
	if (selection == 0) {
		npc.say("Please confirm the departure time you wish to leave. Thank you.");
	} else if (selection == 1) {
		if (!player.hasItem(4031731, 1)) {
			npc.sayNext("Please do purchase the ticket first. Thank you~");
		} else {
			player.loseItem(4031731, 1);
			player.changeMap(map);
		}
	}
}

let selection = npc.askMenu("Hello there~ I am #p" + npc.getNpcId() + "# from Singapore Airport. I was transferred to #m103000000# to celebrate new opening of our service! How can I help you?\r\n#b"
		+ "#L0#I would like to buy a plane ticket to Singapore#l\r\n"
		+ "#L1#Please take me to Singapore.#l");
switch (selection) {
	case 0:
		selection = npc.askYesNo("The ticket will cost you " + COST + " mesos. Will you purchase the ticket?");
		if (selection == 0) {
			npc.sayNext("I am here for a long time. Please talk to me again when you change your mind.");
		} else if (selection == 1) {
			if (!player.canGainItem(4031731, 1) || !player.hasMesos(COST)) {
				npc.say("I don't think you have enough meso or empty slot in your ETC inventory. Please check and talk to me again.");
			} else {
				player.loseMesos(COST);
				player.gainItem(4031731, 1);
			}
		}
		break;
	case 1:
		if (!player.hasItem(4031731)) {
			npc.say("You don't have a ticket to Singapore. Please purchase one to take the plane.")
		} else {
			npc.say("Have a nice flight!")
			player.loseItem(4031731, 1);
			player.changeMap(540010000);
		}

		/*//TODO: write event script
		let ap = cm.getEventManager("AirPlane");
		if (ap != null)
			if (ap.getProperty("entry").equals("true"))
				admit("The plane has arrived as is getting ready for takeoff. Please enter quickly, or you may miss the flight. Would you like to go in now? You will lose your ticket once you go in~ Thank you for choosing Wizet Airline.", 540010100);
			else
				npc.say("The plane has not yet arrived. We have scheduled departures every 5 minutes, and we close our doors 1 minute before departure. Please check again soon.");
		else
			admit("It appears as though the plane to Singapore is having some difficulties. Would you like to be immediately warped to Singapore instead? You will lose your ticket if you accept.", 540010000);
		break;*/
}