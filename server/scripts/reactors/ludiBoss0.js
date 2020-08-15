/* Ludi PQ Crack Reactor ^_^
 *@Author Neuro
  *2201003.js
 */
 
let map = reactor.getEvent("party2").getVariable("party2stage9");
map.setNoSpawn(false);
map.redMessage("Alishar has been summoned.");
map.spawnMob(9300012, 941, 184);