package azurelmao.wolfblock;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import turniplabs.halplibe.helper.TextureHelper;

import java.util.Random;

public class BlockWolf extends BlockTileEntityRotatable {
    private static final int wildFrontTexture = texCoordToIndex(2, 29);
    private static final int[] angryTextures = new int[6];

    public BlockWolf(String key, int id) {
        super(key, id, Material.cloth);
        angryTextures[Side.TOP.getId()] = TextureHelper.getOrCreateBlockTextureIndex(WolfBlock.MOD_ID, "top_angry.png");
        angryTextures[Side.BOTTOM.getId()] = TextureHelper.getOrCreateBlockTextureIndex(WolfBlock.MOD_ID, "bottom_angry.png");
        angryTextures[Side.WEST.getId()] = TextureHelper.getOrCreateBlockTextureIndex(WolfBlock.MOD_ID, "right_angry.png");
        angryTextures[Side.EAST.getId()] = TextureHelper.getOrCreateBlockTextureIndex(WolfBlock.MOD_ID, "left_angry.png");
        angryTextures[Side.SOUTH.getId()] = TextureHelper.getOrCreateBlockTextureIndex(WolfBlock.MOD_ID, "back_angry.png");
        angryTextures[Side.NORTH.getId()] = texCoordToIndex(0, 29);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        int meta = world.getBlockMetadata(x, y, z);
        int powered = (meta >> 3);
        boolean isPowered = powered == 1;

        int chance = isPowered ? 30 : 100;

        if (rand.nextInt(chance) == 0) {
            float volume = isPowered ? 0.5f : 1f;
            float pitch = (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;

            String name;
            if (isPowered) {
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
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        Random random = new Random();
        ItemStack itemStack = player.inventory.getCurrentItem();
        TileEntityWolf tileEntity = (TileEntityWolf) world.getBlockTileEntity(x, y, z);

        if (itemStack == null || itemStack.itemID != Item.bone.id || tileEntity.owner != null) {
            return false;
        }

        itemStack.consumeItem(player);
        if (random.nextInt(3) == 0){
            tileEntity.owner = player.username;
            world.markBlockDirty(x, y, z);
            spawnHeartsOrSmokeParticles(world, random, true, x, y, z);
        } else {
            spawnHeartsOrSmokeParticles(world, random, false, x, y, z);
        }

        return true;
    }

    void spawnHeartsOrSmokeParticles(World world, Random random, boolean flag, int x, int y, int z) {
        String particle = flag ? "heart" : "smoke";
        float width = 1.0f;

        for (int i = 0; i < 7; ++i) {
            double dx = random.nextGaussian() * 0.02;
            double dy = random.nextGaussian() * 0.02;
            double dz = random.nextGaussian() * 0.02;
            world.spawnParticle(
                    particle,
                    x + 0.5 + (double) (random.nextFloat() * width * 2.0F) - (double) width,
                    y + 0.5 + (double) (random.nextFloat() * width),
                    z + 0.5 + (double) (random.nextFloat() * width * 2.0F) - (double) width, dx, dy, dz
            );
        }
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        ItemStack itemStack = new ItemStack(WolfBlock.wolfBlock, 1);

        TileEntityWolf tileEntityWolf = (TileEntityWolf) tileEntity;
        if (tileEntityWolf.owner != null) {
            CompoundTag data = new CompoundTag();
            data.putString("Owner", tileEntityWolf.owner);
            itemStack.setData(data);
        }

        return new ItemStack[] {itemStack};
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        super.onBlockPlaced(world, x, y, z, side, entity, sideHeight);

        int meta = world.getBlockMetadata(x, y, z);
        int orientation = meta & 0b111;

        boolean isPowered = world.isBlockGettingPowered(x, y, z) ||
                world.isBlockIndirectlyGettingPowered(x, y, z) ||
                world.dimension.id == Dimension.nether.id;
        int powered = isPowered ? 1 : 0;

        meta = orientation | (powered << 3);
        world.setBlockMetadataWithNotify(x, y, z, meta);

        String soundName;
        if (isPowered) {
            soundName = "mob.wolf.growl";
        } else {
            soundName = "mob.wolf.bark";
        }

        EnumBlockSoundEffectType soundType = EnumBlockSoundEffectType.PLACE;
        float volume = 1.0f;
        float pitch = 1.0f;

        world.playSoundEffect(
                SoundType.WORLD_SOUNDS,
                (double) x + 0.5,
                (double) y + 0.5,
                (double) z + 0.5,
                soundName,
                soundType.modifyVolume(volume),
                soundType.modifyPitch(pitch)
        );
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId == id) {
            return;
        }
        int meta = world.getBlockMetadata(x, y, z);
        int orientation = meta & 0b111;

        boolean isPowered = world.isBlockGettingPowered(x, y, z) ||
                world.isBlockIndirectlyGettingPowered(x, y, z) ||
                world.dimension.id == Dimension.nether.id;
        int powered = isPowered ? 1 : 0;

        meta = orientation | (powered << 3);
        world.setBlockMetadataWithNotify(x, y, z, meta);
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        int orientation = meta & 0b111;
        int powered = (meta >> 3);
        boolean isPowered = powered == 1;

        int index = Sides.orientationLookUpHorizontal[6 * orientation + side.getId()];

        TileEntityWolf tileEntity = (TileEntityWolf) blockAccess.getBlockTileEntity(x, y, z);


        if (isPowered) {
            return angryTextures[index];
        } else if (tileEntity.owner == null && Side.NORTH.getId() == index) {
            return wildFrontTexture;
        } else {
            return atlasIndices[index];
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityWolf();
    }
}
