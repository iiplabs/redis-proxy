package ru.osp.collectors.csp.model.redis

import com.iiplabs.rp.legacy.CallSession
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash("call-sessions")
data class CallSessionRedis(
    @field:NotNull(message = "{validation.invalid_key}")
    @field:Size(min = 1, max = 100, message = "{validation.invalid_key}")
    @Indexed
    var key: String,

    @field:NotNull(message = "{validation.invalid_session}")
    var callSession: CallSession,

    // minimum timeout 1
    @field:Min(1, message="{validation.invalid_timeout}")
    @field:NotNull(message = "{validation.invalid_timeout}")
    @TimeToLive
    var timeout: Long? = null
) {
    @get:Id
    var id: String? = null
}
