package com.iiplabs.rp.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.osp.collectors.csp.model.redis.CallSessionRedis

@Repository("callSessionsRepository")
interface ICallSessionsRepository: CrudRepository<CallSessionRedis, String> {
}
