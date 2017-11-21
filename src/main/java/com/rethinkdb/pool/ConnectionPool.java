package com.rethinkdb.pool;

import com.google.common.base.Throwables;
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
    private static <T> T propagateException(Exception e) {
        throw Throwables.propagate(e);
    }

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
        run(consumer, ConnectionPool::propagateException);
    }

    public <T> T run(ReqlAst ast) {
        return run(ast, ConnectionPool::propagateException);
    }

    public <T> T run(ReqlAst ast, Class<?> c) {
        return run(ast, c, ConnectionPool::propagateException);
    }

    public void run(Consumer<Connection> consumer, Consumer<Exception> onFailure) {
        Connection c = null;
        try {
            c = retrieve();
            consumer.accept(c);
        } finally {
            if (c != null) requeue(c);
        }
    }

    public <T> T run(ReqlAst ast, Function<Exception, T> onFailure) {
        Connection c = null;
        try {
            c = retrieve();
            return ast.run(c);
        } catch (Exception e) {
            return onFailure.apply(e);
        } finally {
            if (c != null) requeue(c);
        }
    }

    public <T> T run(ReqlAst ast, Class<?> c, Function<Exception, T> onFailure) {
        Connection conn = null;
        try {
            conn = retrieve();
            return ast.run(conn, c);
        } catch (Exception e) {
            return onFailure.apply(e);
        } finally {
            if (conn != null) requeue(conn);
        }
    }

    public <T> T runAndReturn(Function<Connection, T> consumer) {
        return runAndReturn(consumer, ConnectionPool::propagateException);
    }

    public <T> T runAndReturn(Function<Connection, T> consumer, Function<Exception, T> onFailure) {
        Connection c = null;
        try {
            c = retrieve();
            return consumer.apply(c);
        } catch (Exception e) {
            return onFailure.apply(e);
        } finally {
            if (c != null) requeue(c);
        }
    }

    public <A> void runWithArgs(A a, BiConsumer<Connection, A> consumer) {
        runWithArgs(a, consumer, ConnectionPool::propagateException);
    }

    public <A> void runWithArgs(A a, BiConsumer<Connection, A> consumer, Consumer<Exception> onFailure) {
        Connection c = null;
        try {
            c = retrieve();
            consumer.accept(c, a);
        } catch (Exception e) {
            onFailure.accept(e);
        } finally {
            if (c != null) requeue(c);
        }
    }

    public <A, T> T runWithArgsAndReturn(A a, BiFunction<Connection, A, T> consumer) {
        return runWithArgsAndReturn(a, consumer, ConnectionPool::propagateException);
    }

    public <A, T> T runWithArgsAndReturn(A a, BiFunction<Connection, A, T> consumer, Function<Exception, T> onFailure) {
        Connection c = retrieve();
        try {
            return consumer.apply(c, a);
        } catch (Exception e) {
            return onFailure.apply(e);
        } finally {
            if (c != null) requeue(c);
        }
    }
}
