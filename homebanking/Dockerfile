# Usar la imagen base de Gradle
FROM docker.io/library/gradle:7.5.1-jdk11-alpine@sha256:df3978bc9721b713f1494380e20afa1c41a7d97b17782e8e7813dd019179c146

COPY . /home/gradle

WORKDIR /home/gradle

RUN gradle build