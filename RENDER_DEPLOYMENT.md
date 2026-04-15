# Render.com Production Deployment Guide

## Issue Fixed
The API Gateway was hanging during startup because it tried to connect to Eureka at `http://discovery-service:8761/eureka/`, which doesn't exist in the cloud environment.

## Solution
- Added **production profile** that disables Eureka for cloud deployments
- Services use direct URLs instead of service discovery
- Eureka still works in local Docker environment

## Deployment Steps

### 1. Prerequisites
- Push code to GitHub
- Have Render.com account with connected GitHub repo

### 2. Create Services on Render.com

#### A. API Gateway Service
```
Settings:
- Name: api-gateway
- Environment: Docker
- Dockerfile path: ./api-gateway/Dockerfile
- Health check path: /actuator/health

Environment Variables:
- PORT=8080
- SPRING_PROFILES_ACTIVE=production
- EUREKA_ENABLED=false
- JWT_SECRET=(your secret)
- GOOGLE_CLIENT_ID=(your client ID)
```

#### B. Auth Service
```
Settings:
- Name: auth-service
- Environment: Docker
- Dockerfile path: ./auth-service/Dockerfile
- Health check path: /api/auth/ping

Environment Variables:
- PORT=8081
- SPRING_PROFILES_ACTIVE=production
- EUREKA_ENABLED=false
- JWT_SECRET=(your secret)
- GOOGLE_CLIENT_ID=(your client ID)
```

#### C. Quantity Service
```
Settings:
- Name: quantity-service
- Environment: Docker
- Dockerfile path: ./quantity-service/Dockerfile
- Health check path: /api/quantity/actuator/health

Environment Variables:
- PORT=8082
- SPRING_PROFILES_ACTIVE=production
- EUREKA_ENABLED=false
```

### 3. Configure Service Dependencies

In API Gateway environment variables, add:
```
AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com
QUANTITY_SERVICE_URL=https://quantity-service-xxxx.onrender.com
```

(Replace xxxx with your actual Render service URLs)

### 4. Update Application Configuration

For direct service communication, the `application-production.yml` uses these endpoints:

**Auth Service:**
- Health: `{AUTH_SERVICE_URL}/api/auth/ping`
- Routes: `{AUTH_SERVICE_URL}/api/auth/**`

**Quantity Service:**
- Health: `{QUANTITY_SERVICE_URL}/api/quantity/actuator/health`
- Routes: `{QUANTITY_SERVICE_URL}/api/quantity/**`

## File Changes Made

### New Files Created
- `api-gateway/src/main/resources/application-production.yml` - Production config without Eureka
- `auth-service/src/main/resources/application-production.yml` - Production config for auth
- `quantity-service/src/main/resources/application-production.yml` - Production config for quantity
- `api-gateway/src/main/java/com/example/gateway/config/DiscoveryClientConfig.java` - Conditional Eureka
- `auth-service/src/main/java/com/example/auth/config/DiscoveryClientConfig.java` - Conditional Eureka
- `quantity-service/src/main/java/com/example/quantity/config/DiscoveryClientConfig.java` - Conditional Eureka
- `render.yaml` - Multi-service deployment configuration

### Updated Files
- `api-gateway/src/main/resources/application.yml` - Added condition for Eureka
- `api-gateway/src/main/java/com/example/gateway/ApiGatewayApplication.java` - Removed @EnableDiscoveryClient
- `auth-service/src/main/java/com/example/auth/AuthServiceApplication.java` - Removed @EnableDiscoveryClient
- `quantity-service/src/main/java/com/example/quantity/QuantityServiceApplication.java` - Removed @EnableDiscoveryClient

## Environment Profiles

### Development (Local Docker)
```bash
export SPRING_PROFILES_ACTIVE=default  # Uses Eureka
export EUREKA_ENABLED=true
docker-compose up -d
```

### Production (Render)
```
SPRING_PROFILES_ACTIVE=production
EUREKA_ENABLED=false
```

## Testing Production Configuration Locally

```bash
# Build
mvn clean package -DskipTests

# Start with production profile
export SPRING_PROFILES_ACTIVE=production
export EUREKA_ENABLED=false
export AUTH_SERVICE_URL=http://localhost:8081
export QUANTITY_SERVICE_URL=http://localhost:8082

# Run each service
java -jar auth-service/target/auth-service-1.0.0.jar &
java -jar quantity-service/target/quantity-service-1.0.0.jar &
java -jar api-gateway/target/api-gateway-1.0.0.jar &

# Test
curl http://localhost:8080/actuator/health
```

## Troubleshooting

### Still Hanging at Startup?

Check logs for:
```
Getting all instance registry info from the eureka server
```

If you see this, Eureka is still enabled. Verify:
- `SPRING_PROFILES_ACTIVE=production` is set
- `EUREKA_ENABLED=false` is set

### Service Not Found Error

If you get redirects to localhost:
1. Verify `AUTH_SERVICE_URL` and `QUANTITY_SERVICE_URL` are set correctly
2. Check service URLs match actual Render service URLs
3. Ensure all services are deployed and running

### Health Check Failing

Check that health endpoints are correct:
- Auth: `/api/auth/ping`
- Quantity: `/api/quantity/actuator/health`
- Gateway: `/actuator/health`

## Performance Optimizations Applied

- **Gzip Compression** enabled for responses
- **Connection Pooling** (HikariCP) configured
- **Circuit Breaker** pattern for resilience
- **Session Cookies** hardened for production (secure, HttpOnly)
- **CORS** properly configured with production origins

## Monitoring

### Health Endpoints
```bash
curl https://api-gateway-xxxx.onrender.com/actuator/health
curl https://auth-service-xxxx.onrender.com/api/auth/ping
curl https://quantity-service-xxxx.onrender.com/api/quantity/actuator/health
```

### Logs
Available in Render.com dashboard → Logs tab for each service

## Next Steps

1. Commit code with production profiles
2. Create services on Render.com
3. Set environment variables
4. Deploy and monitor health checks
5. Update frontend to use Render API Gateway URL

---

**Status**: Ready for production deployment ✅
**Last Updated**: April 15, 2026
