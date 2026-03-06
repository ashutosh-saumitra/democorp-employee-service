# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy Maven wrapper files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw -B -q -DskipTests dependency:go-offline

# Copy source code
COPY src src

# Build application
RUN ./mvnw -B -q -DskipTests clean package

# Copy the built JAR
RUN JAR_FILE=$(ls target/*-SNAPSHOT.jar 2>/dev/null || ls target/*.jar) \
    && cp "$JAR_FILE" /workspace/app.jar

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-alpine AS runtime

# Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app
COPY --from=build /workspace/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]