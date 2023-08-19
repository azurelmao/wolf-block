package turniplabs.wolfblock;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import turniplabs.wolfblock.mixin.BlockModelRenderBlocksAccessor;
import turniplabs.wolfblock.mixin.RenderBlocksAccessor;

public class BlockModelWolf extends BlockModelRenderBlocks {
    public BlockModelWolf() {
        super(0);
    }

    @Override
    public boolean render(Block block, int x, int y, int z) {
        RenderBlocks rb = BlockModelRenderBlocksAccessor.getRenderBlocks();
        return ((RenderBlocksWolf) rb).wolfblock$renderWolfBlock(block, x, y, z);
    }

    @Override
    public boolean renderNoCulling(Block block, int x, int y, int z) {
        RenderBlocks rb = BlockModelRenderBlocksAccessor.getRenderBlocks();
        ((RenderBlocksAccessor) rb).setRenderAllFaces(true);
        boolean result = this.render(block, x, y, z);
        ((RenderBlocksAccessor) rb).setRenderAllFaces(false);
        return result;
    }

    @Override
    public boolean renderWithOverrideTexture(Block block, int x, int y, int z, int textureIndex) {
        RenderBlocks rb = BlockModelRenderBlocksAccessor.getRenderBlocks();
        ((RenderBlocksAccessor) rb).setOverrideBlockTexture(textureIndex);
        boolean result = this.render(block, x, y, z);
        ((RenderBlocksAccessor) rb).setOverrideBlockTexture(-1);
        return result;
    }

    @Override
    public boolean shouldItemRender3d() {
        return true;
    }

    @Override
    public float getItemRenderScale() {
        return 0.25f;
    }
}
