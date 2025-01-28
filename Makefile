## Makefile to build and run the application

## Pre-requisites: Install SDKMAN : https://sdkman.io/install
# Set up development environment, run:
# sdk env install && sdk env

# Clean up
clean:
	@echo "Cleaning up..."
	@rm -rf jte-*
	@rm -f output.txt
	@./mvnw clean

# Test the application
test: clean
	@echo "Running tests..."
	@./mvnw test

# Build the application
build: clean
	@echo "Building application..."
	@./mvnw package -DskipTests

# Run in development mode
run-dev: clean
	@echo "Starting application in development mode..."
	@./mvnw spring-boot:run

# Run in production mode
run: clean
	@echo "Starting application in production mode..."
	@./mvnw spring-boot:run -Dspring-boot.run.jvmArguments=-Dspring.profiles.active=prod

all: clean test build run

.PHONY: all
