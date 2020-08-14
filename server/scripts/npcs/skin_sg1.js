/**
 * Xan: Skin care Master (NPC 9270025)
 * CBD Singapore (Map 540000000)
 *
 * Allows skin color change.
 *
 * @author joemama
 */

let skinColorChoices = npc.getAllSkinColors();

npc.sayNext("Well, hello! Welcome to the CBD Skin-Care~! Would you like to have a firm, tight, healthy looking skin like mine?  With #b#t5153010##k, you can let us take care of the rest and have the kind of skin you've always wanted~!");
let selection = npc.askAvatar("With our specialized machine, you can see the way you'll look after the treatment PRIOR to the procedure. What kind of a look are you looking for? Go ahead and choose the style of your liking~!", skinColorChoices);

if (player.hasItem(5153010, 1)) {
	player.loseItem(5153010, 1);
	player.setSkin(skinColorChoices[selection]);
	npc.say("Enjoy your new and improved skin!");
} else {
	npc.say("Um...you don't have the skin-care coupon you need to receive the treatment. Sorry, but I am afraid we can't do it for you...");
}