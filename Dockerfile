# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy pom and download dependencies (Layer Caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 3. Copy only the final executable JAR
# Note: If your project name is 'rental', the jar is usually rental-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/*.jar app.jar

# 4. Java 21 Optimizations: Generational ZGC for low-latency
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational"

EXPOSE 8080

# Use exec form for better signal handling
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]