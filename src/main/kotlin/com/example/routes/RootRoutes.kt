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

fun Route.root() {
    val tracer: Tracer = OpenTelemetry.get().getTracer("ktor-otel-app")
    
    get("/") {
        val span = tracer.spanBuilder("root-endpoint").startSpan()
        span.use { 
            span.setAttribute("endpoint", "/")
            span.setAttribute("method", "GET")
            
            call.respondText("Welcome to Ktor OpenTelemetry Application!", ContentType.Text.Plain)
        }
    }
    
    get("/hello/{name}") {
        val name = call.parameters["name"] ?: "World"
        val span = tracer.spanBuilder("hello-endpoint").startSpan()
        
        span.use {
            span.setAttribute("endpoint", "/hello/{name}")
            span.setAttribute("method", "GET")
            span.setAttribute("name", name)
            
            // Simulate some work
            delay(Random.nextLong(100, 500))
            
            call.respondText("Hello, $name!", ContentType.Text.Plain)
        }
    }
    
    get("/metrics") {
        val span = tracer.spanBuilder("metrics-endpoint").startSpan()
        span.use {
            span.setAttribute("endpoint", "/metrics")
            span.setAttribute("method", "GET")
            
            call.respondText("Metrics endpoint - Prometheus format", ContentType.Text.Plain)
        }
    }
} 