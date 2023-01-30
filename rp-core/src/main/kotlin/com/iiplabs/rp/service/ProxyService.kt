package com.iiplabs.rp.service

import com.iiplabs.rp.legacy.CallSession
import com.iiplabs.rp.model.SaveCallSessionRequest
import com.iiplabs.rp.repositories.ICallSessionsRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import ru.osp.collectors.csp.model.redis.CallSessionRedis
import java.util.*

const val CALL_SESSION_PREFIX = "call-session"

private val logger = KotlinLogging.logger {}

interface ProxyService {
    fun findCallSession(key: String): List<CallSession>
    fun saveCallSession(saveCallSessionRequest: SaveCallSessionRequest)
    fun deleteCallSession(key: String)
}

@Service("proxyService")
class ProxyServiceImpl(
    @Qualifier("callSessionsRepository")
    val callSessionsRepository: ICallSessionsRepository,
    @Value("\${redis.appPrefix}")
    val appPrefix: String
) : ProxyService {

    override fun findCallSession(key: String): List<CallSession> {
        val translatedKey: String = getTranslatedKey(key)

        logger.debug("Looking up session key {}", translatedKey)
        var sessions: List<CallSession?> = mutableListOf()

        val oSessionRedis: Optional<CallSessionRedis> = callSessionsRepository.findById(translatedKey)
        if (oSessionRedis.isPresent) {
            logger.debug("Found session for key {}", translatedKey)
            sessions += oSessionRedis.get().callSession
        }

        return sessions.mapNotNull { it }
    }

    override fun saveCallSession(saveCallSessionRequest: SaveCallSessionRequest) {
        val translatedKey: String = getTranslatedKey(saveCallSessionRequest.key)

        logger.debug("Saving session for key {}", saveCallSessionRequest.key)
        callSessionsRepository.save(
            CallSessionRedis(
                translatedKey,
                saveCallSessionRequest.callSession,
                saveCallSessionRequest.timeout
            )
        )
        logger.debug("Saved session for key {}", translatedKey)
    }

    override fun deleteCallSession(key: String) {
        val translatedKey: String = getTranslatedKey(key)

        logger.debug("Deleting session for key {}", translatedKey)
        callSessionsRepository.deleteById(translatedKey)
        logger.debug("Deleted session for key {}", translatedKey)
    }

    private fun getTranslatedKey(key: String): String {
        return "$appPrefix:$CALL_SESSION_PREFIX:$key"
    }

}
