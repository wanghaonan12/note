#!/bin/bash
echo "--------------------------欢迎使用P10企业版部署向导v1.0--------------------------"
#  -------------------------------------------------------------------------初始化检查区域------------------------------------------------------

# 检查是否安装了 Docker
if ! command -v docker >/dev/null 2>&1; then
	echo "Docker 未安装。请安装 Docker 后再运行此脚本。"
	exit 1
fi

# -------------------------------------------------------------------------常量定义区域------------------------------------------------------

# 获取当前脚本文件的路径
installscript_path=$(dirname "$(realpath "$0")")
# docker Root dir
docker_root_dir=$(docker info | grep "Docker Root Dir:" | awk -F': ' '{print $2}')
# 部署目录
install_path=/home/pde
#初始化配置参数
pdes_version=10.1.0
pdes_image_version=10.1.0-RELEASE
#dev=开发环境  test=测试环境  pro=生产环境
pdes_profile=dev
# 注册中心 nacos或eureka
registry_type=nacos
# 部署方式，local：本地jar包构建镜像并部署；remote：远程镜像部署
deploy_type=local
# 镜像仓库地址
pdes_registry_url=harbor.rdc.pde:180
# pdes_registry_url=10.20.40.60:180
placeholder=PDES_PLACEHOLDER_HOST

# eureka
pdes_eureka_port=9901
pdes_config_port=9002
pdes_config_path=$install_path/config

# 脚本存放路径
script_path=$installscript_path/script/
convert_script_path=$installscript_path/script/convert
# jar包存放路径
jars_path=$installscript_path/jar
# 日志相关配置
logs_path=$install_path/logs
log_max_size=100m
log_max_file=1

# 本地存储（宿主机）存储路径配置
#该地址为*存储配置启用的是本地存储时*所配置的宿主机的文件存储地址
#pdes_local_storage=local-storage
#容器内(不要改)
#pdes_local_storage_container=/usr/share/pde/workspace/storage

# 公共工作空间配置(不要改)
#宿主机容器卷名称
pdes_workspace=workspace
#容器内，注意此路径需要与配置文件中的工作空间路径一致（pdes.global-config.workspace）
pdes_workspace_container=/usr/share/pde/workspace

# portainer
pdes_portainer_image_name=$pdes_registry_url/p10/base/portainer
pdes_portainer_version=1.25.0
pdes_portainer_port=7000

# nacos
pdes_nacos_image_name=nacos/nacos-server
pdes_nacos_version=v2.3.2
pdes_nacos_port=8848
pdes_nacos_path=$install_path/nacos
pdes_nacos_namespace=dev
pdes_nacos_username=nacos
pdes_nacos_password=nacos

# redis
pdes_redis_image_name=$pdes_registry_url/p10/base/redis
pdes_redis_version=7.0.4
pdes_redis_port=7001
pdes_redis_path=$install_path/redis
pdes_redis_pwd=pde888888

# mysql
pdes_mysql_image_name=mysql
pdes_mysql_version=5.7
pdes_mysql_port=3306
pdes_mysql_path=$install_path/mysql
pdes_mysql_pwd=pde#123456789

# minio
pdes_minio_image_name=$pdes_registry_url/p10/base/minio
pdes_minio_version=RELEASE.2022-07-24T17-09-31Z
pdes_minio_port_01=7003
pdes_minio_port_02=17003
pdes_minio_path=$install_path/minio
pdes_minio_user=pde
pdes_minio_pwd=pde88888

# Elasticsearch
pdes_elasticsearch_image_name=$pdes_registry_url/p10/base/elasticsearch
pdes_elasticsearch_version=7.17.5
pdes_elasticsearch_port_01=7004
pdes_elasticsearch_port_02=17004
pdes_elasticsearch_path=$install_path/elasticsearch

# kibana
pdes_kibana_image_name=$pdes_registry_url/p10/base/kibana
pdes_kibana_version=7.17.5
pdes_kibana_port=7005
pdes_kibana_path=$install_path/kibana

