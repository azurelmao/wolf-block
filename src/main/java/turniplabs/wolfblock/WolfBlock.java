package turniplabs.wolfblock;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.sound.block.BlockSound;
import net.minecraft.core.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.TextureHelper;


public class WolfBlock implements ModInitializer {
    public static final String MOD_ID = "wolfblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block wolfBlock = new BlockBuilder(MOD_ID)
            .setBottomTexture("bottom.png")
            .setTopTexture("top.png")
            .setWestTexture("right.png")
            .setEastTexture("left.png")
            .setSouthTexture("back.png")
            .setNorthTexture(0, 27)
            .setHardness(0.75f)
            .setVisualUpdateOnMetadata()
            .setBlockSound(new BlockSound("step.cloth", "mob.wolf.death", 0.5f, 1.0f))
            .setBlockModel(new BlockModelWolf())
            .build(new BlockWolf("wolf", 5137));

    static {
        TextureHelper.addTextureToTerrain(MOD_ID, "front0.png", 0, 27);
        TextureHelper.addTextureToTerrain(MOD_ID, "front1.png", 1, 27);
        TextureHelper.addTextureToTerrain(MOD_ID, "front2.png", 0, 28);
        TextureHelper.addTextureToTerrain(MOD_ID, "front3.png", 1, 28);
        TextureHelper.addTextureToTerrain(MOD_ID, "front_angry0.png", 0, 29);
        TextureHelper.addTextureToTerrain(MOD_ID, "front_angry1.png", 1, 29);
        TextureHelper.addTextureToTerrain(MOD_ID, "front_angry2.png", 0, 30);
        TextureHelper.addTextureToTerrain(MOD_ID, "front_angry3.png", 1, 30);
    }

    static {
        EntityHelper.createTileEntity(TileEntityWolf.class, "WolfBlock");
    }

    @Override
    public void onInitialize() {
        LOGGER.info("WolfBlock initialized.");
    }
}
