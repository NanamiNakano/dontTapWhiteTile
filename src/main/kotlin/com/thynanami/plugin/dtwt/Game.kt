package com.thynanami.plugin.dtwt

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player

class Game(gameWorld: World) {
    private val blockZ = -253
    private val world = gameWorld
    private var player: Player? = null
    private val xRange = -152..-149
    private val yRange = 65..69

    private var stage: Stage = Init

    private fun clear() {
        for (blockX in xRange) {
            for (blockY in yRange) {
                world.getBlockAt(blockX, blockY, blockZ).type = Material.QUARTZ_BLOCK
            }
        }
        stage = Init
    }

    private fun placeBlack() {
        var blockX: Int
        for (blockY in yRange) {
            blockX = xRange.random()
            world.getBlockAt(blockX, blockY, blockZ).type = Material.OBSIDIAN
        }
    }



    fun tryStart(player: Player): Boolean {
        if (stage is Init) {
            this.player = player

            stage = Playing
            return true
        }
        player.sendMessage("Another player is playing")
        return false
    }

    fun startGame(player: Player) {
        placeBlack()
        stage = Playing
        player.sendMessage("Game is started")
    }

    fun runGame(player: Player) {

    }

    fun endGame(isFailed: Boolean, isTimeout: Boolean, score: Int = 0) {
        when (stage) {
            is End -> player!!.sendMessage("Game is not started")

            else -> {
                if (isFailed) {
                    stage = End
                    player!!.sendMessage("You tapped white tile! You failed.")
                } else if (isTimeout) {
                    stage = End
                    player!!.sendMessage("You are not playing this game anymore, game is ended.")
                } else {
                    stage = End
                    player!!.sendMessage("Your score is $score, congrats!")
                }
            }
        }
        clear()
    }
}


// Game stage
sealed class Stage
data object Init : Stage()
data object Playing : Stage()
data object End : Stage()