# logstash
pdes_logstash_image_name=$pdes_registry_url/p10/base/logstash
pdes_logstash_version=7.17.5
pdes_logstash_port_01=7006
pdes_logstash_port_02=17006
pdes_logstash_path=$install_path/logstash

# nginx
pdes_nginx_image_name=$pdes_registry_url/p10/base/nginx
pdes_nginx_port=8000
pdes_nginx_version=1.23.1
pdes_nginx_path=$install_path/nginx

# 系统服务配置
pdes_admin_port=9000
pdes_admin_url=http://10.20.40.214:9000
pdes_gateway_port=9003
pdes_security_port=9004
pdes_files_convert_port=9006
pdes_files_convert_workspace=$install_path/files_convert
pdes_job_port=9008
pdes_im_port=9009
pdes_report_port=9010
pdes_cqbc_port=29003
pdes_workbench_port=10000

# 业务服务配置
pdes_ams_ro_port=8001
pdes_ams_sys_port=8005
pdes_ams_am_port=8999
pdes_ams_ar_port=8011

# 镜像名称前缀
docker_registry_prefix_p10=$pdes_registry_url/p10
docker_registry_prefix_pdes=$pdes_registry_url/pdes

#  -------------------------------------------------------------------------常量输出区域------------------------------------------------------

echo '实际配置参数如下：'
echo 'pdes_version='$pdes_version
echo 'pdes_profile='$pdes_profile
echo 'logs_path='$logs_path
echo 'script_path='$script_path
echo 'jars_path='$jars_path
echo 'log_max_size='$log_max_size
echo 'log_max_file='$log_max_file
echo 'pdes_redis_version='$pdes_redis_version
echo 'pdes_redis_port='$pdes_redis_port
echo 'pdes_redis_path='$pdes_redis_path
echo 'pdes_redis_pwd='$pdes_redis_pwd
echo 'pdes_minio_version='$pdes_minio_version
echo 'pdes_minio_port_01='$pdes_minio_port_01
echo 'pdes_minio_port_02='$pdes_minio_port_02
echo 'pdes_minio_path='$pdes_minio_path
echo 'pdes_minio_user='$pdes_minio_user
echo 'pdes_minio_pwd='$pdes_minio_pwd
echo 'pdes_elasticsearch_version='$pdes_elasticsearch_version
echo 'pdes_elasticsearch_port_01='$pdes_elasticsearch_port_01
echo 'pdes_elasticsearch_port_02='$pdes_elasticsearch_port_02
echo 'pdes_elasticsearch_path='$pdes_elasticsearch_path
echo 'pdes_kibana_version='$pdes_kibana_version
echo 'pdes_kibana_port='$pdes_kibana_port
echo 'pdes_kibana_path='$pdes_kibana_path
echo 'pdes_logstash_version='$pdes_logstash_version
echo 'pdes_logstash_port_01='$pdes_logstash_port_01
echo 'pdes_logstash_port_02='$pdes_logstash_port_02
echo 'pdes_logstash_path='$pdes_logstash_path
echo 'pdes_admin_port='$pdes_admin_port
echo 'pdes_admin_url='$pdes_admin_url
echo 'pdes_eureka_port='$pdes_eureka_port
echo 'pdes_config_port='$pdes_config_port
echo 'pdes_config_path='$pdes_config_path
echo 'pdes_gateway_port='$pdes_gateway_port
echo 'pdes_security_port='$pdes_security_port
echo 'pdes_files_convert_port='$pdes_files_convert_port
echo 'pdes_files_convert_workspace='$pdes_files_convert_workspace
echo 'pdes_job_port='$pdes_job_port
echo 'pdes_im_port='$pdes_im_port
echo 'pdes_report_port='$pdes_report_port
echo 'pdes_nginx_version='$pdes_nginx_version
echo 'pdes_nginx_port='$pdes_nginx_port
echo 'pdes_nginx_path='$pdes_nginx_path
echo 'pdes_ams_ro_port='$pdes_ams_ro_port
echo 'pdes_ams_am_port='$pdes_ams_am_port
echo 'pdes_ams_sys_port='$pdes_ams_sys_port
echo 'pdes_ams_ar_port='$pdes_ams_ar_port
echo 'docker_root_dir='$docker_root_dir

