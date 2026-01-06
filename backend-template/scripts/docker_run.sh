#!/usr/bin/env bash


BASE_PATH=$(
    cd $(dirname $0)
    pwd
)


cd $BASE_PATH


appname=$1
mavenProfile=$2
image=$3

echo "base_path: ${base_path}"
echo "appname: ${appname}"
echo "mavenProfile: ${mavenProfile}"
echo "image: ${image}"

echo "start ${appname} from ${image} in ${dirname}"


docker stop $appname
docker rm $appname

if [ ! -d './data' ];then
  mkdir ./data
fi

docker run -d \
-e active=${mavenProfile} \
-e TZ=Asia/Shanghai \
-p 8001:8001 \
-v ${BASE_PATH}/data:/data/ \
--name ${appname} ${image}

