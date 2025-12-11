#!/usr/bin/env bash


BASE_PATH=$(
    cd $(dirname $0)
    pwd
)


cd $BASE_PATH


appname=$1
serverIp=$2
image=$3
web_port=$4

echo "base_path: ${BASE_PATH}"
echo "appname: ${appname}"
echo "image: ${image}"

echo "start ${appname} from ${image} in ${BASE_PATH}"


docker stop $appname
docker rm $appname

if [ ! -d "./logs" ]; then
  mkdir ./logs
fi

if [[ -n "${serverIp}" ]]; then
  sed -i "s/ngs-server-name/${serverIp}/g" ${BASE_PATH}/conf.d/nginx.conf
  echo "sid ngs-server-name -> ${serverIp}"
fi


if [[ -z "${web_port}" ]]; then
  web_port=80
fi


docker run -d \
-e TZ=Asia/Shanghai \
-v ${BASE_PATH}/logs:/data/logs \
-v ${BASE_PATH}/conf.d:/etc/nginx/conf.d \
-p ${web_port}:80 \
--name ${appname} ${image}

