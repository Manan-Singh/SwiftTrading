## Environmental Variables

NOTE: ensure that the spring boot application uses prod properties; this is normally done automatically by default

### Variables in application.properties 

The application.properties file uses DBHOST, DBNAME, USERNAME, and PASSWORD environment variables.

```
// some properties...

spring.datasource.url = jdbc:mysql://${DBHOST}:3306/${DBNAME}?useSSL=false
spring.datasource.username = ${USERNAME}
spring.datasource.password = ${PASSWORD}

// other properties...
```

### Variables in Linux

Set variables in linux like below:

```
export DBNAME=swift
export USERNAME=swift
export PORT=8087
```

Check if these work by echoing them.

```
echo USERNAME
```

### Variables in docker-compose.yml

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
    volumes:
      - ./SQL/sqlscript.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - 3306:3306

  mqbroker:
    image: docker.conygre.com:5000/mqbroker
    ports:
      - 61616:61616

  swift-engine:
    image: docker.conygre.com:5000/swift-engine
    depends_on:
      - swift-mysql
      - mqbroker
    environment:
      DBHOST: swift-mysql
      DBNAME: ${DBNAME}
      USERNAME: ${USERNAME}
      PASSWORD: ${PASSWORD}
      ACTIVEMQ: mqbroker
      PORTAPI: 8081 #PORTAPI is not used, TODO: remove from application.property

  swift-rest-ui:
    image: docker.conygre.com:5000/swift-rest-ui
    depends_on:
      - swift-mysql
    ports:
      - ${PORT}:${PORT}
    environment:
      DBHOST: swift-mysql
      DBNAME: ${DBNAME}
      USERNAME: ${USERNAME}
      PASSWORD: ${PASSWORD}
      PORT: ${PORT}
```

The DBHOST, DBNAME, USERNAME, and PASSWORD variables are passed in under the "environment" field. 
These will then be environment variables within the app container so that application.properties can pick them up.

### Variables when Running Docker Commands

To use environment variables when running commands, use the `-e` flag.
For instance, the MYSQL_ROOT_PASSWORD environment variable in the mysql container called swift-mysql will be set to the PASSWORD environment variable on the linux box.

```
docker run -p 3306:3306 --name swift-mysql -e MYSQL_ROOT_PASSWORD=$PASSWORD -e MYSQL_USER=$USERNAME -e MYSQL_PASSWORD=$PASSWORD -e MYSQL_DATABASE=$DBNAME -d mysql:5.7
```