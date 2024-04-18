#!/bin/bash
echo "--------------------------欢迎使用P10企业版部署向导v1.0--------------------------"
echo "一、部署须知"
echo "1、确保要安装的第三方镜像文件已经导入本地docker中，注意镜像的版本号是否与脚本里的相同"
echo "2、请先创建各服务的工作空间， 并将所属的配置文件放入相应的目录中"
echo "3、打包好的发布jar包请放入/home/pde/jar目录中"
echo "4、前端程序请解压后放入/home/pde/nginx/www目录中"
echo "5、各服务默认的目录如下（如果要调整，请修改本脚本相关的参数值）"
echo "  1）日志存储目录：           /home/pde/logs"
echo "  2）配置文件目录：           /home/pde/config"
echo "  3）ES配置目录：             /home/pde/elasticsearch"
echo "  4）kibana配置目录:          /home/pde/kibana"
echo "  5）logstash配置目录:        /home/pde/logstash"
echo "  6）minio存储目录:           /home/pde/minio"
echo "  7）redis存储目录:           /home/pde/redis"
echo "  8）rabbitmq存储目录:        /home/pde/rabbitmq"
echo "  9）nginx配置目录:           /home/pde/nginx"
echo "  10）镜像脚本目录:           /home/pde/script"
echo "  11）jar存放目录:            /home/pde/jar"
echo "  12）镜像版本号与容器端口号  "

#初始化配置参数
pdes_version=10.1.0
pdes_profile=test   #dev=开发环境  test=测试环境  pro=生产环境
logs_path=/home/pde/logs
script_path=/home/pde/script/
jars_path=/home/pde/jar
log_max_size=100m
log_max_file=1

# 本地存储（宿主机）存储路径配置
#该地址为*存储配置启用的是本地存储时*所配置的宿主机的文件存储地址
pdes_local_storage=local-storage
#容器内(不要改)
pdes_local_storage_container=/usr/share/pde/workspace/storage

# 公共工作空间配置(不要改)
#宿主机
pdes_workspace=workspace
#容器内
pdes_workspace_container=/usr/share/pde/workspace

# nacos
pdes_nacos_ip=10.20.40.195
pdes_nacos_port=8848
pdes_nacos_namespace=PUBLIC

pdes_redis_version=7.0.4
pdes_redis_port=7001
pdes_redis_path=/home/pde/redis
pdes_redis_pwd=pde888

pdes_rabbitmq_version=3.10.6-management
pdes_rabbitmq_port_01=7002
pdes_rabbitmq_port_02=17002
pdes_rabbitmq_path=/home/pde/rabbitmq
pdes_rabbitmq_user=pde
pdes_rabbitmq_pwd=pde888
pdes_rabbitmq_vhost=/pdes

pdes_minio_version=RELEASE.2022-07-24T17-09-31Z
pdes_minio_port_01=7003
pdes_minio_port_02=17003
pdes_minio_path=/home/pde/minio
pdes_minio_user=pde
pdes_minio_pwd=pde88888

pdes_elasticsearch_version=7.17.5
pdes_elasticsearch_port_01=7004
pdes_elasticsearch_port_02=17004
pdes_elasticsearch_path=/home/pde/elasticsearch

pdes_kibana_version=7.17.5
pdes_kibana_port=7005
pdes_kibana_path=/home/pde/kibana

pdes_logstash_version=7.17.5
pdes_logstash_port_01=7006
pdes_logstash_port_02=17006
pdes_logstash_path=/home/pde/logstash


pdes_admin_port=9000
pdes_admin_url=http://10.20.40.216:9000

pdes_eureka_port=9901

pdes_config_port=9002
pdes_config_path=/home/pde/config

pdes_gateway_port=9003

pdes_security_port=9004

pdes_business_log_port=9005

pdes_files_convert_port=9006
pdes_files_convert_workspace=/home/pde/files_convert

pdes_workflow_port=9007

pdes_job_port=9008

pdes_im_port=9009

pdes_report_port=9010

