#! /bin/bash

set -e

export DATABASE=postgres
export MODE=-polling
export SPRING_PROFILES_ACTIVE=EventuatePolling

./_build-and-test-all.sh
