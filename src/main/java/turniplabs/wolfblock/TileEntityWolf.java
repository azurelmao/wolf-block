package turniplabs.wolfblock;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.AABB;

import java.util.List;
import java.util.Random;

public class TileEntityWolf extends TileEntity {
    private int attackTime = 0;
    private final Random rand = new Random();

    public String getInvName() {
        return "WolfBlock";
    }

    @Override
    public void updateEntity() {
        if (worldObj.isClientSide) {
            return;
        }

        if (attackTime > 0) {
            --attackTime;
        }

        boolean powered = worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
        if (!powered) {
            return;
        }

        List<Entity> entities = this.worldObj.getEntitiesWithinAABB(Entity.class, AABB.getBoundingBoxFromPool(xCoord - 0.75, yCoord - 0.75, zCoord - 0.75, xCoord + 1.75, yCoord + 1.75, zCoord + 1.75));
        for (Entity entity : entities) {
            if (entity instanceof EntityLiving && attackTime == 0) {
                entity.hurt(null, 2, DamageType.COMBAT);
                attackTime = 16 + rand.nextInt(7) - 3;
            }
        }
    }
}
