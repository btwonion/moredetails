package dev.nyon.moredetails.components.reorder

import com.mojang.blaze3d.vertex.PoseStack
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.util.horizontalLine
import dev.nyon.moredetails.util.verticalLine
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import dev.nyon.moredetails.minecraft as mc

class ReorderScreen(private val parent: Screen?) : Screen(Component.literal("moredetails reorder screen")) {

    override fun init() {
        super.init()
        config.components.forEach { it.remove() }
    }

    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, partialTick: Float) {
        config.components.filter { it.enabled }.forEach { component ->
            val width = font.width(component.format)
            val height = font.lineHeight
            GuiComponent.fill(
                poseStack,
                component.x - 1,
                component.y - 1,
                component.x + width + 1,
                component.y + font.lineHeight,
                0x99DADADA.toInt()
            )

            poseStack.verticalLine(component.x - 2, component.y - 2, height + 3, 0x99B3B3B3.toInt())
            poseStack.verticalLine(component.x + width + 1, component.y - 2, height + 3, 0x99B3B3B3.toInt())
            poseStack.horizontalLine(component.x - 2, component.y - 2, width + 4, 0x99B3B3B3.toInt())
            poseStack.horizontalLine(component.x - 2, component.y + height, width + 4, 0x99B3B3B3.toInt())

            font.draw(
                poseStack,
                Component.literal(component.format),
                component.x.toFloat(),
                component.y.toFloat(),
                component.color
            )
        }
    }

    override fun onClose() {
        mc.setScreen(parent)
        config.components.filter { it.enabled }.forEach { it.register() }
    }
}