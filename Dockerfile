# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory to the working directory
COPY target/stock-api-1.0-SNAPSHOT.jar stock-api.jar

# Expose the application port
EXPOSE 5001

# Run the application
ENTRYPOINT ["java", "-jar", "/app/stock-api.jar"]

