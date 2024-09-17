package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;


/**
 * Registers this mod's {@link Item}s.
 *
 * @author Untrustedlife
 */
public class MinecraftSweepingDetailItems {
    public static final TagKey<Item> ULSZS_GARBAGE_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MinecraftSweepingDetail.MODID, "sweepgarbage"));
    // Set up the DeferredRegister for items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftSweepingDetail.MODID);
    // Register your custom broom item
    public static final RegistryObject<Item> BROOM = ITEMS.register("straw_broom",
        () -> new BroomItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(100),200,20,0));

    public static final RegistryObject<Item> BUNDLESTICKS = ITEMS.register("bundle_of_sticks",
        () -> new BroomItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(15),350,30,-2));
    // Create a custom Creative Mode Tab
    public static final CreativeModeTab SWEEPING_TAB = new CreativeModeTab("sweepingtab") {
        @Override
        public ItemStack makeIcon() {
            // Set the item icon for the tab (using the broom as an example)
            return new ItemStack(BROOM.get());
        }
    };

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}