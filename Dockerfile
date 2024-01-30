FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw /app/mvnw
COPY pom.xml /app/pom.xml

RUN ./mvnw dependency:go-offline

COPY ./src/main/ app/src/main/

RUN ./mvnw package -Dquarkus.package.type=uber-jar

FROM eclipse-temurin:17-jre-jammy as app
RUN groupadd -g 999 app && \
    useradd -r -u 999 -g app app
USER app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]