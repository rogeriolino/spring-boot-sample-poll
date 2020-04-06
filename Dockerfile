FROM node:12.16.1-alpine as node

WORKDIR /app
COPY src src

RUN NODE_ENV=production npx webpack


FROM openjdk:11.0-jdk-slim as jdk

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY --from=node /app/src/main/resources/static/built /app/src/main/resources/static/built

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM openjdk:11.0-jre-slim

VOLUME /tmp

ARG DEPENDENCY=/app/target/dependency

COPY --from=jdk ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=jdk ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=jdk ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","app.Application"]