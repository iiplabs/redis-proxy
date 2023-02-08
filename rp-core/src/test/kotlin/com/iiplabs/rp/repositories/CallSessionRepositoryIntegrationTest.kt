package com.iiplabs.rp.repositories

import com.iiplabs.rp.config.ProxyConfigurationTest
import com.iiplabs.rp.legacy.CallSession
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import ru.osp.collectors.csp.model.redis.CallSessionRedis

@SpringBootTest(classes = [ProxyConfigurationTest::class])
class CallSessionRepositoryIntegrationTest(
    @Qualifier("callSessionsRepository")
    val callSessionsRepository: ICallSessionsRepository) {

    @Test
    fun testSaveSession() {
        val callSession = callSessionsRepository.save(
            CallSessionRedis("12345",
            CallSession(1, 1), 100)
        )
        Assertions.assertNotNull(callSession)
    }

}