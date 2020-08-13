/**
 * Eric: Hair Stylist (NPC 9270036)
 * CBD Singapore (Map 540000000)
 *
 * Allows hair change (VIP).
 *
 * @author joemama
 */

function getStyleChoices(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30110, 30290, 30230, 30260, 30320, 30190, 30240, 30350, 30270, 30180];
	else if (gender == 1)
		styles = [31260, 31090, 31220, 31250, 31140, 31160, 31100, 31120, 31030, 31270, 31810];
	for (let i = 0; i < styles.length; i++)
		if (npc.isHairValid(styles[i] + color)) //prefer current hair color
			styles[i] += color;
	return styles;
}

let selection = npc.askMenu("Hello I'm Eric. If you have either a #b#t5150033##k or a #b#t5151028##k, then please let me take care of your hair. Choose what you want to do with it.\r\n#b"
		+ "#L0#Haircut: #i5150033##t5150033##l\r\n"
		+ "#L1#Dye your hair: #i5151028##t5151028##l");
let item;
let take;
let styleChange;
let hairs;
switch (selection) {
	case 0:
		hairs = getStyleChoices(player.getGender(), player.getHair());
		selection = npc.askAvatar("I can totally change up your hairstyle and make it look so good. Why don't you change it up a bit? With #b#t5150033##k, I'll take care of the rest for you. Choose the style of your liking!", hairs);
		item = 5150033;
		take = true;
		styleChange = true;
		break;
	case 1:
		hairs = npc.getAllHairColors();
		selection = npc.askAvatar("I can totally change your haircolor and make it look so good. Why don't you change it up a bit? With #b#t5151028##k, I'll take care of the rest. Choose the color of your liking!", hairs);
		item = 5151028;
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