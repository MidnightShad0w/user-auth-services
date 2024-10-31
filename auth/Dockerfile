FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "./app.jar"]
EXPOSE 8080
