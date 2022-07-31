#基础镜像
FROM openjdk:8-jre-alpine

#添加中文字体
ADD fonts/* /usr/share/fonts/

#设置字符集
ENV LANG en_US.UTF-8

#安装字体软件
RUN apk add ttf-dejavu fontconfig && rm -rf /var/cache/apk/*

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#把你的项目war包引入到容器的root目录下
COPY *.war  /root/app.war

CMD ["--server.port=8080"]

#项目的启动方式
ENTRYPOINT ["java","-jar","/root/app.war"]
