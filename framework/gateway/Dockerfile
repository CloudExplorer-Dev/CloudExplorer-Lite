FROM registry.fit2cloud.com/cloudexplorer4/alpine-openjdk17

ARG CE_JAR_VERSION

MAINTAINER FIT2CLOUD <support@fit2cloud.com>

RUN mkdir -p /opt/apps

ADD target/gateway-$CE_JAR_VERSION.jar /opt/apps

ENV JAVA_APP_JAR=/opt/apps/gateway-$CE_JAR_VERSION.jar

ENV AB_OFF=true

ENV JAVA_OPTIONS=-Dfile.encoding=utf-8

HEALTHCHECK --interval=15s --timeout=5s --retries=20 --start-period=30s CMD curl -f 127.0.0.1:9000

CMD ["/deployments/run-java.sh"]
