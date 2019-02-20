#! /bin/bash

set -e

export DATABASE=postgres
export MODE=-wal
export SPRING_PROFILES_ACTIVE=PostgresWal,Redis
export BROKER=redis

./_build-and-test-all.sh
