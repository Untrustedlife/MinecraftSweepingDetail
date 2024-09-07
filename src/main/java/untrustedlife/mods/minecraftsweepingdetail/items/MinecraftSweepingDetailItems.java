package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;

@Mod.EventBusSubscriber(modid = MinecraftSweepingDetail.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftSweepingDetailItems {
    // Set up the DeferredRegister for items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftSweepingDetail.MODID);

    // Register your custom broom item
    public static final RegistryObject<Item> BROOM = ITEMS.register("straw_broom",
        () -> new BroomItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(250)));

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