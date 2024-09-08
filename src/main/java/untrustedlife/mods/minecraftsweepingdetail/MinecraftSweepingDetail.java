package untrustedlife.mods.minecraftsweepingdetail;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import untrustedlife.mods.minecraftsweepingdetail.items.MinecraftSweepingDetailItems;
import untrustedlife.mods.minecraftsweepingdetail.sounds.SweepingDetailSoundRegistry;

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
        // Register items and other things with the mod event bus
        MinecraftSweepingDetailItems.register(bus);
        LOGGER.info("Items loaded.");
        SweepingDetailSoundRegistry.initialise(bus);
        LOGGER.info("Sounds loaded.");
    }

    public void onSetupEvent(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup event triggered");
    }


}