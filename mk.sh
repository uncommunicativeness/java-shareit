#!/bin/bash

CUR_DIR=$(pwd)

MODULES="gateway
        server"

function build() {
  MOD_DIR=$1

  cd "$MOD_DIR"

  echo "Build module: $MOD_DIR"
  docker build -t "uncommunicativeness/shareit-$MOD_DIR" .

  echo "Build module: $MOD_DIR success"

  cd "$CUR_DIR"
}

function build_all() {
  for mod_dir in ${1}; do
    build "${mod_dir}"
  done
}

docker compose down

mvn clean install -Dmaven.test.skip

build_all "${MODULES}"

echo "All module build success"

docker compose up -d