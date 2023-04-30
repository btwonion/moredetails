package dev.nyon.moredetails.util

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiComponent

fun PoseStack.verticalLine(x: Double, y: Double, height: Double, color: Int = 0x000000) =
    GuiComponent.fill(this, x.toInt(), y.toInt(), (x + 1).toInt(), (y + height).toInt(), color)

fun PoseStack.horizontalLine(x: Double, y: Double, width: Double, color: Int = 0x000000) =
    GuiComponent.fill(this, x.toInt(), y.toInt(), (x + width).toInt(), (y + 1).toInt(), color)