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
 * Lakelis (NPC 9020000)
 * Victoria Road: Kerning City (Map 103000000)
 *
 * Admits players into Kerning City party quest.
 *
 * @author GoldenKevin
 */

if (party == null || player.getId() != party.getLeader()) {
	npc.say("How about you and your party members collectively beating a quest? Here you'll find obstacles and problems where you won't be able to beat it without great teamwork.  If you want to try it, please tell the #bleader of your party#k to talk to me.");
} else if (party.numberOfMembersInChannel() != 3 || party.getMembersCount(map.getId(), 1, 200) != 3) {
	npc.say("Your party is not a party of three. Please come back when you have three party members.");
} else if (party.getMembersCount(map.getId(), 21, 30) != 3) {
	npc.say("Someone in your your party does not have a level between 21 ~ 30. Please double-check.");
} else if (npc.makeEvent("party1", true, party) == null) {
	npc.say("Some other party has already gotten in to try clearing the quest. Please try again later.");
}