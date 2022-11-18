package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Widget
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

@Serializable
class CoordinatesOverlay(
    override var x: Int = 0,
    override var y: Int = 50,
    override var enabled: Boolean = true,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var height: Int = 10,
    override var width: Int = 50,
    var prefix: String = "XYZ: ",
    var xColor: Int = color,
    var xPrefix: String = " ",
    var yColor: Int = color,
    var yPrefix: String = " ",
    var zColor: Int = color,
    var zPrefix: String = " ",
    var decimalPlaces: Int = 1
) : DetailComponent {

    @Transient
    private var widget: Widget? = null

    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = Widget { poseStack, _, _, _ ->
            val minecraft = Minecraft.getInstance()
            val component = Component.literal(prefix)
            listOf(
                Triple(minecraft.player?.x, xPrefix, xColor),
                Triple(minecraft.player?.y, yPrefix, yColor),
                Triple(minecraft.player?.z, zPrefix, zColor)
            ).forEach {
                component.append(it.second).append(
                    Component.literal(
                        if ((it.first ?: 0F) is Int) it.first.toString() else "%.${decimalPlaces}f".format(it.first)
                    ).withStyle(
                        Style.EMPTY.withColor(it.third)
                    )
                )
            }
            GuiComponent.fill(poseStack, x, y, x + width, y + height, backgroundColor)
            GuiComponent.drawString(
                poseStack, minecraft.font, component, x, y, color
            )
        }
    }

    override fun remove() {
        widget = null
    }
}