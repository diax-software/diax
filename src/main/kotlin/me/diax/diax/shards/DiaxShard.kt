package me.diax.diax.shards

import com.google.common.util.concurrent.ThreadFactoryBuilder

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DiaxShard(shardId: Int, totalShards: Int) {
    val threadPool: ExecutorService
    val commandPool: ExecutorService

    init {
        threadPool = Executors.newCachedThreadPool(
                ThreadFactoryBuilder()
                        .setNameFormat("Diax-Executor[$shardId/$totalShards] Thread-%d")
                        .build()
        )

        commandPool = Executors.newCachedThreadPool(
                ThreadFactoryBuilder()
                        .setNameFormat("Diax-Command[$shardId/$totalShards] Thread-%d")
                        .build()
        )
    }
}
