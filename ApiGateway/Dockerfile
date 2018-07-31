FROM openjdk:8-alpine

VOLUME /tmp

EXPOSE 8081

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app.jar"]