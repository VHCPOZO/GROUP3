# Stage 1: build the Spring Boot application
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Cache Maven dependencies by copying pom and wrapper first
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Copy source and build jar
COPY src src
RUN chmod +x mvnw && ./mvnw -B -Dmaven.test.skip=true package

# Stage 2: run the application with a lightweight runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
