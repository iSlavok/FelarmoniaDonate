package online.slavok.felarmoniaDonate

import online.slavok.felarmoniaDonate.clans.ClansHandler
import online.slavok.felarmoniaDonate.commands.*
import online.slavok.felarmoniaDonate.database.AuthDatabase
import online.slavok.felarmoniaDonate.database.MetaDatabase
import online.slavok.felarmoniaDonate.database.FelarmoniaDatabase
import online.slavok.felarmoniaDonate.donate.SponsorHandler
import online.slavok.felarmoniaDonate.listeners.PlayerDeathListener
import online.slavok.felarmoniaDonate.meta.MetaHandler
import online.slavok.felarmoniaDonate.utils.Bot
import online.slavok.felarmoniaDonate.utils.Luckperms
import online.slavok.felarmoniaDonate.utils.PAPIExpansion
import org.bukkit.plugin.java.JavaPlugin

class FelarmoniaDonate : JavaPlugin() {
    private lateinit var bot: Bot
    private lateinit var database: FelarmoniaDatabase
    private lateinit var authDatabase: AuthDatabase
    private lateinit var metaDatabase: MetaDatabase
    private lateinit var metaHandler: MetaHandler
    private lateinit var sponsorHandler: SponsorHandler
    private lateinit var clansHandler: ClansHandler
    private var luckPerms = Luckperms()

    override fun onEnable() {
        saveDefaultConfig()
        val whitelistDatabaseUrl = config.getString("whitelist-mysql-url") ?: return
        val authDatabaseUrl = config.getString("auth-mysql-url") ?: return
        val metaDatabaseUrl = config.getString("meta-mysql-url") ?: return
        val botToken = config.getString("bot-token") ?: return
        database = FelarmoniaDatabase(whitelistDatabaseUrl)
        authDatabase = AuthDatabase(authDatabaseUrl)
        metaDatabase = MetaDatabase(metaDatabaseUrl)
        bot = Bot(botToken, this)
        metaHandler = MetaHandler(metaDatabase)
        sponsorHandler = SponsorHandler(this, database, authDatabase, metaHandler, luckPerms, bot)
        clansHandler = ClansHandler(database)
        getCommand("donate")?.setExecutor(DonateCommand(sponsorHandler, database, bot))
        getCommand("death-notify")?.setExecutor(DeathNotifyCommand(sponsorHandler, luckPerms))
        getCommand("fmeta")?.setExecutor(MetaCommand(metaHandler, config))
        getCommand("fclan")?.setExecutor(ClanAdminCommand(clansHandler))
        getCommand("clan")?.setExecutor(ClanCommand(clansHandler))
        server.pluginManager.registerEvents(PlayerDeathListener(bot, database), this)
        PAPIExpansion(metaHandler, clansHandler).register()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}