package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.gui.controllers.ColorController
import dev.isxander.yacl.gui.controllers.TickBoxController
import dev.isxander.yacl.gui.controllers.string.StringController
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController
import kotlinx.serialization.Serializable
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiComponent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.awt.Color

@Serializable
sealed interface DetailComponent {
    var name: String
    var enabled: Boolean
    var x: Int
    var y: Int
    var color: Int
    var background: Boolean
    var backgroundColor: Int
    var format: String
    val placeholders: Map<String, String>

    fun update(poseStack: PoseStack)

    fun register()
    fun remove()

    fun createYACLGroup(group: OptionGroup.Builder): OptionGroup

    @Serializable
    sealed interface CoordinatesComponent : DetailComponent {
        var decimalPlaces: Int
    }

    fun renderBackground(poseStack: PoseStack, value: Component, font: Font) {
        if (background) GuiComponent.fill(
            poseStack,
            x - 1,
            y - 1,
            x + font.width(value.string) + 1,
            y + font.lineHeight + 1,
            backgroundColor
        )
    }

    fun OptionGroup.Builder.createDefaultOptions() {
        this.option(
            Option.createBuilder(String::class.java).name(Component.literal("Name"))
                .tooltip(Component.literal("Change the name of the component."))
                .binding(name, { name }, { new -> name = new }).controller(::StringController).build()
        )
        this.option(
            Option.createBuilder(Boolean::class.java).name(Component.literal("Enabled"))
                .tooltip(Component.literal("Decides whether the component is displayed or not."))
                .binding(enabled, { enabled }, { new -> enabled = new }).controller(::TickBoxController).build()
        )
        this.option(
            Option.createBuilder(Int::class.java).name(Component.literal("X coordinate"))
                .tooltip(Component.literal("Changes the x coordinate where the component is displayed. Suggested to modify via the reorder screen!"))
                .binding(x, { x }, { new -> x = new }).controller(::IntegerFieldController).build()
        )
        this.option(
            Option.createBuilder(Int::class.java).name(Component.literal("Y coordinate"))
                .tooltip(Component.literal("Changes the y coordinate where the component is displayed. Suggested to modify via the reorder screen!"))
                .binding(y, { y }, { new -> y = new }).controller(::IntegerFieldController).build()
        )
        this.option(
            Option.createBuilder(Color::class.java).name(Component.literal("Color"))
                .tooltip(Component.literal("Changes the text color of the component."))
                .binding(Color(color), { Color(color) }, { new -> color = new.rgb })
                .controller { ColorController(it, true) }
                .build()
        )
        this.option(
            Option.createBuilder(Boolean::class.java).name(Component.literal("Background"))
                .tooltip(Component.literal("Decides whether the component should have a background or not."))
                .binding(background, { background }, { new -> background = new }).controller(::TickBoxController)
                .build()
        )
        this.option(
            Option.createBuilder(Color::class.java).name(Component.literal("Background color"))
                .tooltip(Component.literal("Changes the background color of the component."))
                .binding(Color(backgroundColor), { Color(backgroundColor) }, { new -> backgroundColor = new.rgb })
                .controller { ColorController(it, true) }.build()
        )
        this.option(
            Option.createBuilder(String::class.java).name(Component.literal("Format"))
                .tooltip(Component.literal("Changes the format of the component.").apply {
                    placeholders.forEach { (placeholder, description) ->
                        append(Component.literal("\n - "))
                        append(Component.literal(placeholder).withStyle(Style.EMPTY.withColor(0xA1A0AC)))
                        append(Component.literal("  $description").withStyle(Style.EMPTY.withColor(0x75747E)))
                    }
                })
                .binding(format, { format }, { new -> format = new }).controller(::StringController).build()
        )
    }
}