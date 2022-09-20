#bash
#单个模块打包
function buildModule() {
  # 进入目录
  echo "开始构建模块[$build_module_name]:"
  cd $f2c_modules["$build_module_name"]

  #1. 打包jar
  mvn clean package -Dmaven.test.skip=true
  if [ $? -ne 0 ]; then
    cd $project_base_path
    exit 0
  fi

  #2. 打包镜像
  local _imageName="registry.fit2cloud.com/cloudexplorer4/"${build_module_name}":${version}"
  docker build --build-arg IMAGE_TAG=$version -t ${_imageName} .
  if [ $? -ne 0 ]; then
    cd $project_base_path
    exit 0
  fi

  # 回到当前目录
  cd $project_base_path
  echo "模块[$build_module_name]构建完成"
}

#获取版本号
version=`awk '/<revision>[^<]+<\/revision>/{gsub(/<revision>|<\/revision>/,"",$1);print $1;exit;}' pom.xml`

build_module_name=$1

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

function checkNoduleName() {
  local _temp=$f2c_moduleNames["$build_module_name"]
  if [ -z $_temp ]
  then
    echo "模块[${build_module_name}]不存在"
    exit 0
  fi
}


if [ -z $build_module_name ]
then
  echo '请选择一个模块进行构建:'
  #选择模块
  select _selectedItem in $f2c_moduleNames; do
     build_module_name=$_selectedItem
     if [ -z $_selectedItem ]
     then
       echo "请重新选择:"
       continue
     fi
     echo "已选择模块: ${build_module_name}"
     break
  done
else
  checkNoduleName
fi


buildModule
