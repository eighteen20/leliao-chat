#!/bin/bash

# 指定项目的根目录
LY_DIR="/usr/local/project/leliao-chat"

# 拉取最新的源码
# git pull

# 进入Halo根目录
cd $LY_DIR

# 停止Halo
sh $LY_DIR/bin/leliao.sh stop

# 执行打包
mvn package -Pprod

# 进入打包好的Halo目录
cd $LY_DIR/target

# 运行Halo
nohup java -server -jar `find ./ -name "leliao*.jar"` > /dev/null 2>&1 &

echo "部署完毕，Enjoy！"
