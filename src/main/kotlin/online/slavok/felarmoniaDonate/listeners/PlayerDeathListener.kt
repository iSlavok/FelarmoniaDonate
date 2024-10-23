package online.slavok.felarmoniaDonate.listeners

import online.slavok.felarmoniaDonate.utils.Bot
import online.slavok.felarmoniaDonate.database.FelarmoniaDatabase
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener (
    private val bot: Bot,
    private val sponsorsDatabase: FelarmoniaDatabase
) : Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (player.hasPermission("felarmonia.death-notify.enable")) {
            val discordId = sponsorsDatabase.getDiscordId(player.name)
            if (discordId != null) {
                bot.sendDeathMessage(discordId)
            }
        }
    }
}