var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
	setupTask = em.scheduleAtFixedRate("start", 1000 * 60 * 10);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function start() {
    var Message = new Array("@help and @command will show you a list of user commands.","Vote every 12 hours so we can get more players!","Report any bugs on our forums!");
    em.getChannelServer().broadcastPacket(net.sf.odinms.tools.MaplePacketCreator.sendYellowTip("[MapleVintage] " + Message[Math.floor(Math.random() * Message.length)]));
}