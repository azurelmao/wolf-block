package azurelmao.wolfblock;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class ItemBlockWolf extends ItemBlock {
    public ItemBlockWolf(Block block) {
        super(block);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if (stack.stackSize <= 0) {
            return false;
        }

        if (!world.canPlaceInsideBlock(blockX, blockY, blockZ)) {
            blockX += side.getOffsetX();
            blockY += side.getOffsetY();
            blockZ += side.getOffsetZ();
        }

        if (blockY < 0 || blockY >= world.getHeightBlocks()) {
            return false;
        }

        if (!world.canBlockBePlacedAt(this.blockID, blockX, blockY, blockZ, false, side) || !stack.consumeItem(player)) {
            return false;
        }

        Block block = Block.blocksList[this.blockID];
        if (!world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, this.blockID, this.getPlacedBlockMetadata(stack.getMetadata()))) {
            return true;
        }

        block.onBlockPlaced(world, blockX, blockY, blockZ, side, player, yPlaced);
        world.playBlockSoundEffect((float)blockX + 0.5F, (float)blockY + 0.5F, (float)blockZ + 0.5F, block, EnumBlockSoundEffectType.PLACE);

        CompoundTag data = stack.getData();
        if (!data.containsKey("Owner")) {
            return true;
        }

        TileEntityWolf tileEntity = (TileEntityWolf) world.getBlockTileEntity(blockX, blockY, blockZ);
        tileEntity.owner = data.getString("Owner");

        return true;
    }
}
