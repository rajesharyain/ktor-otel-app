FROM gradle:8.4-jdk17 AS build

WORKDIR /app
COPY . .

RUN gradle build --no-daemon

FROM openjdk:17-jre-slim

WORKDIR /app

# Create logs directory
RUN mkdir -p logs

# Copy the built JAR file
COPY --from=build /app/build/libs/ktor-otel-app-0.0.1.jar app.jar

# Expose the application port
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 