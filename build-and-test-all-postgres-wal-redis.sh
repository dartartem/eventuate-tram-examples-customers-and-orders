#! /bin/bash

set -e

export DATABASE=postgres
export MODE=-wal
export SPRING_PROFILES_ACTIVE=PostgresWal,Redis

./_build-and-test-all.sh
