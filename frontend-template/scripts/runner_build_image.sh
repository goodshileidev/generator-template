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
docker-compose up


tagname=`echo $CI_COMMIT_BRANCH | tr '/' '-'`-$CI_COMMIT_SHORT_SHA
IMAGENAME=${IMAGE}:${tagname}

docker build . -t $IMAGENAME

echo "start push docker $IMAGENAME"
docker push $IMAGENAME
