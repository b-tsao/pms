function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == 1) { 
        status++; 
    } else { 
        status--; 
    } 
    if (status == 0) { 
	if(cm.getMeso()<=5000) { 
	cm.sendSimple ("Hello, welcome to #bMapleVintage#k! I can warp you into the training camp if you are ready, and will also reward you with a 5,000mesos starting pack to celebrate your new adventure!\r\n#L0##bI am ready for the tutorial!#k#l\r\n#L1##bBORING, I'd like to skip the tutorial!#k#l");
	}
	else{
	cm.sendOk("What the heck.. are you trying to exploit? I am reporting you.")
} 
     } else if (status == 1) {
       if (selection == 0) {
		   if (cm.getC().isBeta()) {
			cm.gainItem(1002419, 1);
		    cm.gainMeso(5000);
			cm.warp(1, 0); 
            cm.dispose();
		   } else {
			cm.gainMeso(5000);
			cm.warp(1, 0); 
            cm.dispose(); 
		   }
           } else if (selection == 1) {
			if (cm.getC().isBeta()) {
			cm.gainItem(1002419, 1);
		    cm.gainMeso(5000);
			cm.warp(40000, 0); 
            cm.dispose();
			} else {
			cm.gainMeso(5000);
			cm.warp(1, 0); 
            cm.dispose(); 
				}
		   }
	 }
}
        