package com.thynanami.plugin.dtwt

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.minecart.CommandMinecart
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.metadata.FixedMetadataValue

class EventListener(private val game: Game) : Listener {
    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val player = event.player
        val block = event.clickedBlock ?: return
        val buttonLocation = Location(event.player.world, -148.0, 66.0, -252.0)

        if (block.type != Material.OAK_BUTTON || block.location != buttonLocation) {
            return
        }
        game.startGame(player)
    }

    @EventHandler
    fun onPlayerLeftClick(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK) {
            return
        }

        if (game.stage != Stage.PLAYING) {
            return
        }

        if (event.player != game.player) {
            return
        }


        val block = event.clickedBlock ?: return

        val clickedLocation = block.location
        if (clickedLocation.blockX !in game.xRange ||
            clickedLocation.blockZ != game.blockZ ||
            clickedLocation.blockY !in game.yRange
        ) {
            return
        }

        if (block.type == Material.QUARTZ_BLOCK) {
            game.endGame(EndReason.FAILED)
        }

        if (block.type == Material.OBSIDIAN) {
            game.scrollMap(clickedLocation)
        }
    }

//    @EventHandler
//    fun onPlayerPlaceBlock(event: BlockPlaceEvent) {
//        val player = event.player
//        if (!player.hasMetadata("dtwt.setup")) {
//            return
//        }
//
//        var setupData = player.getMetadata("dtwt.setup")[0].asInt()
//        Setup(player).saveData(setupData)
//        setupData--
//        player.setMetadata("dtwt.setup", FixedMetadataValue(PluginMain.plugin, setupData))
//    }
}
