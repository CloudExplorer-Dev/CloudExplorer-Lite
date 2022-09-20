#bash
#整体打包
function buildModules() {
  #1. 打包jar
  mvn clean package -Dmaven.test.skip=true
  if [ $? -ne 0 ]; then
    cd $project_base_path
    exit 0
  fi

  #2. 打包镜像
  for build_module_name in $f2c_moduleNames
  do
    cd $f2c_modules["$build_module_name"]
    local _imageName="registry.fit2cloud.com/cloudexplorer4/"${build_module_name}":${version}"
    docker build --build-arg IMAGE_TAG=$version -t ${_imageName} .
    if [ $? -ne 0 ]; then
      cd $project_base_path
      exit 0
    fi
  done

  # 回到当前目录
  cd $project_base_path
  echo "构建完成"
}

version=`awk '/<revision>[^<]+<\/revision>/{gsub(/<revision>|<\/revision>/,"",$1);print $1;exit;}' pom.xml`

#当前路径
declare project_base_path=$(pwd)

declare -A f2c_modules
declare -A f2c_moduleNames

function getServices() {
  _framework=("eureka" "gateway")
  for _dir in $_framework
  do
    f2c_moduleNames["$_dir"]=$_dir
    f2c_modules["$_dir"]=$project_base_path"/framework/"$_dir
  done

  for _dir in $(ls -l services/ |awk '/^d/ {print $NF}')
  do
    f2c_moduleNames["$_dir"]=$_dir
    f2c_modules["$_dir"]=$project_base_path"/services/"$_dir
  done
}

getServices

buildModules
