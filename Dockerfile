# gradle build container
FROM gradle:jdk19 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:19-jre-alpine

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/when.jar

ENTRYPOINT ["java", "-jar", "/app/when.jar"]
