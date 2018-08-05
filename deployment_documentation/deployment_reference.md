## Reference Information

Potentially useful information relevant to deployment.

### Run Locally with Dev Properties

Use Maven to clean and package then run application with application-dev.properties file

```
mvn clean package
java -jar -Dspring.profiles.active=dev ./target/ApiGateway-1.0-SNAPSHOT.jar
```

### Build App

Build app from current directory and tag as swift_test_app

```
docker build -t swift_test_app .
```

### Push App to Registry

Tag and push the image up to the registry

```
docker tag swift_test_app:latest docker.conygre.com:5000/swift_test_app
docker push docker.conygre.com:5000/swift_test_app
```

### Run MySQL

Run a mysql (5.7) server instance with port forwarding and environment variables set

```
docker run -p 3306:3306 --name swift_mysql -e MYSQL_ROOT_PASSWORD=$PASSWORD -e MYSQL_USER=$USERNAME -e MYSQL_PASSWORD=$PASSWORD -e MYSQL_DATABASE=$DBNAME -d mysql:5.7
```

Test connection

```
mysql -u swift -p -h 127.0.0.1 -P 3306
```

### Linking

```
docker run -p 8080:8080 -e DBHOST=$DBHOST -e DBNAME=$DBNAME -e USERNAME=$USERNAME -e PASSWORD=$PASSWORD --name swift-rest-ui --link $DBHOST:$DBNAME swift-rest-ui
```
