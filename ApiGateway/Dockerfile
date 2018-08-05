FROM dockerreg.training.local:5000/java AS build

WORKDIR /app

COPY . .

FROM dockerreg.training.local:5000/java

WORKDIR /app

COPY --from=build /app/target/ApiGateway-1.0-SNAPSHOT.jar .

# PRODUCTION PORT
EXPOSE 8087

CMD ["/opt/itrs/netprobe/netprobe.linux_64", "$APPNAME", "-port", "7036", "-nopassword", "&"]

ENTRYPOINT ["java", "-jar", "/app/ApiGateway-1.0-SNAPSHOT.jar"]
