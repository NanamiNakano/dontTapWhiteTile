//package com.thynanami.plugin.dtwt
//
//import org.bukkit.Location
//import org.bukkit.Material
//import org.bukkit.entity.Player
//import org.bukkit.inventory.ItemStack
//import org.bukkit.metadata.FixedMetadataValue
//import org.bukkit.metadata.MetadataValue
//
//class Setup(private val player: Player) {
//
//    private val displayName = "Dtwt setup"
//    private val setupBlock = ItemStack(Material.CREEPER_HEAD, 4)
//    private val itemMeta = setupBlock.itemMeta
//
//    private fun init() {
//        val nullLocation = FixedMetadataValue(PluginMain.plugin, Location(null, 0.0, 0.0, 0.0))
//        player.setMetadata("dtwt.data.lineStart", nullLocation)
//        player.setMetadata("dtwt.data.lineEnd", nullLocation)
//        player.setMetadata("dtwt.data.direction", nullLocation)
//        player.setMetadata("dtwt.data.button", nullLocation)
//        player.setMetadata("dtwt.data.range", FixedMetadataValue(PluginMain.plugin, 0))
//    }
//
//    fun setup() {
//        if (player.getMetadata("dtwt.stage")[0].asString() != "idle") {
//            player.sendMessage("You are setting!")
//            return
//        }
//
//        if (!player.hasMetadata("dtwt.date.lineStart")) {
//            init()
//        }
//
//        itemMeta!!.setDisplayName(displayName)
//        setupBlock.setItemMeta(itemMeta)
//
//        player.inventory.addItem(setupBlock)
//        player.setMetadata("dtwt.stage", FixedMetadataValue(PluginMain.plugin, "set"))
//        player.setMetadata("dtwt.setup", FixedMetadataValue(PluginMain.plugin, 5))
//    }
//
//    fun saveData(setupData: Int, data: MetadataValue) {
//        when (setupData) {
//            5 -> {
//                player.sendMessage("5")
//            }
//            4 -> {
//                player.sendMessage("4")
//            }
//
//            3 -> {
//                player.sendMessage("3")
//            }
//
//            2 -> {
//                player.sendMessage("2")
//            }
//
//            1 -> {
//                player.sendMessage("1")
//                player.removeMetadata("dtwt.setup", PluginMain.plugin)
//                PluginMain.plugin.saveConfig()
//            }
//
//            else -> return
//        }
//    }
//
//
//    private fun clearMetadata() {
//        player.removeMetadata("dtwt.data.lineStart", PluginMain.plugin)
//        player.removeMetadata("dtwt.data.lineEnd", PluginMain.plugin)
//        player.removeMetadata("dtwt.data.direction", PluginMain.plugin)
//        player.removeMetadata("dtwt.data.button", PluginMain.plugin)
//        player.removeMetadata("dtwt.data.range", PluginMain.plugin)
//    }
//
//    fun cancel() {
//        if (player.getMetadata("dtwt.stage")[0].asString() == "idle") {
//            player.sendMessage("You are not setting!")
//            return
//        }
//
//        for (i in 0..35) {
//            val itemStack = player.inventory.getItem(i) ?: continue
//            if (itemStack.type != Material.CREEPER_HEAD) {
//                continue
//            }
//            if ((itemStack.itemMeta?.displayName ?: continue) != "Dtwt setup") {
//                continue
//            }
//            player.inventory.remove(itemStack)
//        }
//        player.removeMetadata("dtwt.setup", PluginMain.plugin)
//        clearMetadata()
//        player.setMetadata("dtwt.stage", FixedMetadataValue(PluginMain.plugin, "idle"))
//        player.sendMessage("You canceled setup")
//    }
//}
