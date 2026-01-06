#!/usr/bin/bash

BASE_PATH=$(
    cd $(dirname $0)
    pwd
)


cd $BASE_PATH

cd ..


echo "start build docker"

jar_file="./be-backend/target/be-backend-1.0.0-SNAPSHOT.jar"


if [ ! -f $jar_file ]; then
    echo "${jar_file} is not exist"
    exit 1
fi

#docker build . -t partner/be-backend:0.1
tagname=`echo $CI_COMMIT_BRANCH | tr '/' '-'`-$CI_COMMIT_SHORT_SHA
IMAGENAME=${IMAGE}:${tagname}


docker build . -t $IMAGENAME

echo "start push docker $IMAGENAME"
docker push $IMAGENAME
