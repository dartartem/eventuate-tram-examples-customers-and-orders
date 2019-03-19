#! /bin/bash

set -e

KEEP_RUNNING=
ASSEMBLE_ONLY=
SKIP_EUREKA_TESTS=
USE_EXISTING=

while [ ! -z "$*" ] ; do
  case $1 in
    "--keep-running" )
      KEEP_RUNNING=yes
      ;;
    "--use-existing" )
      USE_EXISTING=yes
      ;;
    "--assemble-only" )
      ASSEMBLE_ONLY=yes
      ;;
    "--skip-eureka-tests" )
      SKIP_EUREKA_TESTS=yes
      ;;
    "--help" )
      echo ./build-and-test-all.sh --keep-running --assemble-only --skip-eureka-tests
      exit 0
      ;;
  esac
  shift
done


. ./set-env-mysql.sh

./gradlew testClasses

if [ -z "USE_EXISTING" ] ; then
    docker-compose -f docker-compose.yml down -v
fi

docker-compose -f docker-compose.yml up -d --build zookeeper mysql redis

./wait-for-mysql.sh

docker-compose -f docker-compose.yml up -d --build cdc-service

./wait-for-services.sh $DOCKER_HOST_IP "8099"

./gradlew -x :end-to-end-tests:test build

docker-compose -f docker-compose.yml up -d --build

./wait-for-services.sh $DOCKER_HOST_IP "8081 8082 8083"

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test


if [ -z "$KEEP_RUNNING" ] ; then
    docker-compose -f docker-compose.yml down -v --remove-orphans
fi
