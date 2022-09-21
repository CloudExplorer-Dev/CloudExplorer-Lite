#bash
#整体打包
function buildModules() {
  #1. 打包jar
  mvn initialize
  mvn clean package -Dmaven.test.skip=true
  if [ $? -ne 0 ]; then
    cd $project_base_path
    exit 0
  fi

  #2. 打包镜像
  for build_module_name in $f2c_moduleNames
  do
    cd $f2c_modules["$build_module_name"]
    local _imageName=${image_registry_base_path}${build_module_name}":${image_tag}"
    docker build --build-arg CE_JAR_VERSION=$version -t ${_imageName} .
    if [ $? -ne 0 ]; then
      cd $project_base_path
      exit 0
    fi

    #3. 上传镜像
    if [ $upload_image -ne 0 ]; then
      echo "开始上传镜像: "${_imageName}
      docker push ${_imageName}
      echo "镜像上传成功"
      if [ $? -ne 0 ]; then
          cd $project_base_path
          exit 0
        fi
    fi

  done

  # 回到当前目录
  cd $project_base_path
  echo "构建完成"
}

version=`awk '/<revision>[^<]+<\/revision>/{gsub(/<revision>|<\/revision>/,"",$1);print $1;exit;}' pom.xml`

declare image_tag=$version
declare image_registry_base_path="registry.fit2cloud.com/cloudexplorer4/"
declare -i upload_image=0

while getopts P:t:u option
do
  case "${option}"
  in
  P)
   image_registry_base_path=${OPTARG};;
  t)
    image_tag=${OPTARG};;
  u)
    upload_image=1;;
  esac
done

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
