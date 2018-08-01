

# DEPLOYMENT INFO

## Test Deployment Documentation

### Test Deployment with Docker

```
// install docker, maven, git, etc.
// git clone project then cd /ApiGateway

export DBHOST="swift_mysql"  // this will be changed to prod's host name
export DBNAME="swift_mysql"
// export USERNAME and PASSWORD

mvn clean package
docker build -t swift_test_app .

docker run -p 3306:3306 --name swift_mysql -e MYSQL_ROOT_PASSWORD=$PASSWORD -e MYSQL_USER=$USERNAME -e MYSQL_PASSWORD=$PASSWORD -e MYSQL_DATABASE=$DBNAME -d mysql:5.7

mysql -u $USERNAME -p $PASSWORD -h 127.0.0.1 -P 3306 < ../SQL/init.sql
mysql -u $USERNAME -p $PASSWORD -h 127.0.0.1 -P 3306 < ../SQL/dummy-data.sql

docker run -p 8080:8080 --name swift_linked_app --link swift_mysql:$DBNAME swift_test_app
```

### Test Deployment with Docker Compose

Checks to Perform:
- Dockerfile profile argument (dev/prod)
- docker-compose.yml (environment variables, port forwarding)
- application.properties/application-dev.properties (port, properties/variables)

NOTE: current docker-compose.yml has most/all properties overwritten

```
// install docker, maven, git, etc.
// git clone project then cd /ApiGateway

export DBHOST="swift_mysql"  // this will be changed to prod's host name
export DBNAME="swift_mysql"
// export USERNAME and PASSWORD

mvn clean package
docker build -t swift_test_app .
docker compose up -d

mysql -u $USERNAME -p $PASSWORD -h 127.0.0.1 -P 3306 < ../SQL/init.sql
mysql -u $USERNAME -p $PASSWORD -h 127.0.0.1 -P 3306 < ../SQL/dummy-data.sql
```


## Reference Information

### Run with dev properties

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
