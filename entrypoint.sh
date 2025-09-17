#!/bin/sh
set -e

: "${SPRING_REDIS_HOST:=redis}"
: "${SPRING_REDIS_PORT:=6379}"
: "${DB_HOST:=db}"
: "${DB_PORT:=5432}"

echo "Waiting for Redis..."
echo "$SPRING_REDIS_HOST"
while ! nc -z "$SPRING_REDIS_HOST" "$SPRING_REDIS_PORT"; do
  echo "Redis is unavailable - sleeping"
  sleep 1
done
echo "Redis is up!"

echo "Waiting for Postgres..."
while ! nc -z "$DB_HOST" "$DB_PORT"; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done
echo "Postgres is up!"

echo "Starting Spring Boot application..."
exec java -jar /app/app.jar
