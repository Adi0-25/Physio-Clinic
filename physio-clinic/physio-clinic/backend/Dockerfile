# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Pre-fetch dependencies (cached layer, speeds up rebuilds)
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/physio-clinic.jar app.jar

# Render provides $PORT at runtime; application.properties reads it automatically
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
