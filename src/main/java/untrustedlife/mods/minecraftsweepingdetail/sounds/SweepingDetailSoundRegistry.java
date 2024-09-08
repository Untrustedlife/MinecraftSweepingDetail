package untrustedlife.mods.minecraftsweepingdetail.sounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link SoundEvent}s.
 *
 * @author Untrustedlife
 */
public class SweepingDetailSoundRegistry {
	private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MinecraftSweepingDetail.MODID);
	private static boolean isInitialised;
	public static final RegistryObject<SoundEvent> SWEEP_SOUND = registerSoundEvent("sweep_sound");


	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}
		SOUND_EVENTS.register(modEventBus);
		isInitialised = true;
	}

	/**
	 * Registers a sound event.
	 *
	 * @param soundName The sound event's name, without the ulsmsd prefix
	 * @return A RegistryObject reference to the SoundEvent
	 */
	private static RegistryObject<SoundEvent> registerSoundEvent(final String soundName) {
		return SOUND_EVENTS.register(soundName, () -> new SoundEvent(new ResourceLocation(MinecraftSweepingDetail.MODID, soundName)));
	}
}