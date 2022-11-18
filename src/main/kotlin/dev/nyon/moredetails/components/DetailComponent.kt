package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import kotlinx.serialization.Serializable
import net.minecraft.client.gui.GuiComponent

@Serializable
sealed interface DetailComponent {

    var enabled: Boolean
    var x: Int
    var y: Int
    var color: Int
    var background: Boolean
    var backgroundColor: Int
    var height: Int
    var width: Int

    fun update(poseStack: PoseStack)

    fun register()
    fun remove()


    @Serializable
    sealed interface CoordinatesComponent : DetailComponent {
        var prefix: String
        var xColor: Int
        var xPrefix: String
        var yColor: Int
        var yPrefix: String
        var zColor: Int
        var zPrefix: String
        var decimalPlaces: Int
    }

    fun renderBackground(poseStack: PoseStack) {
        if (background) GuiComponent.fill(poseStack, x, y, x + width, y + height, backgroundColor)
    }
}