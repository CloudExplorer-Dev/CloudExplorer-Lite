#bash
#为了单独打包模块，需要先install sdk到maven本地仓库
mvn initialize

#当前路径
declare project_base_path=$(pwd)

#0. 打包前端commons
mvn clean
yarn install
yarn build:base
#1. install parent cloudexplorer
mvn install -N
#2. install parent framework
cd $project_base_path/framework
mvn install -N
#3. install providers
cd $project_base_path/framework/provider
mvn clean install -Dmaven.test.skip=true
#4. install parent sdk
cd $project_base_path/framework/sdk
mvn install -N
#5. install sdk
cd $project_base_path/framework/sdk/backend
mvn clean install -Dmaven.test.skip=true

#构建完成回到当前目录
cd $project_base_path

