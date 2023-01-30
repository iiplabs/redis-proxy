package com.iiplabs.rp.rest

import com.iiplabs.rp.RpApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.annotation.DirtiesContext

@ActiveProfiles("test")
@SpringBootTest(classes = [RpApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ProxyControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    val objectMapper = jacksonObjectMapper()

    @Test
    fun contextLoads() {
        //
    }

}
