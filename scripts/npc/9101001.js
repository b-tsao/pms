var status = 0;

function start() {
    cm.sendYesNo("Would you like to continue?");
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("Why not?");
            cm.dispose();
        } else
            status++;
        if (status == 1)
            cm.sendNext("Let's go!");
        else if (status == 2) {
            cm.warp(10000, 0); //cm.warp(40000, 0);
            cm.dispose();
        }
    }
}