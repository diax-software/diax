package me.diax.diax.injection

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.name.Names
import com.rethinkdb.RethinkDB.r
import com.rethinkdb.pool.ConnectionPool
import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.data.config.ConfigManager
import me.diax.diax.data.config.entities.Config
import java.beans.IntrospectionException
import java.beans.Introspector
import java.lang.reflect.InvocationTargetException

class DiaxInjections(private val handler: CommandHandler, private val manager: ConfigManager) : AbstractModule() {

    override fun configure() {
        val config = manager.config

        bind(ConfigManager::class.java)
            .toInstance(manager)

        bind(ConnectionPool::class.java)
            .toInstance(r.connectionPool(config!!.database!!.configure()))

        bind(Config::class.java)
            .toProvider(manager)

        bind(CommandHandler::class.java)
            .toInstance(handler)

        bind(String::class.java)
            .annotatedWith(Names.named("prefix"))
            .toInstance(config.prefix)

        mapConstants(config.tokens, "token")
        mapConstants(config.channels, "channel")
    }

    private fun mapConstants(`object`: Any?, prefix: String) {
        try {
            for (p in Introspector.getBeanInfo(`object`!!.javaClass).propertyDescriptors) {
                val result = p.readMethod.invoke(`object`) ?: continue

                bind(p.propertyType as Class<Any>)
                    .annotatedWith(Names.named(prefix + "." + p.name))
                    .toInstance(result)
            }
        } catch (e: IntrospectionException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }

    fun toInjector(): Injector {
        return Guice.createInjector(this)
    }
}
