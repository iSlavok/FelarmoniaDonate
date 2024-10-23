package online.slavok.felarmoniaDonate.clans

import online.slavok.felarmoniaDonate.database.FelarmoniaDatabase

class ClansHandler (
    private val database: FelarmoniaDatabase,
) {
    private val players: MutableMap<String, String> = mutableMapOf()
    private val clans: MutableMap<String, String> = mutableMapOf()

    fun getPrayerPrefix(nickname: String): String {
        var clan = players[nickname]
        if (clan == null) {
            clan = database.getPlayerClan(nickname)?: ""
            players[nickname] = clan
        }
        var prefix = clans[clan]
        if (prefix == null) {
            prefix = database.getClanPrefix(clan)?: ""
            clans[clan] = prefix
        }
        return prefix
    }

    fun createClan(clan: String, prefix: String, owner: String) {
        database.createClan(clan, prefix, owner)
        database.setPlayerClan(owner, clan)
        players[owner] = clan
        clans[clan] = prefix
    }

    fun getPlayerClan(nickname: String): String? {
        return database.getPlayerClan(nickname)
    }

    fun getClanPlayersList(clan: String): List<String> {
        return database.getClanPlayersList(clan)
    }

    fun getClanOwner(clan: String): String {
        return database.getClanOwner(clan)
    }

    fun removePlayerClan(name: String) {
        database.removePlayerClan(name)
        players.remove(name)
    }

    fun setPlayerClan(player: String, clan: String) {
        database.setPlayerClan(player, clan)
        players[player] = clan
    }

    fun deleteClan(id: String) {
        database.deletePlayersClan(id)
        database.deleteClan(id)
        players.forEach { player ->
            if (player.value == id) {
                players.remove(player.key)
            }
        }
        clans.remove(id)
    }
}