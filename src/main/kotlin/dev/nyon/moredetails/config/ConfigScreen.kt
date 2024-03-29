package dev.nyon.moredetails.config

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.nyon.moredetails.components.reorder.ReorderScreen
import dev.nyon.moredetails.minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun generateConfigScreen(parent: Screen?): Screen {
    val configScreenBuilder = YetAnotherConfigLib.createBuilder()
    configScreenBuilder.title(Component.literal("moredetails"))
    configScreenBuilder.appendComponentsCategory()
    configScreenBuilder.appendAccessibilityCategory()
    configScreenBuilder.save {
        config.components.forEach {
            if (it.enabled) it.register() else it.remove()
        }
        saveConfig()
    }
    val configScreen = configScreenBuilder.build()
    return configScreen.generateScreen(parent)
}

fun YetAnotherConfigLib.Builder.appendComponentsCategory(): YetAnotherConfigLib.Builder {
    this.category(
        ConfigCategory.createBuilder().name(Component.literal("Components"))
            .tooltip(Component.literal("Manage all of your components in here"))
            .option(
                ButtonOption.createBuilder()
                    .name(Component.literal("Reorder components"))
                    .description(OptionDescription.of(Component.literal("Opens a screen to reorder the components.")))
                    .action { screen, _ ->
                        minecraft.setScreen(ReorderScreen(null))
                        screen.config.saveFunction()
                    }
                    .available(minecraft.currentServer != null || minecraft.isSingleplayer)
                    .build()
            )
            .groups(config.components.map { it.createYACLGroup(OptionGroup.createBuilder()) }).build()
    )
    return this
}

fun YetAnotherConfigLib.Builder.appendAccessibilityCategory(): YetAnotherConfigLib.Builder {
    this.category(
        ConfigCategory.createBuilder().name(Component.literal("Accessibility"))
            .tooltip(Component.literal("Change accessibility settings"))
            .option(
                Option.createBuilder<Boolean>().name(Component.literal("Text shadow"))
                    .description(OptionDescription.of(Component.literal("Decides whether the text should have a shadow or not.")))
                    .binding(config.textShadow, { config.textShadow }, { new -> config.textShadow = new })
                    .controller { TickBoxControllerBuilder.create(it) }.build()
            ).build()
    )
    return this
}