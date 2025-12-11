#!/usr/bin/env bash

set -ex

BASE_PATH=$(
    cd $(dirname $0)
    cd ..
    pwd
)

cd $BASE_PATH

# runner 上打包，可以缓存 node_modules
rsync -av --exclude='.git' --exclude='node_modules' $BASE_PATH ~/


cd ~/ngs-admin-web
echo "docker compose yarn"
docker-compose -f docker-compose-yarn.yml up
