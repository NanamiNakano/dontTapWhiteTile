package com.thynanami.plugin.dtwt

import com.thynanami.plugin.dtwt.Stage.*
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min


class Game(gameWorld: World) {
    private val world = gameWorld
    var player: Player? = null
        private set

    val xRange = -152..-149
    val yRange = 65..69
    val blockZ = -253
    private var score: Int = 0

    private var task: BukkitTask? = null
    private var timeoutTask: BukkitTask? = null
    private var lastAction: Instant? = null

    var stage = IDLE

    fun clear() {
        for (blockX in xRange) {
            for (blockY in yRange) {
                world.getBlockAt(blockX, blockY, blockZ).type = Material.QUARTZ_BLOCK
            }
        }
        score = 0
        stage = IDLE
    }

    private fun placeBlack() {
        var blockX: Int
        for (blockY in yRange) {
            blockX = xRange.random()
            world.getBlockAt(blockX, blockY, blockZ).type = Material.OBSIDIAN
        }
    }


    private fun tryStart(player: Player): Boolean {
        if (stage == IDLE) {
            this.player = player

            stage = PLAYING
            return true
        }
        player.sendMessage("Another player is playing")
        return false
    }

    fun startGame(player: Player) {
        if (!tryStart(player)) {
            return
        }
        placeBlack()
        player.sendMessage("Game is started")
        runGame()
    }

    private fun runGame() {
        lastAction = Instant.now()
        task = Bukkit.getScheduler().runTaskLater(PluginMain.plugin, Runnable {
            endGame(EndReason.FINISHED)
        }, 2 * 60 * 20)
        timeoutTask = Bukkit.getScheduler().runTaskTimer(PluginMain.plugin, Runnable {
            val now = Instant.now()
            if (ChronoUnit.SECONDS.between(lastAction, now).absoluteValue >= 10) {
                endGame(EndReason.TIMEOUT)
            }
        }, 0, 1 * 20)
    }

    fun scrollMap(clickedLocation: Location) {
        lastAction = Instant.now()
        if (clickedLocation.blockY != 65) {
            val player = player ?: error("Player is null")
            player.sendMessage("You can only tap blocks at the bottom!")
            return
        }
        for (blockX in xRange) {
            for (blockY in 65..68) {
                world.getBlockAt(blockX, blockY, blockZ).type = world.getType(blockX, blockY + 1, blockZ)
            }
        }
        for (blockX in xRange) {
            world.getBlockAt(blockX, 69, blockZ).type = Material.QUARTZ_BLOCK
        }
        world.getBlockAt(xRange.random(), 69, blockZ).type = Material.OBSIDIAN
        score++
    }

    private fun Player.sendTitle0(title: String, subtitle: String) {
        sendTitle(title, subtitle, 10, 70, 20)
    }

    private fun String.color(): String {
        return ChatColor.translateAlternateColorCodes('&', this)
    }

    fun endGame(reason: EndReason) {
        val player = player ?: error("Player is null")
        stage = END
        when (reason) {
            EndReason.TIMEOUT -> {
                player.sendTitle0("&cYOU FAILED".color(), "Game is timeout. Your score is $score")
                clear()
            }

            EndReason.FAILED -> {
                player.sendTitle0("&cYOU FAILED".color(), "You tapped white tile. Your score is $score")
                splashScreen(SplashColor.RED)
                Bukkit.getScheduler().runTaskLater(PluginMain.plugin, Runnable {
                    clear()
                }, 2 * 20)
            }

            EndReason.FINISHED -> {
                player.sendTitle0("&aYOU WON".color(), "Your score is $score")
                splashScreen(SplashColor.GREEN)
                Bukkit.getScheduler().runTaskLater(PluginMain.plugin, Runnable {
                    clear()
                }, 2 * 20)
            }
        }
        task?.cancel()
        timeoutTask?.cancel()
        task = null
        timeoutTask = null
    }


    private fun splashScreen(color: SplashColor) {
        when (color) {
            SplashColor.RED -> {
                for (blockX in xRange) {
                    for (blockY in yRange) {
                        world.getBlockAt(blockX, blockY, blockZ).type = Material.RED_WOOL
                    }
                }
            }

            SplashColor.GREEN -> {
                for (blockX in xRange) {
                    for (blockY in yRange) {
                        world.getBlockAt(blockX, blockY, blockZ).type = Material.GREEN_WOOL
                    }
                }
            }
        }
    }
}

//class Wall private constructor(lineStart: Block, lineEnd: Block, extension: Block, button: Block) {
//    companion object {
//        fun create(lineStart: Block, lineEnd: Block, extension: Block, button: Block): Wall {
//            require(isLine(lineStart, lineEnd), ) {
//                return
//            }
//        }
//
//        private fun isLine(lineStart: Block, lineEnd: Block): Boolean {
//            val xDiff = (lineStart.x - lineEnd.x).absoluteValue
//            val yDiff = (lineStart.y - lineEnd.y).absoluteValue
//            val zDiff = (lineStart.z - lineEnd.z).absoluteValue
//            val diffList = listOf(xDiff, yDiff, zDiff)
//            return diffList.fold(0) { acc, i ->
//                if (i == 0) {
//                    acc + 1
//                } else {
//                    acc
//                }
//            } == 2
//        }
//    }
//}

enum class Stage {
    IDLE,
    PLAYING,
    END,
}

enum class EndReason {
    TIMEOUT,
    FAILED,
    FINISHED,
}

enum class SplashColor {
    RED,
    GREEN,
}

data class Config(
    private val lineStart: Location,
    private val lineEnd: Location,
    private val direction: Location,
    private val button: Location,
    private val range: Int,
) {
    private lateinit var lineAxis: Axis

    init {
        val xDiff = (lineStart.blockX - lineEnd.blockX).absoluteValue
        val yDiff = (lineStart.blockY - lineEnd.blockY).absoluteValue
        val zDiff = (lineStart.blockZ - lineEnd.blockZ).absoluteValue
        val diffList = listOf(xDiff, yDiff, zDiff)
        val isLine = diffList.fold(0) { acc, i ->
            if (i == 0) {
                acc + 1
            } else {
                acc
            }
        } == 2
        require(isLine) {
            "require line"
        }
        lineAxis = if (xDiff != 0) {
            Axis.X
        } else if (yDiff != 0) {
            Axis.Y
        } else {
            Axis.Z
        }
        val lineStartAxis = lineStart.getByAxis(lineAxis)
        val lineEndAxis = lineEnd.getByAxis(lineAxis)
        require(direction.getByAxis(lineAxis) in min(lineStartAxis, lineEndAxis)..max(lineStartAxis, lineEndAxis)) {
            "invalid direction"
        }

        val otherAxis = mutableSetOf(Axis.X, Axis.Y, Axis.Z).apply { remove(lineAxis) }
        val onWallAxis =
            otherAxis.filter { i ->
                direction.getByAxis(i) == lineStart.getByAxis(i)
            }
        require(onWallAxis.size == 1) {
            "direction is not on wall"
        }
        val directionAxis = onWallAxis.first()
        val directionDiff = direction.getByAxis(directionAxis) - lineStart.getByAxis(directionAxis)

    }
}

fun Location.getByAxis(axis: Axis): Int {
    return when (axis) {
        Axis.X -> this.blockX
        Axis.Y -> this.blockY
        Axis.Z -> this.blockZ
    }
}

enum class Axis {
    X,
    Y,
    Z
}

enum class Positivity {
    P, //positive
    N  //negative
}