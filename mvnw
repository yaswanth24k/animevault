#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Startup Script
# ----------------------------------------------------------------------------

MVNW_REPOURL=https://repo.maven.apache.org/maven2
MVNW_VERBOSE=false

BASE_DIR=$(cd "$(dirname "$0")"; pwd -P)
WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"

if [ ! -f "$WRAPPER_DIR/maven-wrapper.properties" ]; then
  echo "Missing maven-wrapper.properties"
  exit 1
fi

distributionUrl=$(grep distributionUrl "$WRAPPER_DIR/maven-wrapper.properties" | cut -d'=' -f2)

mkdir -p "$WRAPPER_DIR"

exec curl -fsSL "$distributionUrl" | tar -xz -C "$WRAPPER_DIR" --strip-components=1