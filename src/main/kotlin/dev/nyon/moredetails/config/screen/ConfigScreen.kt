package dev.nyon.moredetails.config.screen

import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.config.saveConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun generateConfigScreen(parent: Screen?): Screen {
    val configScreenBuilder = YetAnotherConfigLib.createBuilder()
    configScreenBuilder.title(Component.literal("moredetails"))
    configScreenBuilder.appendComponentsCategory()
    configScreenBuilder.save { saveConfig() }
    val configScreen = configScreenBuilder.build()
    return configScreen.generateScreen(parent)
}

fun YetAnotherConfigLib.Builder.appendComponentsCategory(): YetAnotherConfigLib.Builder {
    this.category(
        ConfigCategory.createBuilder().name(Component.literal("Components"))
            .tooltip(Component.literal("Manage all of your components in here"))
            .groups(config.components.map { it.createYACLGroup(OptionGroup.createBuilder()) }).build()
    )
    return this
}