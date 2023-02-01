package com.iiplabs.rp.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.osp.collectors.csp.model.redis.CallSessionRedis
import java.util.*

@Repository("callSessionsRepository")
interface ICallSessionsRepository: CrudRepository<CallSessionRedis, String> {
    @Transactional(readOnly = true)
    fun findByKey(key: String): Optional<CallSessionRedis>
}
