# ==========================================
# Multi-Stage Dockerfile for Spring Boot Backend
# ==========================================

# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# Copy dependencies descriptor first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and build executable jar
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Production Runtime Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Configure default port for Render/Cloud (Render sets $PORT dynamically)
ENV PORT=8080
EXPOSE 8080

# Copy compiled JAR artifact from builder
COPY --from=builder /build/target/*.jar app.jar

# JVM options optimized for cloud container environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
