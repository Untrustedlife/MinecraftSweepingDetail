package untrustedlife.mods.minecraftsweepingdetail.items;
import java.util.List;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import untrustedlife.mods.minecraftsweepingdetail.UntrustedDiceRolling;

/**
 * A custom sword that extends {@link BroomItem}, providing standard sword functionality 
 * while applying additional effects to enemies based on a chance system.
 * The effects are defined in a list of {@link GarbageEffectData} objects, 
 * each containing a status effect, duration, amplifier, and chance.
 * @author Untrustedlife
 */
public class BroomSword extends BroomItem {

    /**
     * A list of {@link GarbageEffectData} instances that define the effects applied to enemies
     * when they are hit with this sword.
     */
    List<GarbageEffectData> myEffects;

    /**
     * Constructs a {@code BroomSword} with the specified properties and a list of effects.
     *
     * @param properties              The item properties (durability, tab, etc.).
     * @param burnTimeInTicks         The burn time of the item in ticks when used as fuel.
     * @param sweepUseTimeInTicks     The time it takes to sweep in ticks.
     * @param bonusDamage             Additional damage dealt by the broom sword.
     * @param effects                 A list of {@link GarbageEffectData} objects that define
     *                                the effects this sword will apply to enemies when attacking.
     */
    public BroomSword(Properties properties, int burnTimeInTicks, int sweepUseTimeInTicks, int bonusDamage,List<GarbageEffectData> effects, boolean canSweep) {
        super(properties, burnTimeInTicks, sweepUseTimeInTicks, bonusDamage);
        this.myEffects=effects;
        this.canSweep=canSweep;
    }
 
    /**
     * Constructs a {@code BroomSword} with custom attack speed, bonus damage, and a list of effects.
     * The base attack speed is adjusted relative to -2.4, which is the default attack speed for a wooden sword.
     * This allows you to create brooms with different attack speeds relative to standard weapons.
     *
     * @param properties              The item properties (durability, tab, etc.).
     * @param burnTimeInTicks         The burn time of the broom sword in ticks when used as fuel.
     * @param sweepUseTimeInTicks     The time it takes to sweep in ticks.
     * @param bonusDamage             Additional damage dealt by the broom sword in combat.
     * @param attackSpeed             The custom attack speed, relative to the base value of -2.4 (equal to a wooden sword).
     *                                Positive values increase the speed, negative values make it slower.
     * @param effects                 A list of {@link GarbageEffectData} that define the effects applied to enemies
     *                                when the broom sword hits a target.
     */
    public BroomSword(Properties properties, int burnTimeInTicks, int sweepUseTimeInTicks, int bonusDamage, float attackSpeed, List<GarbageEffectData> effects, boolean canSweep) {
        super(properties, burnTimeInTicks, sweepUseTimeInTicks, bonusDamage, -2.4f - attackSpeed);
        this.myEffects = effects;
        this.canSweep=canSweep;
    }

    /**
     * Applies effects to the enemy when the sword hits a target.
     * For each {@link GarbageEffectData} in the {@code myEffects} list, a dice roll is performed to
     * determine whether the effect is applied to the target. If the dice roll is less than or equal
     * to the chance, the effect is applied with the specified duration and amplifier.
     *
     * @param stack    The item stack of the sword.
     * @param target   The living entity that is being attacked.
     * @param attacker The entity using the sword to attack.
     * @return {@code true} if the sword successfully damaged the enemy.
    */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Loop through the list of effects and apply each one to the target based on chance
        for (GarbageEffectData effectData : myEffects) {
            // Roll the dice: if the result is less than or equal to the chance, apply the effect
            int diceRoll = UntrustedDiceRolling.rollDice(100);  // Rolls between 1 and 100
            if (diceRoll <= effectData.getChance() * 100) {     // Multiply chance by 100 for percentage logic
                target.addEffect(new MobEffectInstance(
                    effectData.getEffect(),       // The MobEffect (e.g., Slowness, Poison, etc.)
                    effectData.getDuration(),     // The duration in ticks
                    effectData.getAmplifier()     // The amplifier level
                ));
            }
        }

        // Call the parent class method to handle the normal behavior of damaging the enemy
        return super.hurtEnemy(stack, target, attacker);
    }
}
