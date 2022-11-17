package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.nyon.moredetails.mixins.MinecraftAccessor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Widget
import net.minecraft.network.chat.Component

@Serializable
class FPSComponent(
    override var enabled: Boolean = false,
    override var x: Int = 0,
    override var y: Int = 0,
    override var color: Int = 0x6C92F9,
    override var backgroundColor: Int = 0x5010191D,
    override var background: Boolean = false,
    override var height: Int = 10,
    override var width: Int = 30,
    var prefix: String = "FPS: "
) : DetailComponent {

    @Transient
    private var widget: Widget? = null

    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = Widget { poseStack, _, _, _ ->
            GuiComponent.fill(
                poseStack, x, y, x + width, y + height, backgroundColor
            )
            GuiComponent.drawString(
                poseStack,
                Minecraft.getInstance().font,
                Component.literal("$prefix${(Minecraft.getInstance() as MinecraftAccessor).frames}"),
                x,
                y,
                color
            )
        }
    }

    override fun remove() {
        widget = null
        enabled = false
    }
}