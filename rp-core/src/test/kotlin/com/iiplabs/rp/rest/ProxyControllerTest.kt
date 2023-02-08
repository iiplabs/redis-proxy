package com.iiplabs.rp.rest

import com.iiplabs.rp.RpApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.iiplabs.rp.legacy.CallSession
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import ru.osp.collectors.csp.model.redis.CallSessionRedis

@ActiveProfiles("test")
@SpringBootTest(classes = [RpApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ProxyControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    val objectMapper = jacksonObjectMapper()

    private val GET_SESSION_END_POINT = "/api/v1/find-call-session?key={key}"
    private val SAVE_SESSION_END_POINT = "/api/v1/save-call-session"
    private val DELETE_SESSION_END_POINT = "/api/v1/delete-call-session?key={key}"

    @Test
    fun contextLoads() {
        //
    }

    @Test
    fun testProxyHappyPath() {
        val key = "12345"
        val callSessionId = 12345

        // get call session
        val getResult = restTemplate.getForEntity(GET_SESSION_END_POINT, Array<CallSession>::class.java, key)
        assertNotNull(getResult)
        assertEquals(HttpStatus.NOT_FOUND, getResult?.statusCode)
        assertEquals(0, getResult.body?.toList()?.size)

        // save call session
        val saveResult = restTemplate.postForEntity(SAVE_SESSION_END_POINT,
            getMockCallSessionRedis(key, callSessionId), String::class.java);
        assertNotNull(saveResult)
        assertEquals(HttpStatus.CREATED, saveResult?.statusCode)

        // get call session
        val getResultAfterSave = restTemplate.getForEntity(GET_SESSION_END_POINT, Array<CallSession>::class.java, key)
        assertNotNull(getResultAfterSave)
        assertEquals(HttpStatus.OK, getResultAfterSave?.statusCode)
        assertEquals(1, getResultAfterSave.body?.toList()?.size)
        assertEquals(callSessionId, getResultAfterSave.body?.toList()?.get(0)!!.callSessionId)

        // delete call session
        restTemplate.delete(DELETE_SESSION_END_POINT, key)

        // get call session
        val getResultAfterDelete = restTemplate.getForEntity(GET_SESSION_END_POINT, Array<CallSession>::class.java, key)
        assertNotNull(getResultAfterDelete)
        assertEquals(HttpStatus.NOT_FOUND, getResultAfterDelete?.statusCode)
        assertEquals(0, getResultAfterDelete.body?.toList()?.size)
    }

    @Test
    fun testProxyValidationError() {
        val key = ""
        val callSessionId = 12345

        // save call session
        val saveResult: ResponseEntity<String>? = restTemplate.postForEntity(SAVE_SESSION_END_POINT,
            getMockCallSessionRedis(key, callSessionId), String::class.java);
        assertNotNull(saveResult)
        assertEquals(HttpStatus.BAD_REQUEST, saveResult?.statusCode)
    }

    private fun getMockCallSessionRedis(key: String, callSessionId: Int): CallSessionRedis {
        return CallSessionRedis(key,
            CallSession(callSessionId, callSessionId), 100)
    }

}
