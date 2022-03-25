package com.example.crypto.model.api


import org.junit.Assert.assertEquals
import org.junit.Test

class TestMockResponseFileReader {
    @Test
    fun `read simple file`() {
        val reader = MockResponseFileReader("test_json.json")
        assertEquals(reader.content, "{\"test\": {}}")
    }
}