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
    ./gradlew composeDown
fi

./gradlew infrastructureComposeUp

./gradlew -x :end-to-end-tests:test build

./gradlew composeUp

./wait-for-services.sh $DOCKER_HOST_IP "8081 8082 8083"

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test


if [ -z "$KEEP_RUNNING" ] ; then
    ./gradlew composeDown
fi
