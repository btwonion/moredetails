package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable
import net.minecraft.core.SectionPos
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ChunkPos

@Serializable
class PlayerCoordinatesComponent(
    override var x: Int = 0,
    override var y: Int = 50,
    override var enabled: Boolean = false,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var height: Int = 10,
    override var width: Int = 50,
    override var format: String = "XYZ: %x% %y% %z%",
    override var decimalPlaces: Int = 1
) : DetailComponent.CoordinatesComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = coordinatesWidget {
            val player = Minecraft.getInstance().player ?: error("player is null")
            return@coordinatesWidget Triple(player.x, player.y, player.z)
        }
    }

    override fun remove() {
        widget = null
    }
}

@Serializable
class PlayerChunkCoordinatesComponent(
    override var x: Int = 0,
    override var y: Int = 70,
    override var enabled: Boolean = false,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var height: Int = 10,
    override var width: Int = 50,
    override var format: String = "Chunk XYZ: %x% %y% %z%",
    override var decimalPlaces: Int = 1
) : DetailComponent.CoordinatesComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = coordinatesWidget {
            val player = Minecraft.getInstance().player ?: error("player is null")
            return@coordinatesWidget Triple(
                (player.blockPosition().x and 15).toDouble(),
                (player.blockPosition().y and 15).toDouble(),
                (player.blockPosition().z and 15).toDouble()
            )
        }
    }

    override fun remove() {
        widget = null
    }
}

@Serializable
class ChunkCoordinatesComponent(
    override var x: Int = 0,
    override var y: Int = 90,
    override var enabled: Boolean = false,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var height: Int = 10,
    override var width: Int = 50,
    override var format: String = "Chunk: %x% %z%",
    override var decimalPlaces: Int = 1
) : DetailComponent.CoordinatesComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = coordinatesWidget {
            val player = Minecraft.getInstance().player ?: error("player is null")

            val chunkPos = ChunkPos(player.blockPosition())
            return@coordinatesWidget Triple(
                chunkPos.x.toDouble(), SectionPos.blockToSectionCoord(player.blockY).toDouble(), chunkPos.z.toDouble()
            )
        }
    }

    override fun remove() {
        widget = null
    }
}

fun DetailComponent.CoordinatesComponent.coordinatesWidget(coordinatesResolver: () -> Triple<Double, Double, Double>): Renderable =
    Renderable { poseStack, _, _, _ ->
        fun Double.formatToInt(): String =
            if (this == this.toInt().toDouble()) this.toInt().toString() else "%.${decimalPlaces}f".format(this)

        val minecraft = Minecraft.getInstance()
        val (playerX, playerY, playerZ) = coordinatesResolver()
        val component = Component.literal(
            format.replace("%x%", playerX.formatToInt()).replace(
                "%y%", playerY.formatToInt().replace("%z%", playerZ.formatToInt())
            )
        )

        renderBackground(poseStack)
        GuiComponent.drawString(poseStack, minecraft.font, component, x, y, color)
    }