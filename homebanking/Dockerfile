FROM amazoncorretto:11-alpine-jdk

WORKDIR /app

COPY . /app

RUN apk update && apk add --no-cache wget unzip

RUN chmod +x ./build/libs/homebanking-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/homebanking-0.0.1-SNAPSHOT.jar"]

