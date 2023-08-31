FROM eclipse-temurin:17-jre-alpine
LABEL authors="ADS" version=1.0

WORKDIR /usr/src/app

RUN addgroup -S mkelo && adduser -S mkelo -G mkelo
RUN chown -R mkelo /usr/src/app
USER mkelo

ARG JAR_FILE=./target/mkelo-?.?.?-spring-boot.jar

COPY --chown=mkelo ${JAR_FILE:?error} /usr/src/app/mkelo.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/mkelo.jar"]
EXPOSE 8080