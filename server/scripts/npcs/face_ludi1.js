/**
 * Ellie: Doctor Assistant (NPC 2040010)
 * Ludibrium: Ludibrium Plastic Surgery (Map 220000003)
 *
 * Ludibrium face changer - random face, eye style only.
 *
 * @author joemama
 */

function getStyleChoices(gender, currentFace) {
	let color = currentFace % 1000 - (currentFace % 100);
	let styles;
	if (gender == 0)
		styles = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
	else if (gender == 1)
		styles = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
	for (let i = 0; i < styles.length; i++)
		if (npc.isFaceValid(styles[i] + color)) //prefer current eye color
			styles[i] += color;
	return styles;
}

let faces = getStyleChoices(player.getGender(), player.getFace());
let selection = npc.askAvatar("Let's see... I can totally transform your face into something new. Don't you want to try it? For #b#t5152007##k, you can get the face of your liking. Take your time in choosing the face of your preference...", faces);
if (player.hasItem(5152007, 1)) {
	player.loseItem(5152007, 1);
	player.setFace(faces[selection]);
	npc.sayNext("Enjoy your new and improved face!");
} else {
	npc.sayNext("Hmm ... it looks like you don't have the coupon specifically for this place...sorry to say this, but without the coupon, there's no plastic surgery for you.");
}