FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/task-manager-app-0.0.1-SNAPSHOT.jar app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]