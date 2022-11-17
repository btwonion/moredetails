package dev.nyon.moredetails.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nyon.moredetails.components.DetailComponent;
import dev.nyon.moredetails.config.ConfigKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GUIMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    public void customizeHUD(PoseStack poseStack, float f, CallbackInfo ci) {
        if (!minecraft.options.hideGui)
            ConfigKt.getConfig().component1().stream().filter(DetailComponent::getEnabled).forEach(component -> component.update(poseStack));
    }
}