# -------------------------------------------------------------------------函数定义区域------------------------------------------------------

# 函数：检查镜像是否存在并从 tar 文件加载
check_and_load_image() {
	local image_name="$1"
	local tar_file="$2"

	# 检查镜像是否存在
	if docker image inspect "$image_name" >/dev/null 2>&1; then
		echo "Docker 镜像 '$image_name' 已存在。"
	else
		echo "Docker 镜像 '$image_name' 不存在。"
		if [ -f "$tar_file" ]; then
			echo "从 tar 文件 '$tar_file' 加载镜像..."
			docker load -i "$tar_file"
			if [ $? -eq 0 ]; then
				echo "镜像加载成功。"
			else
				echo "镜像加载失败。"
				exit 1
			fi
		else
			echo "tar 文件 '$tar_file' 不存在。"
			exit 1
		fi
	fi
}

# 函数定义：处理 Docker 容器的停止、删除和运行操作 这里只适用于业务服务的部署
deploy_service() {
	# 服务名称变量
	local service_name=$1
	# 服务端口变量
	local exportPort=$2
	# 服务端口变量
	local port=$3
	# 服务版本号
	local version=$4
	# 服务所需内存变量
	local memory=$5

	docker_registry_prefix=""
	if [[ "$service_name" == *"workbench"* ]]; then
		docker_registry_prefix=$docker_registry_prefix_pdes
	else
		docker_registry_prefix=$docker_registry_prefix_p10
	fi

	echo
	echo "开始部署 $service_name 服务                          --------------------------"
	# 停止和删除现有容器
	docker stop $(docker ps -a | grep $service_name | awk '{print $1}')
	docker rm $(docker ps -a | grep $service_name | awk '{print $1}')
	if [ $isMakeImg = 'Y' ] || [ $isMakeImg = 'y' ] || [ $isMakeImg = 'yes' ] || [ $isMakeImg = 'YES' ]; then
		echo "手动部署 $service_name 服务"
	else
		echo "自动部署 $service_name 服务 正在 rm 准备拉去新的镜像 --------------------------"
		docker rmi -f $(docker images -q $docker_registry_prefix/$service_name:$version)
	fi
	# 运行新容器
	echo "开始运行 $service_name 服务容器                      --------------------------"
	docker run -d -p $exportPort:$port --name $service_name -m $memory \
		--restart=always \
		-e "PROFILE=$pdes_profile" \
		-e "LB-TAG=$pdes_profile" \
		-e "HOST=$hostIP" \
		-e "HOSTPORT=$port" \
		-e "WORKSPACE=/workspace" \
		-e "ADMIN_URL=$pdes_admin_url" \
		-e "LOCAL_ADMIN_URL=http://$hostIP:$pdes_config_port" \
		$registry_server \
		-v $pdes_workspace:$pdes_workspace_container/ \
		--log-opt max-size=$log_max_size \
		--log-opt max-file=$log_max_file \
		$docker_registry_prefix/$service_name:$version

	echo "$service_name 服务部署成功                           --------------------------"
	echo
}

# 函数： 替换配置中ip
replace() {
	local file_path="$1"
	local file_type="$2"
	# echo $file_type
	# 判断是否为普通文件
	if [ "$file_type" == "regular file" ] || [ "$file_type" == "普通文件" ]; then
		# 使用 sed 进行替换
		sed -i "s/10.20.40.104/$hostIP/g" "$file_path"
		# echo "已替换文件中的 IP 地址: $file_path"
	else
		echo "该文件不是普通文件: $file_path"
		# return 1
	fi
}

