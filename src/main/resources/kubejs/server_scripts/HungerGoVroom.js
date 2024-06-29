// Listen to player tick event to modify hunger depletion
console.info("Running HungerGoVroom...");
let previousPosition = { x: 0, y: 0, z: 0 };
PlayerEvents.tick(event => {
  let player = event.player;
  const currentPosition = { x: player.x, y: player.y, z: player.z };
    //20 ticks a second default 0.01, 4 exuhstian for one half shank
    //20 seconds -> 1 food baseline
    // Check if player is not in creative or spectator mode
    if (!player.isCreative() && !player.isSpectator()) {
        let exhaustionToAdd = 0.01;
      // Increase exhaustion more rapidly under certain conditions
      if (player.isAlive()) {
        exhaustionToAdd+=0.01;
      }
      if (player.isSprinting()) {
        exhaustionToAdd+=0.01;
      }
      if (player.isCrouching()){
        exhaustionToAdd+=0.01;
      }
      if (player.isSwimming()){
        exhaustionToAdd+=0.01;
      }
      if (player.isMiningBlock()){
        exhaustionToAdd+=0.02;
      }
      //Might add some functions just to do this with a single function call
      if (previousPosition.x !== currentPosition.x || previousPosition.y !== currentPosition.y || previousPosition.z !== currentPosition.z) {
        // Player has moved
        exhaustionToAdd += 0.01;
      }
      if (player.isBlocking()){
        exhaustionToAdd+=0.015;
      }
      if (player.isSleeping()){
        exhaustionToAdd+=0.5;
      }
      //if player alive increase exhaustion
      if (player.isAlive()) {
        player.addExhaustion(exhaustionToAdd)
      }

    }
  });

  //No attackentity event to use
  EntityEvents.hurt(event => {
    if (event.source.player){
        event.source.player.addExhaustion(1);
    }
  })




