package com.rethinkdb.pool;

import com.rethinkdb.ast.ReqlAst;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Connection.Builder;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConnectionPool {
    private final Connection.Builder builder;
    private final Queue<Connection> connections;

    public ConnectionPool(Builder builder, int capacity) {
        this.builder = builder;
        this.connections = new LinkedBlockingQueue<>(capacity);
    }

    public ConnectionPool(Builder builder) {
        this.builder = builder;
        this.connections = new LinkedBlockingQueue<>();
    }

    private void requeue(Connection connection) {
        if (!connection.isOpen()) return;

        if (!connections.offer(connection)) {
            connection.close();
        }
    }

    private Connection retrieve() {
        Connection c = connections.poll();
        if (c == null) return builder.connect();

        if (!c.isOpen()) c.reconnect();
        return c;
    }

    public void run(Consumer<Connection> consumer) {
        Connection c = retrieve();
        try {
            consumer.accept(c);
        } finally {
            requeue(c);
        }
    }

    public <T> T run(ReqlAst ast) {
        Connection c = retrieve();
        try {
            return ast.run(c);
        } finally {
            requeue(c);
        }
    }

    public <T> T run(ReqlAst ast, Class<?> c) {
        Connection conn = retrieve();
        try {
            return ast.run(conn, c);
        } finally {
            requeue(conn);
        }
    }

    public <T> T runAndReturn(Function<Connection, T> consumer) {
        Connection c = retrieve();
        try {
            return consumer.apply(c);
        } finally {
            requeue(c);
        }
    }

    public <A> void runWithArgs(A a, BiConsumer<Connection, A> consumer) {
        Connection c = retrieve();
        try {
            consumer.accept(c, a);
        } finally {
            requeue(c);
        }
    }

    public <A, T> T runWithArgsAndReturn(A a, BiFunction<Connection, A, T> consumer) {
        Connection c = retrieve();
        try {
            return consumer.apply(c, a);
        } finally {
            requeue(c);
        }
    }
}
