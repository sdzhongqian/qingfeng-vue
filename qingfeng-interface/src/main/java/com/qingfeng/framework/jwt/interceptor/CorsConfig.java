package com.qingfeng.framework.jwt.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Title: CorsConfig
 * @ProjectName wdata
 * @Description: Cors跨域访问配置
 * @author anxingtao
 * @date 2020-4-23 15:36
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
				.allowCredentials(false).maxAge(3600);
	}
}
