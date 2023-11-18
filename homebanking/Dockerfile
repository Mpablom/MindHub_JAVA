FROM amazoncorretto:11-alpine-jdk
MAINTAINER Pablo
COPY build/libs/homebanking-0.0.1-SNAPSHOT.jar app.jar
RUN addgroup -S mygroup && adduser -S myuser -G mygroup
USER myuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
