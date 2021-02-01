package com.qingfeng.base.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Title: CommonConfig
 * @ProjectName wdata
 * @Description: 公共参数设置
 * @author anxingtao
 * @date 2020-10-9 22:48
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.qingfeng.activiti")
// PropertySource默认取application.properties
@PropertySource(value = "classpath:config/config.properties")
public class ActivitiParamConfig {

    //代码生成屏蔽字段
    private String startFlowElementId;

}
