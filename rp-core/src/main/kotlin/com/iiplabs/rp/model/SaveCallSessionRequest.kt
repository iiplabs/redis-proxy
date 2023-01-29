package com.iiplabs.rp.model

import com.iiplabs.rp.legacy.CallSession
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class SaveCallSessionRequest(
    @field:NotNull(message = "{validation.invalid_key}")
    @field:Size(min = 1, max = 100, message = "{validation.invalid_key}")
    val key: String,

    @field:NotNull(message = "{validation.invalid_session}")
    val callSession: CallSession,

    // minimum timeout 1
    @field:Min(1, message="{validation.invalid_timeout}")
    @field:NotNull(message = "{validation.invalid_timeout}")
    val timeout: Long
)
