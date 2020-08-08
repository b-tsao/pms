/**
 * Tepei: Hair Stylist (NPC 9120100)
 * Hair Salon Zipangu (Map 600000001)
 *
 * Showa Town hair changer - select a hair.
 *
 * @author joemama
 */

function getStyleChoices(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30230, 30030, 30260, 30280, 30240, 30290, 30020, 30270, 30340, 30710, 30810];
	else if (gender == 1)
		styles = [31310, 31300, 31050, 31040, 31160, 31100, 31410, 31030, 31790, 31550];
	for (let i = 0; i < styles.length; i++)
		if (npc.isHairValid(styles[i] + color)) //prefer current hair color
			styles[i] += color;
	return styles;
}

let selection = npc.askMenu("Hello I'm Tepei. If you have either a #b#t5150009##k or a #b#t5151009##k, then please let me take care of your hair. Choose what you want to do with it.\r\n#b"
		+ "#L0#Haircut: #i5150009##t5150009##l\r\n"
		+ "#L1#Dye your hair: #i5151009##t5151009##l");
let item;
let take;
let styleChange;
let hairs;
switch (selection) {
	case 0:
		hairs = getStyleChoices(player.getGender(), player.getHair());
		selection = npc.askAvatar("I can totally change up your hairstyle and make it look so good. Why don't you change it up a bit? With #b#t5150009##k, I'll take care of the rest for you. Choose the style of your liking!", hairs);
		item = 5150009;
		take = true;
		styleChange = true;
		break;
	case 1:
		hairs = npc.getAllHairColors();
		selection = npc.askAvatar("I can totally change your haircolor and make it look so good. Why don't you change it up a bit? With #b#t5151009##k, I'll take care of the rest. Choose the color of your liking!", hairs);
		item = 5151009;
		take = true;
		styleChange = false;
		break;
}
if (player.hasItem(item, 1)) {
	if (take)
		player.loseItem(item, 1);
	player.setHair(hairs[selection]);
	npc.say("Enjoy your new and improved " + (styleChange ? "hairstyle" : "haircolor") + "!");
} else {
	npc.say("Hmmm...it looks like you don't have our designated coupon...I'm afraid I can't " + (styleChange ? "give you a haircut" : "dye your hair") + " without it. I'm sorry...");
}