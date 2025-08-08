package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.OpenTelemetry
import kotlinx.coroutines.delay
import kotlin.random.Random

fun Route.userRoutes() {
    val tracer: Tracer = OpenTelemetry.get().getTracer("ktor-otel-app")
    val users = mutableListOf<User>()
    
    // Initialize with some sample data
    users.addAll(listOf(
        User(1, "John Doe", "john@example.com", "Active"),
        User(2, "Jane Smith", "jane@example.com", "Active"),
        User(3, "Bob Johnson", "bob@example.com", "Inactive")
    ))
    
    route("/api/users") {
        get {
            val span = tracer.spanBuilder("get-all-users").startSpan()
            span.use {
                span.setAttribute("endpoint", "/api/users")
                span.setAttribute("method", "GET")
                span.setAttribute("user.count", users.size.toString())
                
                // Simulate database query
                delay(Random.nextLong(50, 200))
                
                call.respond(UsersResponse(users))
            }
        }
        
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val span = tracer.spanBuilder("get-user-by-id").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/users/{id}")
                span.setAttribute("method", "GET")
                span.setAttribute("user.id", id.toString())
                
                if (id == null) {
                    span.setAttribute("error", "Invalid ID")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid user ID"))
                    return@use
                }
                
                val user = users.find { it.id == id }
                if (user == null) {
                    span.setAttribute("error", "User not found")
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                    return@use
                }
                
                // Simulate database query
                delay(Random.nextLong(30, 100))
                
                call.respond(user)
            }
        }
        
        post {
            val span = tracer.spanBuilder("create-user").startSpan()
            span.use {
                span.setAttribute("endpoint", "/api/users")
                span.setAttribute("method", "POST")
                
                try {
                    val userRequest = call.receive<CreateUserRequest>()
                    span.setAttribute("user.name", userRequest.name)
                    span.setAttribute("user.email", userRequest.email)
                    
                    // Simulate validation and database operation
                    delay(Random.nextLong(100, 300))
                    
                    val newUser = User(
                        id = users.maxOfOrNull { it.id }?.plus(1) ?: 1,
                        name = userRequest.name,
                        email = userRequest.email,
                        status = "Active"
                    )
                    
                    users.add(newUser)
                    call.respond(HttpStatusCode.Created, newUser)
                } catch (e: Exception) {
                    span.setAttribute("error", e.message)
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request data"))
                }
            }
        }
        
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val span = tracer.spanBuilder("update-user").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/users/{id}")
                span.setAttribute("method", "PUT")
                span.setAttribute("user.id", id.toString())
                
                if (id == null) {
                    span.setAttribute("error", "Invalid ID")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid user ID"))
                    return@use
                }
                
                try {
                    val userRequest = call.receive<UpdateUserRequest>()
                    span.setAttribute("user.name", userRequest.name)
                    span.setAttribute("user.email", userRequest.email)
                    
                    val userIndex = users.indexOfFirst { it.id == id }
                    if (userIndex == -1) {
                        span.setAttribute("error", "User not found")
                        call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                        return@use
                    }
                    
                    // Simulate database operation
                    delay(Random.nextLong(80, 200))
                    
                    users[userIndex] = users[userIndex].copy(
                        name = userRequest.name,
                        email = userRequest.email
                    )
                    
                    call.respond(users[userIndex])
                } catch (e: Exception) {
                    span.setAttribute("error", e.message)
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request data"))
                }
            }
        }
        
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val span = tracer.spanBuilder("delete-user").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/users/{id}")
                span.setAttribute("method", "DELETE")
                span.setAttribute("user.id", id.toString())
                
                if (id == null) {
                    span.setAttribute("error", "Invalid ID")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid user ID"))
                    return@use
                }
                
                val userIndex = users.indexOfFirst { it.id == id }
                if (userIndex == -1) {
                    span.setAttribute("error", "User not found")
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))
                    return@use
                }
                
                // Simulate database operation
                delay(Random.nextLong(50, 150))
                
                users.removeAt(userIndex)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
} 