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
	cm.sendSimple ("Hello, I am #bProfessor Foxwit!#k Why do they call me Foxwit you ask? Well that's my furry name! Oh sorry.. what would you like to craft?\r\n#L0##b#i4031829# Black Phoenix Shield Forging Manual #k#l\r\n#L1##b#i4031827# Sirius Cloak Forging Manual#k#l\r\n#L2##b#i4031826# Zeta Cape Forging Manual#k#l\r\n\#L3##b #i4031828# Dark Shard Earrings Forging Manual#l");
     } else if (status == 1) {
       if (selection == 0) {
		   if(cm.haveItem(4031751) && cm.haveItem(4031753) && cm.haveItem(4031754)){
			cm.gainItem(4031829, 1);
			cm.gainItem(4031753, -1);
			cm.gainItem(4031754, -1);
			cm.gainItem(4031751, -1);
			cm.sendOk("You've successfully crafted a #bS Forging Manual#k!")
			cm.dispose();
			} else {
            cm.sendOk("To make a Black Phoenix Shield Forging Manual you need: \r\n\r\n#i4031751##b Vorticular Gyro \r\n#i4031753# Zeta Residue \r\n#i4031754# Black Versal Materia #k\r\n\r\nCome back to me when you have all these items.")
			}		
           } else if (selection == 1) {
			if(cm.haveItem(4031752) && cm.haveItem(4031750) && cm.haveItem(4031754)){
			cm.gainItem(4031827, 1);
			cm.gainItem(4031752, -1);
			cm.gainItem(4031750, -1);
			cm.gainItem(4031754, -1);
			cm.sendOk("You've successfully crafted a #bSirius Cloak Forging Manual#k!")
			cm.dispose();
			} else {
            cm.sendOk("To make a Sirius Cloak Forging Manual you need: \r\n\r\n#i4031752##b Blinking Dingbat \r\n#i4031750# Dark Matter \r\n#i4031754# Black Versal Materia #k\r\n\r\nCome back to me when you have all these items.")
			}	
            } else if (selection == 2) {
			if(cm.haveItem(4031751) && cm.haveItem(4031750)){ 
			cm.gainItem(4031826, 1);
			cm.gainItem(4031751, -1);
			cm.gainItem(4031750, -1);
			cm.sendOk("You've successfully crafted a #bZeta Cape Forging Manual#k!")
			cm.dispose();
			} else {
            cm.sendOk("To make a Zeta Cape Forging Manual you need: \r\n\r\n#i4031751##b Vorticular Gyro \r\n#i4031750# Dark Matter \r\n#k\r\n\r\nCome back to me when you have all these items.")
			}	
            } else if (selection == 3) {
			if(cm.haveItem(4031752) && cm.haveItem(4031753)){
			cm.gainItem(4031828, 1);
			cm.gainItem(4031752, -1);
			cm.gainItem(4031753, -1);
			cm.sendOk("You've successfully crafted a #bDark Shard Earrings Forging Manual#k!")
			cm.dispose();
			} else {
            cm.sendOk("To make a Dark Shard Earrings Forging Manual you need: \r\n\r\n#i4031752##b Blinking Dingbat \r\n#i4031753# Zeta Residue #k\r\n\r\nCome back to me when you have all these items.")
			}	
			} 
}
	 }