/**
 * Grandpa Luo: Lead Hair Stylist (NPC 2090100)
 * Mu Lung: Mu Lung Hair Salon (Map 250000003)
 *
 * Allows hair change (VIP).
 *
 * @author joemama
 */

function getStyleChoices(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30250, 30350, 30270, 30150, 30300, 30600, 30160];
	else if (gender == 1)
		styles = [31040, 31250, 31310, 31220, 31300, 31680, 31160, 31030, 31230];
	for (let i = 0; i < styles.length; i++)
		if (npc.isHairValid(styles[i] + color)) //prefer current hair color
			styles[i] += color;
	return styles;
}

let selection = npc.askMenu("Hello I'm Luo. If you have either a #b#t5150025##k or a #b#t5151020##k, then please let me take care of your hair. Choose what you want to do with it.\r\n#b"
		+ "#L0#Haircut: #i5150025##t5150025##l\r\n"
		+ "#L1#Dye your hair: #i5151020##t5151020##l");
let item;
let take;
let styleChange;
let hairs;
switch (selection) {
	case 0:
		hairs = getStyleChoices(player.getGender(), player.getHair());
		selection = npc.askAvatar("I can totally change up your hairstyle and make it look so good. Why don't you change it up a bit? With #b#t5150025##k, I'll take care of the rest for you. Choose the style of your liking!", hairs);
		item = 5150025;
		take = true;
		styleChange = true;
		break;
	case 1:
		hairs = npc.getAllHairColors();
		selection = npc.askAvatar("I can totally change your haircolor and make it look so good. Why don't you change it up a bit? With #b#t5151020##k, I'll take care of the rest. Choose the color of your liking!", hairs);
		item = 5151020;
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