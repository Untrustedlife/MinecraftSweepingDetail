package untrustedlife.mods.minecraftsweepingdetail;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import untrustedlife.mods.minecraftsweepingdetail.items.MinecraftSweepingDetailItems;

@Mod(MinecraftSweepingDetail.MODID)
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
    }

    @SubscribeEvent
    public void onSetupEvent(final FMLClientSetupEvent event) {
    }


}