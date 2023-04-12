#!/usr/bin/env bash

declare BUILD_PATH=`pwd`

declare -A f2c_modules
declare -A f2c_moduleNames

function getServices() {
  for _dir in $(ls -l services/ |awk '/^d/ {print $NF}')
  do
    f2c_moduleNames["$_dir"]=$_dir
    f2c_modules["$_dir"]=$BUILD_PATH"/services/"$_dir
  done
}

function build_services(){
  getServices

  mkdir -p ${BUILD_PATH}/target/apps/extra
  touch ${BUILD_PATH}/target/apps/extra/modules

  for build_module_name in "${f2c_moduleNames[@]}"
  do
    cd ${BUILD_PATH}/target || exit

    echo ${build_module_name}
    echo ${f2c_modules["$build_module_name"]}

    declare APP_NAME=${build_module_name}

    declare APP_PORT=`awk '/<service.port>[^<]+<\/service.port>/{gsub(/<service.port>|<\/service.port>/,"",$1);print $1;exit;}' ${f2c_modules["$build_module_name"]}/backend/pom.xml`

    declare _APP_VERSION=`ls ${APP_NAME}-*.jar`

    _APP_VERSION=${_APP_VERSION//.jar/}

    _APP_VERSION=${_APP_VERSION//$APP_NAME-/}

    echo "APP: $APP_NAME PORT: $APP_PORT APP_VERSION: $_APP_VERSION"

    echo "${APP_NAME}|${APP_PORT}|${_APP_VERSION}" >> ${BUILD_PATH}/target/apps/extra/modules

    cp ${BUILD_PATH}/target/${APP_NAME}-${_APP_VERSION}.jar ${BUILD_PATH}/target/apps/extra

    rm -f ${BUILD_PATH}/target/apps/extra/app.yml
    cp ${f2c_modules["$build_module_name"]}/${APP_NAME}.yml ${BUILD_PATH}/target/apps/extra/app.yml

    cd ${BUILD_PATH}/target/apps/extra || exit

    if [[ "$OSTYPE" =~ ^darwin ]]; then
      sed -i "" "s/APP_VERSION/${_APP_VERSION}/" app.yml
      sed -i "" "s/APP_PORT/${APP_PORT}/" app.yml
    else
      sed -i "s/APP_VERSION/${_APP_VERSION}/" app.yml
      sed -i "s/APP_PORT/${APP_PORT}/" app.yml
    fi

    mv app.yml ${APP_NAME}-${_APP_VERSION}.yml

    mkdir -p ${BUILD_PATH}/target/services
    tar czvf ${BUILD_PATH}/target/services/${APP_NAME}-${_APP_VERSION}.tar.gz ${APP_NAME}-${_APP_VERSION}.yml ${APP_NAME}-${_APP_VERSION}.jar

  done

  tar czvf ${BUILD_PATH}/target/services/all-services-${_APP_VERSION}.tar.gz *.yml *.jar modules

}


build_services
