package com.iiplabs.rp.rest

import com.iiplabs.rp.legacy.CallSession
import com.iiplabs.rp.model.SaveCallSessionRequest
import jakarta.validation.Valid

import com.iiplabs.rp.service.ProxyService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1")
@RestController
class ProxyController(
    @Qualifier("proxyService") val proxyService: ProxyService
) {

    /**
     * Method looks up CallSession object in Redis for given key
     * @param key - session key
     * @return HTTP 200 and the collection of call sessions
     * or HTTP 404 and empty collection if no session found
     */
    @GetMapping("/find-call-session")
    fun findCallSessions(@RequestParam key: String): ResponseEntity<List<CallSession>> {
        val sessions = proxyService.findCallSession(key)
        if (sessions.isEmpty()) {
            return ResponseEntity(sessions, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(sessions)
    }

    @PostMapping("/save-call-session")
    fun saveCallSession(@Valid @RequestBody saveCallSessionRequest: SaveCallSessionRequest) {
        proxyService.saveCallSession(saveCallSessionRequest)
    }

    /**
     * Method deletes CallSession from Redis for given key
     * @param key - session key
     */
    @DeleteMapping("/delete-call-session")
    fun deleteCallSession(@RequestParam key: String) {
        proxyService.deleteCallSession(key)
    }

}
