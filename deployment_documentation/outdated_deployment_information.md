## -- OUTDATED --

Below is outdated information. It is kept for reference in case it may be useful.

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