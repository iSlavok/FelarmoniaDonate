package online.slavok.felarmoniaDonate.commands

import online.slavok.felarmoniaDonate.utils.Bot
import online.slavok.felarmoniaDonate.database.FelarmoniaDatabase
import online.slavok.felarmoniaDonate.donate.SponsorHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DonateCommand (
    private val sponsorHandler: SponsorHandler,
    private val sponsorsDatabase: FelarmoniaDatabase,
    private val bot: Bot,
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args != null) {
            if (args.size != 3) {
                sender.sendMessage("Неверные аргументы")
                return true
            }
        } else {
            sender.sendMessage("Неверные аргументы")
            return true
        }
        val type = args[0]
        val player = args[1]
        val amount = args[2].toIntOrNull()
        if (amount == null) {
            sender.sendMessage("Неверное количество")
            return true
        }
        if (type == "sponsor") {
            sponsorHandler.addSponsor(player, amount)
        } else if (type == "remsponsor") {
            sponsorHandler.removeSponsorCount(player, amount)
        } else if (type == "donate") {
            sponsorHandler.giveTotal(player, amount*10)
        } else if (type == "unban") {
            bot.sendUnbanMessage(sponsorsDatabase.getDiscordId(player), player)
        } else if (type == "item") {
            bot.sendItemMessage(sponsorsDatabase.getDiscordId(player), player, amount)
        }
        return true
    }
}