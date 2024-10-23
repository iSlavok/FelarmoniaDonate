package online.slavok.felarmoniaDonate.commands

import online.slavok.felarmoniaDonate.utils.Luckperms
import online.slavok.felarmoniaDonate.donate.SponsorHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DeathNotifyCommand (
    private val sponsorHandler: SponsorHandler,
    private val luckperms: Luckperms
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        val uuid = sponsorHandler.getUuid(sender.name)
        if (sender.hasPermission("felarmonia.death-notify.enable")) {
            luckperms.removePermission(uuid, "felarmonia.death-notify.enable")
            sender.sendMessage("Уведомления о смерти выключены")
        } else {
            luckperms.addPermission(uuid, "felarmonia.death-notify.enable")
            sender.sendMessage("Уведомления о смерти включены")
        }
        return true
    }
}