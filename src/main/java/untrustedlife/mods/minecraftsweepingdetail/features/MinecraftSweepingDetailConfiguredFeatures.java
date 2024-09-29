package untrustedlife.mods.minecraftsweepingdetail.features;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;
import untrustedlife.mods.minecraftsweepingdetail.blocks.MinecraftSweepingDetailBlocks;
import untrustedlife.mods.minecraftsweepingdetail.blocks.TrashBaseBlock;

public class MinecraftSweepingDetailConfiguredFeatures {

    // Correctly parameterized generic type
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
    DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, MinecraftSweepingDetail.MODID);

    public static SimpleWeightedRandomList<BlockState> createTier0DumpWeights() {
        SimpleWeightedRandomList.Builder<BlockState> tier_0_dump_weights = SimpleWeightedRandomList.<BlockState>builder();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            // Add lower-tier trash blocks (higher weight)
            tier_0_dump_weights.add(
                MinecraftSweepingDetailBlocks.TRASH_BLOCK.get()
                    .defaultBlockState()
                    .setValue(TrashBaseBlock.FACING, direction)  // Set the direction
                    .setValue(TrashBaseBlock.VARIANT, 2), 80);  // Set the variant and weight
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            tier_0_dump_weights.add(
                MinecraftSweepingDetailBlocks.TRASH_BLOCK.get()
                    .defaultBlockState()
                    .setValue(TrashBaseBlock.FACING, direction)  // Set the direction
                    .setValue(TrashBaseBlock.VARIANT, 1), 30);  // Set the variant and weight
        }
        return tier_0_dump_weights.build();
    }


    // RegistryObject for  trash dump feature, now with properly parameterized types
    public static final RegistryObject<ConfiguredFeature<RandomPatchConfiguration, ?>> TRASH_DUMP_1 =
        CONFIGURED_FEATURES.register("trash_tier_0_dump",
            () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(96,6,3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(
                    new WeightedStateProvider(createTier0DumpWeights())),
                BlockPredicate.allOf(
                BlockPredicate.solid(new Vec3i(0,-1,0)),
                BlockPredicate.not(BlockPredicate.solid(new Vec3i(0,0,0))),
                BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(0, 1, 0), MinecraftSweepingDetailBlocks.TRASH_BLOCK.get())),
                BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), MinecraftSweepingDetailBlocks.TRASH_BLOCK.get())),
                BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(0, 0, 0), Blocks.WATER))
                )))));
            // Method to register all features with the event bus
    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus); // Register Configured Features
    }


}
