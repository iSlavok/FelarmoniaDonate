package online.slavok.felarmoniaDonate.commands

import online.slavok.felarmoniaDonate.meta.MetaHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration

class MetaCommand (
    private val metaHandler: MetaHandler,
    private val config: FileConfiguration,
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args != null) {
            when (args.size) {
                1 -> {
                    if (args[0] == "reload") {
                        metaHandler.reload()
                        sender.sendMessage("Мета успешно перезагружена")
                        return true
                    } else return false
                }
                3 -> {
                    when (args[0]) {
                        "get" -> {
                            sender.sendMessage(metaHandler.getMeta(args[1], args[2]))
                        }
                        "set" -> {
                            try {
                                metaHandler.setMeta(args[1], args[2], config.getString("meta.${args[2]}") ?: "")
                                sender.sendMessage("Мета успешно установлена")
                            } catch (e: Exception) {
                                sender.sendMessage("Ошибка при установке меты")
                            }
                        }
                        "remove" -> {
                            metaHandler.setMeta(args[1], args[2], "")
                            sender.sendMessage("Мета успешно удалена")
                        }
                        else -> return false
                    }
                    return true
                }
                4 -> {
                    when (args[0]) {
                        "set" -> {
                            try {
                                metaHandler.setMeta(args[1], args[2], args[3].replace("_", " "))
                                sender.sendMessage("Мета успешно установлена")
                            } catch (e: Exception) {
                                sender.sendMessage("Ошибка при установке меты")
                            }
                        }
                        else -> return false
                    }
                    return true
                }
                else -> return false
            }
        }
        return false
    }
}