package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(200, status.value)
            assertEquals("Welcome to Ktor OpenTelemetry Application!", bodyAsText())
        }
    }

    @Test
    fun testHealth() = testApplication {
        application {
            module()
        }
        client.get("/health").apply {
            assertEquals(200, status.value)
            assertTrue(bodyAsText().contains("UP"))
        }
    }
} 