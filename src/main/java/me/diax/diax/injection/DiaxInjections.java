package me.diax.diax.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.rethinkdb.pool.ConnectionPool;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.data.config.ConfigManager;
import me.diax.diax.data.config.entities.Config;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import static com.rethinkdb.RethinkDB.r;

public class DiaxInjections extends AbstractModule {
    private final CommandHandler handler;
    private final ConfigManager manager;

    public DiaxInjections(CommandHandler handler, ConfigManager manager) {
        this.handler = handler;
        this.manager = manager;
    }

    @Override
    protected void configure() {
        Config config = manager.getConfig();

        bind(ConfigManager.class)
            .toInstance(manager);

        bind(ConnectionPool.class)
            .toInstance(r.connectionPool(config.getDatabase().configure()));

        bind(Config.class)
            .toProvider(manager);

        bind(CommandHandler.class)
            .toInstance(handler);

        bind(String.class)
            .annotatedWith(Names.named("prefix"))
            .toInstance(config.getPrefix());

        mapConstants(config.getTokens(), "token");
        mapConstants(config.getChannels(), "channel");
    }

    @SuppressWarnings("unchecked")
    private void mapConstants(Object object, String prefix) {
        try {
            for (PropertyDescriptor p : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()) {
                Object result = p.getReadMethod().invoke(object);

                if (result == null) continue;

                bind(((Class<Object>) p.getPropertyType()))
                    .annotatedWith(Names.named(prefix + "." + p.getName()))
                    .toInstance(result);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Injector toInjector() {
        return Guice.createInjector(this);
    }
}
