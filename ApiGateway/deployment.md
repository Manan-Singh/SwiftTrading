

## Test Deployment with Docker Compose

Checks to Perform:
- Dockerfile profile argument (dev/prod)
- docker-compose.yml (environment variables, port forwarding)
- application.properties/application-dev.properties (port, properties/variables)

NOTE: current docker-compose.yml has most/all properties overwritten

```
// install docker, maven, git, etc.
// git clone project then cd /ApiGateway

mvn clean package
docker build -t swift_test_app .
docker compose up -d
```

TODO: 
- update profiles to be more robust (include more properties)
- provide deployment instructions for use without docker-compose.yml



### Run with dev properties (application-dev.properties)

```
mvn clean package
java -jar -Dspring.profiles.active=dev ./target/ApiGateway-1.0-SNAPSHOT.jar
```

### Run MySQL

Run a mysql server instance. 

```
docker run -p 3306:3306 --name swift -e MYSQL_ROOT_PASSWORD={PASSWORD} -d mysql:5.7
```

Test connection

```
mysql -u swift -p -h 127.0.0.1 -P 3306
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