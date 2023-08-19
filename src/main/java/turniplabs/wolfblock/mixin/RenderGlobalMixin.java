package turniplabs.wolfblock.mixin;

import net.minecraft.client.render.RenderGlobal;
import net.minecraft.core.block.Block;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.wolfblock.WolfBlock;

@Mixin(value = RenderGlobal.class, remap = false)
public abstract class RenderGlobalMixin {

    @Shadow public abstract void playSound(String soundPath, SoundType type, double x, double y, double z, float volume, float pitch);

    @Inject(
            method = "playBlockSoundEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderGlobal;playSound(Ljava/lang/String;Lnet/minecraft/core/sound/SoundType;DDDFF)V"
            )
    )
    private void playBlockSoundEffect(Block block, EnumBlockSoundEffectType soundType, double x, double y, double z, CallbackInfo ci) {
        if (block.id == WolfBlock.wolfBlock.id && soundType == EnumBlockSoundEffectType.DIG) {
            float volume = 0.3f;
            float pitch = 0.5f;
            String name = "mob.wolf.hurt";

            playSound(name, SoundType.WORLD_SOUNDS, x, y, z, volume, pitch);
        }
    }
}
