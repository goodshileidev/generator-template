#!/usr/bin/env bash

set -ex


ssh_ip=$1
ssh_port=$2
sshName=$3
deploy_dir=$4
commonVarScriptPath=$5

echo "commonVarScriptPath: $commonVarScriptPath"
echo "start build ssh deploy to remote ${ssh_ip}"

chmod +x "$commonVarScriptPath"
source $commonVarScriptPath


echo "branch: $CI_COMMIT_BRANCH"
echo "commit: $CI_COMMIT_SHORT_SHA"

echo "export GIT_BRANCH=$CI_COMMIT_BRANCH" > .version
echo "export GIT_COMMI=$CI_COMMIT_SHORT_SHA" >> .version
echo "export GIT_COMMIT_AUTHOR='$CI_COMMIT_AUTHOR'" >> .version
echo "export BUILD_TIME='$(date +'%Y-%m-%d %H:%M:%S')'" >> .version


# 设置 env 数据库信息，在启动时
echo "export SERVER_IP=${SERVER_IP}" >> .version



deploy_start_shell="$deploy_dir/${APP_NAME}.sh"

# 上传到包目录
ssh -p "$ssh_port" "$sshName"@"$ssh_ip" sudo cp /dev/stdin "${deploy_start_shell}" < "$PWD/scripts/docker_run.sh"

ssh -p "$ssh_port" "$sshName"@"$ssh_ip" "sudo [ -d ${deploy_dir}/conf.d ] || mkdir ${deploy_dir}/conf.d"

ssh -p "$ssh_port" "$sshName"@"$ssh_ip" sudo cp /dev/stdin "$deploy_dir/conf.d/nginx.conf" < "$PWD/ci/nginx.conf"

ssh -p "$ssh_port" "$sshName"@"$ssh_ip" sudo cp /dev/stdin "$deploy_dir/.version" < "$PWD/.version"

#ssh -p "$ssh_port" "$sshName"@"$ssh_ip" sudo echo "bash ${deploy_start_shell} ${APP_NAME} ${SERVER_IP} ${CONTAINER_NAME}" > "$deploy_dir/start.sh"
ssh -p "$ssh_port" "$sshName"@"$ssh_ip" "sudo bash -c 'echo \"bash ${deploy_start_shell} ${APP_NAME} ${SERVER_IP} ${CONTAINER_NAME} ${WEB_PORT}\" > \"${deploy_dir}/start.sh\"'"

# 添加执行权限
ssh -p "$ssh_port" "$sshName"@"$ssh_ip" "sudo chmod +x ${deploy_start_shell}"
ssh -p "$ssh_port" "$sshName"@"$ssh_ip" "sudo chmod +x ${deploy_dir}/start.sh"

ssh -tt ${sshName}@${ssh_ip} -p ${ssh_port} "sudo ${deploy_dir}/start.sh"
