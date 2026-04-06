# ============================================
# Stage 1: Build the application (Java 21 + Maven)
# ============================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom first for better layer caching
COPY pom.xml .

# Download dependencies (cached unless pom changes)
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -B

# ============================================
# Stage 2: Runtime image
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

# Copy the built Spring Boot fat jar (single main artifact)
COPY --from=builder /app/target/mongo-microservice-*.jar app.jar

# Switch to non-root user
USER appuser

# App listens on 8081 (match server.port in application.properties)
ENV SERVER_PORT=8081
EXPOSE 8081

# JVM options (tune as needed for Tencent Cloud)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]