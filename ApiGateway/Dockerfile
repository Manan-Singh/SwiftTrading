FROM openjdk:8-alpine AS build

WORKDIR /app

COPY . .

FROM openjdk:8-alpine

WORKDIR /app

COPY --from=build /app/target/ApiGateway-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "/app/ApiGateway-1.0-SNAPSHOT.jar"]
