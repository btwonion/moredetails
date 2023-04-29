package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.gui.controllers.ColorController
import dev.isxander.yacl.gui.controllers.TickBoxController
import dev.isxander.yacl.gui.controllers.string.StringController
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController
import kotlinx.serialization.Serializable
import net.minecraft.client.gui.GuiComponent
import net.minecraft.network.chat.Component
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
    var height: Int
    var width: Int
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

    fun renderBackground(poseStack: PoseStack) {
        if (background) GuiComponent.fill(poseStack, x, y, x + width, y + height, backgroundColor)
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
            Option.createBuilder(Int::class.java).name(Component.literal("Height"))
                .tooltip(Component.literal("Changes the height of the component."))
                .binding(height, { height }, { new -> height = new }).controller(::IntegerFieldController).build()
        )
        this.option(
            Option.createBuilder(Int::class.java).name(Component.literal("Width"))
                .tooltip(Component.literal("Changes the width of the component."))
                .binding(width, { width }, { new -> width = new }).controller(::IntegerFieldController).build()
        )
        this.option(
            Option.createBuilder(Color::class.java).name(Component.literal("Color"))
                .tooltip(Component.literal("Changes the text color of the component."))
                .binding(Color(color), { Color(color) }, { new -> color = new.rgb }).controller(::ColorController)
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
                .controller(::ColorController).build()
        )
        this.option(
            Option.createBuilder(String::class.java).name(Component.literal("Format"))
                .tooltip(Component.literal("Changes the format of the component.").apply {
                    placeholders.forEach { (placeholder, description) ->
                        append(Component.literal("\n - $placeholder: $description"))
                    }
                })
                .binding(format, { format }, { new -> format = new }).controller(::StringController).build()
        )
    }
}