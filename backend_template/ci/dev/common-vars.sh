#!/usr/bin/env bash
set -v
# build env
mavenProfile=${mavenProfile}

if [[ -z "${mavenProfile}" ]]; then
  mavenProfile="devwh"
fi

deploy_appname=be-backend

# deploy env
deploy_dir=/data/app/be-backend


tagname=`echo $CI_COMMIT_BRANCH | tr '/' '-'`-$CI_COMMIT_SHORT_SHA
CONTAINER_NAME=${IMAGE}:${tagname}
