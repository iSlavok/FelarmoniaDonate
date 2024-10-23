package online.slavok.felarmoniaDonate.utils

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.PermissionNode
import java.util.*


class Luckperms {
    fun addPermission(player: UUID, permission: String) {
        val api: LuckPerms = LuckPermsProvider.get()
        val node = PermissionNode.builder(permission).value(true).build()
        var user: User? = api.userManager.getUser(player)
        if (user != null) {
            user.data().add(node)
            api.userManager.saveUser(user)
        } else {
            user = api.userManager.loadUser(player).join()
            user.data().add(node)
            api.userManager.saveUser(user)
        }
    }

    fun removePermission(player: UUID, permission: String) {
        val api: LuckPerms = LuckPermsProvider.get()
        val node = PermissionNode.builder(permission).build()
        var user: User? = api.userManager.getUser(player)
        if (user != null) {
            user.data().remove(node)
            api.userManager.saveUser(user)
        } else {
            user = api.userManager.loadUser(player).join()
            user.data().remove(node)
            api.userManager.saveUser(user)
        }
    }

    fun addGroup(player: UUID, group: String) {
        val api: LuckPerms = LuckPermsProvider.get()
        val node = Node.builder("group.$group").value(true).build()
        var user: User? = api.userManager.getUser(player)
        if (user != null) {
            user.data().add(node)
            api.userManager.saveUser(user)
        } else {
            user = api.userManager.loadUser(player).join()
            user.data().add(node)
            api.userManager.saveUser(user)
        }
    }

    fun removeGroup(player: UUID, group: String) {
        val api: LuckPerms = LuckPermsProvider.get()
        val node = Node.builder("group.$group").build()
        var user: User? = api.userManager.getUser(player)
        if (user != null) {
            user.data().remove(node)
            api.userManager.saveUser(user)
        } else {
            user = api.userManager.loadUser(player).join()
            user.data().remove(node)
            api.userManager.saveUser(user)
        }
    }
}