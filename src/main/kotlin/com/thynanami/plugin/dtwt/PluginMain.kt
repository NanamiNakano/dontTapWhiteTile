package com.thynanami.plugin.dtwt

import org.bukkit.plugin.java.JavaPlugin

class PluginMain : JavaPlugin() {
    private lateinit var game : Game
    override fun onEnable() {
        game = Game(this.server.getWorld("world") ?: error("World not found"))
        logger.info(" enabled.")
        this.server.pluginManager.registerEvents(EventListener(game), this)
    }

    override fun onDisable() {
        logger.info("Basic disabled.")
    }

}