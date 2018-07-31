

## Run with dev properties (application-dev.properties)

```
mvn clean package
java -jar -Dspring.profiles.active=dev ./target/ApiGateway-1.0-SNAPSHOT.jar
```

### Run MySQL

Run a mysql server instance. 

```
docker run --name swift -e MYSQL_ROOT_PASSWORD={PASSWORD} -d mysql:5.7
```

### Build App

```
docker build -t swift_test_app .
// tagged as swift_test_app, build from current directory
```

### Push App to Registry

```
docker tag swift_test_app:latest docker.conygre.com:5000/centos/swift_test_app
docker push docker.conygre.com:5000/centos/swift_test_app
```

### TODO

Dockerfile
```
FROM docker.conygre.com:5000/centos
```

```
docker tag swift_test_app:latest docker.conygre.com:5000/swift_test_app
docker push docker.conygre.com:5000/swift_test_app
```