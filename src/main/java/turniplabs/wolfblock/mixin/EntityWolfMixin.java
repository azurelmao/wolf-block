package turniplabs.wolfblock.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityPiston;
import net.minecraft.core.block.logic.PistonDirections;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.wolfblock.WolfBlock;

import java.util.Random;

@Mixin(value = EntityWolf.class, remap = false)
public abstract class EntityWolfMixin {
    @Shadow protected abstract String getDeathSound();

    @Shadow protected abstract float getSoundVolume();

    @Unique
    private boolean crushed = false;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void onLivingUpdate(CallbackInfo ci) {
        EntityWolf wolf = (EntityWolf) (Object) this;
        World world = wolf.world;
        int x = MathHelper.floor_double(wolf.x);
        int y = MathHelper.floor_double(wolf.y);
        int z = MathHelper.floor_double(wolf.z);


        if (world.getBlockId(x, y, z) != Block.pistonMoving.id) {
            return;
        }

        TileEntityPiston tileEntity = (TileEntityPiston) world.getBlockTileEntity(x, y, z);
        int facing = tileEntity.func_31009_d();

        int facingId = world.getBlockId(
                x + PistonDirections.xOffset[facing],
                y + PistonDirections.yOffset[facing],
                z + PistonDirections.zOffset[facing]);

        if (facingId == Block.obsidian.id) {
            EntityItem entityitem = new EntityItem(world, x, y + 0.5f, z, new ItemStack(WolfBlock.wolfBlock.asItem()));
            entityitem.delayBeforeCanPickup = 10;
            world.entityJoinedWorld(entityitem);

            Random random = new Random();
            world.playSoundAtEntity(wolf, getDeathSound(), getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            spawnConversionParticle(world, random, wolf);
            wolf.remove();
        }
    }

    @Unique
    private void spawnConversionParticle(World world, Random random, EntityWolf wolf) {
        for(int i = 0; i < 20; ++i) {
            double d = random.nextGaussian() * 0.02;
            double d1 = random.nextGaussian() * 0.02;
            double d2 = random.nextGaussian() * 0.02;
            world.spawnParticle("explode",
                    wolf.x + (double)(random.nextFloat() * wolf.bbHeight * 2.0F) - (double)wolf.bbWidth,
                    wolf.y + (double)(random.nextFloat() * wolf.bbHeight),
                    wolf.z + (double)(random.nextFloat() * wolf.bbWidth * 2.0F) - (double)wolf.bbWidth,
                    d, d1, d2
            );
        }
    }
}
