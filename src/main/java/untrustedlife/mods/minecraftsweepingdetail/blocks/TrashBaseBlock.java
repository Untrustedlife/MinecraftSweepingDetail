package untrustedlife.mods.minecraftsweepingdetail.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;

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

    /**
     * Constructor for TrashBaseBlock. 
     * Sets the block material to DIRT, with low strength and grass-like sound.
     */
    public TrashBaseBlock() {
        super(Properties.of(Material.DIRT)
                .strength(50.0F, 1.0F)  // Tough to break manually, but easily destroyed by explosions to encourage using a broom
                .sound(SoundType.GRASS)); // Grass sound type
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * Defines the block state properties, in this case, only FACING.
     * 
     * @param builder The state definition builder used to add properties for the block.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING); // Add the FACING property to allow block rotation
    }

    /**
     * Determines the default facing direction when the block is placed.
     * The block will face the opposite of the player's facing direction.
     * 
     * @param context Provides information about the placement event such as player and position.
     * @return The BlockState with the appropriate FACING value.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
        return Block.box(0, 0, 0, 16, 1, 16);  // 1 pixel high block shape
    }
}