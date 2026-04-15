# Quantity Measurement App - Deployment Guide

## Overview
This is a microservices-based application consisting of:
- **Discovery Service** (Eureka) - Port 8761
- **Auth Service** - Port 8081
- **Quantity Service** - Port 8082
- **API Gateway** - Port 8080

## Prerequisites
- Docker and Docker Compose installed
- At least 4GB RAM available
- Ports 8080, 8081, 8082, 8761 available

## Quick Start with Docker Compose

### 1. Build and Start All Services
```bash
# From the project root directory
docker-compose up --build
```

### 2. Check Service Health
```bash
# Check if services are running
docker-compose ps

# View logs
docker-compose logs -f [service-name]

# Check Eureka dashboard
open http://localhost:8761
```

### 3. Test the Application
```bash
# Test API Gateway
curl http://localhost:8080/api/auth/ping

# Test individual services
curl http://localhost:8080/api/quantity/actuator/health
```

### 4. Stop Services
```bash
docker-compose down
```

## Manual Docker Deployment

### Build Individual Images
```bash
# Build all services
docker build -t quantity-app/discovery-service ./discovery-service
docker build -t quantity-app/auth-service ./auth-service
docker build -t quantity-app/quantity-service ./quantity-service
docker build -t quantity-app/api-gateway ./api-gateway
```

### Run Services Manually
```bash
# Start discovery service first
docker run -d -p 8761:8761 --name discovery-service quantity-app/discovery-service

# Start other services
docker run -d -p 8081:8081 --name auth-service \
  -e EUREKA_URL=http://host.docker.internal:8761/eureka/ \
  quantity-app/auth-service

docker run -d -p 8082:8082 --name quantity-service \
  -e EUREKA_URL=http://host.docker.internal:8761/eureka/ \
  quantity-app/quantity-service

docker run -d -p 8080:8080 --name api-gateway \
  -e EUREKA_URL=http://host.docker.internal:8761/eureka/ \
  quantity-app/api-gateway
```

## Environment Configuration

### For Production Deployment
Update the `docker-compose.yml` environment variables:

1. **Database Configuration**:
   - Change `DB_URL` to point to your production database
   - Update `DB_USERNAME` and `DB_PASSWORD`

2. **Security**:
   - Change `JWT_SECRET` to a secure random string
   - Update `GOOGLE_CLIENT_ID` for OAuth

3. **External URLs**:
   - Update `ALLOWED_ORIGINS` for CORS
   - Configure `EUREKA_URL` for service discovery

### Using .env Files
Each service has a `.env` file with default values. For production:
1. Copy `.env` to `.env.production`
2. Update values in `.env.production`
3. Use `--env-file` flag in docker run or update docker-compose.yml

## Files to Deploy

### Core Application Files:
- `pom.xml` - Maven configuration
- `src/` - Source code
- `Dockerfile` - Container build instructions
- `.env` - Environment variables (optional)

### Deployment Files:
- `docker-compose.yml` - Orchestration configuration
- `README.md` - This documentation

### Directory Structure for Deployment:
```
quantity-measurement-app/
├── docker-compose.yml
├── discovery-service/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   └── .env
├── auth-service/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   └── .env
├── quantity-service/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   └── .env
└── api-gateway/
    ├── Dockerfile
    ├── pom.xml
    ├── src/
    └── .env
```

## Troubleshooting

### Common Issues:

1. **Port Conflicts**: Ensure ports 8080-8082, 8761 are available
2. **Memory Issues**: Increase Docker memory allocation
3. **Service Discovery**: Check Eureka dashboard at http://localhost:8761
4. **Database Connection**: Verify DB_URL in environment variables

### Logs:
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs auth-service

# Follow logs in real-time
docker-compose logs -f
```

## Production Considerations

1. **Database**: Use external database instead of H2
2. **Security**: 
   - Use HTTPS
   - Secure JWT secrets
   - Configure proper CORS
3. **Monitoring**: Add health checks and metrics
4. **Scaling**: Use Kubernetes for production scaling
5. **Backup**: Regular database backups