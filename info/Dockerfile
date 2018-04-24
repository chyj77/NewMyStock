FROM openjdk:8-jdk

VOLUME /tmp
VOLUME /etc/localtime
VOLUME /etc/timezone

ADD info-0.0.1-SNAPSHOT.jar info-service.jar
ENTRYPOINT ["java", "-jar", "/info-service.jar"]
EXPOSE 8085