/**
 * Gina: Dermatologist (NPC 2041013)
 * Ludibrium: Ludibrium Skin Care(Map 220000005)
 *
 * Ludibrium skin changer.
 *
 * @author joemama
 */

let skinColorChoices = npc.getAllSkinColors();

npc.sayNext("Well, hello! Welcome to the Ludibrium Skin-Care! Would you like to have a firm, tight, healthy looking skin like mine?  With #b#t5153002##k, you can let us take care of the rest and have the kind of skin you've always wanted~!");
let selection = npc.askAvatar("With our specialized machine, you can see yourself after the treatment in advance. What kind of skin-treatment would you like to do? Choose the style of your liking...", skinColorChoices);

if (player.hasItem(5153002, 1)) {
	player.loseItem(5153002, 1);
	player.setSkin(skinColorChoices[selection]);
	npc.sayNext("Enjoy your new and improved skin!");
} else {
	npc.sayNext("Um...you don't have the skin-care coupon you need to receive the treatment. Sorry, but I am afraid we can't do it for you.");
}