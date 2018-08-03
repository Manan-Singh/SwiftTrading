

# DEPLOYMENT INFO

## Deployment Information

### Environmental Variables

NOTE: ensure that the spring boot application uses prod properties; this should be the default

#### Variables in application.properties 

The application.properties file uses DBHOST, DBNAME, USERNAME, and PASSWORD environment variables.

```
// some properties...

spring.datasource.url = jdbc:mysql://${DBHOST}:3306/${DBNAME}?useSSL=false
spring.datasource.username = ${USERNAME}
spring.datasource.password = ${PASSWORD}

// other properties...
```

#### Variables in docker-compose.yml

If using docker-compose.yml file for testing deployment, set environment variables like below:

```
version: "3"

services:
  swift-mysql:
    image: mysql:5.7
    environment:
      MYSQL_ROOT_PASSWORD: ${PASSWORD}
      MYSQL_DATABASE: ${DBNAME}
      MYSQL_USER: ${USERNAME}
      MYSQL_PASSWORD: ${PASSWORD}
    ports:
      - 3306:3306
  swift-rest-ui:
    image: dockerreg.training.local:5000/swift-rest-ui
    depends_on:
      - swift-mysql
    ports:
      - 8080:8080
    environment:
      DBHOST: ${DBHOST}
      DBNAME: ${DBNAME}
      USERNAME: ${USERNAME}
      PASSWORD: ${PASSWORD}
```

The DBHOST, DBNAME, USERNAME, and PASSWORD variables are passed in under the "environment" field. 
These will then be environment variables within the app container so that application.properties can pick them up.

#### Variables when Running Docker Commands

To use environment variables when running commands, use the `-e` flag.

```
docker run -p 8080:8080 -e DBHOST=$DBHOST -e DBNAME=$DBNAME -e USERNAME=$USERNAME -e PASSWORD=$PASSWORD --name swift-rest-ui --link $DBHOST:$DBNAME swift-rest-ui
```

### TeamCity

#### Build Step 1: Angular

Angular frontend setup. Install necessary files and move into static directory under ApiGateway.

```
#!/bin/bash

npm --prefix swift-app install 
npm --prefix swift-app run ng build --prod
mkdir ApiGateway/src/main/resources/static
mv swift-app/dist/* ApiGateway/src/main/resources/static/
echo "setup complete"
```

#### Build Step 2: Maven

Maven clean package.

#### Build Step 3: Docker Build 

Build and tag image.

```
#!/bin/bash

docker build -t swift-rest-ui .
```

#### Build Step 4: Push to Docker Registry

Tag and push image to docker registry. 

```
#!/bin/bash

if docker tag swift-rest-ui dockerreg.training.local:5000/swift-rest-ui:%build.number%
then
  docker push dockerreg.training.local:5000/swift-rest-ui:%build.number%
else
  echo "FAILED to push the image to the Docker Registry" 1>&2
fi
```

#### Build Step 5: Remove Failed Images

Remove failed images.

```
#!/bin/bash

docker rmi swift-rest-ui
docker rmi dockerreg.training.local:5000/swift-rest-ui:%build.number%
```

### Deployment on AWS Box

```
// run containers in detached mode
docker-compose up -d

// turn off and remove containers
docker-compose down
```


## -- OUTDATED BELOW --

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
