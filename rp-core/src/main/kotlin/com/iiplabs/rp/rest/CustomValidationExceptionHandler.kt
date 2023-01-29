package com.iiplabs.rp.rest

import com.iiplabs.rp.model.SendMessageValidationResponseDto
import com.iiplabs.rp.utils.StringUtil

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class CustomValidationExceptionHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationExceptions(
        ex: WebExchangeBindException
    ): ResponseEntity<SendMessageValidationResponseDto> {
        logger.error { ex }

        val errors: Map<String, String?> =
            ex.bindingResult.allErrors.associateBy(
                { StringUtil.getLastField((it as FieldError).field) },
                { it.defaultMessage })

        val sendMessageValidationResponseDto = SendMessageValidationResponseDto(errors)
        logger.error { "Encountered data validation problem: ${sendMessageValidationResponseDto}" }
        return ResponseEntity(sendMessageValidationResponseDto, HttpStatus.BAD_REQUEST)
    }

}
