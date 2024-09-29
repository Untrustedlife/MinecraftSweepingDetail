package untrustedlife.mods.minecraftsweepingdetail.features;

import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

import java.util.List;

public class MinecraftSweepingDetailPlacedFeatures {
        public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, MinecraftSweepingDetail.MODID);


        public static final RegistryObject<PlacedFeature> TRASH_PATCH_PLACED = PLACED_FEATURES.register("add_trash_tier_0_dump",
            () ->  new PlacedFeature(
                Holder.direct(MinecraftSweepingDetailConfiguredFeatures.TRASH_DUMP_1.get()), // Link to the ConfiguredFeature
                List.of(
                    RarityFilter.onAverageOnceEvery(2),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), // Place on surface
                    BiomeFilter.biome(), // Spawn in all biomes
                    BlockPredicateFilter.forPredicate(BlockPredicate.solid(new Vec3i(0,-1,0)))
                )
            ));


    public static void register(IEventBus eventBus){
        PLACED_FEATURES.register(eventBus);
    }
}
