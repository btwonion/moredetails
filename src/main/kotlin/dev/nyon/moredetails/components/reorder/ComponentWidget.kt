package dev.nyon.moredetails.components.reorder

import com.mojang.blaze3d.vertex.PoseStack
import dev.nyon.moredetails.components.DetailComponent
import dev.nyon.moredetails.util.horizontalLine
import dev.nyon.moredetails.util.verticalLine
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable

class ComponentWidget(private val component: DetailComponent, private val font: Font) : Renderable {
    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, partialTick: Float) {
        val width = font.width(component.format)
        val height = font.lineHeight
        GuiComponent.fill(
            poseStack,
            (component.x - 1).toInt(),
            (component.y - 1).toInt(),
            (component.x + width + 1).toInt(),
            (component.y + font.lineHeight).toInt(),
            0x99DADADA.toInt()
        )

        poseStack.verticalLine(component.x - 2, component.y - 2, (height + 3).toDouble(), 0x99B3B3B3.toInt())
        poseStack.verticalLine(component.x + width + 1, component.y - 2, (height + 3).toDouble(), 0x99B3B3B3.toInt())
        poseStack.horizontalLine(component.x - 2, component.y - 2, (width + 4).toDouble(), 0x99B3B3B3.toInt())
        poseStack.horizontalLine(component.x - 2, component.y + height, (width + 4).toDouble(), 0x99B3B3B3.toInt())

        font.draw(
            poseStack,
            component.example,
            component.x.toFloat(),
            component.y.toFloat(),
            component.color
        )
    }
}