package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.gui.controllers.TickBoxController
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component

@Serializable
class TimeComponent(
    override var name: String = "Time Component",
    override var enabled: Boolean = false,
    override var x: Int = 0,
    override var y: Int = 0,
    override var color: Int = 0x6C92F9,
    override var backgroundColor: Int = 0x5010191D,
    override var background: Boolean = false,
    override var height: Int = 10,
    override var width: Int = 30,
    var twentyFourHourFormat: Boolean = true,
    override var format: String = if (twentyFourHourFormat) "%hour%:%minute%" else "%hour%:%minute% %period%",
    override val placeholders: Map<String, String> = mapOf(
        "%hour%" to "the current hour of the day",
        "%minute%" to "the current minute in the current hour",
        "%period%" to "the period in the day"
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
            renderBackground(poseStack)
            GuiComponent.drawString(
                poseStack, Minecraft.getInstance().font, Component.literal(
                    format
                        .replace(
                            "%hour%",
                            if (twentyFourHourFormat || now.hour <= 12) now.hour.toString() else (now.hour - 12).toString()
                        )
                        .replace("%minute%", now.minute.toString())
                        .replace("%second%", now.second.toString())
                        .replace("%period%", if (now.hour > 12) "pm" else "am")
                ), x, y, color
            )
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