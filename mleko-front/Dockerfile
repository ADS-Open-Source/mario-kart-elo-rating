FROM node:lts-alpine AS build
LABEL authors="ADS" version=1.0

WORKDIR /usr/src/app

RUN addgroup -S mkelo && adduser -S mkelo -G mkelo
RUN chown -R mkelo /usr/src/app
USER mkelo

COPY --chown=mkelo package.json package-lock.json ./
RUN npm install --legacy-peer-deps

COPY --chown=mkelo . .

RUN npm run build
