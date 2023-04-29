package dev.nyon.moredetails.util

import kotlinx.serialization.Serializable
import net.minecraft.core.Holder
import net.minecraft.world.level.biome.Biome
import kotlin.jvm.optionals.getOrNull

@Serializable
data class BiomeColors(
    val theVoid: Int = 0x0B0416,
    val plains: Int = 0x4F9D52,
    val sunflowerPlains: Int = 0x9D5E59,
    val snowyPlains: Int = 0x8C9D87,
    val iceSpikes: Int = 0x7AE7F9,
    val desert: Int = 0xE2E047,
    val swamp: Int = 0x2C5237,
    val mangroveSwamp: Int = 0x52401B,
    val forest: Int = 0x347243,
    val flowerForest: Int = 0x4EC87E,
    val birchForest: Int = 0xA0BC7A,
    val darkForest: Int = 0x160E12,
    val oldGrowthBirchForest: Int = 0xA0BC7A,
    val oldGrowthPineTaiga: Int = 0x402821,
    val oldGrowthSpruceTaiga: Int = 0x402821,
    val taiga: Int = 0x402821,
    val snowyTaiga: Int = 0xACA9AB,
    val savanna: Int = 0x646229,
    val savannaPlateau: Int = 0x646229,
    val windsweptHills: Int = 0x4f674f,
    val windsweptGravellyHills: Int = 0x7C7A7B,
    val windsweptForest: Int = 0x357C40,
    val windsweptSavanna: Int = 0x646229,
    val jungle: Int = 0x245a16,
    val sparseJungle: Int = 0x245a16,
    val bambooJungle: Int = 0x36A021,
    val badlands: Int = 0xA05B15,
    val erodedBadlands: Int = 0xA0501C,
    val woodedBadlands: Int = 0xA05F33,
    val meadow: Int = 0x527542,
    val grove: Int = 0x606361,
    val snowySlopes: Int = 0x606361,
    val frozenPeaks: Int = 0x606361,
    val jaggedPeaks: Int = 0x606361,
    val stonyPeaks: Int = 0x474747,
    val river: Int = 0x3D67A0,
    val frozenRiver: Int = 0x7AB0DB,
    val beach: Int = 0xAFBA44,
    val snowyBeach: Int = 0x606361,
    val stonyShore: Int = 0x474747,
    val warmOcean: Int = 0x1380d1,
    val lukewarmOcean: Int = 0x1e71f7,
    val deepLukewarmOcean: Int = 0x146fff,
    val ocean: Int = 0x2525d6,
    val deepOcean: Int = 0x1a19ff,
    val coldOcean: Int = 0x191be2,
    val deepColdOcean: Int = 0x1e1df1,
    val frozenOcean: Int = 0x7592da,
    val deepFrozenOcean: Int = 0x485eb8,
    val mushroomFields: Int = 0x6d5f63,
    val dripstoneCaves: Int = 0x392417,
    val lushCaves: Int = 0x64791e,
    val deepDark: Int = 0x000c0a,
    val netherWastes: Int = 0x491614,
    val crimsonForest: Int = 0x410000,
    val warpedForest: Int = 0x176562,
    val soulSandValley: Int = 0x43372d,
    val basaltDeltas: Int = 0x3f3939,
    val theEnd: Int = 0x98a46d,
    val endHighlands: Int = 0x694c63,
    val endMidlands: Int = 0x100818,
    val smallEndIslands: Int = 0x98a46d,
    val endBarrens: Int = 0x98a46d,
    val cherryGrove: Int = 0xC2035F
)

fun BiomeColors.color(biome: Holder<Biome>) = when (biome.unwrapKey().getOrNull()?.location()?.path) {
    "the_void" -> this.theVoid
    "plains" -> this.plains
    "sunflower_plains" -> this.sunflowerPlains
    "snowy_plains" -> this.snowyPlains
    "ice_spikes" -> this.iceSpikes
    "desert" -> this.desert
    "swamp" -> this.swamp
    "mangrove_swamp" -> this.mangroveSwamp
    "forest" -> this.forest
    "flower_forest" -> this.flowerForest
    "birch_forest" -> this.birchForest
    "dark_forest" -> this.darkForest
    "old_growth_birch_forest" -> this.oldGrowthBirchForest
    "old_growth_pine_taiga" -> this.oldGrowthPineTaiga
    "old_growth_spruce_taiga" -> this.oldGrowthSpruceTaiga
    "taiga" -> this.taiga
    "snowy_taiga" -> this.snowyTaiga
    "savanna" -> this.savanna
    "savanna_plateau" -> this.savannaPlateau
    "windswept_hills" -> this.windsweptHills
    "windswept_gravelly_hills" -> this.windsweptGravellyHills
    "windswept_forest" -> this.windsweptForest
    "windswept_savanna" -> this.windsweptSavanna
    "jungle" -> this.jungle
    "sparse_jungle" -> this.sparseJungle
    "bamboo_jungle" -> this.bambooJungle
    "badlands" -> this.badlands
    "eroded_badlands" -> this.erodedBadlands
    "wooded_badlands" -> this.woodedBadlands
    "meadow" -> this.meadow
    "grove" -> this.grove
    "snowy_slopes" -> this.snowySlopes
    "frozen_peaks" -> this.frozenPeaks
    "jagged_peaks" -> this.jaggedPeaks
    "stony_peaks" -> this.stonyPeaks
    "river" -> this.river
    "frozen_river" -> this.frozenRiver
    "beach" -> this.beach
    "snowy_beach" -> this.snowyBeach
    "stony_shore" -> this.stonyShore
    "warm_ocean" -> this.warmOcean
    "lukewarm_ocean" -> this.lukewarmOcean
    "deep_lukewarm_ocean" -> this.deepLukewarmOcean
    "ocean" -> this.ocean
    "deep_ocean" -> this.deepOcean
    "cold_ocean" -> this.coldOcean
    "deep_cold_ocean" -> this.deepColdOcean
    "frozen_ocean" -> this.frozenOcean
    "deep_frozen_ocean" -> this.deepFrozenOcean
    "mushroom_fields" -> this.mushroomFields
    "dripstone_caves" -> this.dripstoneCaves
    "lush_caves" -> this.lushCaves
    "deep_dark" -> this.deepDark
    "nether_wastes" -> this.netherWastes
    "warped_forest" -> this.warpedForest
    "crimson_forest" -> this.crimsonForest
    "soul_sand_valley" -> this.warpedForest
    "basalt_deltas" -> this.basaltDeltas
    "the_end" -> this.theEnd
    "end_highlands" -> this.endHighlands
    "end_midlands" -> this.endMidlands
    "small_end_islands" -> this.smallEndIslands
    "end_barrens" -> this.endBarrens
    "cherry_grove" -> this.cherryGrove
    else -> 0x64BFC2
}