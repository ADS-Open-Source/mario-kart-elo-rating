#
# Build BACKEND stage
#
FROM maven:3.6.3-openjdk-17-slim AS build
LABEL authors="ADS" version=1.0
COPY mleko-back/src /home/app/src
COPY mleko-back/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Build FRONTEND stage
#
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /home/app/target/mkelo-?.?.?-spring-boot.jar /usr/local/lib/demo.jar

RUN apk add --no-cache bash
RUN echo "@personal http://dl-cdn.alpinelinux.org/alpine/v3.18/main" >> /etc/apk/repositories
RUN apk add nodejs@personal npm@personal
RUN node --version
RUN npm --version

WORKDIR /usr/src/app

COPY mleko-front/package.json .
COPY mleko-front/package-lock.json .
RUN npm install --legacy-peer-deps

# copy over all code files
COPY mleko-front/. .

EXPOSE 4200
EXPOSE 8800

#
# RUN processes in Supervisor
#
RUN apk --update add supervisor
COPY service_script.conf /src/supervisor/service_script.conf
CMD ["supervisord","-c","/src/supervisor/service_script.conf"]