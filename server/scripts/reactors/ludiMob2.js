/*Ludi PQ Stage 7 Reactor - 2201002.js
 *@Author Neuro
 */
 
let map = event.getVariable("party2stage7")
map.setNoSpawn(false);
map.redMessage("Rombard has been summoned somewhere in the map.");
map.spawnMob(9300010, 1, -211);