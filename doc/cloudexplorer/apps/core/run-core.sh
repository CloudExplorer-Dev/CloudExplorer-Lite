#!/usr/bin/env bash

action=$1
target=$2


function run() {

  runMain

  #runExtra

  while true
  do
    clear
    echo ""

    psAll

    sleep 20s
  done
}

function runMain() {
  _version=`cat /opt/cloudexplorer/VERSION`
  echo "run eureka"
  if [ ! -f /opt/cloudexplorer/apps/core/eureka-$_version.jar ]; then
    echo "eureka-$_version.jar not exist" 1>&2
    exit 1
  fi
  nohup java -jar /opt/cloudexplorer/apps/core/eureka-$_version.jar --thin.root=/opt/cloudexplorer/apps/core -Dfile.encoding=utf-8 > /opt/cloudexplorer/logs/eureka-console.log 2>&1 &
  echo "run management-center"
  if [ ! -f /opt/cloudexplorer/apps/core/management-center-$_version.jar ]; then
    echo "management-center-$_version.jar not exist" 1>&2
    exit 1
  fi
  nohup java -jar /opt/cloudexplorer/apps/core/management-center-$_version.jar --thin.root=/opt/cloudexplorer/apps/core -Dfile.encoding=utf-8 > /opt/cloudexplorer/logs/management-center-console.log 2>&1 &
}

function runGateway() {
  _version=`cat /opt/cloudexplorer/VERSION`
  echo "run gateway"
  if [ ! -f /opt/cloudexplorer/apps/core/gateway-$_version.jar ]; then
    echo "gateway-$_version.jar not exist" 1>&2
    exit 1
  fi
  nohup java -jar /opt/cloudexplorer/apps/core/gateway-$_version.jar --thin.root=/opt/cloudexplorer/apps/core -Dfile.encoding=utf-8 > /opt/cloudexplorer/logs/gateway-console.log 2>&1 &
}


