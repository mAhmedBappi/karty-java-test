#!/bin/sh
set -e

exec java \
  -Dfile.encoding=utf-8 -jar -Dspring.profiles.active=prod karty-java-test.jar "$@"
