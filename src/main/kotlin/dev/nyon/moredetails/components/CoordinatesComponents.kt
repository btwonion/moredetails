package dev.nyon.moredetails.components

import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.minecraft
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.core.SectionPos
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ChunkPos

@Serializable
class PlayerCoordinatesComponent(
    override var name: String = "Player Coordinates Component",
    override var x: Double = 1.0,
    override var y: Double = 50.0,
    override var enabled: Boolean = false,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var format: String = "XYZ: %x% %y% %z%",
    override var decimalPlaces: Int = 1,
    @Transient
    override val placeholders: Map<String, String> = mapOf(
        "%x%" to "the x coordinate you are at",
        "%y%" to "the y coordinate you are at",
        "%z%" to "the z coordinate you are at"
    ),
    @Transient
    override val example: Component = Component.literal(
        format.replace("%x%", "0").replace("%y%", "0").replace("%z%", "0")
    )
) : DetailComponent.CoordinatesComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(matrices: GuiGraphics) {
        widget?.render(matrices, 0, 0, 0F)
    }

    override fun register() {
        widget = coordinatesWidget {
            val player = minecraft.player ?: error("player is null")
            return@coordinatesWidget Triple(player.x, player.y, player.z)
        }
    }

    override fun remove() {
        widget = null
    }

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.description(OptionDescription.of(Component.literal("Configure the PlayerCoordinateComponent '$name'")))
        group.createDefaultOptions()
        return group.build()
    }
}

@Serializable
class PlayerChunkCoordinatesComponent(
    override var name: String = "Player Chunk Coordinates Component",
    override var x: Double = 1.0,
    override var y: Double = 70.0,
    override var enabled: Boolean = false,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var format: String = "Chunk XYZ: %x% %y% %z%",
    override var decimalPlaces: Int = 1,
    @Transient
    override val placeholders: Map<String, String> = mapOf(
        "%x%" to "the x coordinate of you in your chunk",
        "%y%" to "the y coordinate of you in your chunk",
        "%z%" to "the z coordinate of you in your chunk"
    ),
    @Transient
    override val example: Component = Component.literal(
        format.replace("%x%", "0").replace("%y%", "0").replace("%z%", "0")
    )
) : DetailComponent.CoordinatesComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(matrices: GuiGraphics) {
        widget?.render(matrices, 0, 0, 0F)
    }

    override fun register() {
        widget = coordinatesWidget {
            val player = minecraft.player ?: error("player is null")
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

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.description(OptionDescription.of(Component.literal("Configure the PlayerChunkCoordinatesComponent '$name'")))
        group.createDefaultOptions()
        return group.build()
    }
}

@Serializable
class ChunkCoordinatesComponent(
    override var name: String = "Chunk Coordinates Component",
    override var x: Double = 1.0,
    override var y: Double = 90.0,
    override var enabled: Boolean = false,
    override var color: Int = 0xA4A7FF,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x37364C,
    override var format: String = "Chunk: %x% %z%",
    override var decimalPlaces: Int = 1,
    @Transient
    override val placeholders: Map<String, String> = mapOf(
        "%x%" to "the x coordinate of the chunk you are at",
        "%z%" to "the z coordinate of the chunk you are at"
    ),
    @Transient
    override val example: Component = Component.literal(
        format.replace("%x%", "0").replace("%z%", "0")
    )
) : DetailComponent.CoordinatesComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(matrices: GuiGraphics) {
        widget?.render(matrices, 0, 0, 0F)
    }

    override fun register() {
        widget = coordinatesWidget {
            val player = minecraft.player ?: error("player is null")

            val chunkPos = ChunkPos(player.blockPosition())
            return@coordinatesWidget Triple(
                chunkPos.x.toDouble(), SectionPos.blockToSectionCoord(player.blockY).toDouble(), chunkPos.z.toDouble()
            )
        }
    }

    override fun remove() {
        widget = null
    }

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.description(OptionDescription.of(Component.literal("Configure the ChunkCoordinatesComponent '$name'")))
        group.createDefaultOptions()
        return group.build()
    }
}

fun DetailComponent.CoordinatesComponent.coordinatesWidget(coordinatesResolver: () -> Triple<Double, Double, Double>): Renderable =
    Renderable { matrices, _, _, _ ->
        fun Double.formatToInt(): String =
            if (this == this.toInt().toDouble()) this.toInt().toString() else "%.${decimalPlaces}f".format(this)

        val (playerX, playerY, playerZ) = coordinatesResolver()
        val component = Component.literal(
            format
                .replace("%x%", playerX.formatToInt())
                .replace("%y%", playerY.formatToInt())
                .replace("%z%", playerZ.formatToInt())
        )

        renderBackground(matrices, component, minecraft.font)
        matrices.drawString(
            minecraft.font,
            component,
            x.toInt(),
            y.toInt(),
            color,
            config.textShadow
        )
    }