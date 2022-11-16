package dev.nyon.moredetails

import dev.nyon.moredetails.config.loadConfig
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
}

object MoreDetails {

    fun init() {
        loadConfig()
    }
}