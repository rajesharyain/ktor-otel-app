@echo off
echo 🚀 Starting Ktor OpenTelemetry Application...

REM Check if Docker is running
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker is not running. Please start Docker first.
    pause
    exit /b 1
)

REM Build and start all services
echo 📦 Building and starting services...
docker-compose up -d --build

REM Wait for services to be ready
echo ⏳ Waiting for services to be ready...
timeout /t 30 /nobreak >nul

REM Check if services are running
echo 🔍 Checking service status...
docker-compose ps

echo.
echo ✅ Services are running!
echo.
echo 🌐 Access your application:
echo    • Ktor App: http://localhost:8080
echo    • Jaeger UI: http://localhost:16686
echo    • Prometheus: http://localhost:9090
echo    • Grafana: http://localhost:3000 (admin/admin)
echo.
echo 📊 To view logs:
echo    • docker-compose logs -f ktor-app
echo    • docker-compose logs -f otel-collector
echo    • docker-compose logs -f jaeger
echo.
echo 🛑 To stop services:
echo    • docker-compose down
echo.
pause 