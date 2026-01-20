#!/bin/bash

# Deploy all services to Kubernetes
echo "Deploying services to Kubernetes..."

# Set kubectl context
NAMESPACE="${NAMESPACE:-novax}"
kubectl config set-context --current --namespace=$NAMESPACE

# Create namespace
echo "Creating namespace..."
kubectl apply -f k8s/namespace.yaml

# Apply ConfigMap and Secret
echo "Applying ConfigMap and Secret..."
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# Deploy Gateway
echo "Deploying Gateway Service..."
kubectl apply -f k8s/gateway-service.yaml

# Wait for Gateway to be ready
kubectl wait --for=condition=available --timeout=300s deployment/gateway-service -n $NAMESPACE

# Deploy all other services
echo "Deploying all services..."
kubectl apply -f k8s/account-service.yaml

echo "Deployment completed!"

# Show service status
kubectl get pods -n $NAMESPACE
kubectl get services -n $NAMESPACE
