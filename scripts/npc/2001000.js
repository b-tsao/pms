var status = 0;
var itemchance = Math.floor(Math.random()*1000);
var boardchance = Math.floor(Math.random()*6);
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }else if (mode == 0){
        cm.dispose();
		return;		
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
				cm.sendNext ("JINGLE BELLS, BATMAN SMELLS, ROBIN LAID AN EGG~! THE BATMOBIL-...Oh hey!");
        } else if (status == 1) {
				cm.sendSimple ("Christmas is my most #bFAVORITE#k time of the year! Did you want to trade #rNevermelting Snow#k for some Christmas items?" +
                 "#k\r\n#L10##i1012013# - #bRudolph's Nose#k 50 Nevermelting Snow" +
				 "#k\r\n#L20##i1002728# - #bRudolph's Horn#k 500 Nevermelting Snow" +
				 "#k\r\n#L30##i2040023# - #bScroll for Rudolph's Horn 60%#k 450 Nevermelting Snow" +
				 "#k\r\n#L40##i1442030# - #bSnowboards#k 150 Nevermelting Snow");
			} else if (selection == 10) {
				if (cm.haveItem(4031875, 50)) {
					cm.gainItem(4031875, -50);
					if ((itemchance >= 1) && (itemchance <= 100)) { // 10%
						cm.gainItem(1012011, 1, true, true); //str
					} else if ((itemchance >= 101) && (itemchance <= 200)) { // 10%
						cm.gainItem(1012012, 1, true, true); //dex
					} else if ((itemchance >= 201) && (itemchance <= 300)) { // 10%
						cm.gainItem(1012013, 1, true, true); //int
					} else if ((itemchance >= 301) && (itemchance <= 400)) { // 10%
						cm.gainItem(1012014, 1, true, true); //luk
					} else if ((itemchance >= 401) && (itemchance <= 500)) { // 10%
						cm.gainItem(1012016, 1, true, true); //wdef
					} else if ((itemchance >= 501) && (itemchance <= 600)) { // 10%
						cm.gainItem(1012018, 1, true, true); //mdef
					} else if ((itemchance >= 601) && (itemchance <= 790)) { // 18% 
						cm.gainItem(1012019, 1, true, true); //accuracy
					} else if ((itemchance >= 791) && (itemchance <= 970)) { // 18%
						cm.gainItem(1012020, 1, true, true); //avoid
					} else if ((itemchance >= 971) && (itemchance <= 980)) { // 1%
						cm.gainItem(1012015, 1, true, true); //w.atk
					} else if ((itemchance >= 981) && (itemchance <= 1000)) { // 2%
						cm.gainItem(1012017, 1, true, true); //m.atk
					}
				} else {
					cm.sendOk("You don't seem to have enough for this!");
				}
			} else if (selection == 20) {  
				if (cm.haveItem(4031875, 500) && !cm.haveItem(1002728) && !cm.haveItem(1002728, 1, true, true)) {
					cm.gainItem(4031875, -500);
					cm.gainItem(1002728, 1, true, true); //Antlers
				} else {
					cm.sendOk("You don't seem to have enough for this or already own this item!");
				}
			} else if (selection == 30) {  
				if (cm.haveItem(4031875, 450)) {
					cm.gainItem(4031875, -450);
					cm.gainItem(2040023, 1, true); //Antler scrolls
				} else {
					cm.sendOk("You don't seem to have enough for this!");
				}
			} else if (selection == 40) {  
				if (cm.haveItem(4031875, 150)) {
					cm.gainItem(4031875, -150);
					if ((itemchance >= 1) && (itemchance <= 850)) { //85% 
						cm.gainItem(1442011+boardchance, 1, true, true); //basic snowboards
					} else if ((itemchance >= 851) && (itemchance <= 950)) { // 10%
						cm.gainItem(1442030, 1, true, true); //Maple Snowboard
					} else if ((itemchance >= 951) && (itemchance <= 1000)) { // 5%
						cm.gainItem(1442046 , 1, true, true); //Super Snowboard
					} 
				} else {
					cm.sendOk("You don't seem to have enough for this!");
				}
			} 
		}
	}