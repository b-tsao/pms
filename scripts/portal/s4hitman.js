function enter(pi) {
    if (pi.getQuestStatus(6141).equals(MapleQuestStatus.Status.STARTED)) {
	pi.getMap().killAllMonsters(true);
	pi.spawnMonster(9300088,6);
	pi.playerMessage(5, "Dark Lord disciples have emerged.");
	} else {
    pi.playerMessage(5, "This portal is currently unavailable");
}
}