package com.iiplabs.rp.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SendMessageRequestDto(
        @field:NotNull(message = "{validation.invalid_source_phone}")
        @field:Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_source_phone}")
        @JsonProperty("msisdnB")
        val sourcePhone: String?,

        @field:NotNull(message = "{validation.invalid_dest_phone}")
        @field:Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_dest_phone}")
        @JsonProperty("msisdnA")
        val destinationPhone: String?,

        @field:NotNull(message = "{validation.invalid_sms_text}")
        @field:Size(min = 1, max = 100, message = "{validation.invalid_sms_text}")
        val text: String?
)