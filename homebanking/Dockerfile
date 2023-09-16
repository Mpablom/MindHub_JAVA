FROM gradle:7.5.1-jdk11-alpine

WORKDIR /app

# Copiar los archivos de aplicación desde el directorio del proyecto al contenedor
COPY ./src /app/src

# Construir la aplicación
RUN gradle build

EXPOSE 8080

ENV PORT=8080

ENTRYPOINT ["java","-jar","build/libs/homebanking-0.0.1-SNAPSHOT.jar"]
