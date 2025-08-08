# Ktor OpenTelemetry Application

A full-stack Kotlin application built with Ktor 3 framework, featuring comprehensive observability with OpenTelemetry and Jaeger tracing.

## Features

- **Ktor 3 Framework**: Modern Kotlin web framework
- **OpenTelemetry Integration**: Distributed tracing and observability
- **Jaeger Tracing**: Trace visualization and analysis
- **RESTful API**: Complete CRUD operations for Users and Products
- **Health Checks**: Application health monitoring
- **Docker Compose**: Complete infrastructure setup
- **Prometheus & Grafana**: Metrics collection and visualization

## Architecture

```
┌─────────────┐    ┌──────────────────┐    ┌─────────────┐
│   Ktor App  │───▶│  OTEL Collector  │───▶│   Jaeger    │
│   (Port 8080)│    │                  │    │  (Port 16686)│
└─────────────┘    └──────────────────┘    └─────────────┘
       │                     │
       │                     ▼
       │              ┌─────────────┐
       │              │ Prometheus  │
       │              │ (Port 9090) │
       │              └─────────────┘
       │                     │
       ▼                     ▼
┌─────────────┐    ┌─────────────┐
│   Grafana   │    │   Metrics   │
│ (Port 3000) │    │ Visualization│
└─────────────┘    └─────────────┘
```

## Quick Start

### Prerequisites

- Docker and Docker Compose
- Java 17 or higher (for local development)
- Gradle (for local development)

### Running with Docker Compose

1. **Clone and navigate to the project:**
   ```bash
   cd ktor-otel-app
   ```

2. **Start all services:**
   ```bash
   docker-compose up -d
   ```

3. **Access the services:**
   - **Ktor Application**: http://localhost:8080
   - **Jaeger UI**: http://localhost:16686
   - **Prometheus**: http://localhost:9090
   - **Grafana**: http://localhost:3000 (admin/admin)

### Local Development

1. **Build the application:**
   ```bash
   ./gradlew build
   ```

2. **Run the application:**
   ```bash
   ./gradlew run
   ```

## API Endpoints

### Root Endpoints
- `GET /` - Welcome message
- `GET /hello/{name}` - Personalized greeting
- `GET /metrics` - Prometheus metrics

### Health Checks
- `GET /health` - Application health status
- `GET /health/ready` - Readiness check
- `GET /health/live` - Liveness check

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Product Management
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{category}` - Get products by category
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

## API Examples

### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'
```

### Get All Products
```bash
curl http://localhost:8080/api/products
```

### Get Products by Category
```bash
curl http://localhost:8080/api/products/category/Electronics
```

## Observability

### Tracing with Jaeger

1. **Access Jaeger UI**: http://localhost:16686
2. **Select Service**: `ktor-otel-app`
3. **View Traces**: All API calls are automatically traced with:
   - Request/response details
   - Timing information
   - Custom attributes
   - Error tracking

### Metrics with Prometheus

1. **Access Prometheus**: http://localhost:9090
2. **Available Metrics**:
   - `ktor_http_requests_total` - Total HTTP requests
   - `ktor_http_request_duration_seconds` - Request duration
   - `ktor_http_requests_active` - Active requests

### Visualization with Grafana

1. **Access Grafana**: http://localhost:3000
2. **Login**: admin/admin
3. **Add Prometheus Data Source**: http://prometheus:9090
4. **Create Dashboards**: Visualize application metrics

## Project Structure

```
ktor-otel-app/
├── src/main/kotlin/com/example/
│   ├── Application.kt              # Main application entry point
│   ├── plugins/                    # Ktor plugins
│   │   ├── Serialization.kt        # JSON serialization
│   │   ├── Monitoring.kt           # Metrics and logging
│   │   └── Routing.kt              # Route configuration
│   ├── routes/                     # API routes
│   │   ├── RootRoutes.kt           # Basic endpoints
│   │   ├── UserRoutes.kt           # User CRUD operations
│   │   ├── ProductRoutes.kt        # Product CRUD operations
│   │   └── HealthRoutes.kt         # Health checks
│   └── models/                     # Data models
│       └── Models.kt               # User, Product, and request models
├── src/main/resources/
│   └── logback.xml                 # Logging configuration
├── docker-compose.yml              # Complete infrastructure
├── Dockerfile                      # Application container
├── otel-collector-config.yaml      # OpenTelemetry configuration
├── prometheus.yml                  # Prometheus configuration
└── build.gradle.kts                # Build configuration
```

## Configuration

### Environment Variables

- `OTEL_EXPORTER_OTLP_ENDPOINT`: OpenTelemetry collector endpoint
- `OTEL_SERVICE_NAME`: Service name for tracing
- `OTEL_SERVICE_VERSION`: Service version

### OpenTelemetry Configuration

The application automatically configures OpenTelemetry with:
- **Service Name**: `ktor-otel-app`
- **Version**: `1.0.0`
- **Exporter**: OTLP gRPC to collector
- **Sampling**: 100% (all traces)

## Monitoring and Debugging

### Logs
```bash
# View application logs
docker-compose logs -f ktor-app

# View collector logs
docker-compose logs -f otel-collector

# View Jaeger logs
docker-compose logs -f jaeger
```

### Health Checks
```bash
# Application health
curl http://localhost:8080/health

# Readiness check
curl http://localhost:8080/health/ready

# Liveness check
curl http://localhost:8080/health/live
```

## Development

### Adding New Routes

1. Create a new route file in `src/main/kotlin/com/example/routes/`
2. Add OpenTelemetry tracing
3. Register the route in `Routing.kt`

### Adding New Models

1. Add data classes to `Models.kt`
2. Use `@Serializable` annotation for JSON serialization

### Custom Tracing

```kotlin
val tracer: Tracer = OpenTelemetry.get().getTracer("ktor-otel-app")
val span = tracer.spanBuilder("operation-name").startSpan()
span.use {
    span.setAttribute("custom.attribute", "value")
    // Your operation here
}
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 8080, 16686, 9090, 3000 are available
2. **Memory issues**: Adjust JVM options in Dockerfile
3. **Network issues**: Check Docker network connectivity

### Debug Mode

Enable debug logging by modifying `logback.xml`:
```xml
<root level="DEBUG">
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License. 