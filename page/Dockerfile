FROM openjdk:8-jdk

VOLUME /tmp
VOLUME /etc/localtime
VOLUME /etc/timezone

ADD page-0.0.1-SNAPSHOT.jar page-service.jar
ENTRYPOINT ["java", "-jar", "/page-service.jar"]
EXPOSE 8080