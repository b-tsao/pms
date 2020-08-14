/**
 * Kelvin: Plastic Surgery (NPC 9270024)
 * CBD Singapore (Map 540000000)
 *
 * Allows face change for Singapore VIP Ticket
 *
 * @author joemama
 */

function getStyleChoices(gender, currentFace) {
	let color = currentFace % 1000 - (currentFace % 100);
	let styles;
	if (gender == 0)
		styles = [20109, 20110, 20106, 20108, 20112, 20013];
	else if (gender == 1)
		styles = [21021, 21009, 21010, 21006, 21008, 21012];
	for (let i = 0; i < styles.length; i++)
		if (npc.isFaceValid(styles[i] + color)) //prefer current eye color
			styles[i] += color;
	return styles;
}

let faces = getStyleChoices(player.getGender(), player.getFace());
let selection = npc.askAvatar("Let's see... I can totally transform your face into something new. Don't you want to try it? For #b#t5152038##k, you can get the face of your liking. Take your time in choosing the face of your preference...", faces);
if (player.hasItem(5152038, 1)) {
	player.loseItem(5152038, 1);
	player.setFace(faces[selection]);
	npc.sayNext("Enjoy your new and improved face!");
} else {
	npc.sayNext("Hmm ... it looks like you don't have the coupon specifically for this place...sorry to say this, but without the coupon, there's no plastic surgery for you.");
}