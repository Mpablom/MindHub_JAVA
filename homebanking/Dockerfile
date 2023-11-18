FROM amazoncorretto:11-alpine-jdk
MAINTAINER Pablo

RUN ./gradlew build

COPY ./build/libs/homebanking-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
