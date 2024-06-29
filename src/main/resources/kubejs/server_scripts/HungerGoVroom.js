// Listen to player tick event to modify hunger depletion
console.info("Running HungerGoVroom...")
PlayerEvents.tick(event => {
    //20 ticks a second default 0.01, 4 exuhstian for one half shank
    //20 seconds -> 1 food baseline
    let player = event.player;
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
      if (player.isMoving()){
        exhaustionToAdd+=0.01;
      }
      if (player.isBlocking()){
        exhaustionToAdd+=0.015;
      }
      if (player.isSleeping()){
        exhaustionToAdd+=0.5;
      }
      //if player alive increase exaustian
      if (player.isAlive()) {
        player.addExhaustion(exhaustionToAdd)
      }

    }
  });

  //No attackentity event tro use
  EntityEvents.hurt(event => {
    if (event.source.player){
        event.source.player.addExhaustion(1);
    }
  })




