/**
 * Pata: Plastic Surgeon (NPC 2090103)
 * Mu Lung: Mu Lung (Map 250000000)
 *
 * Allows face change for Mu Lung VIP Ticket (Face style & color)
 *
 * @author joemama
 */

function getStyleChoices(gender, currentFace) {
	let color = currentFace % 1000 - (currentFace % 100);
	let styles;
	if (gender == 0)
		styles = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014, 20009, 20010];
	else if (gender == 1)
		styles = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014, 21009, 21011];
	for (let i = 0; i < styles.length; i++)
		if (npc.isFaceValid(styles[i] + color)) //prefer current eye color
			styles[i] += color;
	return styles;
}

let selection = npc.askMenu("Hi, there~! I'm Pata, in charge of the VIP cosmetics here at the Mu Lung lens shop! With #b#t5152028##k or #b#t5152041##k, you can let us take care of the rest and have the kind of beautiful look you've always craved~! Remember, the first thing everyone notices about you is the eyes, and we can help you find the cosmetic lens that most fits you! Now, what would you like to use?\r\n#b"
		+ "#L0# Mu Lung Plastic Surgery Coupon (VIP)#l\r\n"
		+ "#L1# Mu Lung Cosmetic Lens Coupon (VIP)#l");
if (selection == 0) {
	let faces = getStyleChoices(player.getGender(), player.getFace());
	let selection = npc.askAvatar("Let's see... I can totally transform your face into something new. Don't you want to try it? For #b#t5152028##k, you can get the face of your liking. Take your time in choosing the face of your preference...", faces);
	if (player.hasItem(5152028, 1)) {
		player.loseItem(5152028, 1);
		player.setFace(faces[selection]);
		npc.sayNext("Enjoy your new and improved face!");
	} else {
	npc.sayNext("Hmm ... it looks like you don't have the coupon specifically for this place...sorry to say this, but without the coupon, there's no plastic surgery for you.");
	} 
}
else if (selection == 1) {
	let faces = npc.getAllEyeColors();
	selection = npc.askAvatar("With our specialized machine, you can see yourself after the treatment in advance. What kind of lens would you like to wear? Choose the style of your liking...", faces);
	if (player.hasItem(5152041, 1)) {
		player.loseItem(5152041, 1);
		player.setFace(faces[selection]);
		npc.sayNext("Enjoy your new and improved cosmetic lenses!");
	} else {
		npc.sayNext("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you");
	}
}