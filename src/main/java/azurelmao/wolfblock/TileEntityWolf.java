package azurelmao.wolfblock;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.Dimension;

import java.util.List;
import java.util.Random;

public class TileEntityWolf extends TileEntity {
    public String owner = null;

    @Override
    public void tick() {
        if (worldObj.isClientSide) {
            return;
        }

        boolean powered =
                worldObj.isBlockGettingPowered(x, y, z) ||
                worldObj.isBlockIndirectlyGettingPowered(x, y, z) ||
                worldObj.dimension.id == Dimension.nether.id;

        if (!powered) {
            return;
        }

        List<Entity> entities = this.worldObj.getEntitiesWithinAABB(Entity.class, AABB.getBoundingBoxFromPool(x - 0.75, y - 0.75, z - 0.75, x + 1.75, y + 1.75, z + 1.75));
        for (Entity entity : entities) {
            if (entity instanceof EntityLiving) {
                if (entity instanceof EntityPlayer &&
                        ((EntityPlayer) entity).username.equals(owner) &&
                        worldObj.dimension.id != Dimension.nether.id
                ) {
                    continue;
                }

                entity.hurt(null, 2, DamageType.COMBAT);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (owner != null) {
            nbttagcompound.putString("Owner", owner);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.containsKey("Owner")) {
            owner = nbttagcompound.getString("Owner");
        }
    }
}
