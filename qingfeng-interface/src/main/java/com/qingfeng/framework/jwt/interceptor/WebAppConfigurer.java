package com.qingfeng.framework.jwt.interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Title: WebAppConfigurer
 * @ProjectName wdata
 * @Description: 拦截配置--调用链
 * @author anxingtao
 * @date 2020-4-23 15:40
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String[] patterns = new String[] { "/vue/common/findGraphicInfo/**","/vue/common/findProductInfo/**","/vue/common/findProductListPage/**","/vue/common/findGraphicListPage/**","/vue/common/findShoworderListPage/**","/vue/login/regist","/vue/login/login","/*.html","/swagger-resources/**"};
		String[] regist = new String[] { "/vue/regist", "/vue/login/login"};
		registry.addInterceptor(new JwtInterceptor())
		                         .addPathPatterns("/vue/**")
		                         .excludePathPatterns(patterns);
//		registry.addInterceptor(new OwnerHandlerInterceptor())
//								.addPathPatterns(regist);

		super.addInterceptors(registry);
	}

}
