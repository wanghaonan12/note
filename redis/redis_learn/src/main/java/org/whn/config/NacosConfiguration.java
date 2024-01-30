package org.whn.config;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 13:31
 * @Description: Nacos 配置
 */
@Configuration
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "43.138.25.182:8848"))
@NacosPropertySource(dataId = "redis_config", autoRefreshed = true)
public class NacosConfiguration {

}
