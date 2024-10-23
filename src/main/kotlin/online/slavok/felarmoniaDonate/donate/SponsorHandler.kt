package online.slavok.felarmoniaDonate.donate

import online.slavok.felarmoniaDonate.utils.Bot
import online.slavok.felarmoniaDonate.FelarmoniaDonate
import online.slavok.felarmoniaDonate.utils.Luckperms
import online.slavok.felarmoniaDonate.database.AuthDatabase
import online.slavok.felarmoniaDonate.database.FelarmoniaDatabase
import online.slavok.felarmoniaDonate.meta.MetaHandler
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*

class SponsorHandler (
    private val instance: FelarmoniaDonate,
    private val database: FelarmoniaDatabase,
    private val authDatabase: AuthDatabase,
    private val metaHandler: MetaHandler,
    private val luckPerms: Luckperms,
    private val bot: Bot,
) {
    private val client: HttpClient = HttpClient.newBuilder().build()

    fun addSponsor(nickname: String, tier: Int) {
        val currentTier = database.getTier(nickname)
        if (tier > currentTier) {
            database.setTier(nickname, tier)
            database.setCount(nickname, 1)
            giveSponsor(nickname, tier)
            val discordId = database.getDiscordId(nickname)
            bot.sendSponsorMessage(discordId, nickname, tier)
            if (discordId != null) {
                bot.giveSponsorRole(discordId)
            }
        } else if (tier == currentTier) {
            database.setCount(nickname, database.getCount(nickname) + 1)
            bot.sendSponsorMessage(database.getDiscordId(nickname), nickname, tier)
        } else {
            val count = when (tier) {
                1 -> 150
                2 -> 300
                3 -> 500
                else -> return
            }
            giveTotal(nickname, count)
        }
    }

    fun removeSponsorCount(nickname: String, tier: Int) {
        if (tier != database.getTier(nickname)) return
        val count = database.getCount(nickname)
        if (count > 1) {
            database.setCount(nickname, count - 1)
        } else {
            removeSponsor(nickname)
        }
    }

    fun giveTotal(nickname: String, amount: Int) {
        val current = database.getTotal(nickname)
        bot.sendDonateMessage(database.getDiscordId(nickname), nickname, amount, current + amount)
        database.setTotal(nickname, current + amount)
    }

    private fun giveSponsor(nickname: String, tier: Int) {
        val uuid = getUuid(nickname)
        when (tier) {
            1 -> {
                metaHandler.setMeta(nickname, "suffix_sponsor", " &a\uD83D\uDD25")
                instance.config.getList("1-level.permissions")?.forEach { permission ->
                    luckPerms.addPermission(uuid, permission.toString())
                }
                instance.config.getList("1-level.groups")?.forEach { group ->
                    luckPerms.addGroup(uuid, group.toString())
                }
            }
            2 -> {
                metaHandler.setMeta(nickname, "suffix_sponsor", " &e\uD83D\uDD25")
                instance.config.getList("2-level.permissions")?.forEach { permission ->
                    luckPerms.addPermission(uuid, permission.toString())
                }
                instance.config.getList("2-level.groups")?.forEach { group ->
                    luckPerms.addGroup(uuid, group.toString())
                }
            }
            3 -> {
                metaHandler.setMeta(nickname, "suffix_sponsor", " &b\uD83D\uDD25")
                instance.config.getList("3-level.permissions")?.forEach { permission ->
                    luckPerms.addPermission(uuid, permission.toString())
                }
                instance.config.getList("3-level.groups")?.forEach { group ->
                    luckPerms.addGroup(uuid, group.toString())
                }
            }
        }
    }

    private fun removeSponsor(nickname: String) {
        val discordId = database.getDiscordId(nickname)
        if (discordId != null) {
            bot.removeSponsorRole(discordId)
        }

        database.setTier(nickname, 0)
        database.setCount(nickname, 0)
        val uuid = getUuid(nickname)
        instance.config.getList("1-level.permissions")?.forEach { permission ->
            luckPerms.removePermission(uuid, permission.toString())
        }
        instance.config.getList("1-level.groups")?.forEach { group ->
            luckPerms.removeGroup(uuid, group.toString())
        }
        instance.config.getList("2-level.permissions")?.forEach { permission ->
            luckPerms.removePermission(uuid, permission.toString())
        }
        instance.config.getList("2-level.groups")?.forEach { group ->
            luckPerms.removeGroup(uuid, group.toString())
        }
        instance.config.getList("3-level.permissions")?.forEach { permission ->
            luckPerms.removePermission(uuid, permission.toString())
        }
        instance.config.getList("3-level.groups")?.forEach { group ->
            luckPerms.removeGroup(uuid, group.toString())
        }
        metaHandler.setMeta(nickname, "suffix_sponsor", "")
    }

    @Throws(IOException::class, InterruptedException::class, ParseException::class)
    private fun getOnlineUUID(nickname: String): UUID {
        val response: HttpResponse<String> = client.send(
            HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/$nickname"))
                .build(),
            HttpResponse.BodyHandlers.ofString()
        )
        val rawJson = response.body()
        val parser = JSONParser()
        val `object` = parser.parse(rawJson) as JSONObject
        val rawUuid = `object`["id"] as String
        val one = rawUuid.substring(0, 8)
        val two = rawUuid.substring(8, 12)
        val three = rawUuid.substring(12, 16)
        val four = rawUuid.substring(16, 20)
        val five = rawUuid.substring(20)
        return UUID.fromString("$one-$two-$three-$four-$five")
    }

    fun getUuid(player: String): UUID {
        return if (authDatabase.isPremium(player)) {
            getOnlineUUID(player)
        } else {
            UUID.nameUUIDFromBytes(("OfflinePlayer:$player").toByteArray(StandardCharsets.UTF_8))
        }
    }
}