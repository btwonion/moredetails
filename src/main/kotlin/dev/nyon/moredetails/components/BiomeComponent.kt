package dev.nyon.moredetails.components

import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.minecraft
import dev.nyon.moredetails.util.color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style

@Serializable
class BiomeComponent(
    override var name: String = "Biome Component",
    override var enabled: Boolean = false,
    override var x: Double = 1.0,
    override var y: Double = 110.0,
    override var color: Int = 0x50C196,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x50C196,
    override var format: String = "Biome: %biome%",
    @Transient
    override val placeholders: Map<String, String> = mapOf("%biome%" to "the biome you are in"),
    private var dynamicColor: Boolean = true,
    @Transient
    override val example: Component = Component.literal(format.replace("%biome%", "plains"))
) : DetailComponent {

    @Transient
    private var widget: Renderable? = null
    override fun update(matrices: GuiGraphics) {
        widget?.render(matrices, 0, 0, 0F)
    }

    override fun register() {
        widget = Renderable { matrices, _, _, _ ->
            val biome = minecraft.player!!.level().getBiome(minecraft.player!!.blockPosition())
            val component: MutableComponent = Component.literal("")
            val split = format.split("%biome%")
            component.append(split[0])
            if (split.getOrNull(1) != null) {
                component.append(
                    Component.literal(biome.unwrapKey().get().location().path).withStyle(
                        Style.EMPTY.withColor(if (dynamicColor) config.biomeColors.color(biome) else color)
                    )
                )
                component.append(split[1])
            }

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
        group.description(OptionDescription.of(Component.literal("Configure the BiomeComponent '$name'")))
        group.createDefaultOptions()
        group.option(
            Option.createBuilder<Boolean>().name(Component.literal("Dynamic color"))
                .description(OptionDescription.of(Component.literal("Decides whether the color of the biome component should change with the biome or not.")))
                .binding(dynamicColor, { dynamicColor }, { new -> dynamicColor = new })
                .controller { TickBoxControllerBuilder.create(it) }
                .build()
        )
        return group.build()
    }
}

