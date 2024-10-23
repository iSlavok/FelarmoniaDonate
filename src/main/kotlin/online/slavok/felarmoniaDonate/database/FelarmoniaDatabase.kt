package online.slavok.felarmoniaDonate.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

class FelarmoniaDatabase (
    private val mysqlUrl: String
) {
    private val dataSource = createHikariDataSource()

    private fun createHikariDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = mysqlUrl
        config.maximumPoolSize = 10
        config.connectionTimeout = 5000
        config.maxLifetime = 60000
        return HikariDataSource(config)
    }

    private fun getConnection(): Connection {
        return dataSource.connection
    }

    fun getTier(nickname: String): Int {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT tier FROM donate WHERE nickname = ?").use { ps ->
                ps.setString(1, nickname)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getInt("tier")
                    } else {
                        0
                    }
                }
            }
        }
    }

    fun getCount(nickname: String): Int {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT count FROM donate WHERE nickname = ?").use { ps ->
                ps.setString(1, nickname)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getInt("count")
                    } else {
                        0
                    }
                }
            }
        }
    }

    fun getTotal(nickname: String): Int {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT total FROM donate WHERE nickname = ?").use { ps ->
                ps.setString(1, nickname)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getInt("total")
                    } else {
                        0
                    }
                }
            }
        }
    }

    fun setTier(nickname: String, tier: Int) {
        getConnection().use { connection ->
            connection.prepareStatement("INSERT INTO donate (nickname, tier) VALUES (?, ?) ON DUPLICATE KEY UPDATE tier = ?").use { ps ->
                ps.setString(1, nickname)
                ps.setInt(2, tier)
                ps.setInt(3, tier)
                ps.executeUpdate()
            }
        }
    }

    fun setCount(nickname: String, count: Int) {
        getConnection().use { connection ->
            connection.prepareStatement("INSERT INTO donate (nickname, count) VALUES (?, ?) ON DUPLICATE KEY UPDATE count = ?").use { ps ->
                ps.setString(1, nickname)
                ps.setInt(2, count)
                ps.setInt(3, count)
                ps.executeUpdate()
            }
        }
    }

    fun setTotal(nickname: String, total: Int) {
        getConnection().use { connection ->
            connection.prepareStatement("INSERT INTO donate (nickname, total) VALUES (?, ?) ON DUPLICATE KEY UPDATE total = ?").use { ps ->
                ps.setString(1, nickname)
                ps.setInt(2, total)
                ps.setInt(3, total)
                ps.executeUpdate()
            }
        }
    }

    fun getDiscordId(nickname: String): String? {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT discord_id FROM whitelist WHERE nickname = ?").use { ps ->
                ps.setString(1, nickname)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getString("discord_id")
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun getClanPrefix(id: String): String? {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT prefix FROM clans WHERE id = ?").use { ps ->
                ps.setString(1, id)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getString("prefix")
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun getPlayerClan(nickname: String): String? {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT id FROM player_clans WHERE nickname = ?").use { ps ->
                ps.setString(1, nickname)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getString("id")
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun setPlayerClan(nickname: String, id: String) {
        getConnection().use { connection ->
            connection.prepareStatement("INSERT INTO player_clans (nickname, id) VALUES (?, ?) ON DUPLICATE KEY UPDATE id = ?").use { ps ->
                ps.setString(1, nickname)
                ps.setString(2, id)
                ps.setString(3, id)
                ps.executeUpdate()
            }
        }
    }

    fun createClan(id: String, prefix: String, owner: String) {
        getConnection().use { connection ->
            connection.prepareStatement("INSERT INTO clans (id, prefix, owner) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE prefix = ?, owner = ?").use { ps ->
                ps.setString(1, id)
                ps.setString(2, prefix)
                ps.setString(3, owner)
                ps.setString(4, prefix)
                ps.setString(5, owner)
                ps.executeUpdate()
            }
        }
    }

    fun getClanPlayersList(clan: String): List<String> {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT nickname FROM player_clans WHERE id = ?").use { ps ->
                ps.setString(1, clan)
                ps.executeQuery().use { rs ->
                    val list = mutableListOf<String>()
                    while (rs.next()) {
                        list.add(rs.getString("nickname"))
                    }
                    list
                }
            }
        }
    }

    fun getClanOwner(clan: String): String {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT owner FROM clans WHERE id = ?").use { ps ->
                ps.setString(1, clan)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getString("owner")
                    } else {
                        ""
                    }
                }
            }
        }
    }

    fun removePlayerClan(name: String) {
        getConnection().use { connection ->
            connection.prepareStatement("DELETE FROM player_clans WHERE nickname = ?").use { ps ->
                ps.setString(1, name)
                ps.executeUpdate()
            }
        }
    }

    fun deleteClan(id: String) {
        getConnection().use { connection ->
            connection.prepareStatement("DELETE FROM clans WHERE id = ?").use { ps ->
                ps.setString(1, id)
                ps.executeUpdate()
            }
        }
    }

    fun deletePlayersClan(id: String) {
        getConnection().use { connection ->
            connection.prepareStatement("DELETE FROM player_clans WHERE id = ?").use { ps ->
                ps.setString(1, id)
                ps.executeUpdate()
            }
        }
    }
}