package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import untrustedlife.mods.minecraftsweepingdetail.UntrustedDiceRolling;
import untrustedlife.mods.minecraftsweepingdetail.sounds.SweepingDetailSoundRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.Level;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
/**
 * Creates the broom item, which is the main feature of this mod.
 *
 * @author Untrustedlife
 */
public class BroomItem extends SwordItem  {
    private final int burnTicks;
    private final int sweepUseTime;
    public BroomItem(Properties properties,int burnTimeInTicks, int sweepUseTimeInTicks) {
         super(Tiers.WOOD, 4, -2.4F,properties);
         this.burnTicks = burnTimeInTicks;
         this.sweepUseTime = sweepUseTimeInTicks;
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
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SweepingDetailSoundRegistry.SWEEP_SOUND.get(), SoundSource.PLAYERS, 0.15f+(float)UntrustedDiceRolling.generateNormalizedValueBetween(0.07), 1.5f+((float)UntrustedDiceRolling.generateRangeNegativeOneToOne()/2));
                    RunSweepRoutineBasedOnTags(level,player,context);
                    player.getCooldowns().addCooldown(this, sweepUseTime); // 20 ticks = 1 second
                }
                return InteractionResult.SUCCESS;
             }
            }
        //Means broom can also scrape moss
        return super.useOn(context);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (stack.getItem().isEdible()) {
           return stack.getFoodProperties(null).isFastFood() ? 20 : 40;
        } else {
            return 80;
        }
     }

     @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return this.burnTicks;
    }

    //Possibly use tags to change this too
    public void generateSweepParticles( Player player, Level level , UseOnContext context){
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
            level.addParticle(new DustParticleOptions(new Vector3f(0, 0.5F, 0.1F), 1.0F), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }



    // Helper method to check if Alt is pressed
    private boolean isAltKeyPressed() {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        // Check if the left Alt key is pressed
        return InputConstants.isKeyDown(windowHandle, InputConstants.KEY_LALT);
    }

    
    public void RunSweepRoutineBasedOnTags(Level level, Player player, UseOnContext context) {
        BlockState state = level.getBlockState(context.getClickedPos());
        
        // Use a data-driven approach to map sweep types to their corresponding loot tables
        Map<String, ResourceLocation> sweepTypeLootMap = Map.of(
            //These should probably be custom loot tables
            "sweep_string", new ResourceLocation("minecraft:blocks/cobweb"),
            "sweep_dirt", new ResourceLocation("ulsmsd:blocks/dirt_sweeping"),
            "sweep_sand", new ResourceLocation("ulsmsd:blocks/sand_sweeping")
            // Add more sweep types and their corresponding loot tables here
        );
        
        // Find the sweep type from block tags
        Optional<String> sweepType = getSweepTypeFromState(state);
        if (sweepType.isPresent() && sweepTypeLootMap.containsKey(sweepType.get())) {
            processSweeping(level, player, context, sweepTypeLootMap.get(sweepType.get()));
        }
    }

    // Helper method to get the sweep type from block state using tags
    private Optional<String> getSweepTypeFromState(BlockState state) {
        for (String sweepType : Arrays.asList("sweep_string", "sweep_dirt","sweep_sand")) {
            if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptypetags/" + sweepType)))) {
                return Optional.of(sweepType);
            }
        }
        return Optional.empty();
    }

    // Map stores the block position and a pair of sweeping progress and block state.
    private final Map<BlockPos, Pair<Integer, BlockState>> sweepingProgressMap = new HashMap<>();

    //Arcadey streak
    private int oneHitCleanStreak = 0;
    private long lastSweepTime = 0;
    private static final long STREAK_EXPIRATION_TIME = 2000L; // 2 seconds

    // Helper method to process the sweeping and run the loot table
    private void processSweeping(Level level, Player player, UseOnContext context, ResourceLocation lootTableLocation) {
        //This is all very mesy and needs to be seperated out into other functions
        BlockPos pos = context.getClickedPos();
        BlockState currentState = level.getBlockState(pos);
        // Retrieve broom and block tiers
        int broomTier = getTierFromTool(context.getItemInHand());
        int blockTier = getTierFromBlock(currentState);
        // Determine the required number of sweeps for this block and broom combo
        //add function for getting base sweeps from tags and defaulting to 2
        int baseSweeps = getSweepAmountFromBlock(currentState);  // Test base number of sweeps
        int requiredSweeps = calculateSweepsRequired(blockTier, broomTier, baseSweeps);
        // Check if the block is in the map and reset if the block type has changed
        Pair<Integer, BlockState> progressData = sweepingProgressMap.get(pos);
        if (progressData != null && !progressData.getSecond().equals(currentState)) {
            // Block type has changed, reset progress
            sweepingProgressMap.remove(pos);
            progressData = null;
        }
        // Get or initialize sweeping progress
        int currentSweeps = progressData != null ? progressData.getFirst() : 0;
        currentSweeps++;
        // Update the progress in the map
        sweepingProgressMap.put(pos, Pair.of(currentSweeps, currentState));
        // Display progress to the player
        int timeLeft = Math.max(1, requiredSweeps - currentSweeps);
        long currentTime = System.currentTimeMillis();
        if (currentTime-lastSweepTime < STREAK_EXPIRATION_TIME){
            oneHitCleanStreak=0;
        }
        if (currentSweeps >= requiredSweeps) {
            if (requiredSweeps <= 1){
                if (oneHitCleanStreak > 0){
                    oneHitCleanStreak+=1;
                    player.displayClientMessage(Component.literal("§a*Swish* One Hit Clean! X"+oneHitCleanStreak), true); 
                }
                else {
                    player.displayClientMessage(Component.literal("§a*Swish* One Hit Clean!"), true); 
                    oneHitCleanStreak+=1;
                }

            }
            else {
                player.displayClientMessage(Component.literal("§aClean!"), true);
                oneHitCleanStreak=0;
            }

        } else if (currentSweeps >= requiredSweeps * 0.75) {
            if (timeLeft > 1){
                player.displayClientMessage(Component.literal("§eAlmost there! Only " + timeLeft + " sweeps left!"), true);
            }
            else {
                player.displayClientMessage(Component.literal("§eAlmost there! Only " + timeLeft + " sweep left!"), true); 
            }
        } else if (currentSweeps >= requiredSweeps * 0.5) {
            if (timeLeft > 1){
                player.displayClientMessage(Component.literal("§6Still dirty! " + timeLeft + " sweeps left."), true);
            }
            else {
                player.displayClientMessage(Component.literal("§6Still dirty! " + timeLeft + " sweep left."), true);
            }
        } else {
            player.displayClientMessage(Component.literal("§cThis might take a while. " + timeLeft + " sweeps left."), true);
        }

        player.swing(context.getHand(), true);  // Makes the player swing their arm as if attacking
        context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));

         // If all sweeps are complete, remove the block and give the loot
        if (currentSweeps >= requiredSweeps) {
            //Should only remove the block when done
            // Remove the block
            level.setBlock(context.getClickedPos(), Blocks.AIR.defaultBlockState(), 1 | 2);
            // Fetch and run the loot table
            LootTable lootTable = level.getServer().getLootTables().get(lootTableLocation);
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

            // Remove the block from the progress map once cleaned
            if (sweepingProgressMap.containsKey(pos)){
                sweepingProgressMap.remove(pos);
            }

        }
    }


    // Method to calculate the number of sweeps required based on block and broom tiers
    private int calculateSweepsRequired(int blockTier, int broomTier, int baseSweeps) {
        // If the broom's tier is lower, multiply the sweeps for each tier difference
        if (broomTier < blockTier) {
            int tierDifference = blockTier - broomTier;
            return baseSweeps * (int) Math.pow(3, tierDifference);  // Multiply by 3 for each tier difference
        } // If the broom's tier is higher, subtract the block's tier from the broom's tier and reduce the base sweep count by that amount (e.g., a tier 3 broom used on a tier 1 block reduces the required sweeps by 2, turning a 3-sweep block into a 1-sweep block)
        else if (broomTier > blockTier){
            int tierDifference = broomTier-blockTier;
            return baseSweeps-tierDifference;
        }
        return baseSweeps;
    }

    // Retrieves the broom tier based on the item in hand (tool tier)
    private int getTierFromTool(ItemStack tool) {
        // Check if the tool has the tag for broom tier 1
        if (tool.is(ItemTags.create(new ResourceLocation("ulsmsd", "broomtiers/broomtier1")))) {
            return 1;
        }
        // Check if the tool has the tag for broom tier 2
        else if (tool.is(ItemTags.create(new ResourceLocation("ulsmsd", "broomtiers/broomtier2")))) {
            return 2;
        }
        // Check if the tool has the tag for broom tier 3
        else if (tool.is(ItemTags.create(new ResourceLocation("ulsmsd", "broomtiers/broomtier3")))) {
            return 3;
        }
        // Default to tier 1 if no tag is found
        return 1;
    }

    private int getTierFromBlock(BlockState state) {
        // The block has a tag that gives the tier level (1, 2, 3)
        if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweeptier_1")))) {
            return 1;
        } else if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweeptier_2")))) {
            return 2;
        } else if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweeptier_3")))) {
            return 3;
        }
        return 1;  // Default to tier 1 if not found
    }

    private int getSweepAmountFromBlock(BlockState state) {
        // Loop through possible sweep amounts (up to any given number)
        for (int i = 1; i <= 20; i++) {  // Adjust this upper limit as needed
            ResourceLocation tag = new ResourceLocation("ulsmsd", "sweeptimetags/sweep_" + i + "_sweeps");
            if (state.is(BlockTags.create(tag))) {
                return i;  // Return the sweep amount based on the tag
            }
        }
        
        // Default to 1 sweep if no tag is found
        return 1;
    }

    


}