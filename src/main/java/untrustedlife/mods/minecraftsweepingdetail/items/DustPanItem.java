package untrustedlife.mods.minecraftsweepingdetail.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeType;

public class DustPanItem extends SwordItem {
    //Alterables
    protected final int burnTicks;

    public DustPanItem(Properties properties,int burnTimeInTicks, int sweepUseTimeInTicks, int bonusDamage) {
         super(Tiers.WOOD, bonusDamage, -2.4F,properties);
         this.burnTicks = burnTimeInTicks;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return this.burnTicks;
    }

    // This method applies the sweep speed reduction
    public int getModifiedSweeps(int originalSweeps, Player player) {
        if (player.getOffhandItem().getItem() instanceof DustPanItem) {
            return Math.max(originalSweeps / 2, 1); // Halve the required sweeps if using the dustpan
        }
        return originalSweeps; // No change if dustpan is not in off-hand
    }
}
