# readme

## 环境需要

1. jdk11
2. 打包时使用 mvn，需要 3.8 以上

打包命令
```
bash ./scripts/runner_build.sh

# 或者

mvn clean package -Dmaven.test.skip=true -f be-parent
```

## docker 构建

```shell
docker build . -t partner/be-backend:0.1
```

启动

```
docker run -d \
-e active=devwh \
-e TZ=Asia/Shanghai \
-p 8001:8001 \
-v $PWD/logs:/data/logs \
--name be-backend partner/be-backend:0.1 

```
