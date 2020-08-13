/**
 * Jimmy: Hair Style Assistant (NPC 9270037)
 * CBD Singapore (Map 540000000)
 *
 * Allows hair change (REG).
 *
 * @author joemama
 */

function getRandomStyle(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30110, 30290, 30230, 30260, 30320, 30190, 30240, 30350, 30270, 30180];
	else if (gender == 1)
		styles = [31260, 31090, 31220, 31250, 31140, 31160, 31100, 31120, 31030, 31270, 31810];
	let style = styles[Math.floor(Math.random() * styles.length)];
	if (npc.isHairValid(style + color)) //prefer current hair color
		style += color;
	return style;
}

function getRandomColor() {
	let array = npc.getAllHairColors();
	return array[Math.floor(Math.random() * array.length)];
}

let selection = npc.askMenu("I'm Jimmy, the assistant. Do you have #b#t5150032##k or #b#t5151027##k with you? If so, what do you think about letting me take care of your hairdo? What do you want to do with your hair?\r\n#b"
		+ "#L0#Haircut: #i5150032##t5150032##l\r\n"
		+ "#L1#Dye your hair: #i5151027##t5151027##l");
let item;
let styleChange;
let hair;
if (selection == 0) {
	item = 5150032;
	selection = npc.askYesNo("If you use the EXP coupon your hair will change RANDOMLY with a chance to obtain a new experimental style that I came up with. Are you going to use #b#t5150032##k and really change your hairstyle?");
	hair = getRandomStyle(player.getGender(), player.getHair());
	styleChange = true;
} else if (selection == 1) {
	item = 5151027;
	selection = npc.askYesNo("If you use a regular coupon your hair will change RANDOMLY. Do you still want to use #b#t5151027##k and change it up?");
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