# 函数：比较并复制文件或文件夹
compare_and_copy() {
	local source_folder="$1" # 源文件夹路径
	local target_folder="$2" # 目标文件夹路径
	local relative_path="$3" # 相对路径

	local source_item="$source_folder/$relative_path"
	local target_item="$target_folder/$relative_path"

	if [ ! -e "$source_item" ]; then
		echo "Error: 源文件或文件夹 $source_item 不存在。"
		# return 1
	fi

	if [ ! -e "$target_item" ]; then
		echo "目标文件或文件夹 $target_item 不存在，复制中..."
		mkdir -p "$(dirname "$target_item")"
		cp -r "$source_item" "$target_item"
		#  echo "复制完成: $source_item -> $target_item"
	fi

	# 获取源文件和目标文件的类型和名称
	local source_type=$(stat -c "%F" "$source_item")
	local target_type=$(stat -c "%F" "$target_item")
	local target_suffix=$(basename "$target_item" | awk -F. '{if (NF>1) {print $NF}}')
	local source_name=$(basename "$source_item")
	local target_name=$(basename "$target_item")

	# 比较类型和名称
	if [ "$source_type" != "$target_type" ] || [ "$source_name" != "$target_name" ]; then
		echo "类型或名称不同，复制中..."
		cp -r "$source_item" "$target_item"
		if [ "$target_suffix" = "yml" ] || [ "$target_suffix" = "yaml" ] || [ "$target_suffix" = "conf" ]; then
			replace "$target_item" "$target_type"
		fi
		#  echo "复制完成: $source_item -> $target_item"
	else
		if [ "$target_suffix" = "yml" ] || [ "$target_suffix" = "yaml" ] || [ "$target_suffix" = "conf" ]; then
			replace "$target_item" "$target_type"
		fi
		echo "类型和名称相同，跳过: $source_item -> $target_item"
	fi
}

# 函数：递归比较源文件夹和目标文件夹的所有文件和文件夹
compare_and_copy_recursive() {
	local source_folder="$1" # 源文件夹路径
	local target_folder="$2" # 目标文件夹路径

	if [ ! -d "$source_folder" ]; then
		echo "Error: 源文件夹 $source_folder 不存在。"
		# return 1
	fi

	echo "比较并复制文件夹 $source_folder 到 $target_folder"

	# 使用 find 命令递归列出所有文件和文件夹
	find "$source_folder" -mindepth 1 -print0 | while IFS= read -r -d '' source_item; do
		relative_path="${source_item#$source_folder/}"
		echo "$relative_path"
		compare_and_copy "$source_folder" "$target_folder" "$relative_path"
	done
}

# -------------------------------------------------------------------------镜像制作查区域------------------------------------------------------

read -p "一、是否进行镜 workspaces 工作空间初始Y/N(默认值N):" isInitWorksapce
isInitWorksapce=${isInitWorksapce:-N}

if [ $isInitWorksapce = 'Y' ] || [ $isInitWorksapce = 'y' ] || [ $isInitWorksapce = 'yes' ] || [ $isInitWorksapce = 'YES' ]; then
	# 检查并 copy 系统服务持久化文件
	compare_and_copy_recursive "$installscript_path/pde_config/workspace" "$docker_root_dir/volumes/workspace"
fi

read -p "二、是否进行镜像制作Y/N(默认值N):" isMakeImg
isMakeImg=${isMakeImg:-N}

if [ $isMakeImg = 'Y' ] || [ $isMakeImg = 'y' ] || [ $isMakeImg = 'yes' ] || [ $isMakeImg = 'YES' ]; then
	check_and_load_image "adoptopenjdk/openjdk8" "$installscript_path/base_images/adoptopenjdk_openjdk8.tar"
	check_and_load_image "pdes-files-convert-base:1.0.0" "$installscript_path/base_images/pdes-files-convert-base-1.0.0.tar"
	for file in $( #注意此处这是两个反引号，表示运行系统命令
		ls $jars_path
	); do
		if [ -d $jars_path"/"$file ]; then #注意此处之间一定要加上空格，否则会报错
			echo "n/a"
		else
			if echo $jars_path"/"$file | grep -q -E '\.jar$'; then
				# 如果名称包含files-convert，则复制到$convert_script_path
				if echo $jars_path"/"$file | grep -q -E 'files-convert'; then
					cp $jars_path"/"$file $convert_script_path"/app.jar"
					cd $convert_script_path
				else
					cp $jars_path"/"$file $script_path"/app.jar"
					cd $script_path
				fi
				filename=$file
				filename=${filename%-$pdes_version.jar}
				if [[ "$filename" == *"workbench"* ]]; then
					rs=$(docker build . -t $docker_registry_prefix_p10/$filename":$pdes_image_version")
				else
					rs=$(docker build . -t $docker_registry_prefix_p10/$filename":$pdes_image_version")
				fi
				echo $rs
			fi
		fi
	done
