# VK Wall watcher

Telegram bot which gives ability to subscript on VK communities wall 
posts and be notified through telegram about new posts filtered by some query.

## Configure

Some properties in `application.properties` not stored in Git due to security reasons.
To override them create `config/application.properties` and setup them.

## Build

To package application execute `./gradlew app:bootJar`. To run execute `./gradlew app:bootRun`.

To run with docker execute `docker-compose up`.

## Deployment

### Prepare environment

- On virtual machine `docker`, `docker-compose` and JDK (needed for gradle build) should be installed. 
- Clone repository (possibly with ssh key setup).
- Create docker volume for database with name `vk_wall_wacther_db_volume` 

Example of the steps for Ubuntu:
```bash
# install docker (TBD add commands)

sudo apt-get install docker-compose openjdk-11-jdk
git pull <repo-path>
docker volume create vk_wall_watcher_db_volume
```

### Deploy new version 

Get fresh sources, build and run with following commands:
```bash
cd project-dir
git pull
./gradlew app:bootJar
docker-compose build
docker-compose down
docker-compose up -d
```
