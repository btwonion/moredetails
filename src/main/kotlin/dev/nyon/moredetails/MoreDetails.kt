package dev.nyon.moredetails

import com.mojang.blaze3d.platform.InputConstants
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.config.loadConfig
import dev.nyon.moredetails.config.screen.generateConfigScreen
import kotlinx.serialization.json.Json
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

val json = Json {
    prettyPrint = true
    encodeDefaults = true
}
val minecraft: Minecraft = Minecraft.getInstance()

object MoreDetails {
    private val configKeyBind: KeyMapping = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "Open moredetails config screen",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "moredetails"
        )
    )

    val reorderKeyBind: KeyMapping = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "Reorder moredetails components",
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

    fun tick() {
        while (configKeyBind.consumeClick()) {
            minecraft.setScreen(generateConfigScreen(null))
        }
    }
}