FROM dockerreg.training.local:5000/java AS build

WORKDIR /app

COPY . .

FROM dockerreg.training.local:5000/java

WORKDIR /app

COPY --from=build /app/target/TradingEngine-1.0-SNAPSHOT.jar .

CMD ["/opt/itrs/netprobe/netprobe.linux_64", "$APPNAME", "-port", "7036", "-nopassword", "&"]

ENTRYPOINT ["java", "$JAVAOPT", "-jar", "/app/TradingEngine-1.0-SNAPSHOT.jar"]