pdes_business_flow_port=9011

pdes_attribute_check_port=9012

pdes_nginx_version=1.23.1
pdes_nginx_port=8000
pdes_nginx_path=/home/pde/nginx

pdes_ams_ro_port=8001

pdes_ams_ca_port=8002

pdes_ams_pw_port=8003

pdes_ams_bc_port=8004

pdes_ams_sys_port=8005
pdes_ams_sc_port=8005

pdes_ams_ad_port=8006

pdes_ams_ac_port=8007

pdes_ams_dm_port=8008
pdes_ams_am_port=8999

pdes_ams_ap_port=8009

pdes_ams_sa_port=8010

pdes_ams_ar_port=8011

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
echo 'pdes_rabbitmq_version='$pdes_rabbitmq_version
echo 'pdes_rabbitmq_port_01='$pdes_rabbitmq_port_01
echo 'pdes_rabbitmq_port_02='$pdes_rabbitmq_port_02
echo 'pdes_rabbitmq_path='$pdes_rabbitmq_path
echo 'pdes_rabbitmq_user='$pdes_rabbitmq_user
echo 'pdes_rabbitmq_pwd='$pdes_rabbitmq_pwd
echo 'pdes_rabbitmq_vhost='$pdes_rabbitmq_vhost
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
echo 'pdes_business_log_port='$pdes_business_log_port
echo 'pdes_files_convert_port='$pdes_files_convert_port
echo 'pdes_files_convert_workspace='$pdes_files_convert_workspace
echo 'pdes_workflow_port='$pdes_workflow_port
echo 'pdes_job_port='$pdes_job_port
echo 'pdes_im_port='$pdes_im_port
echo 'pdes_report_port='$pdes_report_port
echo 'pdes_business_flow_port='$pdes_business_flow_port
echo 'pdes_attribute_check_port='$pdes_attribute_check_port
echo 'pdes_nginx_version='$pdes_nginx_version
echo 'pdes_nginx_port='$pdes_nginx_port
echo 'pdes_nginx_path='$pdes_nginx_path
echo 'pdes_ams_ro_port='$pdes_ams_ro_port
echo 'pdes_ams_ca_port='$pdes_ams_ca_port
echo 'pdes_ams_pw_port='$pdes_ams_pw_port
echo 'pdes_ams_bc_port='$pdes_ams_bc_port
echo 'pdes_ams_sc_port='$pdes_ams_sc_port
echo 'pdes_ams_sys_port='$pdes_ams_sys_port
echo 'pdes_ams_ad_port='$pdes_ams_ad_port
echo 'pdes_ams_ac_port='$pdes_ams_ac_port
echo 'pdes_ams_dm_port='$pdes_ams_dm_port
echo 'pdes_ams_ap_port='$pdes_ams_ap_port
echo 'pdes_ams_sa_port='$pdes_ams_sa_port
echo 'pdes_ams_ar_port='$pdes_ams_ar_port

echo '二、是否进行镜像制作（Y/N）:'
read isMakeImg
if [ $isMakeImg = 'Y' ] || [ $isMakeImg = 'y' ] || [ $isMakeImg = 'yes' ] || [ $isMakeImg = 'YES' ]; then
	for file in `ls $jars_path` #注意此处这是两个反引号，表示运行系统命令
        do
             if [ -d $jars_path"/"$file ] #注意此处之间一定要加上空格，否则会报错
             then
                echo "n/a" 
             else
                if echo $jars_path"/"$file | grep -q -E '\.jar$'
                then
                	cp $jars_path"/"$file $script_path"/app.jar"
					        filename=$file
									#echo $filename
									filename=${filename%-$pdes_version.jar}
									#echo $filename
						      #echo $script_path 
									cd  $script_path
									#rs=`pwd`
									#echo $rs
					        rs=`docker build . -t  "pde/"$filename":$pdes_version"`
			            echo $rs
									#echo $file
                fi  
             fi
        done
fi


echo '三、请输入本机IP地址 (例如: 10.20.40.214):'
read hostIP

