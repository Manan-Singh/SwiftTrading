# Swift API Gateway
---

This application contains the REST API the front-end can use to 
interact with. 

#### Running in DEV mode

In DEV mode, the spring boot tomcat server runs on port 8081 and 
the app looks to try to make a database connection on 
localhost:3306 with mysql

Simply add `-Dspring.profiles.active=dev` to your Java jar command 
or put that in your VM arguments in your run configurations in 
Intellij