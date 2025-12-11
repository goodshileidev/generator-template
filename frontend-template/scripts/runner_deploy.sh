#!/usr/bin/env bash

set -ex


ssh_ip=$1
ssh_port=$2
sshName=$3
deploy_dir=$4

echo "start build ssh deploy to remote ${ssh_ip}"

ssh -tt ${sshName}@${ssh_ip} -p ${ssh_port} "sudo bash ${deploy_dir}/scripts/docker-build.sh"
