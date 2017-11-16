package me.diax.diax.data.config.entities;

import lombok.Data;

import static com.rethinkdb.RethinkDB.r;
import static com.rethinkdb.net.Connection.Builder;

@Data
public class DbConfig {
    private String hostname, dbname;
    private Integer port;
    private String user, password;

    public Builder configure() {
        Builder b = r.connection();
        if (hostname != null) b.hostname(hostname);
        if (hostname != null) b.db(dbname);
        if (port != null) b.port(port);
        if (user != null) b.user(user, password);
        return b;
    }
}
