package dev.nyon.moredetails

import com.mojang.blaze3d.platform.InputConstants
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.config.loadConfig
import kotlinx.serialization.json.Json
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

val json = Json {
    prettyPrint = true
    encodeDefaults = true
}

object MoreDetails {
    val configKeyBind = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "Open MoreDetails config screen",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "moredetails"
        )
    )

    val reorderKeyBind = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "Reorder MoreDetails components",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "moredetails"
        )
    )

    fun init() {
        loadConfig()

        config.components.filter { it.enabled }.forEach {
            it.register()
        }
    }
}