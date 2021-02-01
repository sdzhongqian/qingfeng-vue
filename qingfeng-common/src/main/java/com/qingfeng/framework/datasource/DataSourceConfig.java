package com.qingfeng.framework.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 * 
 * @ClassName DataSourceConfig
 * @Description 多数据源配置
 * @author lide
 * @date 2018年2月27日 下午1:21:39
 */
@Configuration
public class DataSourceConfig {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean(name = "master",initMethod = "init",destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSource dataSource1() {
		logger.info("===============数据库Master部署成功。===============");
		return DruidDataSourceBuilder.create().build();
	}

//	@Bean(name = "slave",initMethod = "init",destroyMethod = "close")
//	@ConfigurationProperties(prefix = "spring.datasource.slave")
//	public DataSource dataSource2() {
//		logger.info("===============从配数据库Slave部署成功。===============");
//		return DruidDataSourceBuilder.create().build();
//	}

	@Bean(name="dynamicDataSource")
//	@DependsOn({ "master", "slave"})
	@DependsOn({ "master" })
	@Primary
	/**优先使用，多数据源**/
	public DataSource dataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		DataSource master = dataSource1();
//		DataSource slave = dataSource2();
		//设置默认数据源
//		System.out.println("===============开启Master数据源。===============");
		dynamicDataSource.setDefaultTargetDataSource(master);
		//配置多数据源
		Map<Object,Object> map = new HashMap<>();
		map.put(DataSourceType.Master.getName(), master);	//key需要跟ThreadLocal中的值对应
//		map.put(DataSourceType.Slave.getName(), slave);
		dynamicDataSource.setTargetDataSources(map);
		return dynamicDataSource;
	}


}
