#! /bin/bash

set -e

export DATABASE=mysql
export MODE=-binlog
export SPRING_PROFILES_ACTIVE=Redis

./_build-and-test-all.sh
