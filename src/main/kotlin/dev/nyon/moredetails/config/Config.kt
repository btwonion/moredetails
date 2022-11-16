package dev.nyon.moredetails.config

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
    val components: List<DetailComponent<*>>
)

var config = Config(listOf())
private val path = FabricLoader.getInstance().configDir.toAbsolutePath().resolve("moredetails.toml")
    .also { if (!it.exists()) it.createFile() }

fun saveConfig() = path.writeText(json.encodeToString(config))

fun loadConfig() {
    if (path.readText().isEmpty()) {
        saveConfig()
        return
    }

    config = json.decodeFromString(path.readText())
}