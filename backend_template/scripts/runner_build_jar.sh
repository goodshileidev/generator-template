#!/usr/bin/bash

BASE_PATH=$(
    cd $(dirname $0)
    pwd
)


cd $BASE_PATH

cd ..
# 获取 Java 版本
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
# 提取主版本号
JAVA_MAJOR_VERSION=$(echo $JAVA_VERSION | awk -F. '{print $1}')

# 检查是否为 Java 11
if [ "$JAVA_MAJOR_VERSION" -ne 11 ]; then
    echo "当前 Java 版本为 $JAVA_MAJOR_VERSION，不是 11，设置 JAVA_HOME"

    # 这里设置 JAVA_HOME 为你的 Java 11 的安装路径
    export JAVA_HOME=/usr/share/jdk-11.0.23
    export PATH=$JAVA_HOME/bin:$PATH

    echo "已设置 JAVA_HOME 为 $JAVA_HOME"
else
    echo "Java 版本为 11，无需更改 JAVA_HOME"
fi
mvn clean package -Dmaven.test.skip=true -f be-parent

