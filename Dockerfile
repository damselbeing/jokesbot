FROM openjdk:17-jdk-slim as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw package -DskipTests


FROM openjdk:17-jdk-slim

COPY --from=build target/jokesbot-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "jokesbot-0.0.1-SNAPSHOT.jar"]
