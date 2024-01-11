package azurelmao.wolfblock.mixin;

import net.minecraft.client.render.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RenderBlocks.class, remap = false)
public interface RenderBlocksAccessor {
    @Accessor
    void setOverrideBlockTexture(int overrideBlockTexture);

    @Accessor
    void setRenderAllFaces(boolean renderAllFaces);
}
