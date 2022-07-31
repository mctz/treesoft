#基础镜像
FROM tomcat:alpine

#添加中文字体
ADD fonts/* /usr/share/fonts/

#设置字符集
ENV LANG en_US.UTF-8

#安装字体软件
RUN apk add ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

WORKDIR /usr/local/tomcat/webapps/

RUN rm -rf *
COPY *.war  ROOT.war

#Expose http port
EXPOSE 8080

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
