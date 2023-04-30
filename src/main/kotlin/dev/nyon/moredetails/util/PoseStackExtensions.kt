package dev.nyon.moredetails.util

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiComponent

fun PoseStack.verticalLine(x: Int, y: Int, height: Int, color: Int = 0x000000) =
    GuiComponent.fill(this, x, y, x + 1, y + height, color)

fun PoseStack.horizontalLine(x: Int, y: Int, width: Int, color: Int = 0x000000) =
    GuiComponent.fill(this, x, y, x + width, y + 1, color)