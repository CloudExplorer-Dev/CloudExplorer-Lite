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
  for build_module_name in ${f2c_moduleNames[@]}
  do
#    echo ${f2c_modules["$build_module_name"]}
    cd ${f2c_modules["$build_module_name"]}
    local _imageName=${image_registry_base_path}${build_module_name}":${image_tag}"
    docker build --build-arg CE_JAR_VERSION=$version --platform ${build_with_platform} -t ${_imageName} .
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
declare build_with_platform="linux/amd64"

TEMP=`getopt -o ut:P: --long upload,tag:,path:,platform: -- "$@"`
while true ; do
    case "$1" in
        -u|--upload)
          echo "$1: Push image: true"
          upload_image=1;
          shift ;;
        -P|--path)
          echo "$1: Image Registry Base Path: '$2'" ;
          image_registry_base_path=$2;
          shift 2 ;;
        -t|--tag)
          echo "$1: Tag ID: '$2'" ;
          image_tag=$2;
          shift 2 ;;
        --platform)
           echo "$1: Use Platform: '$2'" ;
                    build_with_platform=$2;
                    shift 2 ;;
        --) shift ; break ;;
        "") break ;;
        *) echo "Internal error! Unknown param: $1" ; exit 1 ;;
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
