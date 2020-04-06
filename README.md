# Poll Mania

Web application developed using Spring-Boot and React.

## Requirements

- Java 11
- Mysql 5.7


## Build

### Via Docker

```
docker build -t poll .
```

### Manual

ps: First you need to configure the application properties: `src/main/resources/application.properties`

Build Java source using Maven:

```
./mvnw install
```

Build Typescript source using NPM:

```
NODE_ENV=production npx webpack
```


## Run

Via Docker

```
docker run \
    --rm \
    -it \
    --name poll \
    -p 8080:8080
    -e MYSQL_HOST=<host> \
    -e MYSQL_DB=<dbname> \
    -e MYSQL_USER=<user> \
    -e MYSQL_PASSWORD=<password> \
    poll
```

Via Maven

```
./mvnw spring-boot:run
```

Navigate to http://127.0.0.1:8080/