FROM amazoncorretto:11-alpine-jdk
MAINTAINER Pablo

WORKDIR /app

COPY . /app

RUN ./gradlew build

CMD ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT.jar"]
