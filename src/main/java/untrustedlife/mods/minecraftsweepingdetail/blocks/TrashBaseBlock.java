package untrustedlife.mods.minecraftsweepingdetail.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.VoxelShape;
import untrustedlife.mods.minecraftsweepingdetail.UntrustedDiceRolling;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * A block representing trash, with custom rotation and mirroring logic. 
 * This block is also 1 pixel high, making it appear like a pile of trash. 
 * It can be placed and rotated based on the player's orientation and mirrors correctly.
 * 
 * @author Untrustedlife
 */
public class TrashBaseBlock extends Block {
    // Property that stores the facing direction of the block
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    // Custom variant property (can be 1 or 2)
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 1, 2);

    public String blockNameForId = "";
    /**
     * Constructor for TrashBaseBlock. 
     * Sets the block material to DIRT, with low strength and grass-like sound.
     */
    public TrashBaseBlock(String name) {
        super(Properties.of(Material.DIRT)
                .strength(50.0F, 1.0F)  // Tough to break manually, but easily destroyed by explosions to encourage using a broom
                .sound(SoundType.GRASS)
                .noOcclusion()); // Grass sound type
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(VARIANT, 2)); // Default variant 1
        this.blockNameForId = name;
    }

    /**
     * Defines the block state properties, in this case, only FACING.
     * 
     * @param builder The state definition builder used to add properties for the block.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VARIANT); // Add FACING and VARIANT properties
    }

    /**
     * Determines the default facing direction/variant when the block is placed.
     * The block will face a random direction and have a random variant.
     * 
     * @param context Provides information about the placement event such as player and position.
     * @return The BlockState with the appropriate FACING value.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int randomVariant = UntrustedDiceRolling.rollDice(100) > 60 ? 1 : 2;
    
        // Randomly select a horizontal direction (north, south, east, west)
        Direction[] horizontalDirections = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction randomDirection = UntrustedDiceRolling.choose(horizontalDirections);
        return this.defaultBlockState()
            .setValue(FACING, randomDirection)  // Set the facing direction to the random direction
            .setValue(VARIANT, randomVariant);  // Set the variant to the random variant
    }

    /**
     * Rotates the block state when the player rotates the block or when the block is placed in a rotated structure.
     * 
     * @param state The current state of the block.
     * @param rotation The rotation that should be applied.
     * @return The new BlockState with the updated FACING property.
     */
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    /**
     * Mirrors the block state along the specified axis by first converting the mirror action into a rotation
     * and then calling the rotate method.
     * 
     * @param state The current state of the block.
     * @param mirrorIn The mirror transformation to apply.
     * @return The new BlockState after the mirror has been applied.
     */
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        // Use the block's existing rotation logic to flip the direction and call rotate without using a deprecated method
        return this.rotate(state, mirrorIn.getRotation(state.getValue(FACING)));
    }

    /**
     * Defines the hitbox of the block. This block is 1 pixel high, giving the appearance of a thin trash layer.
     * 
     * @param state The current state of the block.
     * @param worldIn The block reader instance.
     * @param pos The position of the block in the world.
     * @param context Provides information about the context in which the shape is being queried.
     * @return The VoxelShape of the block, in this case, a thin, flat shape.
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        int height = state.getValue(VARIANT) == 1 ? 3 : 2;  // Adjust shape height based on variant
        return Block.box(0, 0, 0, 16, height, 16);  // 1 pixel high block shape
    }

    /*This was to fix block sounds when walking on em, still didnt work */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        int height = state.getValue(VARIANT) == 1 ? 3 : 2;  // Adjust shape height based on variant
        // Always return a collision shape that ensures interaction, similar to snow behavior
        return Block.box(0, 0, 0, 16, height, 16);  // Full block collision even if it's thin
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;  // Uses the shape for rendering light correctly
    }

    /*This doesn't seem necessary at all?*/
    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return false;  // Make sure it's not treated as a full block for collision
    }

    /**
     * Called when the player left-clicks (attacks) the block.
     * 
     * @param state The current state of the block.
     * @param world The level where the block resides.
     * @param pos The position of the block.
     * @param player The player who attacked the block.
     */
    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (!world.isClientSide) {
            ItemStack heldItem = player.getMainHandItem(); // Get the item in the player's main hand
            
            // Check if the player is NOT holding a broom (assuming you have a specific broom item)
            if (!isBroom(heldItem)) {
                // Check if the player hasn't seen the message yet
                String message = String.format("You should use a broom to clean this %s up!", state.getBlock().getName().getString());
                player.displayClientMessage(Component.literal(message), true);

            }
        }
    }

    /**
     * Checks if the held item is a broom by checking its tag.
     * 
     * @param stack The ItemStack to check.
     * @return True if the item is tagged as a broom, false otherwise.
     */
    private boolean isBroom(ItemStack stack) {
        // Check if the item has any of the broom tier tags
        return stack.is(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("ulsmsd", "broomtier1"))) ||
               stack.is(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("ulsmsd", "broomtier2"))) ||
               stack.is(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("ulsmsd", "broomtier3")));
    }
}