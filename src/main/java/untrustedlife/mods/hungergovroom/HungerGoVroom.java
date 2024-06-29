package untrustedlife.mods.hungergovroom;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HungerGoVroom.MODID)
public class HungerGoVroom {

    public static final String MODID = "hungergovroom";
    private static final Logger LOGGER = LogUtils.getLogger();

    public HungerGoVroom() {
        // Register the setup method for modloading
        LOGGER.info("Hunger Go Vroom: Thrusting passionately into your KubeJS folder...");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLClientSetupEvent event) {
        // Copy the script file to the Minecraft instance directory
        copyScriptFile();
    }

    private void copyScriptFile() {
        // Define the source and destination paths
        String minecraftDir = System.getProperty("user.dir"); // This should point to the Minecraft instance directory
        Path destinationPath = Paths.get(minecraftDir, "kubejs", "server_scripts", "HungerGoVroom.js");
        try (InputStream inputStream = getClass().getResourceAsStream("/kubejs/server_scripts/HungerGoVroom.js")) {
            if (inputStream == null) {
                throw new IOException("Script file not found in resources: /kubejs/server_scripts/HungerGoVroom.js");
            }

            Files.createDirectories(destinationPath.getParent());
            Files.copy(inputStream, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}