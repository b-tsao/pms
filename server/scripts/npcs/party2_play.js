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

function bitArrayToNum(arr) {
    let val = 0;
    let i = 0;
    for (let i = arr.length - 1; i >= 0; i--) {
        if (arr[i] != 0) {
            val += Math.pow(2, arr.length - i - 1);
        }
    }
    return val;
}

function combination(arr) {
    let combinations = [];
    for (let i = 0; i < arr.length; i++) {
        let c = combinations.length;
        for (let j = 0; j < c; j++) {
            combinations.push([...combinations[j], arr[i]]);
        }
        combinations.push([arr[i]]);
    }
    return combinations;
}

function permutation(arr) {
    let permutations = [];
    backtrackPermutation(permutations, arr, 0);
    return permutations;
}
    
function backtrackPermutation(result, arr, index) {
    if (index == arr.length) {
        result.push([...arr]);
    }
    for (let i = index; i < arr.length; i++) {
        swap(arr, i, index);
        backtrackPermutation(result, arr, index + 1);
        swap(arr, i, index);
    }
}

function subPermutations(arr) {
    let sub = [];
    for (let i = 0; i < arr.length; i++) {
        sub.push(...permutation(arr[i]));
    }
    return sub;
}

function swap(arr, i, j) {
    let temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

function hint(sequence) {
	let ops = [['+', '-'], ['/']];
	let combo = combination(sequence);
	let subPerm = subPermutations(combo);
	let selected = subPerm[Math.floor(Math.random() * subPerm.length)];
	let num = parseInt(selected.join(''));
	let str = '';
	for (let i = 0; i < 2; i++) {
		let type = Math.floor(Math.random() * ops.length);
		let op = ops[type][Math.floor(Math.random() * ops[type].length)];
        let qual = Math.floor(Math.random() * 100) + 1;
		switch (op) {
			case '+':
				num -= qual;
				break;
			case '-':
				num += qual;
				break;
			case '/':
				num *= qual;
				break;
		}
		if (i == 0) {
            str = ` ${op} ${qual}${str}`;
		} else {
            if (num < 0) {
                str = `((${num}) ${op} ${qual})${str}`;
            } else {
                str = `(${num} ${op} ${qual})${str}`;
            }
		}
	}
	str += ' = ?';
	return str;
}

function numbersStage(stage) {
	let leaderPreamble = "leaderpreamble";
	let leaderDialog = "leaderdialog";
	let memberDialog = "memberdialog";
	let stageClearedDialog = "";
	let reward = 0;

	let combos = [
		[0, 0, 0, 0, 1, 1, 1, 1, 1],
		[0, 0, 0, 1, 0, 1, 1, 1, 1],
		[0, 0, 1, 0, 0, 1, 1, 1, 1],
		[0, 1, 0, 0, 0, 1, 1, 1, 1],
		[1, 0, 0, 0, 0, 1, 1, 1, 1],
		[0, 0, 0, 1, 1, 0, 1, 1, 1],
		[0, 0, 1, 0, 1, 0, 1, 1, 1],
		[0, 1, 0, 0, 1, 0, 1, 1, 1],
		[1, 0, 0, 0, 1, 0, 1, 1, 1],
		[0, 0, 1, 1, 0, 0, 1, 1, 1],
		[0, 1, 0, 1, 0, 0, 1, 1, 1],
		[1, 0, 0, 1, 0, 0, 1, 1, 1],
		[0, 1, 1, 0, 0, 0, 1, 1, 1],
		[1, 0, 1, 0, 0, 0, 1, 1, 1],
		[1, 1, 0, 0, 0, 0, 1, 1, 1],
		[0, 0, 0, 1, 1, 1, 0, 1, 1],
		[0, 0, 1, 0, 1, 1, 0, 1, 1],
		[0, 1, 0, 0, 1, 1, 0, 1, 1],
		[1, 0, 0, 0, 1, 1, 0, 1, 1],
		[0, 0, 1, 1, 0, 1, 0, 1, 1],
		[0, 1, 0, 1, 0, 1, 0, 1, 1],
		[1, 0, 0, 1, 0, 1, 0, 1, 1],
		[0, 1, 1, 0, 0, 1, 0, 1, 1],
		[1, 0, 1, 0, 0, 1, 0, 1, 1],
		[1, 1, 0, 0, 0, 1, 0, 1, 1],
		[0, 0, 1, 1, 1, 0, 0, 1, 1],
		[0, 1, 0, 1, 1, 0, 0, 1, 1],
		[1, 0, 0, 1, 1, 0, 0, 1, 1],
		[0, 1, 1, 0, 1, 0, 0, 1, 1],
		[1, 0, 1, 0, 1, 0, 0, 1, 1],
		[1, 1, 0, 0, 1, 0, 0, 1, 1],
		[0, 1, 1, 1, 0, 0, 0, 1, 1],
		[1, 0, 1, 1, 0, 0, 0, 1, 1],
		[1, 1, 0, 1, 0, 0, 0, 1, 1],
		[1, 1, 1, 0, 0, 0, 0, 1, 1],
		[0, 0, 0, 1, 1, 1, 1, 0, 1],
		[0, 0, 1, 0, 1, 1, 1, 0, 1],
		[0, 1, 0, 0, 1, 1, 1, 0, 1],
		[1, 0, 0, 0, 1, 1, 1, 0, 1],
		[0, 0, 1, 1, 0, 1, 1, 0, 1],
		[0, 1, 0, 1, 0, 1, 1, 0, 1],
		[1, 0, 0, 1, 0, 1, 1, 0, 1],
		[0, 1, 1, 0, 0, 1, 1, 0, 1],
		[1, 0, 1, 0, 0, 1, 1, 0, 1],
		[1, 1, 0, 0, 0, 1, 1, 0, 1],
		[0, 0, 1, 1, 1, 0, 1, 0, 1],
		[0, 1, 0, 1, 1, 0, 1, 0, 1],
		[1, 0, 0, 1, 1, 0, 1, 0, 1],
		[0, 1, 1, 0, 1, 0, 1, 0, 1],
		[1, 0, 1, 0, 1, 0, 1, 0, 1],
		[1, 1, 0, 0, 1, 0, 1, 0, 1],
		[0, 1, 1, 1, 0, 0, 1, 0, 1],
		[1, 0, 1, 1, 0, 0, 1, 0, 1],
		[1, 1, 0, 1, 0, 0, 1, 0, 1],
		[1, 1, 1, 0, 0, 0, 1, 0, 1],
		[0, 0, 1, 1, 1, 1, 0, 0, 1],
		[0, 1, 0, 1, 1, 1, 0, 0, 1],
		[1, 0, 0, 1, 1, 1, 0, 0, 1],
		[0, 1, 1, 0, 1, 1, 0, 0, 1],
		[1, 0, 1, 0, 1, 1, 0, 0, 1],
		[1, 1, 0, 0, 1, 1, 0, 0, 1],
		[0, 1, 1, 1, 0, 1, 0, 0, 1],
		[1, 0, 1, 1, 0, 1, 0, 0, 1],
		[1, 1, 0, 1, 0, 1, 0, 0, 1],
		[1, 1, 1, 0, 0, 1, 0, 0, 1],
		[0, 1, 1, 1, 1, 0, 0, 0, 1],
		[1, 0, 1, 1, 1, 0, 0, 0, 1],
		[1, 1, 0, 1, 1, 0, 0, 0, 1],
		[1, 1, 1, 0, 1, 0, 0, 0, 1],
		[1, 1, 1, 1, 0, 0, 0, 0, 1],
		[0, 0, 0, 1, 1, 1, 1, 1, 0],
		[0, 0, 1, 0, 1, 1, 1, 1, 0],
		[0, 1, 0, 0, 1, 1, 1, 1, 0],
		[1, 0, 0, 0, 1, 1, 1, 1, 0],
		[0, 0, 1, 1, 0, 1, 1, 1, 0],
		[0, 1, 0, 1, 0, 1, 1, 1, 0],
		[1, 0, 0, 1, 0, 1, 1, 1, 0],
		[0, 1, 1, 0, 0, 1, 1, 1, 0],
		[1, 0, 1, 0, 0, 1, 1, 1, 0],
		[1, 1, 0, 0, 0, 1, 1, 1, 0],
		[0, 0, 1, 1, 1, 0, 1, 1, 0],
		[0, 1, 0, 1, 1, 0, 1, 1, 0],
		[1, 0, 0, 1, 1, 0, 1, 1, 0],
		[0, 1, 1, 0, 1, 0, 1, 1, 0],
		[1, 0, 1, 0, 1, 0, 1, 1, 0],
		[1, 1, 0, 0, 1, 0, 1, 1, 0],
		[0, 1, 1, 1, 0, 0, 1, 1, 0],
		[1, 0, 1, 1, 0, 0, 1, 1, 0],
		[1, 1, 0, 1, 0, 0, 1, 1, 0],
		[1, 1, 1, 0, 0, 0, 1, 1, 0],
		[0, 0, 1, 1, 1, 1, 0, 1, 0],
		[0, 1, 0, 1, 1, 1, 0, 1, 0],
		[1, 0, 0, 1, 1, 1, 0, 1, 0],
		[0, 1, 1, 0, 1, 1, 0, 1, 0],
		[1, 0, 1, 0, 1, 1, 0, 1, 0],
		[1, 1, 0, 0, 1, 1, 0, 1, 0],
		[0, 1, 1, 1, 0, 1, 0, 1, 0],
		[1, 0, 1, 1, 0, 1, 0, 1, 0],
		[1, 1, 0, 1, 0, 1, 0, 1, 0],
		[1, 1, 1, 0, 0, 1, 0, 1, 0],
		[0, 1, 1, 1, 1, 0, 0, 1, 0],
		[1, 0, 1, 1, 1, 0, 0, 1, 0],
		[1, 1, 0, 1, 1, 0, 0, 1, 0],
		[1, 1, 1, 0, 1, 0, 0, 1, 0],
		[1, 1, 1, 1, 0, 0, 0, 1, 0],
		[0, 0, 1, 1, 1, 1, 1, 0, 0],
		[0, 1, 0, 1, 1, 1, 1, 0, 0],
		[1, 0, 0, 1, 1, 1, 1, 0, 0],
		[0, 1, 1, 0, 1, 1, 1, 0, 0],
		[1, 0, 1, 0, 1, 1, 1, 0, 0],
		[1, 1, 0, 0, 1, 1, 1, 0, 0],
		[0, 1, 1, 1, 0, 1, 1, 0, 0],
		[1, 0, 1, 1, 0, 1, 1, 0, 0],
		[1, 1, 0, 1, 0, 1, 1, 0, 0],
		[1, 1, 1, 0, 0, 1, 1, 0, 0],
		[0, 1, 1, 1, 1, 0, 1, 0, 0],
		[1, 0, 1, 1, 1, 0, 1, 0, 0],
		[1, 1, 0, 1, 1, 0, 1, 0, 0],
		[1, 1, 1, 0, 1, 0, 1, 0, 0],
		[1, 1, 1, 1, 0, 0, 1, 0, 0],
		[0, 1, 1, 1, 1, 1, 0, 0, 0],
		[1, 0, 1, 1, 1, 1, 0, 0, 0],
		[1, 1, 0, 1, 1, 1, 0, 0, 0],
		[1, 1, 1, 0, 1, 1, 0, 0, 0],
		[1, 1, 1, 1, 0, 1, 0, 0, 0],
		[1, 1, 1, 1, 1, 0, 0, 0, 0]
	];

	if (player.getId() == party.getLeader()) {
		let preamble = event.getVariable("leader" + stage + "preamble");
		if (preamble == null || !preamble) {
			npc.sayNext(leaderPreamble);
			event.setVariable("leader" + stage + "preamble", true);
			let sequenceNum = Math.floor(Math.random() * combos.length);
			event.setVariable("stage" + stage + "combo", sequenceNum);
		} else {
			// Check for stage completed
			let complete = event.getVariable(stage + "stageclear");
			if (complete != null && complete) {
				npc.sayNext(stageClearedDialog);
			} else { // Check for people and their positions
				let areas = map.getAreas();
				let objsets = [0,0,0,0,0,0,0,0,0];
				let players = party.getLocalMembers(map.getId());
				let totPlayers = 0;
				for (let i = 0; i < objsets.length; i++) {
					for (let j = 0; j < players.length; j++) {
						if (areas.get(i.toString()).contains(players[j].getPosition())) {
							objsets[i]++;
							totPlayers++;
						}
					}
				}
				// Compare to correct positions
				if (totPlayers == 5) {
					let combo = combos[event.getVariable("stage" + stage + "combo")];
					let missing = [];
					for (let i = 0; i < objsets.length; i++)
						if (combo[i] != objsets[i])
							missing.push(i);
					if (missing.length == 0) {
						clear(stage, reward);
					} else {
						failStage();
						map.tipMessage("Hint: " + hint(missing));
					}
				} else {
					npc.sayNext(leaderDialog);
				}
			}
		}
	} else {
		let complete = event.getVariable(stage + "stageclear");
		if (complete != null && complete)
			npc.sayNext(stageClearedDialog);
		else
			npc.sayNext(memberDialog);
	}
}

function bossStage(stage) {
	let leaderPreamble = "leaderpreamble";
	let leaderDialog = "leaderdialog";
	let memberDialog = "memberdialog";
	let stageClearDialog = "clear";
	let stageClearedDialog = "cleared";
	let itemId = 4001023;
	let required = 1;
	let reward = 0;

	if (player.getId() == party.getLeader()) {
        let lVar = "leader" + stage + "preamble";
        let preamble = event.getVariable(lVar);
        if (preamble == null || !preamble) {
            event.setVariable(lVar, true);
            npc.sayNext(leaderPreamble);
        } else {
			if (!player.hasItem(itemId, required)) {
				npc.sayNext(leaderDialog);
			} else {
				npc.sayNext(stageClearDialog);
				player.loseItem(itemId);
				clear(stage, reward);
				player.changeMap(event.getVariable("party2stage10"), "st00");
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
				player.changeMap(event.getVariable("party2stage10"), "st00");
            } else {
                npc.sayNext(memberDialog);
            }
        }
    }
}

function bonusStage(stage) {
	let selection = npc.askYesNo("Would you like to leave?");
	if (selection == 1) {
		player.changeMap(922011100, "st00");
	}
}

function rewardStage(stage) {
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
		player.changeMap(221024500, "sp");
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
		numbersStage(stage);
		break;
	case 9:
		bossStage(stage);
		break;
	case 10:
		bonusStage(stage);
		break;
	case 11:
		rewardStage(stage);
		break;
}