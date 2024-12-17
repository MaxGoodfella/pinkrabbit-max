FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# multistage-сборка ниже

## Этап 1: Сборка приложения
#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /app
#COPY . .
##либо можно здесь вот так:
##COPY checkstyle.xml . // заранее скопировать отдельные файлы
##COPY suppreessions.xml . // заранее скопировать отдельные файлы
##COPY src ./src // добавлять только src
#
#
##RUN mvn dependency:go-offline
#RUN mvn clean package -DskipTests
#
## Этап 2: Запуск приложения
#FROM amazoncorretto:17-alpine-jdk
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]