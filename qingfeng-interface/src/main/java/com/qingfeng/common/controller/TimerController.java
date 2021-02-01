package com.qingfeng.common.controller;

import com.qingfeng.base.controller.BaseController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


/**
 * @Title: TimerController
 * @ProjectName wdata
 * @Description: 定时器
 * @author anxingtao
 * @date 2020-10-1 21:40
 */
@Controller
@Configuration
@EnableScheduling
@Component
public class TimerController extends BaseController {
    private static Logger logger = LogManager.getLogger(TimerController.class.getName());

//    @Autowired
//    private RoleService roleService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void execSql() throws Exception{
        System.out.println("开始执行定时器。。。。");
    }




    @Scheduled(cron = "0 0 2 * * ?")
    public void timingBackups() throws Exception {
    }



}
