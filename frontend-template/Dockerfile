## 基础镜像为 Node.js，用于安装依赖和打包前端项目
#FROM harbor-test.7moor.com:8443/base/node:18.18 as build-stage
#
## 设置工作目录
#WORKDIR /app
#
## 将前端项目代码复制到容器内
#COPY ./ /app
#
## 安装项目依赖
#RUN yarn install
#
## 构建前端项目
#RUN yarn build

FROM harbor-test.7moor.com:8443/base/nginx:1.17.4

WORKDIR /app

#COPY --from=build-stage /app/dist /app/dist
COPY ./dist /app/dist

RUN echo "Asia/Shanghai" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata



CMD ["nginx", "-g", "daemon off;"]
