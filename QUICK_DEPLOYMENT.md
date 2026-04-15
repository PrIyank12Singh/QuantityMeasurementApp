# Quick Production Deployment Reference

## Problem Solved
❌ **Before**: API Gateway hung at startup trying to reach non-existent Eureka server  
✅ **After**: Services use direct URLs in production, Eureka optional

## What Changed

### 1. Conditional Eureka Discovery
- Created `DiscoveryClientConfig.java` in all services
- Eureka only enabled when `eureka.client.enabled=true` (default)
- Production sets `eureka.client.enabled=false` via profile

### 2. Production Profiles Added
```
api-gateway/src/main/resources/application-production.yml
auth-service/src/main/resources/application-production.yml
quantity-service/src/main/resources/application-production.yml
```

### 3. Direct Service Communication
Instead of load balancer discovery, production uses:
```
AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com
QUANTITY_SERVICE_URL=https://quantity-service-xxxx.onrender.com
```

### 4. Docker Optimization
- Added memory percentage optimization: `-XX:MaxRAMPercentage=75.0`
- Made `.env` file optional (for cloud deployments)
- Uses container JVM detection: `-XX:+UseContainerSupport`

## How to Deploy to Render

### Step 1: Commit Code
```bash
git add .
git commit -m "feat: Add production profiles for cloud deployment"
git push origin feature/UC21-microservices
```

### Step 2: Create 3 Services on Render.com Dashboard

**API Gateway**
```
Repository: QuantityMeasurementApp
Branch: feature/UC21-microservices
Root Directory: ./
Dockerfile Path: ./api-gateway/Dockerfile

Environment Variables:
PORT=8080
SPRING_PROFILES_ACTIVE=production
EUREKA_ENABLED=false
JWT_SECRET=your_secret_here
GOOGLE_CLIENT_ID=your_client_id_here
AUTH_SERVICE_URL=(set after auth-service deployment)
QUANTITY_SERVICE_URL=(set after quantity-service deployment)

Health Check Path: /actuator/health
```

**Auth Service**
```
Repository: QuantityMeasurementApp
Branch: feature/UC21-microservices
Dockerfile Path: ./auth-service/Dockerfile

Environment Variables:
PORT=8081
SPRING_PROFILES_ACTIVE=production
EUREKA_ENABLED=false
JWT_SECRET=your_secret_here
GOOGLE_CLIENT_ID=your_client_id_here

Health Check Path: /api/auth/ping
```

**Quantity Service**
```
Repository: QuantityMeasurementApp
Branch: feature/UC21-microservices
Dockerfile Path: ./quantity-service/Dockerfile

Environment Variables:
PORT=8082
SPRING_PROFILES_ACTIVE=production
EUREKA_ENABLED=false

Health Check Path: /api/quantity/actuator/health
```

### Step 3: Link Services
After all 3 services are deployed and show green status:
1. Go to API Gateway settings
2. Add environment variables:
   - `AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com`
   - `QUANTITY_SERVICE_URL=https://quantity-service-xxxx.onrender.com`
3. Trigger redeploy

### Step 4: Test
```bash
curl https://api-gateway-xxxx.onrender.com/actuator/health
curl https://auth-service-xxxx.onrender.com/api/auth/ping
```

## Local Testing (Optional)

Test production configuration locally:
```bash
# Build all services
mvn clean package -DskipTests

# Start services with production profile
export SPRING_PROFILES_ACTIVE=production
export EUREKA_ENABLED=false
export AUTH_SERVICE_URL=http://localhost:8081
export QUANTITY_SERVICE_URL=http://localhost:8082

java -jar auth-service/target/auth-service-1.0.0.jar &
java -jar quantity-service/target/quantity-service-1.0.0.jar &
java -jar api-gateway/target/api-gateway-1.0.0.jar &

# Test endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8081/api/auth/ping
curl http://localhost:8082/api/quantity/actuator/health
```

## Profiles Reference

| Profile | Eureka | Load Balancer | Use Case |
|---------|--------|---------------|----------|
| default | ✅ Enabled | ✅ Yes | Local Docker |
| production | ❌ Disabled | ❌ No | Cloud (Render) |

## Files Modified

**New:**
- `application-production.yml` (all services)
- `DiscoveryClientConfig.java` (all services)
- `render.yaml`
- `RENDER_DEPLOYMENT.md`

**Updated:**
- `application.yml` (api-gateway) - Added conditional Eureka
- `Dockerfile` (all services) - Optimized JVM flags
- Application classes - Removed `@EnableDiscoveryClient` from main classes

## Success Criteria
- ✅ API Gateway starts in < 2 minutes
- ✅ No Eureka connection timeout errors
- ✅ Health endpoints return 200
- ✅ Services can communicate with each other
- ✅ COOP headers present in responses

## Documentation
- Full guide: `RENDER_DEPLOYMENT.md`
- Optimization details: `OPTIMIZATION_GUIDE.md`
- Deployment checklist: `DEPLOYMENT_CHECKLIST.md`

---

**Status**: Ready for Render deployment ✅
