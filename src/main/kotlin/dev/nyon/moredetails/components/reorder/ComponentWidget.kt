package dev.nyon.moredetails.components.reorder

import dev.nyon.moredetails.components.DetailComponent
import dev.nyon.moredetails.util.horizontalLine
import dev.nyon.moredetails.util.verticalLine
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable

class ComponentWidget(private val component: DetailComponent, private val font: Font) : Renderable {
    override fun render(matrices: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val width = font.width(component.format)
        val height = font.lineHeight
        matrices.fill(
            (component.x - 1).toInt(),
            (component.y - 1).toInt(),
            (component.x + width + 1).toInt(),
            (component.y + font.lineHeight).toInt(),
            0x99DADADA.toInt()
        )

        matrices.verticalLine(component.x - 2, component.y - 2, (height + 3).toDouble(), 0x99B3B3B3.toInt())
        matrices.verticalLine(component.x + width + 1, component.y - 2, (height + 3).toDouble(), 0x99B3B3B3.toInt())
        matrices.horizontalLine(component.x - 2, component.y - 2, (width + 4).toDouble(), 0x99B3B3B3.toInt())
        matrices.horizontalLine(component.x - 2, component.y + height, (width + 4).toDouble(), 0x99B3B3B3.toInt())

        matrices.drawString(
            font,
            component.example,
            component.x.toInt(),
            component.y.toInt(),
            component.color,
            false
        )
    }
}