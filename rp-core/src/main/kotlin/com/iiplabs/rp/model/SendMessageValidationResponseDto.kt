package com.iiplabs.rp.model

import kotlin.collections.MutableMap

data class SendMessageValidationResponseDto(
    val errors: Map<String, String?>
)