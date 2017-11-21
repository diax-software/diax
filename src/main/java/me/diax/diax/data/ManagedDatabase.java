package me.diax.diax.data;

import com.google.inject.Inject;
import com.rethinkdb.pool.ConnectionPool;
import me.diax.diax.data.db.GuildData;
import me.diax.diax.data.db.UserData;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import static com.rethinkdb.RethinkDB.r;

public class ManagedDatabase {
    private final ConnectionPool pool;

    @Inject
    public ManagedDatabase(ConnectionPool pool) {
        this.pool = pool;
    }

    public GuildData get(Guild guild) {
        return get(guild, true);
    }

    public GuildData get(Guild guild, boolean create) {
        return getGuild(guild.getId(), create);
    }

    public UserData get(User user) {
        return get(user, true);
    }

    public UserData get(User user, boolean create) {
        return getUser(user.getId(), create);
    }

    public GuildData getGuild(String id, boolean create) {
        GuildData guild = pool.run(r.table("users").get(id), UserData.class, e -> null);
        return guild == null && create ? new GuildData() : guild;
    }

    public UserData getUser(String id, boolean create) {
        UserData user = pool.run(r.table("users").get(id), UserData.class, e -> null);
        return user == null && create ? new UserData() : user;
    }
}
