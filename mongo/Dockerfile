FROM openjdk:8-jdk

VOLUME /tmp

ADD mongo-0.0.1-SNAPSHOT.jar mongo-service.jar
ENTRYPOINT ["java", "-jar", "/mongo-service.jar"]
EXPOSE 8082