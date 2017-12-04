package me.diax.diax.commands

import me.diax.diax.listeners.CommandListener
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.User

enum class CommandPermission {
    USER {
        override fun test(listener: CommandListener, user: User): Boolean = true

        override fun test(listener: CommandListener, member: Member): Boolean = true
    },

    ADMIN {
        override fun test(listener: CommandListener, user: User): Boolean {
            return when {
                DEVELOPER.test(listener, user) -> true
                else -> false
            }
        }

        override fun test(listener: CommandListener, member: Member): Boolean {
            return when {
                member.isOwner -> true
                member.hasPermission(Permission.ADMINISTRATOR) -> true
                member.hasPermission(Permission.MANAGE_SERVER) -> true

                DEVELOPER.test(listener, member) -> true
                else -> false
            }
        }
    },

    DEVELOPER {
        override fun test(listener: CommandListener, user: User): Boolean {
            return listener.config.developers.contains(user.id)
        }

        override fun test(listener: CommandListener, member: Member): Boolean {
            return test(listener, member.user)
        }
    };

    abstract fun test(listener: CommandListener, member: Member): Boolean
    abstract fun test(listener: CommandListener, user: User): Boolean

    override fun toString(): String {
        val name = name.toLowerCase()
        return Character.toUpperCase(name[0]) + name.substring(1)
    }
}
