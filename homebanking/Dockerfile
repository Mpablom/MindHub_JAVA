FROM openjdk:11-jdk AS build

# Copia el proyecto y la herramienta gradlew a la etapa de construcción
COPY . /app
WORKDIR /app

# Descarga gradle e inicializa el proyecto
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-6.8.3-bin.zip
RUN unzip -d /opt/gradle gradle-6.8.3-bin.zip
RUN ln -s /opt/gradle/gradle-6.8.3/bin/gradle /usr/bin/gradle
RUN gradle wrapper --gradle-version 6.8.3
RUN ./gradlew bootJar --no-daemon

# Etapa de empaquetado
FROM openjdk:11-jdk-slim

EXPOSE 8080

COPY --from=build /app/build/libs/homebanking-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
