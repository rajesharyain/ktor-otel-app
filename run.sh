#!/bin/bash

echo "🚀 Starting Ktor OpenTelemetry Application..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Build and start all services
echo "📦 Building and starting services..."
docker-compose up -d --build

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Check if services are running
echo "🔍 Checking service status..."
docker-compose ps

echo ""
echo "✅ Services are running!"
echo ""
echo "🌐 Access your application:"
echo "   • Ktor App: http://localhost:8080"
echo "   • Jaeger UI: http://localhost:16686"
echo "   • Prometheus: http://localhost:9090"
echo "   • Grafana: http://localhost:3000 (admin/admin)"
echo ""
echo "📊 To view logs:"
echo "   • docker-compose logs -f ktor-app"
echo "   • docker-compose logs -f otel-collector"
echo "   • docker-compose logs -f jaeger"
echo ""
echo "🛑 To stop services:"
echo "   • docker-compose down" 