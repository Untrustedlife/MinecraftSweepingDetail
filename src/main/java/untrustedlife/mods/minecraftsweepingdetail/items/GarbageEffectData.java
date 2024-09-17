package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.effect.MobEffect;

/**
 * A data class that holds information about a custom effect to be applied when a food item is consumed.
 * This class includes the effect type, duration, amplifier, and chance of the effect being applied.
 * @author Untrustedlife
 */
public class GarbageEffectData {
    private final MobEffect effect;
    private final int duration;
    private final int amplifier;
    private final float chance;

    /**
     * Constructs a new {@code GarbageEffectData}.
     *
     * @param effect    The {@link MobEffect} that represents the type of effect (e.g., healing, poison).
     * @param duration  The duration of the effect in ticks. The higher the value, the longer the effect will last.
     * @param amplifier The strength of the effect. A higher amplifier means the effect will be more powerful.
     * @param chance    The probability (0.0 to 1.0) that the effect will be applied when the item is consumed. A value of 1.0 means the effect is guaranteed.
     */
    public GarbageEffectData(MobEffect effect, int duration, int amplifier, float chance) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.chance = chance;
    }

    /**
     * Gets the {@link MobEffect} associated with this data.
     *
     * @return The {@link MobEffect} that will be applied when the item is consumed.
    */
    public MobEffect getEffect() {
        return effect;
    }

    /**
     * Gets the duration of the effect in ticks.
     *
     * @return The duration in ticks for how long the effect will last once applied.
    */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets the amplifier of the effect.
     * 
     * @return The strength of the effect. Higher values mean a stronger effect.
    */
    public int getAmplifier() {
        return amplifier;
    }

    /**
     * Gets the chance that the effect will be applied.
     *
     * @return A float value between 0.0 and 1.0, representing the probability that the effect will be applied when the item is consumed.
    */
    public float getChance() {
        return chance;
    }
    
}