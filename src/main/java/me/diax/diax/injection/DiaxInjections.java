package me.diax.diax.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.util.Data;

public class DiaxInjections extends AbstractModule {
    private final CommandHandler handler;
    private final Data data;

    public DiaxInjections(CommandHandler handler, Data data) {
        this.handler = handler;
        this.data = data;
    }

    @Override
    protected void configure() {
        bind(String.class)
            .annotatedWith(Names.named("prefix"))
            .toInstance(data.getPrefix());

        bind(Data.class).toInstance(data);

        bind(CommandHandler.class).toInstance(handler);
    }

    public Injector toInjector() {
        return Guice.createInjector(this);
    }
}
