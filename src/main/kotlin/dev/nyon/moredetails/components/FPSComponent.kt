package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl.api.OptionGroup
import dev.nyon.moredetails.minecraft
import dev.nyon.moredetails.mixins.MinecraftAccessor
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Serializable
class FPSComponent(
    override var name: String = "FPS Component",
    override var enabled: Boolean = false,
    override var x: Int = 0,
    override var y: Int = 0,
    override var color: Int = 0x6C92F9,
    override var backgroundColor: Int = 0x5010191D,
    override var background: Boolean = false,
    override var format: String = "FPS: %fps%",
    override val placeholders: Map<String, String> = mapOf("%fps%" to "the fps you have at the moment"),
    var updateCooldown: Duration = 500.milliseconds
) : DetailComponent {

    @Transient
    private var widget: Renderable? = null

    private var nextUpdate = Clock.System.now() + updateCooldown
    private var currentFPS: Int = 0
    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = Renderable { poseStack, _, _, _ ->
            if (Clock.System.now() >= nextUpdate) {
                currentFPS = (minecraft as MinecraftAccessor).frames
                nextUpdate = Clock.System.now() + updateCooldown
            }
            val component = Component.literal(
                format.replace(
                    "%fps%",
                    currentFPS.toString()
                )
            )
            renderBackground(poseStack, component, minecraft.font)
            GuiComponent.drawString(
                poseStack, minecraft.font, component, x, y, color
            )
        }
    }

    override fun remove() {
        widget = null
    }

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.tooltip(Component.literal("Configure the FPSComponent '$name'"))
        group.createDefaultOptions()
        return group.build()
    }
}