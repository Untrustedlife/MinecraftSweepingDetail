package untrustedlife.mods.minecraftsweepingdetail.items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import java.util.List;

public class BroomItem extends Item {
    public BroomItem(Properties properties) {
        super(properties);
        properties=properties.rarity(Rarity.COMMON);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (!level.isClientSide && player != null) {
            // just a test
            if (level.getBlockState(context.getClickedPos()).is(Blocks.GRASS_BLOCK)) {
                level.setBlock(context.getClickedPos(), Blocks.DIRT.defaultBlockState(), 3);
                player.playSound(SoundEvents.GRASS_PLACE, 1.0F, 1.0F);
                player.swing(context.getHand(), true);  // Makes the player swing their arm as if attacking
                Direction clickedFace = context.getClickedFace();
                BlockPos clickedPos = context.getClickedPos();
                double xPos = clickedPos.getX() + 0.5;
                double yPos = clickedPos.getY() + 0.5;
                double zPos = clickedPos.getZ() + 0.5;
                switch (clickedFace) {
                    case UP:
                        yPos += 0.5;
                        break;
                    case DOWN:
                        yPos -= 0.5;
                        break;
                    case NORTH:
                        zPos -= 0.5;
                        break;
                    case SOUTH:
                        zPos += 0.5;
                        break;
                    case WEST:
                        xPos -= 0.5;
                        break;
                    case EAST:
                        xPos += 0.5;
                        break;
                }
                for (int i = 0; i < 20; i++) {  // Increase particles for visibility
                    double xSpeed = (level.random.nextDouble() - 0.5) * 0.2;
                    double ySpeed = level.random.nextDouble() * 0.1;
                    double zSpeed = (level.random.nextDouble() - 0.5) * 0.2;
                    level.addParticle(new DustParticleOptions(new Vector3f(0, 0.5F, 0), 1.0F), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
                }
                context.getItemInHand().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(context.getHand()));
                // Run a loot table (using the vanilla grass loot table for testing purposes)
                LootTable lootTable = level.getServer().getLootTables().get(new ResourceLocation("minecraft:blocks/grass_block"));
                LootContext.Builder builder = new LootContext.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.ORIGIN, context.getClickLocation())
                    .withParameter(LootContextParams.TOOL, context.getItemInHand())
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, player);
                LootContext lootContext = builder.create(LootContextParamSets.BLOCK);
                List<ItemStack> loot = lootTable.getRandomItems(lootContext);
                // Give the player the loot
                for (ItemStack itemStack : loot) {
                    player.addItem(itemStack);
                }

                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }
}