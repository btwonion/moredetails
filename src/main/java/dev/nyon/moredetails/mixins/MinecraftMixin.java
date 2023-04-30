package dev.nyon.moredetails.mixins;

import dev.nyon.moredetails.MoreDetails;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
        method = "runTick",
        at = @At("HEAD")
    )
    public void tick(boolean renderLevel, CallbackInfo ci) {
        MoreDetails.INSTANCE.tick();
    }
}