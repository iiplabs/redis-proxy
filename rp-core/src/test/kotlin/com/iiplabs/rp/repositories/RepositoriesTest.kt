package com.iiplabs.rp.repositories

import com.iiplabs.rp.RpApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@ContextConfiguration(classes = [RpApplication::class])
@DataRedisTest
class RepositoriesTest @Autowired constructor (
    @Qualifier("callSessionsRepository") val callSessionsRepository: ICallSessionsRepository) {

    @Test
    fun testInjectedComponents() {
        assertThat(callSessionsRepository).isNotNull
    }
}