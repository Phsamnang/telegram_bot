# Use an efficient base image
FROM openjdk:21-alpine
# Set working directory
WORKDIR /app

# Copy build artifacts (important order)
COPY build/libs/*.jar app.jar


# Command to run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
