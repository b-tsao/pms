/**
 * Lilishu: Hair Style Assistant (NPC 2090101)
 * Mu Lung: Mu Lung Hair Salon (Map 250000003)
 *
 * Allows hair change (REG).
 *
 * @author joemama
 */

function getRandomStyle(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30250, 30350, 30270, 30150, 30300, 30600, 30160, 30700, 30720, 30420];
	else if (gender == 1)
		styles = [31040, 31250, 31310, 31220, 31300, 31680, 31160, 31030, 31230, 31690, 31210, 31170, 31450];
	let style = styles[Math.floor(Math.random() * styles.length)];
	if (npc.isHairValid(style + color)) //prefer current hair color
		style += color;
	return style;
}

function getRandomColor() {
	let array = npc.getAllHairColors();
	return array[Math.floor(Math.random() * array.length)];
}

let selection = npc.askMenu("I'm Lilishu, the assistant. Do you have #b#t5150024##k or #b#t5151019##k with you? If so, what do you think about letting me take care of your hairdo? What do you want to do with your hair?\r\n#b"
		+ "#L0#Haircut: #i5150024##t5150024##l\r\n"
		+ "#L1#Dye your hair: #i5151019##t5151019##l");
let item;
let styleChange;
let hair;
if (selection == 0) {
	item = 5150024;
	selection = npc.askYesNo("If you use the EXP coupon your hair will change RANDOMLY with a chance to obtain a new experimental style that I came up with. Are you going to use #b#t5150024##k and really change your hairstyle?");
	hair = getRandomStyle(player.getGender(), player.getHair());
	styleChange = true;
} else if (selection == 1) {
	item = 5151019;
	selection = npc.askYesNo("If you use a regular coupon your hair will change RANDOMLY. Do you still want to use #b#t5151019##k and change it up?");
	hair = getRandomColor();
	styleChange = false;
}
if (selection == 1) {
	if (player.hasItem(item, 1)) {
		player.loseItem(item, 1);
		player.setHair(hair);
		npc.say("Enjoy your new and improved hair" + (styleChange ? "style" : "color") + "!");
	} else {
		npc.say("Hmmm...it looks like you don't have our designated coupon...I'm afraid I can't " + (styleChange ? "give you a haircut" : "dye your hair") + " without it. I'm sorry...");
	}
}