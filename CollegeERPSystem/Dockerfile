# Use official Java 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Give execution permission to the Maven wrapper
RUN chmod +x mvnw

# Build the project (creates jar in target/)
RUN ./mvnw package -DskipTests

# Expose port for Render
ENV PORT=8080
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/CollegeERPSystem-0.0.1-SNAPSHOT.jar"]
