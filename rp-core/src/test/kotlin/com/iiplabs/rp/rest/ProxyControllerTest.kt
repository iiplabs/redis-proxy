package com.iiplabs.rp.rest

import com.iiplabs.rp.RpApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.web.client.TestRestTemplate

@ActiveProfiles("test")
@SpringBootTest(classes = arrayOf(RpApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProxyControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    val objectMapper = jacksonObjectMapper()

    @Test
    fun contextLoads() {
        //
    }

}
