

## eureka去除服务

	【eureka去除服务】用postman，鉴定服务
		 【强制下线】 PUT方法
		http://10.20.40.214:9001/eureka/apps/pdes-ams-ad/pdes-ams-ad:10.20.40.104:8006/status?value=OUT_OF_SERVICE
	【恢复上线】 PUT方法
		http://10.20.40.214:9001/eureka/apps/pdes-ams-ad/pdes-ams-ad:10.20.40.104:8006/status?value=UP