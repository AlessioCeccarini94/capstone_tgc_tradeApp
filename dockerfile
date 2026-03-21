# Java 17
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
# Build
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
# RUN
CMD ["java", "-jar", "target/*.jar"]