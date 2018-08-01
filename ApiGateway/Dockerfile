FROM dockerreg.training.local:5000/centos AS build

WORKDIR /app

COPY . .

FROM dockerreg.training.local:5000/centos

WORKDIR /app

COPY --from=build /app/target/ApiGateway-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "/app/ApiGateway-1.0-SNAPSHOT.jar"]
