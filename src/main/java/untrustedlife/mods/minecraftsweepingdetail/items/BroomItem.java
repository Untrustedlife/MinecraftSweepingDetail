package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import untrustedlife.mods.minecraftsweepingdetail.UntrustedDiceRolling;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import java.util.List;

/*
 * 
 * Tag wishlist for later
 * 
 * Sweepable
 *       "horror_element_mod:blood_1",
      "horror_element_mod:blood_2",
      "horror_element_mod:blood_3",
      "horror_element_mod:blood_4",
      "horror_element_mod:blood_5",
      "horror_element_mod:blood_6",
      "horror_element_mod:blood_7",
      "horror_element_mod:blood_8",
      "horror_element_mod:blood_9",
      "horror_element_mod:blood_10",
      "horror_element_mod:acid_blood_1",
      "horror_element_mod:acid_blood_2",
      "horror_element_mod:green_blood_1",
      "horror_element_mod:green_blood_2",
      "apocalypsenow:papertrash",
      "apocalypsenow:paperstrash",
      "apocalypsenow:papertrashvariant",
      "apocalypsenow:papertrashvariant_2",
      "apocalypsenow:paperstrashvariant_2",
      "apocalypsenow:paperstrashvariant",
      "apocalypsenow:papersvariant",
      "apocalypsenow:papersvariant_2"
 */
public class BroomItem extends Item {
    private final int burnTicks;
    public BroomItem(Properties properties,int burnTimeInTicks) {
        super(properties);
        this.burnTicks = burnTimeInTicks;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player != null) {
            //If item is 'sweepable' it should be swept 
            if (level.getBlockState(context.getClickedPos()).is(BlockTags.create(new ResourceLocation("ulsmsd","sweeptiertags/sweepable")))) {
                if (level.isClientSide){
                    generateSweepParticles(player,level,context);
                }
                if (!level.isClientSide){
                    //Need to do tag logic here

                    //if grass do the gras one, otherwise eventually turn to air etc
                    
                    //This is only valid for grass blocks, but ill test it with all for now
                    level.setBlock(context.getClickedPos(), Blocks.DIRT.defaultBlockState(), 1 | 2);
                    player.swing(context.getHand(), true);  // Makes the player swing their arm as if attacking
                    context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                    // Run a loot table (using the vanilla grass loot table for testing purposes)
                    LootTable lootTable = level.getServer().getLootTables().get(new ResourceLocation("minecraft:blocks/grass_block"));
                    LootContext.Builder builder = new LootContext.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.ORIGIN, context.getClickLocation())
                        .withParameter(LootContextParams.TOOL, context.getItemInHand())
                        .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(context.getClickedPos()))  // Include the block state
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

                    LootContext lootContext = builder.create(LootContextParamSets.BLOCK);
                    List<ItemStack> loot = lootTable.getRandomItems(lootContext);
                    // Give the player the loot
                    for (ItemStack itemStack : loot) {
                        player.addItem(itemStack);
                    }

                }
                return InteractionResult.SUCCESS;
             }
            }
        return super.useOn(context);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entity, int count)
    {

    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (stack.getItem().isEdible()) {
           return stack.getFoodProperties(null).isFastFood() ? 20 : 40;
        } else {
            return 40;
        }
     }

     @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return this.burnTicks;
    }

    //Possibly use tags to change this too
    public void generateSweepParticles( Player player, Level level , UseOnContext context){
        player.playSound(SoundEvents.GRASS_PLACE, 1.0F, 1.0F);
        Direction clickedFace = context.getClickedFace();
        BlockPos clickedPos = context.getClickedPos();
        for (int i = 0; i < 20; i++) {  // Increase particles for visibility
            double xPos = clickedPos.getX() + 0.5;
            double yPos = clickedPos.getY() + 0.5;
            double zPos = clickedPos.getZ() + 0.5;
            switch (clickedFace) {
                case UP:
                    yPos += 0.5;
                    xPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    zPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    break;
                case DOWN:
                    yPos -= 0.5;
                    xPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    zPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    break;
                case NORTH:
                    xPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    yPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    zPos -= 0.5;
                    break;
                case SOUTH:
                    xPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    yPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    zPos += 0.5;
                    break;
                case WEST:
                    zPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    yPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    xPos -= 0.5;
                    break;
                case EAST:
                    zPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    yPos+=(UntrustedDiceRolling.generateRangeNegativeOneToOne());
                    xPos += 0.5;
                    break;
            }
            double xSpeed = (level.random.nextDouble()- 0.5) * 0.5;
            double ySpeed = (level.random.nextDouble()- 0.5) * 0.5;
            double zSpeed = (level.random.nextDouble()- 0.5) * 0.5;
            level.addParticle(new DustParticleOptions(new Vector3f(0, 0.5F, 0), 1.0F), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }



}