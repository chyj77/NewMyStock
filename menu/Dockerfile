FROM openjdk:8-jdk

VOLUME /tmp

ADD menu-0.0.1-SNAPSHOT.jar menu-boot.jar
ENTRYPOINT ["java", "-jar", "/menu-boot.jar"]
EXPOSE 8084