echo '四、请输入注册中心eureka地址 (例如: 10.20.40.214):'
read eurekaIP


echo '安装列表：'
echo "1、pdes-redis             2、pdes-rabbitmq           3、pdes-minio"
echo "4、pdes-elasticsearch     5、pdes-kibana             6、pdes-logstash"
echo "7、pdes-admin             8、pdes-eureka             9、pdes-config"
echo "10、pdes-gateway          11、pdes-security          12、pdes-business-log"
echo "13、pdes-files-convert    14、pdes-job               15、pdes-im"
echo "16、pdes-report           17、pdes-business-flow     18、pdes-attribute-check"
echo "19、pdes-nginx            20、pdes-ams-ro            21、pdes-ams-am"
echo "22、pdes-ams-sys          23、pdes-ams-ar"

echo '五、请选择要部署的服务，输入服务序号，多个服务使用‘,’号分隔，(列如: 1,2,4,6,7，全部请输入all, 全部业务输入yw):'
read servers
array=(${servers//,/ })


echo '六、开始部署'
for var in ${array[@]}
do
   if [ $var = '1' ] || [ $var = 'all' ]; then
   	echo
    echo "开始部署pdes-redis服务--------------------------"
    docker stop `docker ps -a| grep pdes-redis | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-redis | awk '{print $1}' `
    docker run -d -p $pdes_redis_port:6379 --name pdes-redis \
     -v $pdes_redis_path:/data \
     --privileged=true \
     --restart=always \
     redis:$pdes_redis_version redis-server --appendonly yes --requirepass $pdes_redis_pwd

    sleep 10;
    echo "pdes-redis服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '2' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-rabbitmq服务--------------------------"
   	docker stop `docker ps -a| grep pdes-rabbitmq | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-rabbitmq | awk '{print $1}' `
   	docker run -d --name pdes-rabbitmq \
   	 --privileged=true \
   	 --restart always \
   	 -p $pdes_rabbitmq_port_01:5672 \
   	 -p $pdes_rabbitmq_port_02:15672 \
   	 -v $pdes_rabbitmq_path:/var/lib/rabbitmq \
   	 rabbitmq:$pdes_rabbitmq_version

     sleep 15;
   	 docker exec -it pdes-rabbitmq  rabbitmqctl add_user $pdes_rabbitmq_user $pdes_rabbitmq_pwd
     docker exec -it pdes-rabbitmq  rabbitmqctl set_user_tags $pdes_rabbitmq_user  administrator
     docker exec -it pdes-rabbitmq  rabbitmqctl add_vhost  $pdes_rabbitmq_vhost
     docker exec -it pdes-rabbitmq  rabbitmqctl set_permissions -p $pdes_rabbitmq_vhost  $pdes_rabbitmq_user  ".*" ".*" ".*"
    echo "pdes-rabbitmq服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '3' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-minio服务--------------------------"
   	docker stop "$(docker ps -a | grep jenkins-pdes-minio | awk '{print $1}')"
    docker rm "$(docker ps -a | grep jenkins-pdes-minio | awk '{print $1}')"
   	docker run -d -p $pdes_minio_port_01:9000 -p $pdes_minio_port_02:9001 --name pdes-minio \
   	 -e "MINIO_ROOT_USER=$pdes_minio_user" \
   	 -e "MINIO_ROOT_PASSWORD=$pdes_minio_pwd" \
   	 -v $pdes_minio_path:/data \
   	 minio/minio:$pdes_minio_version server /data --console-address ":9001"
    echo "pdes-minio服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '4' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-elasticsearch服务--------------------------"
   	docker stop `docker ps -a| grep pdes-elasticsearch | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-elasticsearch | awk '{print $1}' `
   	docker run -d -p $pdes_elasticsearch_port_01:9200 -p $pdes_elasticsearch_port_02:9300 --name pdes-elasticsearch -m 4G \
   	 --privileged=true \
   	 -e "discovery.type=single-node" \
   	 -e TAKE_FILE_OWNERSHIP=true \
   	 -v $pdes_elasticsearch_path/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
   	 -v $pdes_elasticsearch_path/plugins:/usr/share/elasticsearch/plugins \
   	 -v $pdes_elasticsearch_path/data:/usr/share/elasticsearch/data \
   	 -v $pdes_elasticsearch_path/logs/:/usr/share/elasticsearch/logs \
   	 elasticsearch:$pdes_elasticsearch_version
   	 sleep 30;

    echo "pdes-elasticsearch服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '5' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-kibana服务--------------------------"
   	docker stop `docker ps -a| grep pdes-kibana | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-kibana | awk '{print $1}' `
   	docker run -d -p $pdes_kibana_port:5601 --name pdes-kibana \
   	 --privileged=true \
   	 -v $pdes_kibana_path/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
   	 kibana:$pdes_kibana_version
    echo "pdes-kibana服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '6' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-logstash服务--------------------------"
   	docker stop `docker ps -a| grep pdes-logstash | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-logstash | awk '{print $1}' `
   	docker run -d -p $pdes_logstash_port_01:5044 -p $pdes_logstash_port_02:9600 --name pdes-logstash \
   	 --privileged=true \
   	 -v $pdes_logstash_path/config/logstash.yml:/usr/share/logstash/config/logstash.yml \
   	 -v $pdes_logstash_path:/data/logstash \
   	 docker.elastic.co/logstash/logstash:$pdes_logstash_version -f /data/logstash/config/logstash-pdes.conf
    echo "pdes-logstash服务部署成功--------------------------" 
    echo 
   fi
   
   if [ $var = '7' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-admin服务--------------------------"
   	docker stop `docker ps -a| grep pdes-admin | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-admin | awk '{print $1}' `
   	docker run -d -p $pdes_admin_port:$pdes_admin_port --name pdes-admin \
   	 --privileged=true \
   	 pde/pdes-admin:$pdes_version
    echo "pdes-admin服务部署成功--------------------------"
    echo
   fi

   if [ $var = '8' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-eureka服务--------------------------"
   	docker stop `docker ps -a| grep pdes-eureka | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-eureka | awk '{print $1}' `
   	docker run -d -p $pdes_eureka_port:$pdes_eureka_port --name pdes-eureka \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_eureka_port" \
   	 -e "DEFAULTZONE=http://$hostIP:$pdes_eureka_port/eureka" \
   	 -e "ADMIN_URL=$pdes_admin_url" \
   	 -e "LOCAL_ADMIN_URL=http://$hostIP:$pdes_eureka_port" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-eureka:$pdes_version
   	 
   	 sleep 10;
    echo "pdes-eureka服务部署成功--------------------------"
    echo
   fi

   
   if [ $var = '9' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-config服务--------------------------"
   	docker stop `docker ps -a| grep pdes-config | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-config | awk '{print $1}' `
   	docker run -d -p $pdes_config_port:$pdes_config_port --name pdes-config \
   	 -e "WORKSPACE=/workspace" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_config_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 -e "ADMIN_URL=$pdes_admin_url" \
   	 -e "LOCAL_ADMIN_URL=http://$hostIP:$pdes_config_port" \
   	 -v $pdes_config_path:/workspace \
   	 --privileged=true \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-config:$pdes_version

   	 sleep 20;
    echo "pdes-config服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '10' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-gateway服务--------------------------"
   	docker stop `docker ps -a| grep pdes-gateway | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-gateway | awk '{print $1}' `
   	docker run -d -p $pdes_gateway_port:$pdes_gateway_port --name pdes-gateway \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_gateway_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-gateway:$pdes_version
    echo "pdes-gateway服务部署成功--------------------------"
    echo
   fi

   if [ $var = '11' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-security服务--------------------------"
   	docker stop `docker ps -a| grep pdes-security | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-security | awk '{print $1}' `
   	docker run -d -p $pdes_security_port:$pdes_security_port -m 1G --name pdes-security \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_security_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --privileged=true \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-security:$pdes_version
    echo "pdes-security服务部署成功--------------------------"   
    echo
   fi
   
   if [ $var = '12' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-business-log服务--------------------------"
   	docker stop `docker ps -a| grep pdes-business-log | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-business-log | awk '{print $1}' `
   	docker run -d -p $pdes_business_log_port:$pdes_business_log_port --name pdes-business-log -m 1G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_business_log_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-business-log:$pdes_version
    echo "pdes-business-log服务部署成功--------------------------"   
    echo
   fi
   
   if [ $var = '13' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-files-convert服务--------------------------"
   	docker stop `docker ps -a| grep pdes-files-convert | awk '{print $1}' `
     docker rm `docker ps -a| grep pdes-files-convert | awk '{print $1}' `
   	docker rmi pde/pdes-files-convert:$pdes_version
   	docker build -t pde/pdes-files-convert:$pdes_version -f ./convert/script/Dockerfile .
   	docker run -d -p $pdes_files_convert_port:$pdes_files_convert_port -t --name pdes-files-convert -m 6G \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_files_convert_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 -v $pdes_workspace:$pdes_workspace_container/ \
   	 -v $pdes_local_storage:$pdes_local_storage_container/ \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-files-convert:$pdes_version
    echo "pdes-files-convert服务部署成功--------------------------"   
    echo
   fi
   
   if [ $var = '14' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-job服务--------------------------"
   	docker stop `docker ps -a| grep pdes-job | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-job | awk '{print $1}' `
   	docker run -d -p $pdes_job_port:$pdes_job_port --name pdes-job \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_job_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-job:$pdes_version
    echo "pdes-job服务部署成功--------------------------"
    echo
   fi 
   
   if [ $var = '15' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-im服务--------------------------"
   	docker stop `docker ps -a| grep pdes-im | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-im | awk '{print $1}' `
   	docker run -d -p $pdes_im_port:$pdes_im_port -m 1G --name pdes-im \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_im_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-im:$pdes_version
    echo "pdes-im服务部署成功--------------------------"
    echo
   fi

   if [ $var = '16' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-report服务--------------------------"
   	docker stop `docker ps -a| grep pdes-report | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-report | awk '{print $1}' `
   	docker run -d -p $pdes_report_port:$pdes_report_port -m 2G --name pdes-report \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_report_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-report:$pdes_version
    echo "pdes-report服务部署成功--------------------------"
    echo
   fi 

	 if [ $var = '17' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-business-flow服务--------------------------"
   	docker stop `docker ps -a| grep pdes-business-flow | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-business-flow | awk '{print $1}' `
   	docker run -d -p $pdes_business_flow_port:$pdes_business_flow_port --name pdes-business-flow -m 1G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_business_flow_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-business-flow:$pdes_version
    echo "pdes-business-flow服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '18' ] || [ $var = 'all' ]; then
   	echo
   	echo "开始部署pdes-attribute-check服务--------------------------"
   	docker stop `docker ps -a| grep pdes-attribute-check | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-attribute-check | awk '{print $1}' `
   	docker run -d -p $pdes_attribute_check_port:$pdes_attribute_check_port --name pdes-attribute-check -m 1G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_attribute_check_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-attribute-check:$pdes_version
    echo "pdes-attribute-check服务部署成功--------------------------"
    echo
   fi
   
   if [ $var = '19' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
   	echo
   	echo "开始部署pdes-nginx服务--------------------------"
   	docker stop `docker ps -a| grep pdes-nginx | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-nginx | awk '{print $1}' `
   	docker run -d -p $pdes_nginx_port:8080 --name pdes-nginx \
   	 --privileged=true \
   	 -v $pdes_nginx_path:$pdes_nginx_path \
   	 -v $pdes_nginx_path/www:/usr/share/nginx/html \
   	 -v $pdes_nginx_path/conf/nginx.conf:/etc/nginx/nginx.conf \
   	 -v $pdes_nginx_path/logs:/var/log/nginx \
   	 nginx:$pdes_nginx_version
    echo "pdes-nginx服务部署成功--------------------------"
    echo
   fi

   if [ "$var" = '20' ] || [ "$var" = 'all' ] || [ "$var" = 'yw' ]; then
   	echo
   	echo "开始部署pdes-ams-ro在线接收服务--------------------------"
   	docker stop "$(docker ps -a | grep pdes-ams-ro | awk '{print $1}')"
    docker rm "$(docker ps -a | grep pdes-ams-ro | awk '{print $1}')"
   	docker run -d -p $pdes_ams_ro_port:$pdes_ams_ro_port --name pdes-ams-ro -m 2G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_ams_ro_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 -v $pdes_workspace:$pdes_workspace_container/ \
   	 -v $pdes_local_storage:$pdes_local_storage_container/ \
   	 --log-opt max-size=$log_max_size \
     --log-opt max-file=$log_max_file \
   	 pde/pdes-ams-ro:$pdes_version
    echo "pdes-ams-ro在线接收服务部署成功--------------------------"
    echo
   fi
   
    if [ $var = '21' ] || [ $var = 'all' ] || [ $var = 'am' ]; then
   	echo
   	echo "开始部署pdes-ams-am数据管理服务--------------------------"
   	docker stop `docker ps -a| grep pdes-ams-am | awk '{print $1}' `
     docker rm `docker ps -a| grep pdes-ams-am | awk '{print $1}' `
   	docker run -d -p $pdes_ams_am_port:$pdes_ams_am_port --name pdes-ams-am -m 4G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_ams_am_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 -v $pdes_workspace:$pdes_workspace_container/ \
   	 -v $pdes_local_storage:$pdes_local_storage_container/ \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-ams-am:$pdes_version
    echo "pdes-ams-am数据管理服务部署成功--------------------------"
    echo
   fi
   if [ $var = '22' ] || [ $var = 'all' ] || [ $var = 'sys' ]; then
   	echo
   	echo "开始部署pdes-ams-sys系统配置服务--------------------------"
   	docker stop `docker ps -a| grep pdes-ams-sys | awk '{print $1}' `
     docker rm `docker ps -a| grep pdes-ams-sys | awk '{print $1}' `
   	docker run -d -p $pdes_ams_sys_port:$pdes_ams_sys_port --name pdes-ams-sys -m 2G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_ams_sys_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 -v $pdes_workspace:$pdes_workspace_container/ \
   	 -v $pdes_local_storage:$pdes_local_storage_container/ \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
      pde/pdes-ams-sys:$pdes_version
    echo "pdes-ams-sys系统配置服务部署成功--------------------------"
    echo
   fi
    if [ $var = '23' ] || [ $var = 'all' ] || [ $var = 'yw' ]; then
   	echo
   	echo "开始部署pdes-ams-ar自助查档服务--------------------------"
   	docker stop `docker ps -a| grep pdes-ams-ar | awk '{print $1}' `
    docker rm `docker ps -a| grep pdes-ams-ar | awk '{print $1}' `
   	docker run -d -p $pdes_ams_ar_port:$pdes_ams_ar_port --name pdes-ams-ar -m 1G \
   	 -e "PROFILE=$pdes_profile" \
   	 -e "HOST=$hostIP" \
   	 -e "HOSTPORT=$pdes_ams_ar_port" \
     -e "NACOS_URL=http://$pdes_nacos_ip:$pdes_nacos_port" \
     -e "NAMESPACE=$pdes_nacos_namespace" \
   	 -e "EUREKA_URLS=http://$eurekaIP:$pdes_eureka_port/eureka" \
   	 -v $pdes_workspace:$pdes_workspace_container/ \
   	 -v $pdes_local_storage:$pdes_local_storage_container/ \
   	 --log-opt max-size=$log_max_size \
      --log-opt max-file=$log_max_file \
   	 pde/pdes-ams-ar:$pdes_version
    echo "pdes-ams-ar自助查档服务部署成功--------------------------"
    echo
   fi
done

echo '七、部署任务执行完毕，再见---------------------------------------------------'