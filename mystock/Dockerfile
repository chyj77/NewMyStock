FROM openjdk:8-jdk

VOLUME /tmp
VOLUME /etc/localtime
VOLUME /etc/timezone

ADD mystock-0.0.1-SNAPSHOT.jar mystock-service.jar
ENTRYPOINT ["java", "-jar", "/mystock-service.jar"]
EXPOSE 8888