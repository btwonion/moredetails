package dev.nyon.moredetails.components

import com.mojang.blaze3d.vertex.PoseStack
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.components.Renderable
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.level.biome.Biome
import kotlin.jvm.optionals.getOrNull

@Serializable
class BiomeComponent(
    override var enabled: Boolean = false,
    override var x: Int = 0,
    override var y: Int = 110,
    override var color: Int = 0x50C196,
    override var background: Boolean = false,
    override var backgroundColor: Int = 0x50C196,
    override var height: Int = 10,
    override var width: Int = 30,
    var prefix: String = "",
    var dynamicColor: Boolean = true
) : DetailComponent {

    @Transient
    private var widget: Renderable? = null
    override fun update(poseStack: PoseStack) {
        widget?.render(poseStack, 0, 0, 0F)
    }

    override fun register() {
        widget = Renderable { poseStack, _, _, _ ->
            val minecraft = Minecraft.getInstance()
            val biome = minecraft.player!!.level.getBiome(minecraft.player!!.blockPosition())
            renderBackground(poseStack)
            GuiComponent.drawString(
                poseStack, Minecraft.getInstance().font, Component.literal(prefix).append(
                    Component.literal(biome.unwrapKey().get().location().path).withStyle(
                        Style.EMPTY.withColor(if (dynamicColor) biome.color() else color)
                    )
                ), x, y, color
            )
        }
    }

    override fun remove() {
        widget = null
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun Holder<Biome>.color() = when (this.unwrapKey().getOrNull()?.location()?.path) {
    "the_void" -> 0x0B0416
    "plains" -> 0x4F9D52
    "sunflower_plains" -> 0x9D5E59
    "snowy_plains" -> 0x8C9D87
    "ice_spikes" -> 0x7AE7F9
    "desert" -> 0xE2E047
    "swamp" -> 0x2C5237
    "mangrove_swamp" -> 0x52401B
    "forest" -> 0x347243
    "flower_forest" -> 0x4EC87E
    "birch_forest" -> 0xA0BC7A
    "dark_forest" -> 0x160E12
    "old_growth_birch_forest" -> 0xA0BC7A
    "old_growth_pine_taiga" -> 0x402821
    "old_growth_spruce_taiga" -> 0x402821
    "taiga" -> 0x402821
    "snowy_taiga" -> 0xACA9AB
    "savanna" -> 0x646229
    "savanna_plateau" -> 0x646229
    "windswept_hills" -> 0x4f674f
    "windswept_gravelly_hills" -> 0x7C7A7B
    "windswept_forest" -> 0x357C40
    "windswept_savanna" -> 0x646229
    "jungle" -> 0x245a16
    "sparse_jungle" -> 0x245a16
    "bamboo_jungle" -> 0x36A021
    "badlands" -> 0xA05B15
    "eroded_badlands" -> 0xA0501C
    "wooded_badlands" -> 0xA05F33
    "meadow" -> 0x527542
    "grove" -> 0x606361
    "snowy_slopes" -> 0x606361
    "frozen_peaks" -> 0x606361
    "jagged_peaks" -> 0x606361
    "stony_peaks" -> 0x474747
    "river" -> 0x3D67A0
    "frozen_river" -> 0x7AB0DB
    "beach" -> 0xAFBA44
    "snowy_beach" -> 0x606361
    "stony_shore" -> 0x474747
    "warm_ocean" -> 0x1380d1
    "lukewarm_ocean" -> 0x1e71f7
    "deep_lukewarm_ocean" -> 0x146fff
    "ocean" -> 0x2525d6
    "deep_ocean" -> 0x1a19ff
    "cold_ocean" -> 0x191be2
    "deep_cold_ocean" -> 0x1e1df1
    "frozen_ocean" -> 0x7592da
    "deep_frozen_ocean" -> 0x485eb8
    "mushroom_fields" -> 0x6d5f63
    "dripstone_caves" -> 0x392417
    "lush_caves" -> 0x64791e
    "deep_dark" -> 0x000c0a
    "nether_wastes" -> 0x491614
    "warped_forest" -> 0x176562
    "crimson_forest" -> 0x410000
    "soul_sand_valley" -> 0x43372d
    "basalt_deltas" -> 0x3f3939
    "the_end" -> 0x98a46d
    "end_highlands" -> 0x694c63
    "end_midlands" -> 0x100818
    "small_end_islands" -> 0x98a46d
    "end_barrens" -> 0x98a46d
    else -> 0x64BFC2
}