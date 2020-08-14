/**
 * Noma: Assistant Plastic Surgeon (NPC 2090104)
 * Mu Lung: Mu Lung (Map 250000000)
 *
 * Allows face change for Mu Lung REG Ticket (Face style & color)
 *
 * @author joemama
 */

function getRandomStyle(gender, currentFace) {
	let color = currentFace % 1000 - (currentFace % 100);
	let styles;
	if (gender == 0)
		styles = [20020, 20017, 20021, 20022, 20005, 20006, 20007, 20008, 20012, 20014];
	else if (gender == 1)
		styles = [21313, 21021, 21012, 21009, 21005, 21006, 21007, 21008, 21012, 21014];
	let style = styles[Math.floor(Math.random() * styles.length)];
	if (npc.isFaceValid(style + color)) //prefer current eye color
		style += color;
	return style;
}

function getRandomColor() {
	let array = npc.getAllEyeColors();
    return array[Math.floor(Math.random() * array.length)];
}

let selection = npc.askMenu("Hi, there~! I'm Pata, in charge of the REG cosmetics here at the Mu Lung lens shop! With #b#t5152027##k or #b#t5152042##k, you can let us take care of the rest and have the kind of beautiful look you've always craved~! Remember, the first thing everyone notices about you is the eyes, and we can help you find the cosmetic lens that most fits you! Now, what would you like to use?\r\n#b"
		+ "#L0# Mu Lung Plastic Surgery Coupon (REG)#l\r\n"
        + "#L1# Mu Lung Cosmetic Lens Coupon (REG)#l");
if (selection == 0) {
    let selection = npc.askYesNo("If you use the regular coupon, your face may transform into a random new look...do you still want to do it using #b#t5152027##k?");
    if (selection == 1) {
	    if (player.hasItem(5152027, 1)) {
		    player.loseItem(5152027, 1);
		    player.setFace(getRandomStyle(player.getGender(), player.getFace()));
		    npc.sayNext("Enjoy!");
	    } else {
		    npc.sayNext("Hmm ... it looks like you don't have the coupon specifically for this place. Sorry to say this, but without the coupon, there's no plastic surgery for you.");
        }
    } else if (selection == 0) {
	npc.sayNext("I see ... take your time, see if you really want it. Let me know when you make up your mind.");
    }
} else if (selection == 1) {
	selection = npc.askYesNo("If you use the regular coupon, you'll be awarded a random pair of cosmetic lenses. Are you going to use #b#t5152042##k and really make the change to your eyes?");
	if (selection == 1) {
		if (player.hasItem(5152042, 1)) {
			player.loseItem(5152042, 1);
			player.setFace(getRandomColor());
			npc.sayNext("Enjoy your new and improved cosmetic lenses!");
		} else {
			npc.sayNext("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you");
		}
	} else if (selection == 0) {
		npc.sayNext("I see. That's understandable, since you're unsure whether you'll get the cosmetic lenses of your liking. We're in no hurry, whatsoever, so take your time! Please let me know when you decide to make the change~!");
	}
}