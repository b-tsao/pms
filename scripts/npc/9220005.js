//Niggerdolph coded by Cipher of MapleShitage//

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
	cm.sendSimple ("Where would you like to go?\r\n#L0##bHappyville#k#l\r\n#L1##bExtra Frosty Snow Zone#k#l"); //lazy way 4 days
} 
     else if (status == 1) {
       if (selection == 0) {
		  cm.warp(209000000, 0); 
			cm.dispose();
           } else if (selection == 1) {
			  cm.warp(209080000, 0);
			cm.dispose();
			} 
}
} 