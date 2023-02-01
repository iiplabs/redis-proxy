package com.iiplabs.rp.config

import io.lettuce.core.ClientOptions
import io.lettuce.core.TimeoutOptions
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import io.lettuce.core.resource.DefaultEventLoopGroupProvider
import io.netty.util.concurrent.DefaultEventExecutorGroup
import io.netty.util.concurrent.RejectedExecutionHandler
import io.netty.util.concurrent.SingleThreadEventExecutor
import mu.KotlinLogging
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import java.time.Duration
import java.util.concurrent.RejectedExecutionException

private val logger = KotlinLogging.logger {}

@Configuration
class ProxyConfiguration {

    @Bean(name = ["clientResources"], destroyMethod = "shutdown")
    fun clientResources(redisConfigData: RedisConfigData): ClientResources {
        return DefaultClientResources.builder()
            // для I/O операций, альтернативой является настройка ioThreadPoolSize(poolSize)
            .eventLoopGroupProvider(DefaultEventLoopGroupProvider(
                redisConfigData.ioThreadPoolSize
            ) { poolName -> CustomizableThreadFactory("redis-io-loop-$poolName") })
            // для вычислительных операций, альтернативой является настройка computationThreadPoolSize(poolSize)
            .eventExecutorGroup(
                DefaultEventExecutorGroup(
                    redisConfigData.computationThreadPoolSize,
                    CustomizableThreadFactory("redis-computation-executor-"),
                    redisConfigData.maxPendingTasks, object : RejectedExecutionHandler {
                        val logger = LoggerFactory.getLogger("redisExecutorRejectLogger")
                        override fun rejected(task: Runnable, executor: SingleThreadEventExecutor) {
                            logger.warn("task rejected")
                            throw RejectedExecutionException()
                        }
                    }
                )
            ).build()
    }

    @Bean("clientOptions")
    fun clientOptions(redisConfigData: RedisConfigData): ClientOptions {
        return ClientOptions.builder()
            .timeoutOptions(TimeoutOptions.enabled(Duration.ofMillis(redisConfigData.timeout)))
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .autoReconnect(true)
            .build()
    }

    @Bean("lettucePoolConfig")
    fun lettucePoolConfig(
        @Qualifier("clientOptions") clientOptions: ClientOptions,
        @Qualifier("clientResources") clientResources: ClientResources,
        redisConfigData: RedisConfigData
    ): LettucePoolingClientConfiguration {
        val poolConfig = GenericObjectPoolConfig<Any>()
        poolConfig.maxIdle = redisConfigData.maxIdle
        poolConfig.minIdle = redisConfigData.minIdle
        poolConfig.maxTotal = redisConfigData.maxTotal
        return LettucePoolingClientConfiguration.builder()
            .poolConfig(poolConfig)
            .clientOptions(clientOptions)
            .clientResources(clientResources)
            .commandTimeout(Duration.ofMillis(redisConfigData.timeout))
            .build()
    }

    @Bean("redisConfig")
    fun redisConfig(redisConfigData: RedisConfigData): RedisConfiguration {
        logger.info("Redis host: {}", redisConfigData.hosts)

        val configuration = RedisStandaloneConfiguration(redisConfigData.hosts)
        // если username == null, то будет использоваться пользователь default
        configuration.username = redisConfigData.username
        redisConfigData.password?.let { password -> configuration.password = RedisPassword.of(password) }
        return configuration
    }

    @Bean("redisConnectionFactory")
    fun redisConnectionFactory(
        @Qualifier("redisConfig") redisConfig: RedisConfiguration,
        @Qualifier("lettucePoolConfig") lettucePoolConfig: LettucePoolingClientConfiguration
    ): RedisConnectionFactory {
        return LettuceConnectionFactory(redisConfig, lettucePoolConfig)
    }

    @Primary
    @Bean("redisTemplate")
    fun redisTemplate(@Qualifier("redisConnectionFactory") redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(redisConnectionFactory)

        template.keySerializer = JdkSerializationRedisSerializer()
        template.valueSerializer = JdkSerializationRedisSerializer()
        template.hashKeySerializer = JdkSerializationRedisSerializer()
        template.hashValueSerializer = JdkSerializationRedisSerializer()

        return template
    }

}
