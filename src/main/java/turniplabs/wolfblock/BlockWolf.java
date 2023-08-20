package turniplabs.wolfblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockWolf extends BlockTileEntityRotatable {
    private static final int angryTexture = texCoordToIndex(0, 29);

    public BlockWolf(String key, int id) {
        super(key, id, Material.cloth);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        boolean powered = world.isBlockGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y, z);
        int chance = powered ? 30 : 100;

        if (rand.nextInt(chance) == 0) {
            float volume = powered ? 0.5f : 1f;
            float pitch = (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;

            String name;
            if (powered) {
                name = "mob.wolf.growl";
            } else if (rand.nextInt(3) == 0) {
                name = rand.nextInt(50) == 0 ? "mob.wolf.whine" : "mob.wolf.panting";
            } else {
                name = "mob.wolf.bark";
            }

            RenderGlobal rg = Minecraft.getMinecraft(Minecraft.class).renderGlobal;
            rg.playSound(name, SoundType.WORLD_SOUNDS, x, y, z, volume, pitch);
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        super.onBlockPlaced(world, x, y, z, side, entity, sideHeight);
        EnumBlockSoundEffectType soundType = EnumBlockSoundEffectType.PLACE;
        float volume = 1.0f;
        float pitch = 1.0f;
        String name = "mob.wolf.bark";

        RenderGlobal rg = Minecraft.getMinecraft(Minecraft.class).renderGlobal;
        rg.playSound(name, SoundType.WORLD_SOUNDS, x, y, z, soundType.modifyVolume(volume), soundType.modifyPitch(pitch));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId == id) {
            return;
        }

        int meta = world.getBlockMetadata(x, y, z);
        int orientation = meta & 0b111;
        int powered = (world.isBlockGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y, z)) ? 1 : 0;

        meta = orientation | (powered << 3);
        world.setBlockMetadataWithNotify(x, y, z, meta);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(Side side, int meta) {
        int orientation = meta & 0b111;
        int powered = meta >> 3;

        int index = Sides.orientationLookUpHorizontal[6 * orientation + side.getId()];
        if (powered == 1 && index == Side.NORTH.getId()) {
            return angryTexture;
        } else {
            return atlasIndices[index];
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityWolf();
    }
}
