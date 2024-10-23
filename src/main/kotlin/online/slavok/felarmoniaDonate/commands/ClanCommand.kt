package online.slavok.felarmoniaDonate.commands

import online.slavok.felarmoniaDonate.clans.ClansHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ClanCommand (
    private val handler: ClansHandler
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args != null) {
            when (args.size) {
                1 -> {
                    if (args[0] == "list") {
                        val clan = handler.getPlayerClan(sender.name)
                        if (clan != null) {
                            val players = handler.getClanPlayersList(clan)
                            val owner = handler.getClanOwner(clan)
                            sender.sendMessage("Ваш клан: $clan")
                            sender.sendMessage("Владелец: $owner")
                            sender.sendMessage("Игроки: $players")
                        } else {
                            sender.sendMessage("Вы не состоите в клане")
                        }
                    } else if (args[0] == "leave") {
                        val clan = handler.getPlayerClan(sender.name)
                        if (clan != null) {
                            if (handler.getClanOwner(clan) == sender.name) {
                                handler.deleteClan(clan)
                                sender.sendMessage("Ваш клан удален")
                            } else {
                                handler.removePlayerClan(sender.name)
                                sender.sendMessage("Вы покинули клан")
                            }
                        } else {
                            sender.sendMessage("Вы не состоите в клане")
                        }
                    } else return false
                    return true
                }
                2 -> {
                    if (args[0] == "add") {
                        val clan = handler.getPlayerClan(sender.name)
                        if (clan != null) {
                            if (handler.getClanOwner(clan) == sender.name) {
                                val player = args[1]
                                if (handler.getPlayerClan(player) == null) {
                                    handler.setPlayerClan(player, clan)
                                    sender.sendMessage("Игрок $player добавлен в клан")
                                } else {
                                    sender.sendMessage("Игрок уже состоит в клане")
                                }
                            } else {
                                sender.sendMessage("Вы не являетесь владельцем клана")
                            }
                        } else {
                            sender.sendMessage("Вы не состоите в клане")
                        }
                    } else if (args[0] == "remove") {
                        val clan = handler.getPlayerClan(sender.name)
                        if (clan != null) {
                            if (handler.getClanOwner(clan) == sender.name) {
                                val player = args[1]
                                if (handler.getPlayerClan(player) == clan) {
                                    handler.removePlayerClan(player)
                                    sender.sendMessage("Игрок $player удален из клана")
                                } else {
                                    sender.sendMessage("Игрок не состоит в вашем клане")
                                }
                            } else {
                                sender.sendMessage("Вы не являетесь владельцем клана")
                            }
                        } else {
                            sender.sendMessage("Вы не состоите в клане")
                        }
                    } else return false
                    return true
                }
            }
        }
        return false
    }
}