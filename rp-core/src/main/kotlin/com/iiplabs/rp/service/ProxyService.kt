package com.iiplabs.rp.service

import com.iiplabs.rp.legacy.CallSession
import com.iiplabs.rp.model.SaveCallSessionRequest
import com.iiplabs.rp.repositories.ICallSessionsRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

import org.springframework.beans.factory.annotation.Qualifier
import ru.osp.collectors.csp.model.redis.CallSessionRedis
import java.util.*

interface ProxyService {
    fun findCallSession(key: String): List<CallSession>
    fun saveCallSession(saveCallSessionRequest: SaveCallSessionRequest)
    fun deleteCallSession(key: String)
}

private val logger = KotlinLogging.logger {}

@Service("proxyService")
class ProxyServiceImpl(
    @Qualifier("callSessionsRepository") val callSessionsRepository: ICallSessionsRepository
) : ProxyService {

    override fun findCallSession(key: String): List<CallSession> {
        logger.debug("Looking up session key {}", key)
        var sessions: List<CallSession?> = mutableListOf()

        val oSessionRedis: Optional<CallSessionRedis> = callSessionsRepository.findById(key)
        if (oSessionRedis.isPresent) {
            logger.debug("Found session for key {}", key)
            sessions += oSessionRedis.get().callSession
        }

        return sessions.mapNotNull { it }
    }

    override fun saveCallSession(saveCallSessionRequest: SaveCallSessionRequest) {
        logger.debug("Saving session for key {}", saveCallSessionRequest.key)
        callSessionsRepository.save(
            CallSessionRedis(
            saveCallSessionRequest.key,
            saveCallSessionRequest.callSession,
            saveCallSessionRequest.timeout
        )
        )
        logger.debug("Saved session for key {}", saveCallSessionRequest.key)
    }

    override fun deleteCallSession(key: String) {
        logger.debug("Deleting session for key {}", key)
        callSessionsRepository.deleteById(key)
        logger.debug("Deleted session for key {}", key)
    }

}
