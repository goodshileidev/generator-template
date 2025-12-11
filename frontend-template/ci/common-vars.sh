#!/usr/bin/env bash
set -v

deploy_dir=/data/app/ngs-admin-web

tagname=`echo $CI_COMMIT_BRANCH | tr '/' '-'`-$CI_COMMIT_SHORT_SHA
CONTAINER_NAME=${IMAGE}:${tagname}
