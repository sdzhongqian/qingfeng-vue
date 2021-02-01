package com.qingfeng.quartz.config;

import com.qingfeng.system.service.RoleService;
import com.qingfeng.util.PageData;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化一个测试Demo任务
 * 创建者
 * 创建时间	2018年4月3日
 */
@Component
public class TaskRunner implements ApplicationRunner {

	private final static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	@Qualifier("Scheduler")
    private Scheduler scheduler;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void run(ApplicationArguments var) throws Exception{
		System.out.println("TaskRunner#####:"+roleService.findList(new PageData()).size());
	}

}