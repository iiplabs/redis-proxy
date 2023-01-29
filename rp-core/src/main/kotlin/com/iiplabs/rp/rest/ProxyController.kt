package com.iiplabs.rp.rest

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import com.iiplabs.rp.model.SendMessageRequestDto
import com.iiplabs.rp.model.SendMessageResponseDto

import com.iiplabs.rp.service.ProxyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/v1")
@RestController
class ProxyController {

    @Qualifier("proxyService")
    @Autowired
    private lateinit var proxyService: ProxyService

    @PostMapping("/sendSms")
    suspend fun sendSms(@Valid @RequestBody sendMessageRequestDto: SendMessageRequestDto): SendMessageResponseDto {
        proxyService.send(sendMessageRequestDto)
        return SendMessageResponseDto()
    }

}
