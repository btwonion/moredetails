package dev.nyon.moredetails.components

import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.controller.LongFieldControllerBuilder
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.minecraft
import dev.nyon.moredetails.mixins.MinecraftAccessor
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Serializable
class FPSComponent(
    override var name: String = "FPS Component",
    override var enabled: Boolean = false,
    override var x: Double = 1.0,
    override var y: Double = 1.0,
    override var color: Int = 0x6C92F9,
    override var backgroundColor: Int = 0x5010191D,
    override var background: Boolean = false,
    override var format: String = "FPS: %fps%",
    @Transient
    override val placeholders: Map<String, String> = mapOf("%fps%" to "the fps you have at the moment"),
    private var updateCooldown: Duration = 500.milliseconds,
    @Transient
    override val example: Component = Component.literal(format.replace("%fps%", "100"))
) : DetailComponent {

    @Transient
    private var widget: Renderable? = null

    @Transient
    private var nextUpdate = Clock.System.now() + updateCooldown

    @Transient
    private var currentFPS: Int = 0
    override fun update(matrices: GuiGraphics) {
        widget?.render(matrices, 0, 0, 0F)
    }

    override fun register() {
        widget = Renderable { matrices, _, _, _ ->
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
    }

    override fun remove() {
        widget = null
    }

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.description(OptionDescription.of(Component.literal("Configure the FPSComponent '$name'")))
        group.createDefaultOptions()
        group.option(
            Option.createBuilder<Long>()
                .name(Component.literal("Update Cooldown"))
                .description(OptionDescription.of(Component.literal("The time the component waits until the next update")))
                .binding(
                    updateCooldown.inWholeMilliseconds,
                    { updateCooldown.inWholeMilliseconds },
                    { updateCooldown = it.milliseconds })
                .controller { LongFieldControllerBuilder.create(it) }
                .build()
        )
        return group.build()
    }
}