package dev.nyon.moredetails

import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.config.loadConfig
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    encodeDefaults = true
}

object MoreDetails {
    fun init() {
        loadConfig()

        config.components.filter { it.enabled }.forEach {
            it.register()
        }
    }
}