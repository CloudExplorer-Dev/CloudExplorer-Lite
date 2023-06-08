#!/usr/bin/env bash

# 必须传入环境变量 CE_IMAGE_REPOSITORY   指定镜像仓库
# 可选传入环境变量 revision             指定打包后版本

function createCoreDockerComposeFile() {
  cat > target/docker-compose-core.yml << EOF
version: "3.9"
services:
  cloudexplorer:
    image: \${CE_IMAGE_REPOSITORY}cloudexplorer-core:${APP_VERSION}
    hostname: cloudexplorer
    container_name: cloudexplorer
    ports:
      - "\${CE_PORT}:${APP_GATEWAY_PORT}"
#    environment:
#      eureka.instance.hostname: eureka
    env_file:
      - \${CE_BASE}/cloudexplorer/.env
#health_check_mysql
    healthcheck:
      test: ["CMD", "curl", "-f", "127.0.0.1:${APP_GATEWAY_PORT}"]
      interval: 15s
      timeout: 5s
      retries: 20
    volumes:
      - \${CE_BASE}/cloudexplorer/logs:/opt/cloudexplorer/logs
      - \${CE_BASE}/cloudexplorer/conf:/opt/cloudexplorer/conf
      - \${CE_BASE}/cloudexplorer/apps/extra:/opt/cloudexplorer/apps/extra
      - \${CE_BASE}/cloudexplorer/downloads:/opt/cloudexplorer/downloads
      - \${CE_BASE}/cloudexplorer/data:/opt/cloudexplorer/data
    networks:
      - ce-network
    deploy:
      resources:
        limits:
          memory: \${CE_CORE_MEMORY_LIMIT:-8G}

EOF
}

#获取版本号
declare _APP_VERSION=`awk '/<revision>[^<]+<\/revision>/{gsub(/<revision>|<\/revision>/,"",$1);print $1;exit;}' pom.xml`
declare APP_VERSION=${revision:-$_APP_VERSION}

declare APP_EUREKA_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' framework/eureka/pom.xml`
declare APP_GATEWAY_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' framework/gateway/pom.xml`
declare APP_MANAGEMENT_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' framework/management-center/backend/pom.xml`


createCoreDockerComposeFile

