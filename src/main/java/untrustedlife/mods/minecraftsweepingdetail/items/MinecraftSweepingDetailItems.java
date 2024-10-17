package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
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


    /*Actual Brooms */
    public static final RegistryObject<Item> BROOM = ITEMS.register("straw_broom",
        () -> new BroomItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(100),200,20,0));
    
    public static final RegistryObject<Item> DUST_PAN = ITEMS.register("dust_pan",
        () -> new DustPanItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(32),200,0));

    public static final RegistryObject<Item> BUNDLESTICKS = ITEMS.register("bundle_of_sticks",
        () -> new BroomItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(15),350,30,-2));
    
    /*Useful Items */
    public static final RegistryObject<Item> ICICLE = ITEMS.register("icicle",
    () -> new BroomSword(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(30),0,30,1,List.of(
        new GarbageEffectData(MobEffects.MOVEMENT_SLOWDOWN, 200, 0, 1f)
    ),true));

    public static final RegistryObject<Item> FROST_SWORD = ITEMS.register("frost_sword",
    () -> new BroomSword(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB).durability(150),0,30,2,List.of(
        new GarbageEffectData(MobEffects.MOVEMENT_SLOWDOWN, 400, 1, 1f)
    ),false));

    public static final RegistryObject<Item> EMERALD_SHARD = ITEMS.register("emerald_shard",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB)));

    public static final RegistryObject<Item> DIAMOND_SHARD = ITEMS.register("diamond_bit",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB)));
    
    public static final RegistryObject<Item> CLEANING_SHARD = ITEMS.register("cleaning_shard",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB)));

    public static final RegistryObject<Item> CLEANING_EMERALD = ITEMS.register("cleaning_emerald",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB)));

    public static final RegistryObject<Item> FROST_ROOT = ITEMS.register("frost_root",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 3, 0.7f, false, List.of(
        new GarbageEffectData(MobEffects.MOVEMENT_SPEED, 400, 1, 1f)
    )));

    public static final RegistryObject<Item> HEART_ROOT = ITEMS.register("heart_root",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 4, 0.8f, false, List.of(
        new GarbageEffectData(MobEffects.HEAL, 1, 0, 1f)
    )));

    public static final RegistryObject<Item> ROCK_ROOT = ITEMS.register("rock_root",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 4, 0.8f, false, List.of(
        new GarbageEffectData(MobEffects.DAMAGE_RESISTANCE, 400, 0, 1f)
    )));

    public static final RegistryObject<Item> FROST_BERRY = ITEMS.register("frost_berries",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 3, 0.8f, false, List.of(
        new GarbageEffectData(MobEffects.SATURATION, 4, 0, 1f)
    )));

    public static final RegistryObject<Item> PLAIN_ICECREAM = ITEMS.register("plain_icecream",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 8, 1.2f, false, List.of(
        new GarbageEffectData(MobEffects.FIRE_RESISTANCE, 200, 0, 1f)
    )));

    public static final RegistryObject<Item> FROST_BERRY_ICECREAM = ITEMS.register("frost_berry_icecream",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 8, 1.2f, false, List.of(
        new GarbageEffectData(MobEffects.SATURATION, 6, 0, 1f)
    )));

    public static final RegistryObject<Item> ROCK_ROOT_ICECREAM = ITEMS.register("rock_root_icecream",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 8, 1.4f, false, List.of(
        new GarbageEffectData(MobEffects.DAMAGE_RESISTANCE, 800, 0, 1f)
    )));

    public static final RegistryObject<Item> FROST_ROOT_ICECREAM = ITEMS.register("frost_root_icecream",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 8, 1.2f, false, List.of(
        new GarbageEffectData(MobEffects.MOVEMENT_SPEED, 800, 1, 1f)
    )));

    public static final RegistryObject<Item> HEART_ROOT_ICECREAM = ITEMS.register("heart_root_icecream",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 10, 1.2f, false, List.of(
        new GarbageEffectData(MobEffects.HEAL, 1, 0, 1f)
    )));


    //with sprinkles
    public static final RegistryObject<Item> PLAIN_ICECREAM_SPRINKLES = ITEMS.register("plain_icecream_sprinkles",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 11, 1.3f, false, List.of(
        new GarbageEffectData(MobEffects.FIRE_RESISTANCE, 100, 0, 1f)
    )));

    public static final RegistryObject<Item> FROST_BERRY_ICECREAM_SPRINKLES = ITEMS.register("frost_berry_icecream_sprinkles",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 11, 1.3f, false, List.of(
        new GarbageEffectData(MobEffects.SATURATION, 8, 0, 1f)
    )));

    public static final RegistryObject<Item> ROCK_ROOT_ICECREAM_SPRINKLES = ITEMS.register("rock_root_icecream_sprinkles",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 11, 1.5f, false, List.of(
        new GarbageEffectData(MobEffects.DAMAGE_RESISTANCE, 1060, 0, 1f)
    )));

    public static final RegistryObject<Item> FROST_ROOT_ICECREAM_SPRINKLES = ITEMS.register("frost_root_icecream_sprinkles",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 11, 1.3f, false, List.of(
        new GarbageEffectData(MobEffects.MOVEMENT_SPEED, 1060, 1, 1f)
    )));

    public static final RegistryObject<Item> HEART_ROOT_ICECREAM_SPRINKLES = ITEMS.register("heart_root_icecream_sprinkles",
    () -> new BaseGarbageItem(new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB), 13, 1.3f, false, List.of(
        new GarbageEffectData(MobEffects.HEAL, 1, 1, 1f)
    )));

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