fi

# -------------------------------------------------------------------------服务部署区域------------------------------------------------------

read -p "三、请输入本机IP地址 (例如: 10.20.40.105):" hostIP
hostIP=${hostIP:-10.20.40.105}

read -p "四、请输入注册中心nacos地址 (例如: 10.20.40.105):" registryIP
registryIP=${registryIP:-10.20.40.105}
compare_and_copy_recursive "$installscript_path/config" "$installscript_path/naoc_config/DEFAULT_GROUP"
# 注册中心地址
# 如果registry_type为nacos,则使用nacos注册中心地址，若为eureka，则使用eureka注册中心地址
registry_server=""
if [ "$registry_type" = "nacos" ]; then
	registry_server="-e NACOS_URL=http://$registryIP:$pdes_nacos_port -e NAMESPACE=$pdes_nacos_namespace -e NACOS_USERNAME=$pdes_nacos_username -e NACOS_PASSWORD=$pdes_nacos_password"
else
	if [ "$registry_type" = "eureka" ]; then
		registry_server="-e EUREKA_URLS=http://$registryIP:$pdes_eureka_port/eureka"
	fi
fi

echo 'third 安装列表 '
echo "1、pdes-redis              2、pdes-nginx                      3、pdes-minio"
echo "4、pdes-elasticsearch      5、pdes-kibana                     6、pdes-logstash"
echo "7、pdes-nacos              8、pdes-portainer                  9、pdes-mysql"
echo
echo 'yw 安装列表 '
echo "20、pdes-admin             21、pdes-eureka                     22、pdes-config"
echo "23、pdes-gateway           24、pdes-security                   25、pdes-files-convert"
echo "26、pdes-job               27、pdes-im                         28、pdes-report"
echo "29、pdes-ams-ro            30、pdes-ams-am                     31、pdes-ams-sys"
echo "32、pdes-ams-ar            33、pdes-cqbc                       34、pdes-workbench"

