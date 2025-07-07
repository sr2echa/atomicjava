FROM openjdk:21-jdk-slim as build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN ./mvnw clean install -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 18080
ENTRYPOINT ["java", "-jar", "app.jar"]
