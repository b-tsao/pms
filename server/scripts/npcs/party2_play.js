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
 * Red Balloon (NPC 2040036)
 *   Hidden Street: Abandoned Tower <Stage 1> (Map 922010100),
 *   Hidden Street: Abandoned Tower <Stage 2> (Map 922010200),
 *   Hidden Street: Abandoned Tower <Stage 3> (Map 922010300),
 *   Hidden Street: Abandoned Tower <Stage 4> (Map 922010400),
 *   Hidden Street: Abandoned Tower <Stage 5> (Map 922010500),
 *   Hidden Street: Abandoned Tower <Stage 6> (Map 922010600),
 *   Hidden Street: Abandoned Tower <Stage 7> (Map 922010700),
 *   Hidden Street: Abandoned Tower <Stage 8> (Map 922010800),
 *   Hidden Street: Abandoned Tower <Determine to adventure> (Map 922011100)
 *
 * Lets the party continue on through each stage in the Ludibrium party
 * quest.
 *
 * @author Neuro
 */

let stage = (map.getId() - 922010000) / 100;
let event = npc.getEvent("party2");

//TODO: GMS-like conversation
function clear(stage, exp) {
	event.setVariable(stage + "stageclear", true);
	map.screenEffect("quest/party/clear");
	map.soundEffect("Party1/Clear");
	map.portalEffect("gate");
	let members = event.getVariable("members");
	for (let i = 0; i < members.length; i++)
		if (members[i].getHp() > 0)
			members[i].gainExp(exp);
}

function failStage() {
	map.screenEffect("quest/party/wrong_kor");
	map.soundEffect("Party1/Failed");
}

