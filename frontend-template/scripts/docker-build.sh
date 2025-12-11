#!/usr/bin/env bash



set -ex

BASE_PATH=$(
    cd $(dirname $0)
    pwd
)


cd $BASE_PATH

cd ..

docker-compose up

echo "build success"
