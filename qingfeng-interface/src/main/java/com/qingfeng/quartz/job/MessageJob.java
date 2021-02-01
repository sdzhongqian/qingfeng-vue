package com.qingfeng.quartz.job;

import com.qingfeng.quartz.service.BusTaskService;
import com.qingfeng.util.PageData;
import org.quartz.*;

import javax.annotation.Resource;
import java.io.Serializable;


/**
 * @author anxingtao
 * @Title: MessageJob
 * @ProjectName property
 * @Description: TODO
 * @date 2019-10-3113:47
 */
@DisallowConcurrentExecution
public class MessageJob implements Job,Serializable {

    private static final long serialVersionUID = 1L;

    @Resource
    private BusTaskService busTaskService;


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        JobDetail jobDetail = arg0.getJobDetail();
        System.out.println("=================开始执行===================");
        System.out.println(jobDetail.getDescription());
        System.out.println(jobDetail.getKey().getName());
        System.out.println(jobDetail.getKey().getGroup());
        System.out.println("roleService.findList:"+busTaskService.findList(new PageData()).size());;
        System.out.println("###########注入："+busTaskService);
        //查询用户
//        String[] data = jobDetail.getDescription().split("#");
//        String[] user_names = data[1].split(",");//根据用户名查询用户信息


        System.out.println("====================消息====================");
    }

}
