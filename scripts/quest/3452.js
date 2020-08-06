var status = -1;

function end(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			qm.sendNext("Hmmm... wait, this is...!! Wow... interesting... it's definitely worth studying. Hey, you look like a traveler... have you ever been to Eos Tower? If so, then you may have met a monster that resembles an Octopus, #o3230302#. A monster called #b#o3230302##k, isn't it strange?");
		} else if (status == 1) {
			qm.sendNextPrev("What do I mean by this? Well, the way #o3230302# is structured, it really looks familiar...and after some researching, I have come to conclusion that it may be close to that of the aliens. Maybe #o3230302# is made by the aliens. It's defintely worth more attention. Can you help me on this?");
		} else if (status == 2) {
			qm.sendNextPrev("But you know... there's gotta be a better method to research something like #t4000099#. Something like a #bManual#k... but it&apos;s really hard to get, so just disregard what I just said.");
		} else if (status == 3) {
			qm.gainExp(96000);
			qm.gainItem(2000011, 80);
			qm.gainItem(4000099, -1);
			qm.forceCompleteQuest(3452);
			qm.dispose();
		}
	}
}

function start(mode, type, selection){
}