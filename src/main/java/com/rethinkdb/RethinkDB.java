package com.rethinkdb;

import com.rethinkdb.gen.model.TopLevel;
import com.rethinkdb.net.Connection;
import com.rethinkdb.pool.ConnectionPool;

public class RethinkDB extends TopLevel {

    /**
     * The Singleton to use to begin interacting with RethinkDB Driver
     */
    public static final RethinkDB r = new RethinkDB();

    public Connection.Builder connection() {
        return Connection.build();
    }

    public ConnectionPool connectionPool(Connection.Builder builder) {
        return new ConnectionPool(builder);
    }
}
