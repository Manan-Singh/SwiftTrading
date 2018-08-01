FROM dockerreg.training.local:5000/java AS build

WORKDIR /app

COPY . .

FROM dockerreg.training.local:5000/java

WORKDIR /app

COPY --from=build /app/target/TradingEngine-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "/app/TradingEngine-1.0-SNAPSHOT.jar"]
