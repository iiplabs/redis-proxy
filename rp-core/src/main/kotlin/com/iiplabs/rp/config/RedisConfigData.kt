package com.iiplabs.rp.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "redis")
data class RedisConfigData (
    val hosts: String,
    val username: String,
    val password: String,
    val minIdle: Int,
    val maxIdle: Int,
    val maxTotal: Int,
    val timeout: Long,
    val ioThreadPoolSize: Int,
    val computationThreadPoolSize: Int,
    val maxPendingTasks: Int
)