function passesStages(stage) {
    let leaderPreamble, leaderDialog, memberDialog, stageClearDialog, stageClearedDialog;
    let itemId, required;
    let reward;
    switch (stage) {
        case 1:
            leaderPreamble = "Hello, and welcome to the first stage of Ludibrium PQ. There are #r25#k monsters in here, kill them to retrieve the passes, and give them to me, and I will open the portal.";
            leaderDialog = "I'm sorry, but you do not have all 25 passes needed to clear this stage.";
            memberDialog = "Hello, and welcome to the first stage of Ludibrium PQ. There are #r25#k monsters in here, kill them to retrieve the passes, and give them to me, and I will open the portal.";
            stageClearDialog = "Congratulations on clearing the first stage! I will open the portal now.";
            stageClearedDialog = "Please proceed in the Party Quest, the portal opened!";
            itemId = 4001022;
            required = 25;
            reward = 2100;
            break;
        case 2:
            leaderPreamble = "Hello, and welcome to the second stage of Ludibrium PQ. There are #r15#k boxes in here, break them to retrieve the passes, and give them to me, and I will open the portal. But be careful, one of them will lead you into a trap!";
            leaderDialog = "I'm sorry, but you do not have all 15 passes needed to clear this stage.";
            memberDialog = "Hello, and welcome to the second stage of Ludibrium PQ. There are #r15#k boxes in here, break them to retrieve the passes, and give them to me, and I will open the portal. But be careful, one of them will lead you into a trap!";
            stageClearDialog = "Congratulations on clearing the second stage! I will open the portal now.";
            stageClearedDialog = "Please proceed in the Party Quest, the portal opened!";
            itemId = 4001022;
            required = 15;
            reward = 2520;
            break;
        case 3:
            leaderPreamble = "Hello, and welcome to the third stage of Ludibrium PQ. There are #r32#k #bBlocktopus#k in here, kill them to retrieve the passes, and give them to me, and I will open the portal.";
            leaderDialog = "I'm sorry, but you do not have all 32 passes needed to clear this stage.";
            memberDialog = "Hello, and welcome to the third stage of Ludibrium PQ. There are #r32#k #bBlocktopus#k in here, kill them to retrieve the passes, and give them to me, and I will open the portal.";
            stageClearDialog = "Congratulations on clearing the third stage! I will open the portal now.";
            stageClearedDialog = "Please proceed in the Party Quest, the portal opened!";
            itemId = 4001022;
            required = 32;
            reward = 2940;
            break;
        case 4:
            leaderPreamble = "Hello, and welcome to the fourth stage of Ludibrium PQ. There are #r8#k #bPasses#k to be collected in here, go through the portals and you'll see some Shadow Eyes. Kill them to retrieve the passes, and give them to me, and I will open the portal.";
            leaderDialog = "I'm sorry, but you do not have all 8 passes needed to clear this stage.";
            memberDialog = "Hello, and welcome to the fourth stage of Ludibrium PQ. There are #r8#k #bPasses#k to be collected in here, go through the portals and you'll see some Shadow Eyes. Kill them to retrieve the passes, and give them to me, and I will open the portal.";
            stageClearDialog = "Congratulations on clearing the fourth stage! I will open the portal now.";
            stageClearedDialog = "Please proceed in the Party Quest, the portal opened!";
            itemId = 4001022;
            required = 8;
            reward = 3360;
            break;
        case 5:
            leaderPreamble = "Hello, and welcome to the fifth stage of Ludibrium PQ. There are #r24#k #bPasses#k to be collected in here, go through the portals and you'll see some Boxes. Break them to retrieve the passes, and give them to me, and I will open the portal.";
            leaderDialog = "I'm sorry, but you do not have all 24 passes needed to clear this stage.";
            memberDialog = "Hello, and welcome to the fifth stage of Ludibrium PQ. There are #r24#k #bPasses#k to be collected in here, go through the portals and you'll see some Boxes. Break them to retrieve the passes, and give them to me, and I will open the portal.";
            stageClearDialog = "Congratulations on clearing the fifth stage! I will open the portal now.";
            stageClearedDialog = "Please proceed in the Party Quest, the portal opened!";
            itemId = 4001022;
            required = 24;
            reward = 3770;
            break;
        case 7:
            leaderPreamble = "Hello, and welcome to the seventh stage of Ludibrium PQ. There are #r3#k #bPasses#k to be collected in here. You need a Bowman or Thief with max Keen Eyes or Eye of the Amazon. Kill the monsters up there, and a Rombot will spawn somewhere in the map. Kill the Rombot to retrieve the pass. Collect for me 3 Pass, and I will open the portal.";
            leaderDialog = "I'm sorry, but you do not have all 3 passes needed to clear this stage.";
            memberDialog = "Hello, and welcome to the seventh stage of Ludibrium PQ. There are #r3#k #bPasses#k to be collected in here. You need a Bowman or Thief with max Keen Eyes or Eye of the Amazon. Kill the monsters up there, and a Rombot will spawn somewhere in the map. Kill the Rombot to retrieve the pass. Collect for me 3 Pass, and I will open the portal.";
            stageClearDialog = "Congratulations on clearing the seventh stage! I will open the portal now.";
            stageClearedDialog = "Please proceed in the Party Quest, the portal opened!";
            itemId = 4001022;
            required = 3;
            reward = 4620;
            break;
    }

    if (player.getId() == party.getLeader()) {
        let lVar = "leader" + stage + "preamble";
        let preamble = event.getVariable(lVar);
        if (preamble == null || !preamble) {
            event.setVariable(lVar, true);
            npc.sayNext(leaderPreamble);
        } else {
            let complete = event.getVariable(stage + "stageclear");
            if (complete != null && complete) {
                npc.sayNext(stageClearedDialog);
            } else {
                 // Check how many passes they have compared to number of party members
                if (!player.hasItem(itemId, required)) {
                    npc.sayNext(leaderDialog);
                } else {
                    npc.sayNext(stageClearDialog);
                    player.loseItem(itemId);
                    clear(stage, reward);
                }
            }
        }
    } else {
        let pVar = "member" + stage + "preamble" + player.getId();
        let preamble = event.getVariable(pVar);
        if (preamble == null || !preamble) {
            npc.sayNext(memberDialog);
            event.setVariable(pVar, true);
        } else {
            let complete = event.getVariable(stage + "stageclear");
            if (complete != null && complete) {
                npc.sayNext(stageClearedDialog);
            } else {
                npc.sayNext(memberDialog);
            }
        }
    }
}

