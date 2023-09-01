FROM public.ecr.aws/docker/library/node:alpine
LABEL authors="ADS" version=1.0

WORKDIR /usr/src/app

COPY package.json .
COPY package-lock.json .
RUN npm install --legacy-peer-deps

# copy over all code files
COPY . .

# expose internal docker container port to external environment
EXPOSE 4200

# specify default command to run when we run the image
CMD npm run start:docker:local
