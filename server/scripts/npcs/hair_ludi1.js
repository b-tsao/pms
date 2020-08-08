/**
 * Miyu: Owner (NPC 2041007)
 * Ludibrium: Ludibrium Hair Salon (Map 220000004)
 *
 * Ludibrium hair changer - VIP coupons.
 *
 * @author joemama
 */

function getStyleChoices(gender, currentHair) {
	let color = currentHair % 10;
	let styles;
	if (gender == 0)
		styles = [30030, 30020, 30000, 30250, 30190, 30150, 30050, 30280, 30240, 30300, 30160];
	else if (gender == 1)
		styles = [31040, 31000, 31150, 31280, 31160, 31120, 31290, 31270, 31030, 31230, 31010];
	for (let i = 0; i < styles.length; i++)
		if (npc.isHairValid(styles[i] + color)) //prefer current hair color
			styles[i] += color;
	return styles;
}

let selection = npc.askMenu("I'm the head of this hair salon, Miyu. If you have #b#t5150007##k, #b#t5151007##k or #b#t5420005##k, allow me to take care of your hairdo. Please choose the one you want.\r\n#b"
		+ "#L0# Haircut(VIP coupon)#l\r\n"
		+ "#L1# Dye your hair(VIP coupon)#l\r\n"
		+ "#L2# Change Hairstyle (VIP Membership Coupon)#l");
let item;
let take;
let styleChange;
let hairs;
switch (selection) {
	case 0:
		hairs = getStyleChoices(player.getGender(), player.getHair());
		selection = npc.askAvatar("I can totally change up your hairstyle and make it look so good. Why don't you change it up a bit? If you have #b#t5150007##k I'll change it for you. Choose the one to your liking~", hairs);
		item = 5150007;
		take = true;
		styleChange = true;
		break;
	case 1:
		hairs = npc.getAllHairColors();
		selection = npc.askAvatar("I can totally change your haircolor and make it look so good. Why don't you change it up a bit? With #b#t5151007##k I'll change it for you. Choose the one to your liking.", hairs);
		item = 5151007;
		take = true;
		styleChange = false;
		break;
	case 2:
		hairs = getStyleChoices(player.getGender(), player.getHair());
		selection = npc.askAvatar("I can totally change up your hairstyle and make it look so good. Why don't you change it up a bit? If you have #b#t5420005##k I'll change it for you. With this coupon, you have the power to change your hairstyle to something totally new, as often as you want, for ONE MONTH! Now, please choose the hairstyle of your liking.", hairs);
		item = 5420005;
		take = false;
		styleChange = true;
		break;
}
if (player.hasItem(item, 1)) {
	if (take)
		player.loseItem(item, 1);
	player.setHair(hairs[selection]);
	npc.say("Enjoy your new and improved hairstyle!");
} else {
	npc.sayNext("Hmmm...it looks like you don't have our designated coupon...I'm afraid I can't " + (styleChange ? "give you a haircut" : "dye your hair") + " without it. I'm sorry.");
}