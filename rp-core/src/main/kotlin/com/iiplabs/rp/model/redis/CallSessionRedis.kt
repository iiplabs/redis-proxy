package ru.osp.collectors.csp.model.redis

import com.iiplabs.rp.legacy.CallSession
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash("call-sessions")
class CallSessionRedis(
    key: String,
    callSession: CallSession,
    timeout: Long
) {
    @Indexed
    var key: String? = null

    var callSession: CallSession? = null

    @get:Id
    var id: String? = "$key"
        get() = "$field"

    @TimeToLive
    var timeout: Long? = null
}