function runExtra() {
  if [ -f /opt/cloudexplorer/apps/extra/modules ]; then

    declare -i _err=0

    mapfile modules < /opt/cloudexplorer/apps/extra/modules
    for module_name in "${modules[@]}"
    do
      echo "run ${module_name}"
      name_version=(${module_name//|/ })
      if [ ! -f /opt/cloudexplorer/apps/extra/${name_version[0]}-${name_version[2]}.jar ]; then
        _err=1
        echo "${name_version[0]}-${name_version[2]}.jar not exist" 1>&2
        continue
      fi
      nohup java -jar /opt/cloudexplorer/apps/extra/${name_version[0]}-${name_version[2]}.jar --thin.root=/opt/cloudexplorer/apps/core -Dfile.encoding=utf-8 > /opt/cloudexplorer/logs/${name_version[0]}-console.log 2>&1 &
    done

    if [ $_err -ne 0 ]; then
      exit 1
    fi

  else
    touch /opt/cloudexplorer/apps/extra/modules
  fi
}

function psAll() {
  eureka_pid=`ps x | grep 'java -jar' | grep '/opt/cloudexplorer/apps/core/eureka' | grep -v grep | awk '{print $1}'`
  gateway_pid=`ps x | grep 'java -jar' | grep '/opt/cloudexplorer/apps/core/gateway' | grep -v grep | awk '{print $1}'`
  management_center_pid=`ps x | grep 'java -jar' | grep '/opt/cloudexplorer/apps/core/management-center' | grep -v grep | awk '{print $1}'`

  eureka_status="unhealthy"
  curl -si 127.0.0.1:8761 | grep -q 'HTTP/1.1 200 OK'
  if [ $? -ne 1 ]; then
    eureka_status="healthy"
  fi

  gateway_status="unhealthy"
  curl -si 127.0.0.1:9000 | grep -q 'HTTP/1.1 200 OK'
  if [ $? -ne 1 ]; then
    gateway_status="healthy"
  fi

  management_center_status="unhealthy"
  curl -si 127.0.0.1:9010/management-center/ | grep -q 'HTTP/1.1 200 OK'
  if [ $? -ne 1 ]; then
    management_center_status="healthy"
  fi

  echo "主应用:"
  echo "x gateway pid:${gateway_pid} status:${gateway_status} x"
  echo "x eureka pid:${eureka_pid} status:${eureka_status} x"
  echo "x management-center pid:${management_center_pid} status:${management_center_status} x"

  if [ -f /opt/cloudexplorer/apps/extra/modules ]; then
    echo "其他模块:"

    mapfile modules < /opt/cloudexplorer/apps/extra/modules
    for module_name in "${modules[@]}"
    do
      name_version=(${module_name//|/ })
      _pid=`ps x | grep 'java -jar' | grep '/opt/cloudexplorer/apps/extra/'${name_version[0]} | grep -v grep | awk '{print $1}'`

      _status="unhealthy"
      curl -si 127.0.0.1:${name_version[1]}/${name_version[0]}/ | grep -q 'HTTP/1.1 200 OK'
      if [ $? -ne 1 ]; then
        _status="healthy"
      fi

      echo "x ${name_version[0]} pid:${_pid} status:${_status} x"
    done
  fi
}

function stopModule() {
  stop $target
}

function stop() {
  _target=$1
  if [ $_target ]; then
    _pid=`ps x | grep 'java -jar' | grep '/opt/cloudexplorer/apps/extra/'$_target | grep -v grep | awk '{print $1}'`
    echo "stop $_target pid: ${_pid}"
    kill -9 ${_pid} &>/dev/null
    if [ $? -ne 0 ]; then
      echo "killed nothing"
    else
      echo "killed $_target"
    fi
  fi
}

function stopExtra() {
  _pid=`ps x | grep 'java -jar' | grep '/opt/cloudexplorer/apps/extra/' | grep -v grep | awk '{print $1}'`
  echo "stop extra pid: ${_pid}"
  kill -9 ${_pid} &>/dev/null
  if [ $? -ne 0 ]; then
    echo "killed nothing"
  else
    echo "killed ${_pid}"
  fi
}

function restartModule() {
  stop $target
  start $target
}

function start() {
  _target=$1
  if [ $_target ]; then
    if [ -f /opt/cloudexplorer/apps/extra/modules ]; then
      mapfile modules < /opt/cloudexplorer/apps/extra/modules
      for module_name in "${modules[@]}"
      do
        if [[ "${module_name}" =~ ^"$_target" ]]; then
          echo "run ${module_name}"
          name_version=(${module_name//|/ })
          nohup java -jar /opt/cloudexplorer/apps/extra/${name_version[0]}-${name_version[2]}.jar --thin.root=/opt/cloudexplorer/apps/core -Dfile.encoding=utf-8 > /opt/cloudexplorer/logs/${name_version[0]}-console.log 2>&1 &
          break
        fi
      done
    fi
  fi
}



function uninstallModule() {
  uninstall $target
}


function uninstall() {
  _target=$1
  if [ $_target ]; then

    stop $_target

    echo "uninstall $_target"
    if [[ "$OSTYPE" =~ ^darwin ]]; then
      sed -i "" "/"$_target"/d" /opt/cloudexplorer/apps/extra/modules
    else
      sed -i "/"$_target"/d" /opt/cloudexplorer/apps/extra/modules
    fi

  fi

}

function updateModule() {

  echo "download from $target"
  _file="module-`date +%s`-$RANDOM.tar.gz"
  curl -SL $target -o /opt/cloudexplorer/downloads/$_file || exit 1

  install $_file

}


function installModule() {
  install $target
}


function install() {

  _target=$1

  echo "install by $_target"

  yml_name=`tar -tf /opt/cloudexplorer/downloads/$_target | grep 'yml' | grep -v '._'` || exit 1

  if [[ "$OSTYPE" =~ ^darwin ]]; then
    tar -xzvf /opt/cloudexplorer/downloads/$_target -C /opt/cloudexplorer/apps/extra  || exit 1
  else
    tar -xzvf /opt/cloudexplorer/downloads/$_target -C /opt/cloudexplorer/apps/extra --overwrite  || exit 1
  fi

  _module=`cat /opt/cloudexplorer/apps/extra/$yml_name | grep -v 'display_name' | grep 'name: '`
  _module=${_module//name: /}

  echo "_module: $_module"

  _version=`cat /opt/cloudexplorer/apps/extra/$yml_name | grep 'version: '`
  _version=${_version//version: /}

  echo "_version: $_version"

  _port=`cat /opt/cloudexplorer/apps/extra/$yml_name | grep 'port: '`
  _port=${_port//port: /}

  echo "_port: $_port"

  uninstall $_module

  echo "install $_module"
  echo "$_module|$_port|$_version" >> /opt/cloudexplorer/apps/extra/modules

  echo "prepare ${module_name}"

  rm -rf META-INF/maven/com.fit2cloud/${_module}
  jar xf sample-module-v1.0.0.jar META-INF/maven/com.fit2cloud/${_module}/pom.xml

  declare _parent_name=`xmllint --xpath '/*[local-name()="project"]/*[local-name()="parent"]/*[local-name()="artifactId"]/text()' /opt/cloudexplorer/apps/extra/META-INF/maven/com.fit2cloud/${_module}/pom.xml`
  declare _parent_version=`xmllint --xpath '/*[local-name()="project"]/*[local-name()="parent"]/*[local-name()="version"]/text()' /opt/cloudexplorer/apps/extra/META-INF/maven/com.fit2cloud/${_module}/pom.xml`

  mkdir -p /opt/cloudexplorer/apps/core/repository/com/fit2cloud/${_parent_name}/${_parent_version}

  _main_version=`cat /opt/cloudexplorer/VERSION`

  cat > /opt/cloudexplorer/apps/core/repository/com/fit2cloud/${_parent_name}/${_parent_version}/${_parent_name}-${_parent_version}.pom << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.fit2cloud</groupId>
    <artifactId>services</artifactId>
    <version>${_main_version}</version>
  </parent>
  <groupId>com.fit2cloud</groupId>
  <artifactId>${_parent_name}</artifactId>
  <version>${_parent_version}</version>
  <packaging>pom</packaging>
  <description>${_parent_name}</description>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0</url>
    </license>
  </licenses>
  <modules>
    <module>frontend</module>
    <module>backend</module>
  </modules>
</project>

EOF

  start $_module

}


function getModules() {
  cat /opt/cloudexplorer/apps/extra/modules
}

function main() {
  case "${action}" in
    run)
       run
       ;;
    runGateway)
       runGateway
       ;;
    runExtra)
       runExtra
       ;;
    stopExtra)
       stopExtra
       ;;
    restartExtra)
       stopExtra
       runExtra
       ;;
    getModules)
       getModules
       ;;
    stopModule)
       stopModule
       ;;
    restartModule)
       restartModule
       ;;
    uninstallModule)
       uninstallModule
       ;;
    updateModule)
       updateModule
       ;;
    installModule)
       installModule
       ;;
    ps)
       psAll
       ;;
    *)
       echo "不支持的参数" 1>&2
       exit 1
  esac
}
main


