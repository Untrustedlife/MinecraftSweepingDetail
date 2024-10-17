package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import untrustedlife.mods.minecraftsweepingdetail.UntrustedDiceRolling;
import untrustedlife.mods.minecraftsweepingdetail.sounds.SweepingDetailSoundRegistry;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;

import net.minecraft.ChatFormatting;
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
    //Constants
    protected static final long STREAK_EXPIRATION_TIME = 1050L; // little over 1 second
    private static final float INSTANT_BREAK_SPEED = 99999.0F;
    protected static final int MAX_SWEEP_AMOUNT = 20;
    protected static final int MIN_COOLDOWN_TIME = 7;

    //Alterables
    protected final int burnTicks;
    protected final int sweepUseTime;

    //Arcade streak
    protected int oneHitCleanStreak = 0;
    protected long lastSweepTime = 0;
    protected boolean canSweep=true;


    public BroomItem(Properties properties,int burnTimeInTicks, int sweepUseTimeInTicks, int bonusDamage) {
         super(Tiers.WOOD, 4+bonusDamage, -2.4F,properties);
         this.burnTicks = burnTimeInTicks;
         this.sweepUseTime = sweepUseTimeInTicks;
    }

    public BroomItem(Properties properties,int burnTimeInTicks, int sweepUseTimeInTicks, int bonusDamage, float attackSpeed) {
        super(Tiers.WOOD, 4+bonusDamage, attackSpeed,properties);
        this.burnTicks = burnTimeInTicks;
        this.sweepUseTime = sweepUseTimeInTicks;
   }


    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState state) {
    if (isSweepableBlock(state)) {
        return INSTANT_BREAK_SPEED;
    }
    return super.getDestroySpeed(itemStack, state);
   }


    // Prevent normal block-breaking for sweepable blocks
    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, Player player) {
        Level level = player.getLevel();
        BlockState state = level.getBlockState(pos);
        if (isSweepableBlock(state)) {
            // Return true to prevent the block from breaking
            return true;
        }
        // Allow normal block-breaking for non-sweepable blocks
        return super.onBlockStartBreak(itemStack, pos, player);
    }



    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (isSweepableBlock(level.getBlockState(pos))) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return false; // Prevent sweeping if the item is on cooldown
            }
            else {
                InteractionHand hand = InteractionHand.MAIN_HAND; // or off-hand if applicable
                BlockHitResult hitResult = new BlockHitResult(player.getLookAngle(), player.getDirection(), pos, false);
                UseOnContext context = new UseOnContext(player, hand, hitResult);
                // Reuse the logic from useOn by calling it directly
                InteractionResult result = useOn(context);
                if (level.isClientSide){
                    generateSweepParticles(player,level,context);
                }
                // If sweeping was successful, return true to prevent block breaking
                if (result == InteractionResult.SUCCESS && !level.isClientSide) {
                    if (oneHitCleanStreak <= 2){
                        player.getCooldowns().removeCooldown(this);
                        player.getCooldowns().addCooldown(this, Math.max(MIN_COOLDOWN_TIME,sweepUseTime-2));
                    }
                    return false; // Cancels normal block breaking
                }
            }
        }
        // If the block isn't sweepable, return true to allow block breaking
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player != null) {
            if (isSweepableBlock(level.getBlockState(context.getClickedPos()))) {
                if (level.isClientSide){
                    generateSweepParticles(player,level,context);
                }
                if (!level.isClientSide){
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SweepingDetailSoundRegistry.SWEEP_SOUND.get(), SoundSource.PLAYERS, 0.15f+(float)UntrustedDiceRolling.generateNormalizedValueBetween(0.07), 1.5f+((float)UntrustedDiceRolling.generateRangeNegativeOneToOne()/2));
                    RunSweepRoutineBasedOnTags(level,player,context);
                    player.getCooldowns().addCooldown(this, Math.max(MIN_COOLDOWN_TIME,sweepUseTime-oneHitCleanStreak));
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

    // Helper method to check sweepable block
    private boolean isSweepableBlock(BlockState state) {
        if (!canSweep){
            return false;
        }
        return state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweepable")));
    }

    //TODO: Possibly use tags to change this too
    protected void generateSweepParticles( Player player, Level level , UseOnContext context){
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
            level.addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0, 0.5F, 0.1F), 1.0F), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }
    
    protected void RunSweepRoutineBasedOnTags(Level level, Player player, UseOnContext context) {
        BlockState state = level.getBlockState(context.getClickedPos());
        
        // Use a data-driven approach to map sweep types to their corresponding loot tables
        Map<String, ResourceLocation> sweepTypeLootMap = Map.of(
            "sweep_string", new ResourceLocation("minecraft:blocks/cobweb"),
            "sweep_dirt", new ResourceLocation("ulsmsd:blocks/dirt_sweeping"),
            "sweep_sand", new ResourceLocation("ulsmsd:blocks/sand_sweeping"),
            "sweep_snow", new ResourceLocation("ulsmsd:blocks/snow_sweeping"),
            "sweep_podzol", new ResourceLocation("ulsmsd:blocks/podzol_sweeping"),
            "sweep_mycelium", new ResourceLocation("ulsmsd:blocks/mycelium_sweeping"),
            "sweep_tier1_trash", new ResourceLocation("ulsmsd:blocks/tier1_trash_sweeping"),
            "sweep_tier2_trash", new ResourceLocation("ulsmsd:blocks/tier2_trash_sweeping"),
            "sweep_tier3_trash", new ResourceLocation("ulsmsd:blocks/tier3_trash_sweeping"),
            "sweep_slime", new ResourceLocation("ulsmsd:blocks/slime_sweeping")
            // Add more sweep types and their corresponding loot tables here
        );
        
        // Find the sweep type from block tags
        Optional<String> sweepType = getSweepTypeFromState(state);
        if (sweepType.isPresent() && sweepTypeLootMap.containsKey(sweepType.get())) {
            processSweeping(level, player, context, sweepTypeLootMap.get(sweepType.get()));
        }
    }

    // Helper method to get the sweep type from block state using tags
    protected Optional<String> getSweepTypeFromState(BlockState state) {
        for (String sweepType : Arrays.asList("sweep_string", "sweep_dirt","sweep_sand","sweep_snow","sweep_podzol","sweep_mycelium","sweep_tier1_trash","sweep_tier2_trash","sweep_tier3_trash",
        "sweep_slime")) {
            if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptypetags/" + sweepType)))) {
                return Optional.of(sweepType);
            }
        }
        return Optional.empty();
    }

    //Helper method that informs player of progress
    protected void tellPlayerSweepInfo(int currentSweeps, int requiredSweeps, Player player){
        int timeLeft = Math.max(1, requiredSweeps - currentSweeps);
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
                if (oneHitCleanStreak > 0){
                    oneHitCleanStreak+=1;
                    player.displayClientMessage(Component.literal("§aClean! X"+oneHitCleanStreak), true); 
                }
                else {
                    player.displayClientMessage(Component.literal("§aClean!"), true);
                    oneHitCleanStreak+=1;
                }
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

    }

    // Map stores the block position and a pair of sweeping progress and block state.
    protected final Map<BlockPos, Pair<Integer, BlockState>> sweepingProgressMap = new HashMap<>();
    protected BlockPos lastSweptBlockPos = null;  // Tracks the last block position that was swept
    protected boolean wasUsingDustpan = false;   // Tracks whether the dustpan was used during sweeping

    
    // Helper method to process the sweeping and run the loot table
    protected void processSweeping(Level level, Player player, UseOnContext context, ResourceLocation lootTableLocation) {
        //This is all very messy and needs to be seperated out into other functions
        BlockPos pos = context.getClickedPos();
        BlockState currentState = level.getBlockState(pos);

        
        /*DUST PAN LOGIC***********************************************************************************************/
        // Check if the dustpan was equipped during sweeping and is now unequipped or changed mid-sweep
        ItemStack offHandItem = player.getOffhandItem();
        boolean unequippedDustpan = !(offHandItem.getItem() instanceof DustPanItem);
        if (wasUsingDustpan && lastSweptBlockPos != null && ((sweepingProgressMap.containsKey(lastSweptBlockPos) && unequippedDustpan) ||
         (lastSweptBlockPos != null && !lastSweptBlockPos.equals(pos)))) {
            Pair<Integer, BlockState> progressData = sweepingProgressMap.get(lastSweptBlockPos);
            Block block = progressData.getSecond().getBlock();
            String reason = unequippedDustpan ?  "[Unequipped the dustpan]" : "[Switched blocks]";
            Component blockName = Component.translatable(block.getDescriptionId()).withStyle(ChatFormatting.DARK_RED); 
            player.displayClientMessage(Component.literal("§4You dumped the ").append(blockName).append("§4, resetting progress! ").append(Component.literal(reason).withStyle(ChatFormatting.GRAY)), true);
            sweepingProgressMap.clear();  // Clear all progress if sweeping is interrupted
            lastSweptBlockPos = null;
            wasUsingDustpan = false;  // Reset the dustpan usage tracker
            return;  // Exit early
        }
        /**************************************************************************************************************/

        // Retrieve broom and block tiers
        int broomTier = getTierFromTool(context.getItemInHand());
        int blockTier = getTierFromBlock(currentState);
        lastSweptBlockPos = pos;

        // Determine the required number of sweeps for this block and broom combo
        int baseSweeps = getSweepAmountFromBlock(currentState); 
        int requiredSweeps = calculateSweepsRequired(blockTier, broomTier, baseSweeps);

        /*DUST PAN LOGIC***********************************************************************************************/
        // Modify sweeps if the dustpan is held in the off-hand
        if (offHandItem.getItem() instanceof DustPanItem) {
            // If the required sweeps is an odd number, we add +1 to make the dustpan's reduction feel 
            // more intuitive. For odd numbers (e.g., 3), the dustpan would reduce the number of sweeps 
            // by half (e.g., 3 -> 1 (Integer division is fun lol), despite feeling like it should be 2). By rounding the odd number 
            // up to the nearest even number (e.g., 3 -> 4), the reduction results in a fairer halving 
            // (4 -> 2). This ensures a more balanced and predictable sweeping experience.
            if (requiredSweeps % 2 != 0) {
                requiredSweeps += 1;
            }
            requiredSweeps = ((DustPanItem) offHandItem.getItem()).getModifiedSweeps(requiredSweeps, player);
            wasUsingDustpan = true;  // Mark that the dustpan was being used
        }
        /**************************************************************************************************************/

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
        sweepingProgressMap.put(pos, Pair.of(currentSweeps, currentState));

        //Core streak logic, the higher the streak the faster you sweep!
        long currentTime = System.currentTimeMillis();
        if (currentTime-lastSweepTime > STREAK_EXPIRATION_TIME){
            oneHitCleanStreak=0;
        }
        lastSweepTime=currentTime;

        //Text to tell player whats left for their sweeping
        tellPlayerSweepInfo(currentSweeps,requiredSweeps,player);

        player.swing(context.getHand(), true);
        context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));

         // If all sweeps are complete, remove the block and give the loot
        if (currentSweeps >= requiredSweeps) {
            //THis should be fine, because if the player rmeoves it their progress will get totally reset anyway. 
            if (offHandItem.getItem() instanceof DustPanItem) {
                offHandItem.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.OFF_HAND));
            }
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
                lastSweptBlockPos=null;
            }

        }
    }

    // Method to calculate the number of sweeps required based on block and broom tiers
    protected int calculateSweepsRequired(int blockTier, int broomTier, int baseSweeps) {
        if (broomTier < blockTier) {
            int tierDifference = blockTier - broomTier;
            // return baseSweeps * (int) Math.pow(3, tierDifference);  // Multiply by 3 for each tier difference (2 tier difference ended up being 27)
            return baseSweeps * (tierDifference*3);  // Multiply by 3 for each tier difference
        } // If the broom's tier is higher, subtract the block's tier from the broom's tier and reduce the base sweep count by that amount (e.g., a tier 3 broom used on a tier 1 block reduces the required sweeps by 2, turning a 3-sweep block into a 1-sweep block)
        else if (broomTier > blockTier){
            int tierDifference = broomTier-blockTier;
            return Math.max(1, baseSweeps - tierDifference);
        }
        return baseSweeps;
    }

    // Retrieves the broom tier based on the item in hand (tool tier)
    protected int getTierFromTool(ItemStack tool) {
        if (tool.is(ItemTags.create(new ResourceLocation("ulsmsd", "broomtiers/broomtier1")))) {
            return 1;
        }
        else if (tool.is(ItemTags.create(new ResourceLocation("ulsmsd", "broomtiers/broomtier2")))) {
            return 2;
        }
        else if (tool.is(ItemTags.create(new ResourceLocation("ulsmsd", "broomtiers/broomtier3")))) {
            return 3;
        }
        // Default to tier 1 if no tag is found
        return 1;
    }

    // Retrieves the block tier based on the block tags
    protected int getTierFromBlock(BlockState state) {
        if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweeptier_1")))) {
            return 1;
        } else if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweeptier_2")))) {
            return 2;
        } else if (state.is(BlockTags.create(new ResourceLocation("ulsmsd", "sweeptiertags/sweeptier_3")))) {
            return 3;
        }
        return 1;  // Default to tier 1 if not found
    }

    protected int getSweepAmountFromBlock(BlockState state) {
        // Loop through possible sweep amounts
        for (int i = 1; i <= MAX_SWEEP_AMOUNT; i++) {  // Adjust this upper limit as needed
            ResourceLocation tag = new ResourceLocation("ulsmsd", "sweeptimetags/sweep_" + i + "_sweeps");
            if (state.is(BlockTags.create(tag))) {
                return i;  // Return the sweep amount based on the tag
            }
        }
        // Default to 1 sweep if no tag is found
        return 1;
    }

}