package online.slavok.felarmoniaDonate.commands

import online.slavok.felarmoniaDonate.clans.ClansHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ClanAdminCommand (
    private val handler: ClansHandler
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args != null) {
            when (args.size) {
                2 -> {
                    if (args[0] == "delete") {
                        val id = args[1]
                        handler.deleteClan(id)
                        return true
                    }
                }
                4 -> {
                    if (args[0] == "create") {
                        val id = args[1]
                        val prefix = args[2].replace("_", " ")
                        val owner = args[3]
                        handler.createClan(id, prefix, owner)
                        return true
                    }
                }
            }
        }
        return false
    }
}