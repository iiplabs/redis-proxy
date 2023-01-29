package com.iiplabs.rp.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringUtilTest {

    @Test
    fun testGoodLastField() {
        val source = "card.expiry"
        assertEquals("expiry", StringUtil.getLastField(source))
    }

    @Test
    fun testBadLastField() {
        val source = "expiry"
        assertEquals("expiry", StringUtil.getLastField(source))
    }

}