package online.slavok.felarmoniaDonate.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import online.slavok.felarmoniaDonate.FelarmoniaDonate
import java.util.function.Consumer


class Bot (
    token: String,
    private val instance: FelarmoniaDonate,
) {
    private val jda: JDA = JDABuilder.createDefault(token).build()

    fun giveSponsorRole(discordId: String) {
        val guild = jda.getGuildById("1278106662267523072")!!
        val role = guild.getRoleById("1289316037313171487")!!
        val callback = Consumer<Member> { member ->
            val callback = Consumer<Void> { _ ->
                instance.logger.info("Роль успешно выдана игроку $discordId")
            }
            val exception = Consumer<Throwable> { _ ->
                instance.logger.warning("Не удалось добавить роль игроку $discordId")
            }
            guild.addRoleToMember(member, role).queue(callback, exception)
        }
        val exception = Consumer<Throwable> { _ ->
            instance.logger.warning("Не удалось получить игрока $discordId")
        }
        guild.retrieveMemberById(discordId).queue(callback, exception)
    }

    fun removeSponsorRole(discordId: String) {
        val guild = jda.getGuildById("1278106662267523072")!!
        val role = guild.getRoleById("1289316037313171487")!!
        val callback = Consumer<Member> { member ->
            val callback = Consumer<Void> { _ ->
                instance.logger.info("Роль успешно удалена у игрока $discordId")
            }
            val exception = Consumer<Throwable> { _ ->
                instance.logger.warning("Не удалось удалить роль у игрока $discordId")
            }
            guild.removeRoleFromMember(member, role).queue(callback, exception)
        }
        val exception = Consumer<Throwable> { _ ->
            instance.logger.warning("Не удалось получить игрока $discordId")
        }
        guild.retrieveMemberById(discordId).queue(callback, exception)
    }

    fun sendDonateMessage(discordId: String?, nickname: String?, amount: Int, sumAmount: Int) {
        val guild = jda.getGuildById("1278106662267523072")!!
        val channel: TextChannel? = guild.getTextChannelById("1289317179673677847")
        if (channel == null) {
            instance.logger.warning("Не удалось получить канал sponsors")
            return
        }
        val callback = Consumer<Message> { _ ->
            instance.logger.info("Сообщение успешно отправлено в канал ${channel.name}")
        }
        val exception = Consumer<Throwable> { _ ->
            instance.logger.warning("Не удалось отправить сообщение в канал ${channel.name}")
        }
        if (discordId == null) {
            channel.sendMessage("$nickname отправил $amount руб (всего $sumAmount руб)").queue(callback, exception)
        } else {
            channel.sendMessage("<@$discordId> отправил $amount руб (всего $sumAmount руб)").queue(callback, exception)
        }
    }

    fun sendSponsorMessage(discordId: String?, nickname: String?, tier: Int) {
        val guild = jda.getGuildById("1278106662267523072")!!
        val channel: TextChannel? = guild.getTextChannelById("1289317179673677847")
        if (channel == null) {
            instance.logger.warning("Не удалось получить канал sponsors")
            return
        }
        val callback = Consumer<Message> { _ ->
            instance.logger.info("Сообщение успешно отправлено в канал ${channel.name}")
        }
        val exception = Consumer<Throwable> { _ ->
            instance.logger.warning("Не удалось отправить сообщение в канал ${channel.name}")
        }
        if (discordId == null) {
            channel.sendMessage("$nickname купил спонсорку $tier уровня").queue(callback, exception)
        } else {
            channel.sendMessage("<@$discordId> купил спонсорку $tier уровня").queue(callback, exception)
        }
    }

    fun sendUnbanMessage(discordId: String?, nickname: String?) {
        val guild = jda.getGuildById("1278106662267523072")!!
        val channel: TextChannel? = guild.getTextChannelById("1289534885371052154")
        if (channel == null) {
            instance.logger.warning("Не удалось получить канал unban")
            return
        }
        val callback = Consumer<Message> { _ ->
            instance.logger.info("Сообщение успешно отправлено в канал ${channel.name}")
        }
        val exception = Consumer<Throwable> { _ ->
            instance.logger.warning("Не удалось отправить сообщение в канал ${channel.name}")
        }
        if (discordId == null) {
            channel.sendMessage("$nickname купил разбан").queue(callback, exception)
        } else {
            channel.sendMessage("<@$discordId> купил разбан").queue(callback, exception)
        }
    }

    fun sendItemMessage(discordId: String?, nickname: String?, amount: Int) {
        val guild = jda.getGuildById("1278106662267523072")!!
        val channel: TextChannel? = guild.getTextChannelById("1289534885371052154")
        if (channel == null) {
            instance.logger.warning("Не удалось получить канал items")
            return
        }
        val callback = Consumer<Message> { _ ->
            instance.logger.info("Сообщение успешно отправлено в канал ${channel.name}")
        }
        val exception = Consumer<Throwable> { _ ->
            instance.logger.warning("Не удалось отправить сообщение в канал ${channel.name}")
        }
        if (discordId == null) {
            channel.sendMessage("$nickname купил кастомный предмет ($amount шт)").queue(callback, exception)
        } else {
            channel.sendMessage("<@$discordId> купил кастомный предмет ($amount шт)").queue(callback, exception)
        }
    }

    fun sendDeathMessage(discordId: String) {
        jda.retrieveUserById(discordId).queue { user: User ->
            user.openPrivateChannel().queue { privateChannel: PrivateChannel ->
                val callback = Consumer<Message> { _ ->
                    instance.logger.info("Сообщение о смерти успешно отправлено в лс игроку $discordId")
                }
                val exception = Consumer<Throwable> { _ ->
                    instance.logger.warning("Не удалось отправить сообщение о смерти в лс игроку $discordId")
                }
                privateChannel.sendMessage("Вы умерли.").queue(callback, exception)
            }
        }
    }
}