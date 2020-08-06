var status = 0;

function start() {
    cm.sendYesNo("Have you finished the tutorial?");
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("Then finish it already!");
            cm.dispose();
        } else
            status++;
        if (status == 1)
            cm.sendNext("Onto the next stage my brave warrior!");
        else if (status == 2) {
            cm.warp(40000, 0); //cm.warp(40000, 0);
            cm.dispose();
        }
    }
}