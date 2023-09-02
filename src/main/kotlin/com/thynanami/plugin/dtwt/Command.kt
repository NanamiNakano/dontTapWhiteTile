//package com.thynanami.plugin.dtwt
//
//import org.bukkit.command.Command
//import org.bukkit.command.CommandExecutor
//import org.bukkit.command.CommandSender
//import org.bukkit.entity.Player
//import org.bukkit.metadata.FixedMetadataValue
//
//class CommandDtwt : CommandExecutor {
//    override fun onCommand(
//        sender: CommandSender,
//        command: Command,
//        label: String,
//        args: Array<out String>,
//    ): Boolean {
//        if (sender !is Player) {
//            sender.sendMessage("This command can only be used by a player")
//            return false
//        }
//
//        if (args.size != 1) {
//            return false
//        }
//
//        val setup = Setup(sender)
//        if (!sender.hasMetadata("dtwt.stage")) {
//            sender.setMetadata("dtwt.stage", FixedMetadataValue(PluginMain.plugin, "idle"))
//        }
//
//        return when (args[0]) {
//            "set" -> {
//                setup.setup()
//                true
//            }
//
//            "get" -> {
//                sender.sendMessage("Map got")
//                true
//            }
//
//            "cancel" -> {
//                setup.cancel()
//                true
//            }
//
//            else -> {
//                false
//            }
//        }
//    }
//}
