#!/bin/bash

# Build all services
echo "Building all services..."

# Set Maven parameters
MAVEN_OPTS="-Xmx2g"
MAVEN_ARGS="-DskipTests clean package"

# Build parent project
cd "$(dirname "$0")/.."
mvn $MAVEN_ARGS

if [ $? -eq 0 ]; then
    echo "✓ Build completed successfully"
else
    echo "✗ Build failed"
    exit 1
fi

# Build Docker images
echo "Building Docker images..."

services=(
    "gateway-service"
    "bff-consumer"
    "bff-operator"
    "bff-oem"
    "bff-utility"
    "bff-admin"
    "account-service"
    "station-service"
    "device-service"
    "iot-gateway-service"
    "session-service"
    "billing-service"
    "settlement-service"
    "payment-service"
    "pricing-service"
    "scheduling-service"
    "dr-vpp-service"
    "member-service"
    "notification-service"
    "work-order-service"
    "report-service"
    "algorithm-service"
    "data-sync-service"
    "search-service"
)

for service in "${services[@]}"; do
    echo "Building Docker image for $service..."
    docker build -t novax/$service:latest -f Dockerfile.$service .
    if [ $? -eq 0 ]; then
        echo "✓ Docker image for $service built successfully"
    else
        echo "✗ Failed to build Docker image for $service"
    fi
done

echo "All services built successfully!"
