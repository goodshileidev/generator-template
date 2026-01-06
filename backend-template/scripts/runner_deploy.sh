#!/usr/bin/env bash
set -xe
currentScriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ssh_ip=$1
ssh_port=$2
sshName=$3
commonVarScriptPath=$4

echo "commonVarScriptPath: $commonVarScriptPath"
chmod +x "$commonVarScriptPath"
source $commonVarScriptPath

echo "ssh_ip: $ssh_ip"
echo "ssh_port: $ssh_port"
echo "sshName: $sshName"
echo "deploy_dir: $deploy_dir"
echo "mavenProfile: $mavenProfile"
echo "deploy_appname:${deploy_appname}"
echo "CONTAINER_NAME:${CONTAINER_NAME}"


# 重启 如果开启了systemd的  使用systemd启动  没有的是传统方式发布


deploy_start_shell="$deploy_dir/${deploy_appname}.sh"

# 上传到包目录
ssh -p "$ssh_port" "$sshName"@"$ssh_ip" sudo cp /dev/stdin "${deploy_start_shell}" < "$PWD/scripts/docker_run.sh"
# 添加执行权限
ssh -p "$ssh_port" "$sshName"@"$ssh_ip" "sudo chmod +x ${deploy_start_shell}"

ssh -tt ${sshName}@${ssh_ip} -p ${ssh_port} "sudo ${deploy_start_shell} ${deploy_appname} ${mavenProfile} ${CONTAINER_NAME}"
