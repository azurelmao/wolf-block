package azurelmao.wolfblock.mixin;

import azurelmao.wolfblock.WolfBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import azurelmao.wolfblock.RenderBlocksWolf;

@Mixin(value = RenderBlocks.class, remap = false)
public abstract class RenderBlocksMixin implements RenderBlocksWolf {
    @Shadow
    private Minecraft mc;
    @Shadow
    private int uvRotateTop;
    @Shadow
    private World world;
    @Shadow
    private int uvRotateBottom;


    @Shadow public abstract boolean renderStandardBlock(Block block, int x, int y, int z, float r, float g, float b);

    @Override
    public boolean wolfblock$renderWolfBlock(Block block, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int orientation = meta & 0b111;

        int rotateTop;
        int rotateBottom;
        switch (orientation) {
            case 2:
                rotateTop = 3;
                rotateBottom = 3;
                break;
            case 3:
                rotateTop = 4;
                rotateBottom = 4;
                break;
            case 4:
                rotateTop = 1;
                rotateBottom = 2;
                break;
            default:
                rotateTop = 2;
                rotateBottom = 1;
                break;
        }
        uvRotateTop = rotateTop;
        uvRotateBottom = rotateBottom;
        boolean result = renderStandardBlock(block, x, y, z, 1f, 1f, 1f);
        uvRotateTop = 0;
        uvRotateBottom = 0;
        return result;
    }

    @ModifyVariable(
            method = "renderNorthFace",
            at = @At("STORE"),
            ordinal = 4
    )
    private double northD4(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original = original + unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderNorthFace",
            at = @At("STORE"),
            ordinal = 6
    )
    private double northD6(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderSouthFace",
            at = @At("STORE"),
            ordinal = 4
    )
    private double southD4(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderSouthFace",
            at = @At("STORE"),
            ordinal = 6
    )
    private double southD6(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderEastFace",
            at = @At("STORE"),
            ordinal = 4
    )
    private double eastD4(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderEastFace",
            at = @At("STORE"),
            ordinal = 6
    )
    private double eastD6(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderWestFace",
            at = @At("STORE"),
            ordinal = 4
    )
    private double westD4(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }

    @ModifyVariable(
            method = "renderWestFace",
            at = @At("STORE"),
            ordinal = 6
    )
    private double westD6(double original, @Local(ordinal = 0) Block block, @Local(ordinal = 0) int textureIndex) {
        if (block.id == WolfBlock.wolfBlock.id && textureIndex == Block.texCoordToIndex(0, 27) || textureIndex == Block.texCoordToIndex(0, 29) || textureIndex == Block.texCoordToIndex(2, 29)) {
            double unit = TextureFX.tileWidthTerrain / (double) (TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            original += unit;
        }
        return original;
    }
}
