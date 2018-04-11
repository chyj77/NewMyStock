FROM openjdk:8-jdk

VOLUME /tmp

ADD websocket-0.0.1-SNAPSHOT.jar websocket-service.jar
ENTRYPOINT ["java", "-jar", "/websocket-service.jar"]
EXPOSE 8081