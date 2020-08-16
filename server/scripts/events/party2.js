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
 * Logic for starting and exiting Ludibrium party quest (AKA party2) using
 * timers and party member triggers.
 *
 * @author Neuro
 */

let EXIT_MAP = 922010000;
let BONUS_MAP = 922011000;
let REWARD_MAP = 922011100;

let map;
let party;
let members;
let endTime;

function init(attachment) {
    //create a new instance of the maps so we don't have to deal with clearing
	//the map and respawning the mobs and boxes in the right position
    map = event.makeMap(922011000); // bonus
    event.setVariable("party2stage10", map);
    map = event.makeMap(922010900); // boss
    event.setVariable("party2stage9", map);
	for (let stage = 8; stage > 0 ; stage--) {
        map = event.makeMap(922010000 + 100 * stage);
        map.overridePortal("next00", "party2");
		event.setVariable("party2stage" + stage, map);
		
		if (stage == 2) {
			// Trap
			let subMap = event.makeMap(922010201);
			subMap.overridePortal("out00", "party2sub");
			event.setVariable("party2trap", subMap);
		} else if (stage == 4) {
			// Darkness portals
			for (let i = 1; i <= 5; i++) {
				let subMap = event.makeMap(922010400 + i);
				subMap.overridePortal("out00", "party2sub");
				map.overridePortal("in0" + i, "party2sub");
				event.setVariable("party2darkness" + i, subMap);
			}
		} else if (stage == 5) {
			// Boring portals
			for (let i = 1; i <= 6; i++) {
				let subMap = event.makeMap(922010500 + i);
				subMap.overridePortal("out00", "party2sub");
				map.overridePortal("in0" + i, "party2sub");
				event.setVariable("party2portal" + i, subMap);
			}
		}
	}
	
	event.setVariable("6stageclear", true);

    party = attachment;
	party.loseItem(4001022);
	party.changeMap(map, "st00");
	members = party.getLocalMembers();

	map.showTimer(60 * 60);
	event.startTimer("kick", 60 * 60 * 1000);
	endTime = new Date().getTime() + 60 * 60 * 1000;

	event.setVariable("members", members);

	for (let i = 0; i < members.length; i++)
		members[i].setEvent(event);
}

function removePlayer(playerId, changeMap) {
	if (party.getLeader() == playerId) {
		//boot the entire party (changeMap parameter only applies to member
		//whose player ID matches playerId parameter, so those who are not the
		//leader are always booted)
		for (let i = 0; i < members.length; i++) {
			//dissociate event before changing map so playerChangedMap is not
			//called and this method is not called again by the player
			members[i].setEvent(null);
			if (changeMap || members[i].getId() != playerId)
				members[i].changeMap(EXIT_MAP, "st00");
		}
		event.destroyEvent();
	} else {
		for (let i = 0; i < members.length; i++) {
			if (members[i].getId() == playerId) {
				//dissociate event before changing map so playerChangedMap is
				//not called and this method is not called again by the player
				members[i].setEvent(null);
				if (changeMap)
					members[i].changeMap(EXIT_MAP, "st00");
				//collapse the members array so we don't accidentally warp
				//this member again if the leader leaves later.
				members.splice(i, 1);
				break;
			}
		}
	}
}

function playerDisconnected(player) {
	//changeMap is false since all PQ maps force return the player to the exit
	//map on his next login anyway, and we don't want to deal with sending
	//change map packets to a disconnected client
	removePlayer(player.getId(), false);
}

function playerChangedMap(player, destination) {
	if (destination.getId() == REWARD_MAP) {
		player.setEvent(null);
	} else if (destination.getId() == BONUS_MAP) {
		if (party.getLeader() == player.getId()) {
			event.stopTimer("kick");
			event.startTime("clear", 60 * 1000);
			destination.showTimer(60);
		}
	} else if (destination.getId() == EXIT_MAP)
		//player died and respawned or clicked Sgt. Anderson to leave PQ
		//changeMap is false so player doesn't get re-warped to exit map
		removePlayer(player.getId(), false);
	else
		player.showTimer((endTime - new Date().getTime()) / 1000);
}

function partyMemberDischarged(party, player) {
	removePlayer(player.getId(), true);
}

function timerExpired(key) {
	switch (key) {
		case "kick":
			removePlayer(party.getLeader(), true);
			break;
		case "clear":
			let maps = [
				922010100,
				922010200, 922010201,
				922010300,
				922010400, 922010401, 922010402, 922010403, 922010404, 922010405,
				922010500, 922010501, 922010502, 922010503, 922010504, 922010505, 922010506,
				922010600,
				922010700,
				922010800,
				922010900,
				BONUS_MAP
			];
			for (let i = 0; i < members.length; i++) {
				if (maps.includes(members[i].getMapId())) {
					members[i].changeMap(REWARD_MAP, "st00");
				}
			}
			event.destroyEvent();
			break;
	}
}

function deinit() {
	for (let i = 0; i < members.length; i++)
		members[i].setEvent(null);

    for (let stage = 1; stage <= 10; stage++)
		event.destroyMap(event.getVariable("party2stage" + stage));
	event.destroyMap(event.getVariable("party2trap"));
	for (let i = 1; i <= 5; i++)
		event.destroyMap(event.getVariable("party2darkness" + i));
	for (let i = 1; i <= 6; i++)
		event.destroyMap(event.getVariable("party2portal" + i));
}