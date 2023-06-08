package dev.nyon.moredetails.util

import net.minecraft.client.gui.GuiGraphics

fun GuiGraphics.verticalLine(x: Double, y: Double, height: Double, color: Int = 0x000000) =
    fill(x.toInt(), y.toInt(), (x + 1).toInt(), (y + height).toInt(), color)

fun GuiGraphics.horizontalLine(x: Double, y: Double, width: Double, color: Int = 0x000000) =
    fill(x.toInt(), y.toInt(), (x + width).toInt(), (y + 1).toInt(), color)