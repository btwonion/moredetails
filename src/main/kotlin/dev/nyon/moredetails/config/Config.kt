package dev.nyon.moredetails.config

import dev.nyon.moredetails.components.CoordinatesComponent
import dev.nyon.moredetails.components.DetailComponent
import dev.nyon.moredetails.components.FPSComponent
import dev.nyon.moredetails.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText


@Serializable
data class Config(
    val components: List<DetailComponent>
)

var config = Config(listOf(FPSComponent(), CoordinatesComponent()))
private val path = FabricLoader.getInstance().configDir.toAbsolutePath().resolve("moredetails.json")
    .also { if (!it.exists()) it.createFile() }

fun saveConfig() = path.writeText(json.encodeToString(config))

fun loadConfig() {
    if (path.readText().isEmpty()) {
        saveConfig()
        return
    }

    config = json.decodeFromString(path.readText())
}