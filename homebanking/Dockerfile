FROM amazoncorretto:11-alpine-jdk@sha256:a1d5c7d78d7ba10040ea256a8d5af7d76b0da3d77592793b3515bb98b029fb2b

WORKDIR /app

COPY . /app

RUN apk update && apk add --no-cache wget unzip

ENV GRADLE_HOME=/opt/gradle/gradle-7.3
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN chmod +x /app/gradlew

RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT.jar"]
