package dev.nyon.moredetails.components

import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.controller.ColorControllerBuilder
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import kotlinx.serialization.Serializable
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.awt.Color

@Serializable
sealed interface DetailComponent {
    var name: String
    var enabled: Boolean
    var x: Double
    var y: Double
    var color: Int
    var background: Boolean
    var backgroundColor: Int
    var format: String
    val placeholders: Map<String, String>
    val example: Component

    fun update(matrices: GuiGraphics)

    fun register()
    fun remove()

    fun createYACLGroup(group: OptionGroup.Builder): OptionGroup

    @Serializable
    sealed interface CoordinatesComponent : DetailComponent {
        var decimalPlaces: Int
    }

    fun renderBackground(matrices: GuiGraphics, value: Component, font: Font) {
        if (background) matrices.fill(
            (x - 1).toInt(),
            (y - 1).toInt(),
            (x + font.width(value.string) + 1).toInt(),
            (y + font.lineHeight + 1).toInt(),
            backgroundColor
        )
    }

    fun OptionGroup.Builder.createDefaultOptions() {
        this.option(
            Option.createBuilder<Boolean>().name(Component.literal("Enabled"))
                .description(OptionDescription.of(Component.literal("Decides whether the component is displayed or not.")))
                .binding(enabled, { enabled }, { new -> enabled = new })
                .controller { TickBoxControllerBuilder.create(it) }.build()
        )
        this.option(
            Option.createBuilder<Double>().name(Component.literal("X coordinate"))
                .description(OptionDescription.of(Component.literal("Changes the x coordinate where the component is displayed. Suggested to modify via the reorder screen!")))
                .binding(x, { x }, { new -> x = new }).controller { DoubleFieldControllerBuilder.create(it) }.build()
        )
        this.option(
            Option.createBuilder<Double>().name(Component.literal("Y coordinate"))
                .description(OptionDescription.of(Component.literal("Changes the y coordinate where the component is displayed. Suggested to modify via the reorder screen!")))
                .binding(y, { y }, { new -> y = new }).controller { DoubleFieldControllerBuilder.create(it) }.build()
        )
        this.option(
            Option.createBuilder<Color>().name(Component.literal("Color"))
                .description(OptionDescription.of(Component.literal("Changes the text color of the component.")))
                .binding(Color(color), { Color(color) }, { new -> color = new.rgb })
                .controller { ColorControllerBuilder.create(it).allowAlpha(true) }
                .build()
        )
        this.option(
            Option.createBuilder<Boolean>().name(Component.literal("Background"))
                .description(OptionDescription.of(Component.literal("Decides whether the component should have a background or not.")))
                .binding(background, { background }, { new -> background = new })
                .controller { TickBoxControllerBuilder.create(it) }
                .build()
        )
        this.option(
            Option.createBuilder<Color>().name(Component.literal("Background color"))
                .description(OptionDescription.of(Component.literal("Changes the background color of the component.")))
                .binding(Color(backgroundColor), { Color(backgroundColor) }, { new -> backgroundColor = new.rgb })
                .controller { ColorControllerBuilder.create(it).allowAlpha(true) }.build()
        )
        this.option(
            Option.createBuilder<String>().name(Component.literal("Format"))
                .description(OptionDescription.of(Component.literal("Changes the format of the component.").apply {
                    placeholders.forEach { (placeholder, description) ->
                        append(Component.literal("\n - "))
                        append(Component.literal(placeholder).withStyle(Style.EMPTY.withColor(0xA1A0AC)))
                        append(Component.literal("  $description").withStyle(Style.EMPTY.withColor(0x75747E)))
                    }
                }))
                .binding(format, { format }, { new -> format = new }).controller { StringControllerBuilder.create(it) }
                .build()
        )
    }
}