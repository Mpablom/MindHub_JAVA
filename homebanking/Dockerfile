FROM docker.io/library/amazoncorretto:11-alpine-jdk@sha256:a1d5c7d78d7ba10040ea256a8d5af7d76b0da3d77592793b3515bb98b029fb2b

WORKDIR /app

RUN mkdir -p /app/build/libs

COPY . /app

RUN ls -la /app/build/libs/ || true

RUN apk update && \
    apk add --no-cache wget unzip && \
    chmod +x /app/*

EXPOSE 8080

CMD ["java", "-jar", "/app/build/libs/homebanking-0.0.1-SNAPSHOT.jar"]
