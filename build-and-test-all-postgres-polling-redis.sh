#! /bin/bash

set -e

export DATABASE=postgres
export MODE=-polling
export SPRING_PROFILES_ACTIVE=EventuatePolling,Redis
export BROKER=redis

./_build-and-test-all.sh
