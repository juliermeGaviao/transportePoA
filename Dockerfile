FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ponto_taxi.csv ponto_taxi.csv
ENTRYPOINT ["java","-jar","/app.jar"]
