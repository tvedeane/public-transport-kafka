# public-transport-kafka
REST API for public transport. Consumes delays from Kafka queue.

## Endpoints ##

`/line/:line_name/is-delayed` - returns true or false

`/line` with parameters `x`, `y`, and `time` - returns line name for given parameters

`/stop/:stop_id/next-line` - returns line name arriving next at a given stop

## Dependencies ##

- Docker
- Docker Compose
- Java
- SBT

## Build & Run ##

First start dependencies with `docker-compose up data-producer` (assuming Docker daemon is running).

Run application:
```sh
$ sbt
> jetty:start
```

Application should be available at `localhot:8081`.

### Automatic code reloading ###
```sh
$ sbt
> ~;jetty:stop;jetty:start
```
