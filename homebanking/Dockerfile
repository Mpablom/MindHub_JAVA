FROM gradle:7.5.1-jdk11-alpine

# Establece un directorio de trabajo en el contenedor
WORKDIR /homebanking
# Copia solo los archivos Gradle necesarios
COPY . .
# Ejecuta gradle build en el directorio /app
RUN gradle build

EXPOSE 8080

ENV PORT=8080

ENTRYPOINT ["java","-jar","build/libs/homebanking-0.0.1-SNAPSHOT.jar"]
