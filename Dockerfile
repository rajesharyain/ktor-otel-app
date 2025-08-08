FROM openjdk:17-jdk AS build

WORKDIR /app

# Install Gradle
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-8.4-bin.zip
RUN unzip gradle-8.4-bin.zip
RUN mv gradle-8.4 /opt/gradle
ENV PATH="/opt/gradle/bin:${PATH}"

# Copy source code
COPY . .

# Build the application
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre-jammy

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