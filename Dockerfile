# Use official OpenJDK as base image
FROM openjdk:17-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY target/app2-1.0.jar app2-1.0.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app2-1.0.jar"]