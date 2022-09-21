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
cd framework
mvn install -N
#3. install parent sdk
cd sdk
mvn install -N
#3. install sdk
cd backend
mvn clean install -Dmaven.test.skip=true

#构建完成回到当前目录
cd $project_base_path

