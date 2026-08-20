# Dockerfile para el backend: construye el JAR con Maven y lo ejecuta en JRE
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml mvnw .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/demo-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

# Healthcheck to allow orchestration tools to detect readiness
HEALTHCHECK --interval=10s --timeout=3s --start-period=10s --retries=5 \
	CMD curl -f http://localhost:8080/api/health || exit 1