function rectangleStages(stage) {
	let debug = false; //see which positions are occupied
	let stages = ["2nd", "3rd", "4th"];
	let objs = ["ropes", "platforms", "barrels"];
	let verbs = ["hang", "stand", "stand"];
	let donts = ["hang on the ropes too low", "stand too close to the edges", "stand too close to the edges"];
	let combos = [
		[ //stage 2
			[0, 1, 1, 1],
			[1, 0, 1, 1],
			[1, 1, 0, 1],
			[1, 1, 1, 0]
		], 
		[ //stage 3
			[0, 0, 1, 1, 1],
			[0, 1, 0, 1, 1],
			[0, 1, 1, 0, 1],
			[0, 1, 1, 1, 0],
			[1, 0, 0, 1, 1],
			[1, 0, 1, 0, 1],
			[1, 0, 1, 1, 0],
			[1, 1, 0, 0, 1],
			[1, 1, 0, 1, 0],
			[1, 1, 1, 0, 0]
		],
		[ //stage 4
			[0, 0, 0, 1, 1, 1],
			[0, 0, 1, 0, 1, 1],
			[0, 0, 1, 1, 0, 1],
			[0, 0, 1, 1, 1, 0],
			[0, 1, 0, 0, 1, 1],
			[0, 1, 0, 1, 0, 1],
			[0, 1, 0, 1, 1, 0],
			[0, 1, 1, 0, 0, 1],
			[0, 1, 1, 0, 1, 0],
			[0, 1, 1, 1, 0, 0],
			[1, 0, 0, 0, 1, 1],
			[1, 0, 0, 1, 0, 1],
			[1, 0, 0, 1, 1, 0],
			[1, 0, 1, 0, 0, 1],
			[1, 0, 1, 0, 1, 0],
			[1, 0, 1, 1, 0, 0],
			[1, 1, 0, 0, 0, 1],
			[1, 1, 0, 0, 1, 0],
			[1, 1, 0, 1, 0, 0],
			[1, 1, 1, 0, 0, 0]
		]
	];
	let rects = [ //[x, y, width, height]
		[ //stage 2
			[-770, -132, 28, 178],
			[-733, -337, 26, 105],
			[-601, -328, 29, 105],
			[-495, -125, 24, 165]
		],
		[ //stage 3
			[608, -180, 140, 50],
			[791, -117, 140, 45],
			[958, -180, 140, 50],
			[876, -238, 140, 45],
			[702, -238, 140, 45]
		],
		[ //stage 4
			[910, -236, 35, 5],
			[877, -184, 35, 5],
			[946, -184, 35, 5],
			[845, -132, 35, 5],
			[910, -132, 35, 5],
			[981, -132, 35, 5]
		]
	];
	let objsets = [
		[0, 0, 0, 0],
		[0, 0, 0, 0, 0],
		[0, 0, 0, 0, 0, 0]
	];

	let index = stage - 2;

	if (player.getId() == party.getLeader()) {
		let preamble = event.getVariable("leader" + stages[index] + "preamble");
		if (preamble == null || !preamble) {
			npc.sayNext("Hi. Welcome to the " + stages[index] + " stage. Next to me, you'll see a number of " + objs[index] + ". Out of these " + objs[index] + ", #b3 are connected to the portal that sends you to the next stage#k. All you need to do is have #b3 party members find the correct " + objs[index] + " and " + verbs[index] + " on them.#k\r\nBUT, it doesn't count as an answer if you " + donts[index] + "; please be near the middle of the " + objs[index] + " to be counted as a correct answer. Also, only 3 members of your party are allowed on the " + objs[index] + ". Once they are " + verbs[index] + "ing on them, the leader of the party must #bdouble-click me to check and see if the answer's correct or not#k. Now, find the right " + objs[index] + " to " + verbs[index] + " on!");
			event.setVariable("leader" + stages[index] + "preamble", true);
			let sequenceNum = Math.floor(Math.random() * combos[index].length);
			event.setVariable("stage" + stages[index] + "combo", sequenceNum);
		} else {
			// Check for stage completed
			let complete = event.getVariable(stage + "stageclear");
			if (complete != null && complete) {
				npc.sayNext("You all have cleared the quest for this stage. Use the portal to move to the next stage...");
			} else { // Check for people on ropes and their positions
				let totplayers = 0;
				let members = event.getVariable("members");
				for (let i = 0; i < members.length; i++) {
					for (let j = 0; j < objsets[index].length; j++) {
						let rectangle = rects[index][j];
						if (members[i].getMapId() == map.getId() && members[i].inRectangle(rectangle[0], rectangle[1], rectangle[2], rectangle[3])) {
							objsets[index][j]++;
							totplayers++;
							break;
						}
					}
				}
				// Compare to correct positions
				// Don't even bother if there aren't three players.
				if (totplayers == 3 || debug) {
					let combo = combos[index][event.getVariable("stage" + stages[index] + "combo")];
					let testcombo = true;
					for (let i = 0; i < objsets[index].length && testcombo; i++)
						if (combo[i] != objsets[index][i])
							testcombo = false;
					if (debug) {
						let str = "Objects contain:"
						for (let i = 0; i < objsets[index].length; i++)
							str += "\r\n" + (i + 1) + ". " + objsets[index][i];
						str += "\r\nCorrect combination: ";
						for (let i = 0; i < combo.length; i++)
							str += "\r\n" + (i + 1) + ". " + combo[i];
						if (testcombo) {
							str += "\r\nResult: #gClear#k";
							npc.say(str);
						} else {
							str += "\r\nResult: #rWrong#k";
							str += "\r\n#bForce clear stage?#k";
							debug = npc.askYesNo(str);
						}
					}
					if (testcombo || debug) {
						clear(stage, Math.pow(2, stage) * 50);
					} else {
						failStage();
					}
				} else {
					npc.sayNext("It looks like you haven't found the 3 " + objs[index] + " just yet. Please think of a different combination of " + objs[index] + ". Only 3 are allowed to " + verbs[index] + " on " + objs[index] + ", and if you " + donts[index] + " it may not count as an answer, so please keep that in mind. Keep going!");
				}
			}
		}
	} else {
		let complete = event.getVariable(stage + "stageclear");
		if (complete != null && complete)
			npc.sayNext("You all have cleared the quest for this stage. Use the portal to move to the next stage...");
		else
			npc.sayNext("Please have the party leader talk to me.");
	}
}

