#!/usr/bin/env bash

set -ex


ssh_ip=$1
ssh_port=$2
sshName=$3
deploy_dir=$4


echo "start build ssh deploy to remote ${ssh_ip}"
rsync -avz -e "ssh -p ${ssh_port}" $PWD/ ${sshName}@${ssh_ip}:${deploy_dir}
