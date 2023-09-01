# Mario Kart Elo Rating
A simple ELO rating system which can be used to set up a ranking while playing MarioKart with friends.

# Docker Hub Repository
https://hub.docker.com/repositories/adsopensource

## Description
![main_page.png](.github%2Fmain_page.png)
This application aims to provide a simple ELO-based system for ranking small number of users.
It enables players to compare themselves, check their progress and view past races to further enhance sports rivalry.

## Getting Started

### Dependencies

* JDK 17
* Node >v18
* Docker [*not mandatory*]

### Installing

1. Install frontend packages and build the project
   1. while in the project root `cd mleko-front`
   2. `npm install --legacy-peer-deps`
   3. `npm run build`
2. Install backend prerequirements
   1. while in the project root `cd mleko-back`
   2. `.\mvnw clean`
   3. `.\mvnw install`
   4. `.\mvnw package`
3. [Optional] Building Docker image
   1. while in the project root `docker build -t mkelo .`

4. Define environment variables
```
AWS_PROFILE_NAME=[OPTIONAL] used for storing data on AWS S3
AWS_ACCESS_KEY_ID=[OPTIONAL] used for storing data on AWS S3
AWS_SECRET_ACCESS_KEY=[OPTIONAL] used for storing data on AWS S3
DATA_FILENAME=YOUR_DATA_FILENAME
H2_PASSWORD=YOUR_DATABASE_PASSWORD
H2_USERNAME=YOUR_DATABASE_USERNAME
MAIL_HOST=[OPTIONAL] used for emailing service
MAIL_PASSWORD=[OPTIONAL] used for emailing service
MAIL_USERNAME=[OPTIONAL] used for emailing service
MAIL_SENDER=[OPTIONAL] used for emailing service
```

### Executing program

1. Spring-based application run
   1. while in the project root `cd mleko-back`
   2. `.\mvnw spring-boot:run`
   3. application will be hosted at https://localhost:8080
2. Docker-based application run
   1. define environmental variables inside `mkelo.env`
   2. `docker run -p 8080:8080 --env-file=mkelo.env mkelo`
   3. application will be hosted at https://localhost:8080


## Authors

MKELO-TEAM

## Version History

* 0.1
    * Initial Release

## License

This project is licensed under the [MIT](https://opensource.org/license/mit/) License - see the [LICENSE](LICENSE) file for details
