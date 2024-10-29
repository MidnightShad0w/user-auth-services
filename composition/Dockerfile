FROM openjdk:17-jdk-slim
RUN mvn clean package -DskipTests

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ../.env .env
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]
