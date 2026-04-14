@echo off
echo ========================================
echo Quantity Measurement App - Deployment
echo ========================================

echo.
echo Building and starting all services...
echo.

docker-compose up --build -d

echo.
echo Waiting for services to start...
timeout /t 30 /nobreak > nul

echo.
echo Checking service status...
echo.

docker-compose ps

echo.
echo Services should be available at:
echo - API Gateway: http://localhost:8080
echo - Eureka Dashboard: http://localhost:8761
echo - Auth Service: http://localhost:8081
echo - Quantity Service: http://localhost:8082
echo.

echo To view logs: docker-compose logs -f
echo To stop services: docker-compose down
echo.

pause