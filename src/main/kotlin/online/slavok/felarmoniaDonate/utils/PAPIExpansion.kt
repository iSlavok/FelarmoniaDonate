package online.slavok.felarmoniaDonate.utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import online.slavok.felarmoniaDonate.clans.ClansHandler
import online.slavok.felarmoniaDonate.meta.MetaHandler
import org.bukkit.OfflinePlayer

class PAPIExpansion (
    private val metaHandler: MetaHandler,
    private val clansHandler: ClansHandler
) : PlaceholderExpansion() {

    override fun persist(): Boolean {
        return true
    }

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        if (player != null) {
            val nickname = player.name
            if (nickname != null) {
                if (params == "prefix_clan") {
                    return clansHandler.getPrayerPrefix(nickname)
                }
                return metaHandler.getMeta(nickname, params)
            }
        }
        return null
    }

    override fun getIdentifier(): String {
        return "felarmonia"
    }

    override fun getAuthor(): String {
        return "isSLAVOK"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }
}