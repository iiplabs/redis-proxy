package com.iiplabs.rp.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class ProxyConfigurationTest(redisConfigData: RedisConfigData) {

    lateinit var redisServer: RedisServer

    init {
        var port = 6379
        val hostConfig: List<String> = redisConfigData.hosts.split(":")
        if (hostConfig.size > 1) {
            port = hostConfig[1].toInt()
        }
        redisServer = RedisServer(port)
    }

    @PostConstruct
    fun postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }

}