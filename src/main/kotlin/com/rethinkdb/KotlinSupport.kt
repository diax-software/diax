package com.rethinkdb

import com.rethinkdb.ast.ReqlAst
import com.rethinkdb.gen.ast.*
import com.rethinkdb.pool.ConnectionPool
import java.lang.Exception
import java.util.function.Function

operator fun ReqlExpr.get(any: Any): GetField = g(any)

operator fun ReqlExpr.plus(any: Any): Add = add(any)

operator fun ReqlExpr.minus(any: Any): Sub = sub(any)

operator fun ReqlExpr.times(any: Any): Mul = mul(any)

operator fun ReqlExpr.div(any: Any): Div = div(any)

operator fun ReqlExpr.rem(any: Any): Mod = mod(any)

infix fun ReqlExpr.gt(any: Any): Gt = gt(any)

infix fun ReqlExpr.lt(any: Any): Lt = lt(any)

fun GetAll.onIndex(s: String): GetAll = optArg("index", s)

fun <T> ReqlAst.run(pool: ConnectionPool): T = pool.run<T>(this)

fun <T> ReqlAst.run(pool: ConnectionPool, c: Class<*>): T = pool.run<T>(this, c)

fun <T> ReqlAst.run(pool: ConnectionPool, c: Class<*>, onFailure: Function<Exception, T>): T = pool.run<T>(this, c, onFailure)

