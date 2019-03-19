#! /bin/bash

set -e


. ./set-env-mysql.sh

docker-compose -f docker-compose-mysql-binlog.yml down -v

docker-compose -f docker-compose-mysql-binlog.yml up -d --build zookeeper mysql redis

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog.yml up -d --build cdcservice

./wait-for-services.sh $DOCKER_HOST_IP "8099"

./gradlew -x :end-to-end-tests:test build

docker-compose -f docker-compose-mysql-binlog.yml up -d --build

./wait-for-services.sh $DOCKER_HOST_IP "8081 8082 8083"

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

docker-compose -f docker-compose-mysql-binlog.yml down -v
