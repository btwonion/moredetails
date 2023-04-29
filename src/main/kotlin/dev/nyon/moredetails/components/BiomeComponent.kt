package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.gui.controllers.TickBoxController
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.util.color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style

@Serializable
class BiomeComponent(
    override var name: String = "Biome Component",
    override var enabled: Boolean = false,
    override var x: Int = 0,
    override var y: Int = 110,
    override var color: Int = 0x50C196,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x50C196,
    override var height: Int = 10,
    override var width: Int = 30,
    override var format: String = "Biome: %biome%",
    override val placeholders: Map<String, String> = mapOf("%biome%" to "the biome you are in"),
    var dynamicColor: Boolean = true
) : DetailComponent {

    @Transient
    private var widget: Renderable? = null
    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = Renderable { poseStack, _, _, _ ->
            renderBackground(poseStack)
            val minecraft = Minecraft.getInstance()
            val biome = minecraft.player!!.level.getBiome(minecraft.player!!.blockPosition())
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

            GuiComponent.drawString(poseStack, Minecraft.getInstance().font, component, x, y, color)
        }
    }

    override fun remove() {
        widget = null
    }

    override fun createYACLGroup(group: OptionGroup.Builder): OptionGroup {
        group.collapsed(true)
        group.name(Component.literal(name))
        group.tooltip(Component.literal("Configure the BiomeComponent '$name'"))
        group.createDefaultOptions()
        group.option(
            Option.createBuilder(Boolean::class.java).name(Component.literal("Dynamic color"))
                .tooltip(Component.literal("Decides whether the color of the biome component should change with the biome or not."))
                .binding(dynamicColor, { dynamicColor }, { new -> dynamicColor = new }).controller(::TickBoxController)
                .build()
        )
        return group.build()
    }
}

