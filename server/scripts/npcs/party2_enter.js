/*
 * ArgonMS MapleStory server emulator written in Java
 * Copyright (C) 2011-2013  GoldenKevin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Red Sign (NPC 2040034)
 * Ludibrium: Eos Tower 101st Floor (Map 221024500)
 *
 * Admits players into Ludibrium party quest.
 *
 * @author Neuro
 */

// if (party == null) {
// 	npc.say("From here on above, this place is full of dangerous objects and monsters, so I can't let you make your way up anymore. If you're interested in saving us and bring peace back into Ludibrium, however, that's a different story. If you want to defeat a powerful creature residing at the very top, then please gather up your party members. It won't be easy. but ... I think you can do it.");
// } else if (player.getId() != party.getLeader()) {
// 	npc.say("If you want to try the quest, please tell the #bleader of your party#k to talk to me.");
// } else if (party.numberOfMembersInChannel() != 4 || party.getMembersCount(map.getId(), 1, 200) != 4) {
// 	npc.say("Your party does not consist of four, therefore making you ineligible to participate in this party quest. Please come back when you have four party members.")
// } else if (party.getMembersCount(map.getId(), 35, 50) != 4) {
// 	npc.say("Please check that all your party members are between the levels of 35 ~ 50.");
// } else if (npc.makeEvent("party2", true, party) == null) {
// 	npc.say("Some other party has already gotten in to try clearing the quest. Please try again later.");
// }

for (let i = -10; i < 30; i++) {
	map.testMessage(i, "test_message " + i);
}