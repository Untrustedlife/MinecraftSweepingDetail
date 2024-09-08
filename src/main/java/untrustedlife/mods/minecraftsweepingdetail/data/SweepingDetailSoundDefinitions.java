package untrustedlife.mods.minecraftsweepingdetail.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.fml.common.Mod;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;
import untrustedlife.mods.minecraftsweepingdetail.sounds.SweepingDetailSoundRegistry;


@Mod.EventBusSubscriber(modid = MinecraftSweepingDetail.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SweepingDetailSoundDefinitions extends SoundDefinitionsProvider {
    public SweepingDetailSoundDefinitions(DataGenerator output, ExistingFileHelper helper) {
		super(output, MinecraftSweepingDetail.MODID, helper);
	}

	@Override
	public void registerSounds() {
		add(
				SweepingDetailSoundRegistry.SWEEP_SOUND,
				definition()
                .with(modSound("sweep_sound"))
                .subtitle("sweep")
		);
	}

	private SoundDefinition.Sound modSound(String name) {
		return sound(new ResourceLocation(MinecraftSweepingDetail.MODID, name));
	}
}
