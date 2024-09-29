package untrustedlife.mods.minecraftsweepingdetail;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import untrustedlife.mods.minecraftsweepingdetail.blocks.MinecraftSweepingDetailBlocks;
import untrustedlife.mods.minecraftsweepingdetail.features.MinecraftSweepingDetailConfiguredFeatures;
import untrustedlife.mods.minecraftsweepingdetail.features.MinecraftSweepingDetailPlacedFeatures;
import untrustedlife.mods.minecraftsweepingdetail.items.MinecraftSweepingDetailItems;
import untrustedlife.mods.minecraftsweepingdetail.sounds.SweepingDetailSoundRegistry;

/**
 * Actually creates the mod.
 *
 * @author Untrustedlife
 */
@Mod(MinecraftSweepingDetail.MODID)
@Mod.EventBusSubscriber(modid = MinecraftSweepingDetail.MODID, bus = Bus.MOD)
public class MinecraftSweepingDetail {
    public static final String MODID = "ulsmsd";
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public MinecraftSweepingDetail() {
        // Register the setup method for modloading
        LOGGER.info("MinecraftSweepingDetail loaded.");
         IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
         bus.addListener(this::onSetupEvent);
         
        MinecraftSweepingDetailItems.register(bus);
        LOGGER.info("Items loaded.");

        MinecraftSweepingDetailBlocks.register(bus);
        LOGGER.info("Blocks loaded.");

        SweepingDetailSoundRegistry.initialise(bus);
        LOGGER.info("Sounds loaded.");

        MinecraftSweepingDetailConfiguredFeatures.register(bus);
        LOGGER.info("Configured Feature loaded.");

        MinecraftSweepingDetailPlacedFeatures.register(bus);
        LOGGER.info("Placed Feature loaded.");


    }

    public void onSetupEvent(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup event triggered");
    }

}