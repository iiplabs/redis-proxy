package com.iiplabs.rp.service

import mu.KotlinLogging
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

import com.iiplabs.rp.model.SendMessageRequestDto
import kotlin.time.Duration.Companion.seconds

interface ProxyService {
    suspend fun send(sendMessageRequestDto: SendMessageRequestDto)
}

private val logger = KotlinLogging.logger {}

@Service("proxyService")
class ProxyServiceImpl : ProxyService {

    companion object {
        private val MAX_EXECUTION_DELAY = 5L
    }

    override suspend fun send(sendMessageRequestDto: SendMessageRequestDto) {
        logger.info { "Sending SMS to ${sendMessageRequestDto.destinationPhone} that ${sendMessageRequestDto.sourcePhone} is now available" }

        val randomDelayLong = (1..MAX_EXECUTION_DELAY).shuffled().first().toLong()
        delay(randomDelayLong.seconds)

        logger.info { "Success sending SMS to ${sendMessageRequestDto.destinationPhone}, with delay ${randomDelayLong} seconds" }
    }

}