echo '五、请选择要部署的服务，输入服务序号，多个服务使用‘,’号分隔，(列如: 1,2,4,6,7，全部请输入all, 全部业务输入yw，全部三方输入third):'
read servers
array=(${servers//,/ })

echo '六、开始部署'
for var in ${array[@]}; do
	if [ $var = '1' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-redis配置环境--------------------------"
		# redis 没有配置仅做了持久化
		check_and_load_image "$pdes_redis_image_name:$pdes_redis_version" "$installscript_path/base_images/redis.tar"
		echo "开始部署pdes-redis服务--------------------------"
		docker stop $(docker ps -a | grep pdes-redis | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-redis | awk '{print $1}')
		docker run -d -p $pdes_redis_port:6379 --name pdes-redis \
			-v $pdes_redis_path:/data \
			--privileged=true \
			--restart=always \
			$pdes_redis_image_name:$pdes_redis_version redis-server --appendonly yes --requirepass $pdes_redis_pwd
		sleep 10
		echo "pdes-redis服务部署成功--------------------------"
		echo
	fi

	if [ $var = '2' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-nginx配置环境--------------------------"
		check_and_load_image "$pdes_nginx_image_name:$pdes_nginx_version" "$installscript_path/base_images/nginx.tar"
		compare_and_copy_recursive "$installscript_path/third/nginx" "$pdes_nginx_path"
		echo "开始部署pdes-nginx服务--------------------------"
		docker stop $(docker ps -a | grep pdes-nginx | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-nginx | awk '{print $1}')
		docker run -d -p $pdes_nginx_port:8080 --name pdes-nginx \
			--privileged=true \
			--restart=always \
			-v $pdes_nginx_path:$pdes_nginx_path \
			-v $pdes_nginx_path/www:/usr/share/nginx/html \
			-v $pdes_nginx_path/conf/nginx.conf:/etc/nginx/nginx.conf \
			-v $pdes_nginx_path/logs:/var/log/nginx \
			$pdes_nginx_image_name:$pdes_nginx_version
		echo "pdes-nginx服务部署成功--------------------------"
		echo
	fi

	if [ $var = '3' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-minio配置环境--------------------------"
		check_and_load_image "$pdes_minio_image_name:$pdes_minio_version" "$installscript_path/base_images/minio.tar"
		# minio 没有配置仅做了持久化
		echo "开始部署pdes-minio服务--------------------------"
		docker stop "$(docker ps -a | grep jenkins-pdes-minio | awk '{print $1}')"
		docker rm "$(docker ps -a | grep jenkins-pdes-minio | awk '{print $1}')"
		docker run -d -p $pdes_minio_port_01:9000 -p $pdes_minio_port_02:9001 --name pdes-minio \
			--restart=always \
			-e "MINIO_ROOT_USER=$pdes_minio_user" \
			-e "MINIO_ROOT_PASSWORD=$pdes_minio_pwd" \
			-v $pdes_minio_path:/data \
			$pdes_minio_image_name:$pdes_minio_version server /data --console-address ":9001"
		echo "pdes-minio服务部署成功--------------------------"
		echo
	fi

	if [ $var = '4' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-elasticsearch配置环境--------------------------"
		check_and_load_image "$pdes_elasticsearch_image_name:$pdes_elasticsearch_version" "$installscript_path/base_images/elasticsearch.tar"
		compare_and_copy_recursive "$installscript_path/third/elasticsearch" "$pdes_elasticsearch_path"
		echo "开始部署pdes-elasticsearch服务--------------------------"
		docker stop $(docker ps -a | grep pdes-elasticsearch | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-elasticsearch | awk '{print $1}')
		docker run -d -p $pdes_elasticsearch_port_01:9200 -p $pdes_elasticsearch_port_02:9300 --name pdes-elasticsearch -m 4G \
			--privileged=true \
			--restart=always \
			-e "discovery.type=single-node" \
			-e TAKE_FILE_OWNERSHIP=true \
			-v $pdes_elasticsearch_path/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
			-v $pdes_elasticsearch_path/plugins:/usr/share/elasticsearch/plugins \
			-v $pdes_elasticsearch_path/data:/usr/share/elasticsearch/data \
			-v $pdes_elasticsearch_path/logs/:/usr/share/elasticsearch/logs \
			$pdes_elasticsearch_image_name:$pdes_elasticsearch_version
		sleep 30

		echo "pdes-elasticsearch服务部署成功--------------------------"
		echo
	fi

	if [ $var = '5' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-kibana配置环境--------------------------"
		check_and_load_image "$pdes_kibana_image_name:$pdes_kibana_version" "$installscript_path/base_images/kibana.tar"
		compare_and_copy_recursive "$installscript_path/third/kibana" "$pdes_kibana_path"
		echo "开始部署pdes-kibana服务--------------------------"
		docker stop $(docker ps -a | grep pdes-kibana | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-kibana | awk '{print $1}')
		docker run -d -p $pdes_kibana_port:5601 --name pdes-kibana \
			--privileged=true \
			--restart=always \
			-v $pdes_kibana_path/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
			$pdes_kibana_image_name:$pdes_kibana_version
		echo "pdes-kibana服务部署成功--------------------------"
		echo
	fi

	if [ $var = '6' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-logstash配置环境--------------------------"
		check_and_load_image "$pdes_logstash_image_name:$pdes_logstash_version" "$installscript_path/base_images/logstash.tar"
		compare_and_copy_recursive "$installscript_path/third/logstash" "$pdes_logstash_path"
		echo "开始部署pdes-logstash服务--------------------------"
		docker stop $(docker ps -a | grep pdes-logstash | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-logstash | awk '{print $1}')
		docker run -d -p $pdes_logstash_port_01:5044 -p $pdes_logstash_port_02:9600 --name pdes-logstash \
			--privileged=true \
			--restart=always \
			-v $pdes_logstash_path/config/logstash.yml:/usr/share/logstash/config/logstash.yml \
			-v $pdes_logstash_path:/data/logstash \
			$pdes_logstash_image_name:$pdes_logstash_version -f /data/logstash/config/logstash-pdes.conf
		echo "pdes-logstash服务部署成功--------------------------"
		echo
	fi
	# nacos 注册配置中心
	if [ $var = '7' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始准备pdes-nacos配置环境--------------------------"
		check_and_load_image "$pdes_nacos_image_name:$pdes_nacos_version" "$installscript_path/base_images/nacos.tar"
		compare_and_copy_recursive "$installscript_path/third/nacos" "$pdes_nacos_path"
		echo "开始部署pdes-nacos服务--------------------------"
		docker stop $(docker ps -a | grep pdes-nacos | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-nacos | awk '{print $1}')
		docker run -d --name pdes-nacos -m 2G \
			-p $pdes_nacos_port:$pdes_nacos_port \
			-p 9848:9848 -p 9849:9849 \
			--privileged=true -e JVM_XMS=1G -e JVM_XMX=1G -e MODE=standalone \
			--restart=always \
			-v $pdes_nacos_path/logs:/home/nacos/logs \
			-v $pdes_nacos_path/conf/application.properties:/home/nacos/conf/application.properties \
			-v $pdes_nacos_path/plugins:/home/nacos/plugins \
			--log-opt max-size=$log_max_size \
			--log-opt max-file=$log_max_file \
			$pdes_nacos_image_name:$pdes_nacos_version
		echo "pdes-nacos 部署成功--------------------------"
		echo
	fi
	# protianer docker 可视化页面
	if [ $var = '8' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始部署pdes-portainer服务--------------------------"
		check_and_load_image "$pdes_portainer_image_name:$pdes_portainer_version" "$installscript_path/base_images/portainer.tar"
		docker stop $(docker ps -a | grep pdes-portainer | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-portainer | awk '{print $1}')
		docker run -d -p $pdes_portainer_port:9000 --name pdes-portainer \
			--restart=always \
			-v /var/run/docker.sock:/var/run/docker.sock \
			$pdes_portainer_image_name:$pdes_portainer_version
		echo "pdes-portainer docker 可视化部署成功--------------------------"
		echo
	fi
	# mysql 数据存储服务
	if [ $var = '9' ] || [ $var = 'all' ] || [ $var = 'third' ]; then
		echo
		echo "开始部署pdes-portainer服务--------------------------"
		check_and_load_image "$pdes_mysql_image_name:$pdes_mysql_version" "$installscript_path/base_images/mysql.tar"
		compare_and_copy_recursive "$installscript_path/third/mysql" "$pdes_mysql_path"
		echo "开始部署pdes-mysql服务--------------------------"
		docker stop $(docker ps -a | grep pdes-mysql | awk '{print $1}')
		docker rm $(docker ps -a | grep pdes-mysql | awk '{print $1}')
		docker run -d -p $pdes_mysql_port:3306 --name pdes-mysql \
			--restart=always \
			-e MYSQL_ROOT_PASSWORD=$pdes_mysql_pwd \
			-v /var/run/docker.sock:/var/run/docker.sock \
			-v $pdes_mysql_path/data:/var/lib/mysql \
			-v $pdes_mysql_path/config:/etc/mysql/conf.d \
			-v $pdes_mysql_path/log:/var/log/mysql \
			$pdes_mysql_image_name:$pdes_mysql_version
		echo "pdes-mysql 数据库部署成功--------------------------"
		echo
	fi

	# 以一下服务在需要用到的时候开启，方便与业务服务部署

	# # admin服务
	# if [ $var = '20' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
	# 	deploy_service "pdes-admin" "$pdes_admin_port" "$pdes_admin_port" "$pdes_image_version" "1G"
	# 	# 该服务需要优先启动
	# 	sleep 20
	# fi
	# # eureka 注册中心
	# if [ $var = '21' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
	# 	deploy_service "pdes-eureka-server" "$pdes_eureka_port" "$pdes_eureka_port" "$pdes_image_version" "1G"
	# 	# 该服务需要优先启动
	# 	sleep 20
	# fi
	# # config 配置中心
	# if [ $var = '22' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
	# 	compare_and_copy_recursive "$installscript_path/config" "$pdes_image_version"
	# 	echo
	# 	echo "开始部署pdes-config服务--------------------------"
	# 	docker stop $(docker ps -a | grep pdes-config | awk '{print $1}')
	# 	docker rm $(docker ps -a | grep pdes-config | awk '{print $1}')
	# 	docker run -d -p $pdes_config_port:$pdes_config_port --name pdes-config \
	# 		-e "WORKSPACE=/workspace" \
	# 		-e "HOST=$hostIP" \
	# 		-e "HOSTPORT=$pdes_config_port" \
	# 		-e "EUREKA_URLS=http://$registryIP:$pdes_eureka_port/eureka" \
	# 		-e "ADMIN_URL=$pdes_admin_url" \
	# 		-e "LOCAL_ADMIN_URL=http://$hostIP:$pdes_config_port" \
	# 		-v $pdes_config_path:/workspace \
	# 		--privileged=true \
	# 		--log-opt max-size=$log_max_size \
	# 		--log-opt max-file=$log_max_file \
	# 		$docker_registry_prefix/pdes-config:$pdes_version

	# 	# 该服务需要优先启动
	# 	sleep 20
	# 	echo "pdes-config服务部署成功--------------------------"
	# 	echo

	# fi
	# 网关服务
	if [ $var = '23' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-starter-gateway-server" "$pdes_gateway_port" "$pdes_gateway_port" "$pdes_image_version" "1G"
	fi
	# 安全认证服务
	if [ $var = '24' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-starter-security-boot" "$pdes_security_port" "$pdes_security_port" "$pdes_image_version" "1G"
	fi
	# 文件转换服务
	if [ $var = '25' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-starter-files-convert-boot" "$pdes_files_convert_port" "$pdes_files_convert_port" "$pdes_image_version" "1G"
	fi
	# 任务调度
	if [ $var = '26' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-starter-job-boot" "$pdes_job_port" "$pdes_job_port" "$pdes_image_version" "1G"
	fi
	# 消息通知
	if [ $var = '27' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-starter-im-boot" "$pdes_im_port" "$pdes_im_port" "$pdes_image_version" "1G"
	fi
	# 报表服务
	if [ $var = '28' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-starter-report-boot" "$pdes_report_port" "$pdes_report_port" "$pdes_image_version" "2G"
	fi
	# 档案收集
	if [ "$var" = '29' ] || [ "$var" = 'all' ] || [ "$var" = 'yw' ]; then
		deploy_service "pdes-ams-ro-boot" "$pdes_ams_ro_port" "$pdes_ams_ro_port" "$pdes_image_version" "2G"
	fi
	# 档案管理
	if [ $var = '30' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-ams-am-boot" "$pdes_ams_am_port" "$pdes_ams_am_port" "$pdes_image_version" "6G"
	fi
	# 系统配置
	if [ $var = '31' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-ams-sys-boot" "$pdes_ams_sys_port" "$pdes_ams_sys_port" "$pdes_image_version" "4G"
	fi
	# 档案利用
	if [ $var = '32' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-ams-ar-boot" "$pdes_ams_ar_port" "$pdes_ams_ar_port" "$pdes_image_version" "1G"
	fi
	# 长期保存
	if [ $var = '33' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-cqbc-boot" "$pdes_cqbc_port" "9003" "$pdes_image_version" "1G"
	fi
	# 工作台
	if [ $var = '34' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
		deploy_service "pdes-workbench-boot" "$pdes_workbench_port" "$pdes_workbench_port" "latest" "4G"
	fi
done

echo '部署任务执行完毕，再见---------------------------------------------------'
