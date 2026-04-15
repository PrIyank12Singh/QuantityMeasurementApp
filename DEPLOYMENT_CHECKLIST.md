# Pre-Deployment Checklist

## Code Changes Verification

- [x] API Gateway Response Headers Filter added
- [x] API Gateway Exception Handler added
- [x] Circuit Breaker dependencies added to pom.xml
- [x] CORS configuration added to Auth Service
- [x] CORS configuration added to Quantity Service
- [x] Eureka configuration optimized (all services)
- [x] Connection pool settings configured
- [x] API Gateway YAML configuration updated
- [x] Docker Compose health checks added
- [x] Service dependencies corrected

## Build Steps

```bash
# Step 1: Clean build all modules
mvn clean package -DskipTests -DskipDockerBuild

# Step 2: Verify JAR files created
ls -la */target/*.jar

# Optional: Build Docker images
docker-compose build --no-cache
```

## Pre-Deployment Tests (Local)

### Step 1: Start Services
```bash
docker-compose up -d
sleep 30  # Wait for services to be healthy
```

### Step 2: Verify Service Health
```bash
# Check all containers are running
docker-compose ps

# Test each health endpoint
curl -i http://localhost:8761/eureka/apps
curl -i http://localhost:8081/api/auth/ping
curl -i http://localhost:8082/api/quantity/actuator/health
curl -i http://localhost:8080/actuator/health
```

### Step 3: Test Auth Endpoints
```bash
# Sign up
curl -X POST http://localhost:8081/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123!",
    "firstName": "Test",
    "lastName": "User"
  }'

# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123!"
  }'
```

### Step 4: Verify CORS Headers
```bash
# Check response headers for COOP
curl -i -X OPTIONS http://localhost:8080/api/auth/ping \
  -H "Origin: http://localhost:4200"

# Expected headers:
# Cross-Origin-Opener-Policy: same-origin-allow-popups
# Cross-Origin-Embedder-Policy: require-corp
```

### Step 5: Test Circuit Breaker
```bash
# Simulate service failure by stopping auth-service
docker-compose stop auth-service

# Try to call gateway - should return 503 with proper error message
curl -i http://localhost:8080/api/auth/ping

# Restart and verify recovery
docker-compose start auth-service
sleep 10
curl -i http://localhost:8080/api/auth/ping
```

### Step 6: Check Circuit Breaker Status
```bash
curl -X GET http://localhost:8080/actuator/circuitbreakers | jq

# Individual circuit breaker
curl -X GET http://localhost:8080/actuator/circuitbreakers/authCircuitBreaker | jq
```

## Browser Console Verification

1. Open browser DevTools (F12)
2. Go to Console tab
3. Verify NO errors about "Cross-Origin-Opener-Policy"
4. Go to Network tab
5. Inspect response headers for:
   - `Cross-Origin-Opener-Policy: same-origin-allow-popups`
   - `Cross-Origin-Embedder-Policy: require-corp`
   - `X-Content-Type-Options: nosniff`

## Performance Validation

### Service Discovery Speed
```bash
time curl http://localhost:8761/eureka/apps
# Target: < 200ms
```

### Request Latency (through Gateway)
```bash
# Test multiple times and check response time
for i in {1..5}; do 
  time curl http://localhost:8080/api/auth/ping
done
# Target: < 500ms per request
```

### Connection Pool Status
```bash
# Watch logs for "HikariPool-" messages
docker-compose logs auth-service | grep HikariPool
```

## Production Deployment Checklist

### Before Pushing to Production
- [ ] All tests pass locally
- [ ] No console errors from COOP
- [ ] Circuit breaker endpoints responding
- [ ] Auth endpoints work correctly
- [ ] Health checks configured
- [ ] Environment variables set correctly
- [ ] CORS origins updated for production frontend

### Environment Configuration
```bash
# For production (Render, AWS, etc.)
export PORT=8080
export EUREKA_URL=http://discovery-service:8761/eureka/
export ALLOWED_ORIGINS=https://your-frontend-domain.com
export JWT_SECRET=your-production-secret
export GOOGLE_CLIENT_ID=your-production-client-id
```

### Monitoring Setup
1. Enable health endpoint monitoring
2. Set up alerts for circuit breaker state changes
3. Monitor request latency via /actuator/metrics

## Rollback Plan

If issues occur in production:

```bash
# Stop current deployment
docker-compose down

# Revert to previous version
git checkout previous-commit-hash

# Rebuild and redeploy
mvn clean package
docker-compose up -d
```

## Success Criteria

- ✅ All services start within 30 seconds
- ✅ No 503 errors on auth endpoints
- ✅ No COOP policy errors in browser console
- ✅ Circuit breaker active and functioning
- ✅ Response time < 500ms (p95)
- ✅ All health checks passing
- ✅ CORS properly configured

---

**Next Steps**: Run the build and deployment tests above, then review the OPTIMIZATION_GUIDE.md for detailed information.
