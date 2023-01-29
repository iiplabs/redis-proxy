package com.iiplabs.rp.model

data class PostObjectValidationResponseDto(
    val errors: Map<String, String?>
)