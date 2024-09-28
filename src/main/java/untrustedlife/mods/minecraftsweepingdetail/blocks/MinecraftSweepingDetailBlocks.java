package untrustedlife.mods.minecraftsweepingdetail.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untrustedlife.mods.minecraftsweepingdetail.MinecraftSweepingDetail;
import untrustedlife.mods.minecraftsweepingdetail.items.MinecraftSweepingDetailItems;

public class MinecraftSweepingDetailBlocks {

    // Create the DeferredRegister for Blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MinecraftSweepingDetail.MODID);

    // Register your custom block (example of a trash block)
    public static final RegistryObject<Block> TRASH_BLOCK = BLOCKS.register("trash_block_tier_1", 
        () -> new TrashBaseBlock());

    // Register the block's item form so it appears in the creative tab
    public static final RegistryObject<Item> TRASH_BLOCK_ITEM = MinecraftSweepingDetailItems.ITEMS.register("trash_block_tier_1", 
        () -> new BlockItem(TRASH_BLOCK.get(), new Item.Properties().tab(MinecraftSweepingDetailItems.SWEEPING_TAB)));

    // Register the DeferredRegister to the event bus
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}