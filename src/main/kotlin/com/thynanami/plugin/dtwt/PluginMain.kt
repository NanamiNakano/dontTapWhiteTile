package com.thynanami.plugin.dtwt

import org.bukkit.Location
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files
import java.util.Locale
import kotlin.io.path.Path

class PluginMain : JavaPlugin() {

    private lateinit var game: Game

    override fun onEnable() {
        logger.info(" enabled.")

        plugin = this

        game = Game(this.server.getWorld("world") ?: error("World not found"))
        this.server.pluginManager.registerEvents(EventListener(game), this)
//        this.getCommand("dtwt")!!.setExecutor(CommandDtwt())

        game.clear()

        if (Files.notExists(Path("./plugins/DTWT/config.yml"))) {
            this.saveDefaultConfig()
        }
    }

    override fun onDisable() {
        logger.info("disabled.")
    }

    companion object {
        lateinit var plugin: PluginMain
            private set
    }

}
