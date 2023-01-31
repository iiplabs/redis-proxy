# redis-proxy

## Build Setup

1. Check out this repository.

2. Add ".env" file and set environmental variables in it. Check .env.example for the list of variables to be set and/or use the section below.

3. Install Docker.

## Environment variables

Below is the list of recommended content for your local .env file.

RP_CORE_SERVER_PORT=9091

RP_CORE_REDIS_HOSTS=rp-redis:6379

RP_CORE_REDIS_USERNAME=

RP_CORE_REDIS_PASSWORD=

profileName=rp

## Docker

### Start the system

```bash
docker compose up -d
```

### Shutdown the system

```bash
docker compose down
```

### Rebuild an individual service

```bash
docker compose build rp-core
```

### Check the latest build date of a service

```bash
docker inspect -f '{{.Created}}' rp-core
```

### Redeploy an individual service

```bash
docker compose up --no-deps -d rp-core
```

### Connect to logs of Spring Boot backend

```bash
docker logs --tail 50 --follow --timestamps rp-core
```

## Testing

Maven shall be used for unit testing.

```bash
mvn test
```