function getPrize() {
	let scrolls = [
		2040502, 1, 2040505, 1,				// Overall DEX and DEF
		2040802, 1,							// Gloves for DEX 
		2040002, 1, 2040402, 1, 2040602, 1	// Helmet, Topwear and Bottomwear for DEF
	];
	let potions = [
		2000001, 80, 2000002, 80, 2000003, 80,	// Orange, White, Blue Potions
		2000006, 50, 2000004, 5,				// Mana Elixir, Elixir
		2022000, 15, 2022003, 15				// Pure Water, Unagi
	];
	let equips = [
		1032004, 1, 1032005, 1, 1032009, 1,	// Level 20-25 Earrings
		1032006, 1, 1032007, 1, 1032010, 1,	// Level 30 Earrings
		1032002, 1,							// Level 35 Earring
		1002026, 1, 1002089, 1, 1002090, 1	// Bamboo Hats
	];
	let etc = [
		4010000, 15, 4010001, 15, 4010002, 15, 4010003, 15,	// Mineral Ores
		4010004, 8, 4010005, 8, 4010006, 8,					// Mineral Ores
		4020000, 8, 4020001, 8, 4020002, 8, 4020003, 8,		// Jewel Ores
		4020004, 8, 4020005, 8, 4020006, 8,					// Jewel Ores
		4020007, 3, 4020008, 3, 4003000, 30					// Diamond and Black Crystal Ores and Screws
	];

	let rewards;
	let itemSetSel = Math.random();
	if (itemSetSel < 0.3) //30% chance
		rewards = scrolls;
	else if (itemSetSel < 0.6) //30% chance
		rewards = equips;
	else if (itemSetSel < 0.9) //30% chance
		rewards = potions;
	else //10% chance
		rewards = etc;

	let index = Math.floor(Math.random() * (rewards.length / 2)) * 2;
	if (player.gainItem(rewards[index], rewards[index + 1]))
		player.changeMap(103000805, "sp");
	else //TODO: GMS-like line
		npc.say("Please check whether your inventory is full.");
}

switch (stage) {
	case 1:
	case 2:
	case 3:
	case 4:
	case 5:
	case 7:
		passesStages(stage);
		break;
	case 6: // 133221333123111
        npc.sayNext("Hello and welcome to the sixth stage of Ludibrium Party Quest. Look here, and you'll see a number of boxes. All you have to do, is find the right combination, and press up on it to teleport up. But, if you get it wrong, you will be teleported back down to the bottom. Good Luck!");
        break;
    case 8:
		let areas = map.getAreas();
		let objset = [0,0,0,0,0,0,0,0,0];
		let players = party.getLocalMembers(map.getId());
		let totPlayers = 0;
		for (let i = 0; i < objset.length; i++) {
			for (let j = 0; j < players.length; j++) {
				if (areas.get(i.toString()).contains(players[j].getPosition())) {
					objset[i]++;
					totPlayers++;
				}
			}
		}
		npc.sayNext(objset);
		// rectangleStages(stage);
		break;
		let complete = event.getVariable(stage + "stageclear");
		if (complete == null || !complete) {
			if (player.getId() == party.getLeader()) {
				if (player.hasItem(4001008, 10)) {
					player.loseItem(4001008, 10);
					clear(stage, 1500);
					npc.sayNext("Here's the portal that leads you to the last, bonus stage. It's a stage that allows you to defeat regular monsters a little easier. You'll be given a set amount of time to hunt as much as possible, but you can always leave the stage in the middle of it through the NPC. Again, congratulations on clearing all the stages. Take care...");
				} else {
					npc.sayNext("Hello. Welcome to the 5th and final stage. Walk around the map and you'll be able to find some Boss monsters. Defeat all of them, gather up #bthe passes#k, and please get them to me. Once you earn your pass, the leader of your party will collect them, and then get them to me once the #bpasses#k are gathered up. The monsters may be familiar to you, but they may be much stronger than you think, so please be careful. Good luck!\r\nAs a result of complaints, it is now mandatory to kill all the Slimes! Do it!");
				}
			} else {
				npc.sayNext("Welcome to the 5th and final stage.  Walk around the map and you will be able to find some Boss monsters.  Defeat them all, gather up the #bpasses#k, and give them to your leader.  Once you are done, return to me to collect your reward.");
			}
		} else {
			npc.sayNext("Incredible! You cleared all the stages to get to this point. Here's a small prize for your job well done. Before you accept it, however, please make sure your use and etc. inventories have empty slots available.\r\n#bYou will not receive a prize if you have no free slots!#k");
			getPrize();
		}
		break;
}