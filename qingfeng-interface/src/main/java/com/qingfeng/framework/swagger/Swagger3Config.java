package com.qingfeng.framework.swagger;//package com.qingfeng.framework.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Title: Swagger3Config
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2020-10-18 11:33
 */
@Configuration
@EnableSwagger2
public class Swagger3Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("青锋在线接口文档")
                .description("更多请求内容请联系青锋客服。")
                .contact(new Contact("com.qingfeng", "http://www.com.qingfeng.cn", "axtaxt@163.com"))
                .version("1.0")
                .build();
    }

}