package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.OpenTelemetry
import kotlinx.coroutines.delay
import kotlin.random.Random

fun Route.healthRoutes() {
    val tracer: Tracer = OpenTelemetry.get().getTracer("ktor-otel-app")
    
    route("/health") {
        get {
            val span = tracer.spanBuilder("health-check").startSpan()
            span.use {
                span.setAttribute("endpoint", "/health")
                span.setAttribute("method", "GET")
                
                call.respond(
                    mapOf(
                        "status" to "UP",
                        "service" to "ktor-otel-app",
                        "version" to "1.0.0",
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }
        }
        
        get("/ready") {
            val span = tracer.spanBuilder("readiness-check").startSpan()
            span.use {
                span.setAttribute("endpoint", "/health/ready")
                span.setAttribute("method", "GET")
                
                // Simulate readiness check
                delay(Random.nextLong(10, 50))
                
                call.respond(
                    mapOf(
                        "status" to "READY",
                        "service" to "ktor-otel-app",
                        "dependencies" to mapOf(
                            "database" to "UP",
                            "external-api" to "UP"
                        ),
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }
        }
        
        get("/live") {
            val span = tracer.spanBuilder("liveness-check").startSpan()
            span.use {
                span.setAttribute("endpoint", "/health/live")
                span.setAttribute("method", "GET")
                
                call.respond(
                    mapOf(
                        "status" to "ALIVE",
                        "service" to "ktor-otel-app",
                        "uptime" to "running",
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }
        }
    }
} 