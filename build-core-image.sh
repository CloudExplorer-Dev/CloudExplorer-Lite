#!/usr/bin/env bash

# 必须传入环境变量 build_with_platform   指定构建平台 linux/amd64,linux/arm64
# 必须传入环境变量 CE_IMAGE_REPOSITORY   指定镜像仓库
# 可选传入环境变量 reversion             指定打包后版本

function createCoreDockerfile() {
  cat > target/Dockerfile << EOF
FROM registry.cn-qingdao.aliyuncs.com/cloudexplorer/alpine-openjdk17:latest

MAINTAINER FIT2CLOUD <support@fit2cloud.com>

WORKDIR /opt/cloudexplorer/apps/core/repository
COPY repository ./

WORKDIR /opt/cloudexplorer/apps/core
COPY run-core.sh ./
RUN chmod 755 run-core.sh && ln -s /opt/cloudexplorer/apps/core/run-core.sh /usr/bin/run-core && mkdir -p /opt/cloudexplorer/downloads

COPY eureka-${APP_VERSION}.jar gateway-${APP_VERSION}.jar management-center-${APP_VERSION}.jar ./

WORKDIR /opt/cloudexplorer
COPY VERSION ./

CMD ["run-core", "run"]

EOF
}

#获取版本号
declare _APP_VERSION=`awk '/<revision>[^<]+<\/revision>/{gsub(/<revision>|<\/revision>/,"",$1);print $1;exit;}' pom.xml`
declare APP_VERSION=${reversion:-$_APP_VERSION}

echo ${APP_VERSION} > target/VERSION

declare APP_EUREKA_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' framework/eureka/pom.xml`
declare APP_GATEWAY_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' framework/gateway/pom.xml`
declare APP_MANAGEMENT_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' framework/management-center/backend/pom.xml`

cp doc/cloudexplorer/apps/core/run-core.sh target

createCoreDockerfile

cd target

declare docker_build_command="docker buildx build --platform ${build_with_platform} -t ${CE_IMAGE_REPOSITORY}cloudexplorer-core:${APP_VERSION} --push ."

echo $docker_build_command

`$docker_build_command` || exit 1;


