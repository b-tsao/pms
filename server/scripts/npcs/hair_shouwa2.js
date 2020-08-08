/**
 * Midori: Assistant Hair Stylist (NPC 9120101)
 * Hair Salon Zipangu (Map 801000001)
 *
 * Showa Town hair changer - random hair.
 *
 * @author joemama
 */

function getRandomStyle(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30230, 30030, 30780, 30260, 30280, 30240, 30290, 30020, 30270, 30340, 30710, 30920, 30810, 30800, 30790];
	else if (gender == 1)
		styles = [31310, 31150, 31300, 31050, 31040, 31160, 31100, 31410, 31030, 31790, 31550, 31800, 31770];
	let style = styles[Math.floor(Math.random() * styles.length)];
	if (npc.isHairValid(style + color)) //prefer current hair color
		style += color;
	return style;
}

function getRandomColor() {
	let array = npc.getAllHairColors();
	return array[Math.floor(Math.random() * array.length)];
}

let selection = npc.askMenu("I'm Midori the assistant. If you have #b#t5150008##k or #b#t5151008##k by any chance, then how about letting me change your hairdo?\r\n#b"
		+ "#L0# Haircut(EXP coupon)#l\r\n"
		+ "#L1# Dye your hair(REG coupon)#l");
let item;
let hair;
if (selection == 0) {
	item = 5150008;
	selection = npc.askYesNo("If you use the EXP coupon your hair will change RANDOMLY with a chance to obtain a new experimental style that even you didn't think was possible. Are you going to use #b#t5150008##k and really change your hairstyle?");
	hair = getRandomStyle(player.getGender(), player.getHair());
} else if (selection == 1) {
	item = 5151008;
	selection = npc.askYesNo("If you use a regular coupon your hair will change RANDOMLY. Do you still want to use #b#t5151008##k and change it up?");
	hair = getRandomColor();
}
if (selection == 1) {
	if (player.hasItem(item, 1)) {
		player.loseItem(item, 1);
		player.setHair(hair);
		npc.say("Enjoy!");
	} else {
		npc.sayNext("Hmmm...it looks like you don't have our designated coupon...I'm afraid I can't give you a haircut without it. I'm sorry.");
	}
} else if (selection == 0) {
	npc.sayNext("I understand...think about it, and if you still feel like changing come talk to me.");
}