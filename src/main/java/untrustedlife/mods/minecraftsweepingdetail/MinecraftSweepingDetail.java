package untrustedlife.mods.minecraftsweepingdetail;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftSweepingDetail.MODID)
public class MinecraftSweepingDetail {

    public static final String MODID = "ulsmsd";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MinecraftSweepingDetail() {
        // Register the setup method for modloading
        LOGGER.info("MinecraftSweepingDetail");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLClientSetupEvent event) {
    }


}