package com.whn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:55
 * @Description: SwaggerConfig
 */
@Configuration
public class SwaggerConfig {
    @Value("${spring.swagger2.enabled:true}")
    private Boolean enabled;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabled)
                //通过.select()方法，去配置扫描接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.whn.controller"))
                // 配置如何通过path过滤
                .paths(PathSelectors.any())
                .build();
                //.securitySchemes(securitySchemes())
                //.securityContexts(Collections.singletonList(securityContext()));
    }

    Contact contact = new Contact("whn", "https://blog.csdn.net/", "1470918223@qq.com");

    //配置Swagger 信息 = ApiInfo
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
//                .contact(contact)
                .title("springboot利用swagger2构建api接口文档 " + "\t" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()))
                .description("springboot+redis整合")
                .version("1.0")
                .build();
    }
    //
    //
    //private List<SecurityScheme> securitySchemes() {
    //    List<SecurityScheme> apiKeyList = new ArrayList<>();
    //    //注意，这里应对应登录token鉴权对应的k-v
    //    apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
    //    return apiKeyList;
    //}
    //
    ///**
    // * 这里设置 swagger2 认证的安全上下文
    // */
    //private SecurityContext securityContext() {
    //    return SecurityContext.builder()
    //            .securityReferences(Collections.singletonList(new SecurityReference("Authorization", scopes())))
    //            .build();
    //}
    //
    ///**
    // * 这里是写允许认证的scope
    // */
    //private AuthorizationScope[] scopes() {
    //    return new AuthorizationScope[]{
    //            new AuthorizationScope("web", "All scope is trusted!")
    //    };
    //}


}

