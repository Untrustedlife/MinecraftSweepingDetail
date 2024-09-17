package untrustedlife.mods.minecraftsweepingdetail.items;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

/**
 * This class represents a custom food item (Usually found when sweeping) that can be consumed by players in the game.
 * It supports dynamic food properties, such as nutrition, saturation, meat type, and custom effects.
 * @author Untrustedlife
 */
public class BaseGarbageItem extends Item {

     /**
     * Constructs a new {@code BaseGarbageItem}.
     *
     * @param properties The general properties of the item, such as stack size or rarity.
     * @param nutrition  The amount of hunger restored by consuming this item.
     * @param saturation The saturation modifier, affecting how long the player remains full after consuming the item.
     * @param meat       If {@code true}, the item is considered meat, and will be recognized as such by in-game mechanics (e.g., wolves eating it).
     * @param effects    A list of {@code GarbageEffectData} objects, each representing a custom effect with a duration, amplifier, and chance of applying to the player when consumed.
     */
    public BaseGarbageItem(Properties properties, int nutrition, float saturation, boolean meat, List<GarbageEffectData> effects) {
        super(properties.food(createFoodProperties(nutrition,saturation,meat,effects)));
    }
    
    /**
     * Creates and configures the food properties for this item, including nutrition, saturation, meat status, 
     * and any custom effects that should apply when the item is consumed.
     *
     * @param nutrition  The amount of hunger restored by consuming the item.
     * @param saturation The saturation modifier, affecting how long the player remains full after consuming the item.
     * @param meat       If {@code true}, the item is considered meat, triggering specific in-game behaviors (e.g., wolves eating the item).
     * @param effects    A list of {@code GarbageEffectData} objects, representing custom effects that apply when the item is consumed.
     * @return A {@code FoodProperties} object, which holds all the food-related properties of the item.
     */
    private static FoodProperties createFoodProperties(int nutrition, float saturation, boolean meat,List<GarbageEffectData> effects) {
        FoodProperties.Builder prop = new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationMod(saturation);
        if (meat){
            prop.meat();
        }

        for (GarbageEffectData effectData : effects) {
            prop.effect(() -> new MobEffectInstance(effectData.getEffect(), effectData.getDuration(), effectData.getAmplifier()), effectData.getChance());
       
        }

        return prop.build();
    }
}