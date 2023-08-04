package com.thynanami.plugin.dtwt

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class EventListener(private val game: Game) : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        val block = event.clickedBlock ?: return
        val buttonLocation = Location(event.player.world, -148.0, 66.0, -252.0)
        if (block.type != Material.OAK_BUTTON || block.location != buttonLocation) {
            return
        }
        if (!game.tryStart(player)) {
            return
        }
        game.startGame(player)
    }
}
