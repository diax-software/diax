package com.rethinkdb

import com.rethinkdb.gen.ast.*

operator fun ReqlExpr.get(s: String): Any = this[s]

operator fun ReqlExpr.plus(any: Any): Add = plus(any)

operator fun ReqlExpr.minus(any: Any): Sub = sub(any)

operator fun ReqlExpr.times(any: Any): Mul = mul(any)

operator fun ReqlExpr.div(any: Any): Div = div(any)

operator fun ReqlExpr.rem(exprA: Any): Mod = mod(exprA)

infix fun ReqlExpr.gt(exprA: Any): Gt = gt(exprA)

infix fun ReqlExpr.lt(exprA: Any): Lt = lt(exprA)

fun GetAll.index(s: String): GetAll = optArg("index", s)
