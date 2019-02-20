#! /bin/bash

set -e

export DATABASE=mysql
export MODE=-binlog
export SPRING_PROFILES_ACTIVE=Redis
export BROKER=redis

./_build-and-test-all.sh
