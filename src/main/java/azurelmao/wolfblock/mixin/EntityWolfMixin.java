package azurelmao.wolfblock.mixin;

import azurelmao.wolfblock.WolfBlock;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.logic.PistonDirections;
import net.minecraft.core.block.piston.TileEntityPistonMoving;
import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = EntityWolf.class, remap = false)
public abstract class EntityWolfMixin {
    @Unique
    private boolean crushed = false;

    @Shadow
    protected abstract String getDeathSound();

    @Shadow
    protected abstract float getSoundVolume();

    @Shadow
    protected abstract void dropFewItems();

    @Shadow public abstract String getWolfOwner();

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void onLivingUpdate(CallbackInfo ci) {
        EntityWolf wolf = (EntityWolf) (Object) this;
        World world = wolf.world;
        double wolfX = wolf.x;
        double wolfY = wolf.y;
        double wolfZ = wolf.z;
        int x = MathHelper.floor_double(wolfX);
        int y = MathHelper.floor_double(wolfY);
        int z = MathHelper.floor_double(wolfZ);


        if (world.getBlockId(x, y, z) != Block.pistonMoving.id) {
            return;
        }

        TileEntityPistonMoving tileEntity = (TileEntityPistonMoving) world.getBlockTileEntity(x, y, z);
        int direction = tileEntity.getDirection();

        int blockIdInDirection = world.getBlockId(
                x + PistonDirections.xOffset[direction],
                y + PistonDirections.yOffset[direction],
                z + PistonDirections.zOffset[direction]);

        if (blockIdInDirection == Block.obsidian.id) {
            ItemStack itemStack = new ItemStack(WolfBlock.wolfBlock.asItem(), 1);
            CompoundTag data = new CompoundTag();
            if (getWolfOwner() != null) {
                data.putString("Owner", getWolfOwner());
            }

            itemStack.setData(data);
            wolf.spawnAtLocation(itemStack, 0f);

            Random random = new Random();
            spawnConversionParticle(world, random, wolf);

            world.playSoundAtEntity(wolf, getDeathSound(), getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            dropFewItems();

            crushed = true;
            wolf.remove();
        }
    }

    @Unique
    private void spawnConversionParticle(World world, Random random, EntityWolf wolf) {
        for (int i = 0; i < 20; ++i) {
            double d = random.nextGaussian() * 0.02;
            double d1 = random.nextGaussian() * 0.02;
            double d2 = random.nextGaussian() * 0.02;
            world.spawnParticle("explode",
                    wolf.x + (double) (random.nextFloat() * wolf.bbHeight * 2.0F) - (double) wolf.bbWidth,
                    wolf.y + (double) (random.nextFloat() * wolf.bbHeight),
                    wolf.z + (double) (random.nextFloat() * wolf.bbWidth * 2.0F) - (double) wolf.bbWidth,
                    d, d1, d2
            );
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/util/phys/AABB;)Ljava/util/List;"
            ),
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (crushed) {
            ci.cancel();
        }
    }
}
