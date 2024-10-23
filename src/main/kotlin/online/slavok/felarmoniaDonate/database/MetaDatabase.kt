package online.slavok.felarmoniaDonate.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

class MetaDatabase (
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

    fun getMeta(nickname: String): Map<String, String> {
        return getConnection().use { connection ->
            connection.prepareStatement("SELECT * FROM meta WHERE nickname = ?;").use { ps ->
                ps.setString(1, nickname)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        val metaData = rs.metaData
                        val columnCount = metaData.columnCount
                        (1..columnCount)
                            .map { metaData.getColumnName(it) to rs.getString(it) }
                            .filter { it.first.lowercase() != "nickname" }
                            .toMap()
                    } else {
                        emptyMap()
                    }
                }
            }
        }
    }

    fun setMeta(nickname: String, metaKey: String, metaValue: String) {
        getConnection().use { connection ->
            connection.prepareStatement("INSERT INTO meta (nickname, $metaKey) VALUES (?, ?) ON DUPLICATE KEY UPDATE $metaKey = ?;").use { ps ->
                ps.setString(1, nickname)
                ps.setString(2, metaValue)
                ps.setString(3, metaValue)
                ps.executeUpdate()
            }
        }
    }
}