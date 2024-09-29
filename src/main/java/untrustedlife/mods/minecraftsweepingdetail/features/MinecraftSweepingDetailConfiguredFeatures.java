package untrustedlife.mods.minecraftsweepingdetail.features;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;
import untrustedlife.mods.minecraftsweepingdetail.blocks.MinecraftSweepingDetailBlocks;

public class MinecraftSweepingDetailConfiguredFeatures {

    // Correctly parameterized generic type
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
    DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, MinecraftSweepingDetail.MODID);

    // RegistryObject for your trash dump feature, now with properly parameterized types
    public static final RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, ?>> TRASH_DUMP_1 =
        CONFIGURED_FEATURES.register("trash_tier_0_dump",
            () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(MinecraftSweepingDetailBlocks.TRASH_BLOCK.get()))));


            // Method to register all features with the event bus
    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus); // Register Configured Features
    }

}
