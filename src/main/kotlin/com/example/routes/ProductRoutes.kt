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

fun Route.productRoutes() {
    val tracer: Tracer = OpenTelemetry.get().getTracer("ktor-otel-app")
    val products = mutableListOf<Product>()
    
    // Initialize with some sample data
    products.addAll(listOf(
        Product(1, "Laptop", "High-performance laptop", 999.99, "Electronics", 10),
        Product(2, "Smartphone", "Latest smartphone model", 699.99, "Electronics", 15),
        Product(3, "Coffee Mug", "Ceramic coffee mug", 12.99, "Kitchen", 50),
        Product(4, "Book", "Programming guide", 29.99, "Books", 25)
    ))
    
    route("/api/products") {
        get {
            val span = tracer.spanBuilder("get-all-products").startSpan()
            span.use {
                span.setAttribute("endpoint", "/api/products")
                span.setAttribute("method", "GET")
                span.setAttribute("product.count", products.size.toString())
                
                // Simulate database query
                delay(Random.nextLong(50, 200))
                
                call.respond(ProductsResponse(products))
            }
        }
        
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val span = tracer.spanBuilder("get-product-by-id").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/products/{id}")
                span.setAttribute("method", "GET")
                span.setAttribute("product.id", id.toString())
                
                if (id == null) {
                    span.setAttribute("error", "Invalid ID")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid product ID"))
                    return@use
                }
                
                val product = products.find { it.id == id }
                if (product == null) {
                    span.setAttribute("error", "Product not found")
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Product not found"))
                    return@use
                }
                
                // Simulate database query
                delay(Random.nextLong(30, 100))
                
                call.respond(product)
            }
        }
        
        get("/category/{category}") {
            val category = call.parameters["category"]
            val span = tracer.spanBuilder("get-products-by-category").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/products/category/{category}")
                span.setAttribute("method", "GET")
                span.setAttribute("category", category ?: "")
                
                if (category == null) {
                    span.setAttribute("error", "Category parameter missing")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Category parameter is required"))
                    return@use
                }
                
                // Simulate database query
                delay(Random.nextLong(40, 150))
                
                val filteredProducts = products.filter { it.category.equals(category, ignoreCase = true) }
                span.setAttribute("product.count", filteredProducts.size.toString())
                
                call.respond(ProductsResponse(filteredProducts))
            }
        }
        
        post {
            val span = tracer.spanBuilder("create-product").startSpan()
            span.use {
                span.setAttribute("endpoint", "/api/products")
                span.setAttribute("method", "POST")
                
                try {
                    val productRequest = call.receive<CreateProductRequest>()
                    span.setAttribute("product.name", productRequest.name)
                    span.setAttribute("product.category", productRequest.category)
                    span.setAttribute("product.price", productRequest.price.toString())
                    
                    // Simulate validation and database operation
                    delay(Random.nextLong(100, 300))
                    
                    val newProduct = Product(
                        id = products.maxOfOrNull { it.id }?.plus(1) ?: 1,
                        name = productRequest.name,
                        description = productRequest.description,
                        price = productRequest.price,
                        category = productRequest.category,
                        stockQuantity = productRequest.stockQuantity
                    )
                    
                    products.add(newProduct)
                    call.respond(HttpStatusCode.Created, newProduct)
                } catch (e: Exception) {
                    span.setAttribute("error", e.message)
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request data"))
                }
            }
        }
        
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val span = tracer.spanBuilder("update-product").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/products/{id}")
                span.setAttribute("method", "PUT")
                span.setAttribute("product.id", id.toString())
                
                if (id == null) {
                    span.setAttribute("error", "Invalid ID")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid product ID"))
                    return@use
                }
                
                try {
                    val productRequest = call.receive<UpdateProductRequest>()
                    span.setAttribute("product.name", productRequest.name)
                    span.setAttribute("product.category", productRequest.category)
                    span.setAttribute("product.price", productRequest.price.toString())
                    
                    val productIndex = products.indexOfFirst { it.id == id }
                    if (productIndex == -1) {
                        span.setAttribute("error", "Product not found")
                        call.respond(HttpStatusCode.NotFound, ErrorResponse("Product not found"))
                        return@use
                    }
                    
                    // Simulate database operation
                    delay(Random.nextLong(80, 200))
                    
                    products[productIndex] = products[productIndex].copy(
                        name = productRequest.name,
                        description = productRequest.description,
                        price = productRequest.price,
                        category = productRequest.category,
                        stockQuantity = productRequest.stockQuantity
                    )
                    
                    call.respond(products[productIndex])
                } catch (e: Exception) {
                    span.setAttribute("error", e.message)
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid request data"))
                }
            }
        }
        
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val span = tracer.spanBuilder("delete-product").startSpan()
            
            span.use {
                span.setAttribute("endpoint", "/api/products/{id}")
                span.setAttribute("method", "DELETE")
                span.setAttribute("product.id", id.toString())
                
                if (id == null) {
                    span.setAttribute("error", "Invalid ID")
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid product ID"))
                    return@use
                }
                
                val productIndex = products.indexOfFirst { it.id == id }
                if (productIndex == -1) {
                    span.setAttribute("error", "Product not found")
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Product not found"))
                    return@use
                }
                
                // Simulate database operation
                delay(Random.nextLong(50, 150))
                
                products.removeAt(productIndex)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
} 