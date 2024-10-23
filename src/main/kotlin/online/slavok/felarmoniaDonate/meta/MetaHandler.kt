package online.slavok.felarmoniaDonate.meta

import online.slavok.felarmoniaDonate.database.MetaDatabase

class MetaHandler (
    private val database: MetaDatabase
) {
    private val cache: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    fun getMeta(nickname: String, metaKey: String) : String {
        return cache.getOrPut(nickname) { database.getMeta(nickname).toMutableMap() }
            .getOrDefault(metaKey, "")
    }

    fun setMeta(nickname: String, metaKey: String, metaValue: String) {
        cache[nickname] = database.getMeta(nickname).toMutableMap()
        cache[nickname]!![metaKey] = metaValue
        database.setMeta(nickname, metaKey, metaValue)
    }

    fun reload() {
        cache.clear()
    }
}