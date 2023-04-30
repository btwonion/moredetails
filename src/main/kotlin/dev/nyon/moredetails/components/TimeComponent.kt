package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.gui.controllers.TickBoxController
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.minecraft
import dev.nyon.moredetails.util.assertMissingNull
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component

@Serializable
class TimeComponent(
    override var name: String = "Time Component",
    override var enabled: Boolean = false,
    override var x: Double = 1.0,
    override var y: Double = 20.0,
    override var color: Int = 0x6C92F9,
    override var backgroundColor: Int = 0x5010191D,
    override var background: Boolean = false,
    private var twentyFourHourFormat: Boolean = true,
    override var format: String = if (twentyFourHourFormat) "%hour%:%minute%" else "%hour%:%minute% %period%",
    @Transient
    override val placeholders: Map<String, String> = mapOf(
        "%hour%" to "the current hour of the day",
        "%minute%" to "the current minute in the current hour",
        "%period%" to "the period in the day"
    ),
    @Transient
    override val example: Component = Component.literal(
        format.replace("%hour%", "10").replace("%minute%", "10").replace("%period%", "pm")
    )
) : DetailComponent {

    @Transient
    private var widget: Renderable? = null

    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }


    override fun register() {
        widget = Renderable { poseStack, _, _, _ ->
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val component = Component.literal(
                format
                    .replace(
                        "%hour%",
                        if (twentyFourHourFormat || now.hour <= 12) now.hour.assertMissingNull() else (now.hour - 12).assertMissingNull()
                    )
                    .replace("%minute%", now.minute.assertMissingNull())
                    .replace("%second%", now.second.assertMissingNull())
                    .replace("%period%", if (now.hour > 12) "pm" else "am")
            )
            renderBackground(poseStack, component, minecraft.font)
            if (config.textShadow) GuiComponent.drawString(
                poseStack,
                minecraft.font,
                component,
                x.toInt(),
                y.toInt(),
                color
            )
            else minecraft.font.draw(poseStack, component, x.toFloat(), y.toFloat(), color)
        }
    }

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.tooltip(Component.literal("Configure the TimeComponent '$name'"))
        group.createDefaultOptions()
        group.option(
            Option.createBuilder(Boolean::class.java)
                .name(Component.literal("24h format"))
                .tooltip(Component.literal("Decides whether the time should be displayed in 24 hour format or not."))
                .binding(twentyFourHourFormat, { twentyFourHourFormat }, { new -> twentyFourHourFormat = new })
                .controller(::TickBoxController).build()
        )
        return group.build()
    }

    override fun remove() {
        widget = null
    }
}