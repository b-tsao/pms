/**
 * Dr. Bosch: Lens Specialist (NPC 9200102)
 * Ludibrium: Ludibrium Plastic Surgery (Map 220000003)
 *
 * Ludibrium face changer - eye color only.
 *
 * @author joemama
 */

function getRandomColor() {
	let array = npc.getAllEyeColors();
	return array[Math.floor(Math.random() * array.length)];
}

let selection = npc.askMenu("Hi, there. I'm Dr. Bosch, in charge of the cosmetic lenses here at the Ludibrium Plastic Surgery Shop! With #b#t5152012##k or #b#t5152015##k, you can let us take care of the rest and have the kind of beautiful look you've always craved~! Remember, the first thing everyone notices about you is the eyes, and we can help you find the cosmetic lens that most fits you! Now, what would you like to use?\r\n#b"
		+ "#L0# Cosmetic Lenses at Ludibrium (Reg. coupon)#l\r\n"
		+ "#L1# Cosmetic Lenses at Ludibrium (VIP coupon)#l");
if (selection == 0) {
	selection = npc.askYesNo("If you use the regular coupon, you'll be awarded a random pair of cosmetic lenses. Are you going to use #b#t5152012##k and really make the change to your eyes?");
	if (selection == 1) {
		if (player.hasItem(5152012, 1)) {
			player.loseItem(5152012, 1);
			player.setFace(getRandomColor());
			npc.sayNext("Enjoy your new and improved cosmetic lenses!");
		} else {
			npc.sayNext("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you");
		}
	} else if (selection == 0) {
		npc.sayNext("I see. That's understandable, since you're unsure whether you'll get the cosmetic lenses of your liking. We're in no hurry, whatsoever, so take your time! Please let me know when you decide to make the change~!");
	}
} else if (selection == 1) {
	let faces = npc.getAllEyeColors();
	selection = npc.askAvatar("With our specialized machine, you can see yourself after the treatment in advance. What kind of lens would you like to wear? Choose the style of your liking...", faces);
	if (player.hasItem(5152015, 1)) {
		player.loseItem(5152015, 1);
		player.setFace(faces[selection]);
		npc.sayNext("Enjoy your new and improved cosmetic lenses!");
	} else {
		npc.sayNext("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you");
	}
}