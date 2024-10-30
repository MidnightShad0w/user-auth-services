# Build stage
FROM maven:3-openjdk-18-slim AS build
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn clean package -DskipTests

# Package stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /src
COPY --from=build /home/maven/src/target/auth-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
