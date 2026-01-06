FROM harbor-test.7moor.com:8443/base/java:11

COPY ./be-backend/target/be-backend-1.0.0-SNAPSHOT.jar .
#
#RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && apk add tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo "Asia/Shanghai" > /etc/timezone \
#    && apk del tzdata
# jvm参数：通过环境变量 JAVA_TOOL_OPTIONS 指定


RUN echo "Asia/Shanghai" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata


ENV active test

CMD ["java", "-jar", "be-backend-1.0.0-SNAPSHOT.jar","--spring.profiles.active=${active}"]
