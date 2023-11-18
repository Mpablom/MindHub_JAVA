FROM amazoncorretto:11-alpine-jdk

WORKDIR /app

COPY . /app

RUN apk update && apk add --no-cache wget unzip

EXPOSE 8080

CMD ["java", "-jar", "homebaking-0.0.1-SNAPSHOT.jar"]

