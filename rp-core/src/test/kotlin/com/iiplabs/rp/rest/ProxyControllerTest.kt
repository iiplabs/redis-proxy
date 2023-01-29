package com.iiplabs.rp.rest

import com.iiplabs.rp.RpApplication
import com.iiplabs.rp.model.SendMessageRequestDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.iiplabs.rp.model.SendMessageResponseDto
import com.iiplabs.rp.model.SendMessageStatus
import com.iiplabs.rp.model.SendMessageValidationResponseDto
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@ActiveProfiles("test")
@SpringBootTest(classes = arrayOf(RpApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProxyControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    val objectMapper = jacksonObjectMapper()

    private val testJsonSendMessageRequestDto =
        "{\"msisdnB\":\"79031112233\",\"msisdnA\":\"79063332211\",\"text\":\"Этот абонент снова в сети\"}"

    private val SEND_SMS_END_POINT = "/api/v1/sendSms"

    @Test
    fun contextLoads() {
        //
    }

    @Test
    fun testSendSmsHappyPath() {
        val result: ResponseEntity<SendMessageResponseDto>? = restTemplate.postForEntity(SEND_SMS_END_POINT,
            getMockSendMessageRequestDto(), SendMessageResponseDto::class.java);

        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals(SendMessageStatus.OK, result?.body?.status)
    }

    @Test
    fun testSendSmsEmptyPhone() {
        val result: ResponseEntity<SendMessageValidationResponseDto>? = restTemplate.postForEntity(SEND_SMS_END_POINT,
            getMockSendMessageRequestDtoEmptyPhone(), SendMessageValidationResponseDto::class.java);

        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
        assertNotNull(result?.body?.errors)
    }

    @Test
    fun testSendSmsMissingPhone() {
        val result: ResponseEntity<SendMessageValidationResponseDto>? = restTemplate.postForEntity(SEND_SMS_END_POINT,
            getMockSendMessageRequestDtoMissingPhone(), SendMessageValidationResponseDto::class.java);

        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
        assertNotNull(result?.body?.errors)
    }

    @Test
    fun testSendSmsMissingText() {
        val result: ResponseEntity<SendMessageValidationResponseDto>? = restTemplate.postForEntity(SEND_SMS_END_POINT,
            getMockSendMessageRequestDtoMissingText(), SendMessageValidationResponseDto::class.java);

        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
        assertNotNull(result?.body?.errors)
    }

    @Test
    fun serializationTest() {
        val sendMessageRequestDto: SendMessageRequestDto = objectMapper.readValue(
            testJsonSendMessageRequestDto,
            SendMessageRequestDto::class.java
        )
        val serializedPaymentDtoAsJson = objectMapper.writeValueAsString(sendMessageRequestDto)
        assertEquals(serializedPaymentDtoAsJson, testJsonSendMessageRequestDto)
    }

    @Test
    fun deserializationTest() {
        val deserializedSendMessageRequestDto: SendMessageRequestDto = objectMapper.readValue(
            testJsonSendMessageRequestDto,
            SendMessageRequestDto::class.java
        )
        val sendMessageRequestDto: SendMessageRequestDto = getMockSendMessageRequestDto()
        assertEquals(deserializedSendMessageRequestDto, sendMessageRequestDto)
    }

    private fun getMockSendMessageRequestDto(): SendMessageRequestDto {
        return SendMessageRequestDto("79031112233",
            "79063332211", "Этот абонент снова в сети")
    }

    private fun getMockSendMessageRequestDtoEmptyPhone(): SendMessageRequestDto {
        return SendMessageRequestDto("",
            "79063332211", "Этот абонент снова в сети")
    }

    private fun getMockSendMessageRequestDtoMissingPhone(): SendMessageRequestDto {
        return SendMessageRequestDto(null,
            "79063332211", "Этот абонент снова в сети")
    }

    private fun getMockSendMessageRequestDtoMissingText(): SendMessageRequestDto {
        return SendMessageRequestDto("79031112233",
            "79063332211", null)
    }

}
