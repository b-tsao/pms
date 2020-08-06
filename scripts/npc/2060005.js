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
	if(cm.getLevel()>=70) { 
	cm.sendSimple ("Hello, I am #bKenta#k and I just love riding mounts! The wind in my hair! Oh sorry.. what would you like?\r\n#L0##b#i1902000# Hog - 10,000,000 mesos#k#l\r\n#L1##b#i1902001# Silver Mane - 20,000,000 mesos#k#l\r\n#L2##b#i1902002# Red Draco - Free#k#l\r\n\#L3##b #i1912000# Saddle - Free#l\r\n#L4##b #s0001004# Mount Rider - Free#l");
	}
	else{
	cm.sendOk("You are not high enough level to talk to me yet..")
} 
     } else if (status == 1) {
       if (selection == 0) {
		   if(cm.getLevel()>=70 && !cm.haveItem(1902000) && cm.getMeso() >= 10000000){
			cm.gainItem(1902000, 1);
			cm.gainMeso(-10000000);
			cm.sendOk("Enjoy your Hog!")
			cm.dispose();
			} else {
            cm.sendOk("You either are not level 70, already have a Hog, or do not have 10,000,000mesos!")
			}		
           } else if (selection == 1) {
			    if(cm.getLevel()>=120 && !cm.haveItem(1902001) && cm.getMeso() >= 20000000){
			cm.gainItem(1902001, 1);
			cm.gainMeso(-20000000);
			cm.sendOk("Enjoy your Silver Mane!")
			cm.dispose();
			} else {
            cm.sendOk("You either are not level 120, already have a Silver Mane, or do not have 20,000,000mesos!")
			}		
            } else if (selection == 2) {
			if(cm.getLevel()==200 && !cm.haveItem(1902002)){
			cm.gainItem(1902002, 1);
			cm.sendOk("Enjoy your free Red Draco!")
			cm.dispose();
			} else {
            cm.sendOk("You either are not level 200 or already have a Red Draco!")
			}		
            } else if (selection == 3) {	
			 if(!cm.haveItem(1912000)){
			cm.gainItem(1912000, 1);
			cm.sendOk("Here is your Saddle!")
			cm.dispose();
			} else {
            cm.sendOk("You already have a Saddle!")
			}		
			} else if (selection == 4) {
			cm.teachSkill(1004,1,1);
			cm.sendOk("You have learned the Mount Rider skill!")
			cm.dispose();
			} 
}
	 }