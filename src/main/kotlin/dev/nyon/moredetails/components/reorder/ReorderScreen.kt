package dev.nyon.moredetails.components.reorder

import com.mojang.blaze3d.vertex.PoseStack
import dev.nyon.moredetails.components.DetailComponent
import dev.nyon.moredetails.config.config
import dev.nyon.moredetails.config.saveConfig
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import dev.nyon.moredetails.minecraft as mc

class ReorderScreen(private val parent: Screen?) : Screen(Component.literal("moredetails reorder screen")) {

    private var draggedComponent: DetailComponent? = null

    override fun init() {
        super.init()
        config.components.forEach { it.remove() }
    }

    override fun render(poseStack: PoseStack, mouseX: Int, mouseY: Int, partialTick: Float) {
        val enabledComponents = config.components.filter { it.enabled }
        enabledComponents.forEach { component ->
            ComponentWidget(component, font).render(poseStack, mouseX, mouseY, partialTick)
        }
        if (enabledComponents.isEmpty())
            GuiComponent.drawCenteredString(
                poseStack,
                font,
                Component.literal("No enabled components!"),
                width / 2,
                height / 2 - 50,
                0xE8B547
            )
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        if (draggedComponent == null) {
            draggedComponent = config.components.firstOrNull {
                (it.x..it.x + font.width(it.format)).contains(mouseX) &&
                        (it.y..it.y + font.lineHeight).contains(mouseY)
            }
        }
        if (draggedComponent == null) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
        config.components.first { it.name == draggedComponent?.name }.apply {
            this.x = this.x.plus(dragX)
            this.y = this.y.plus(dragY)
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        save()
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun onClose() {
        mc.setScreen(parent)
        config.components.filter { it.enabled }.forEach { it.register() }
        save()
    }

    private fun save() {
        config.components.replaceAll {
            val name = draggedComponent?.name
            return@replaceAll if (name == null) it else if (it.name == name) draggedComponent!! else it
        }
        draggedComponent = null
        saveConfig()
    }
}