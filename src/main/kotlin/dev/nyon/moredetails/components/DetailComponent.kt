package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import kotlinx.serialization.Serializable

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
}