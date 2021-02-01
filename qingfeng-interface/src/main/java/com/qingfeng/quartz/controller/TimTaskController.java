package com.qingfeng.quartz.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.quartz.model.QuartzEntity;
import com.qingfeng.quartz.service.TimTaskService;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.Json;
import com.qingfeng.util.Page;
import com.qingfeng.util.PageData;
import com.qingfeng.util.Verify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: timTaskController
 * @ProjectName wdata
 * @Description: 定时器任务
 * @author anxingtao
 * @date 2020-10-1 21:54
 */
@Api(tags = "quartz定时业务接口")
@Controller
@RequestMapping(value = "/quartz/timTask")
public class TimTaskController extends BaseController {

    @Autowired @Qualifier("Scheduler")
    private Scheduler scheduler;
    @Autowired
    private TimTaskService timTaskService;
    @Autowired
    private UserService userService;

    /**
     * @Description: findByPage
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-8-24 11:51
     */
    @ApiOperation("查询数据分页列表接口")
    @RequestMapping(value = "/findListPage", method = RequestMethod.POST)
    public void findListPage(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException {
        Page page = new Page();
        //处理数据权限
        String  token = headers.get("access-token").get(0);
        System.out.println("findUserInfo###########TOKEN:"+token);
        String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
        String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
        pd.put("auth_user",user_id);
        pd.put("auth_organize",organize_id);
        pd.put("user_id",user_id);
        PageData orgPd = userService.findUserOrganizeInfo(pd);
        if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
            pd.put("auth_organize_ids", Arrays.asList(orgPd.get("authOrgIds").toString().split(",")));
        }else{
            pd.put("auth_organize_ids",new ArrayList<String>());
        }
        //处理分页
        if(Verify.verifyIsNotNull(pd.get("page"))){
            page.setIndex(Integer.parseInt(pd.get("page").toString()));
        }else{
            page.setIndex(1);
        }
        if(Verify.verifyIsNotNull(pd.get("limit"))){
            page.setShowCount(Integer.parseInt(pd.get("limit").toString()));
        }else{
            page.setShowCount(10);
        }
        page.setPd(pd);
        List<PageData> list = timTaskService.findListPage(page);

        int num = timTaskService.findListSize(page);
        Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setCode(0);
        json.setCount(num);
        json.setData(list);
        json.setSuccess(true);
        this.writeJson(response,json);
    }

    /**
     * @Description: save
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-8-24 13:02
     */
    @ApiOperation("保存或更新数据接口")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException  {
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("操作成功。");
        try {
//            {jobName=test01, cronExpression=0/5 * * * * ? , jobClassName=com.qingfeng.quartz.job.TestJob, description=测试, jobGroup=group01}
            QuartzEntity quartz = new QuartzEntity();
            quartz.setJobName(pd.get("jobName").toString());
            quartz.setCronExpression(pd.get("cronExpression").toString());
            quartz.setJobClassName(pd.get("jobClassName").toString());
            quartz.setJobGroup(pd.get("jobGroup").toString());
            if(Verify.verifyIsNotNull(pd.get("description"))){
                quartz.setDescription(pd.get("description").toString());
            }
            if(Verify.verifyIsNotNull(pd.get("oldJobName"))){
                quartz.setOldJobName(pd.get("oldJobName").toString());
                quartz.setOldJobGroup(pd.get("oldJobGroup").toString());
            }
            //获取Scheduler实例、废弃、使用自动注入的scheduler、否则spring的service将无法注入
            //Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //如果是修改  展示旧的 任务
            if(quartz.getOldJobGroup()!=null){
                JobKey key = new JobKey(quartz.getOldJobName(),quartz.getOldJobGroup());
                scheduler.deleteJob(key);
            }
            Class cls = Class.forName(quartz.getJobClassName()) ;
            cls.newInstance();
            //构建job信息
            JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
                    quartz.getJobGroup())
                    .withDescription(quartz.getDescription()).build();
            // 触发时间点
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
                    .startNow().withSchedule(cronScheduleBuilder).build();
            //交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("操作失败。");
        }
        this.writeJson(response,json);
    }


    /**
     * @Description: del
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-8-27 14:46
     */
    @ApiOperation("删除数据接口")
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public void del(@RequestHeader HttpHeaders headers, @RequestParam String jobName, @RequestParam String jobGroup, HttpServletResponse response) throws IOException  {
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("操作成功。");
        try {
            String jobnames[] = jobName.split(",");
            String jobgroups[] = jobGroup.split(",");
            for (int i = 0; i < jobnames.length; i++) {
                TriggerKey triggerKey = TriggerKey.triggerKey(jobnames[i], jobgroups[i]);
                // 停止触发器
                scheduler.pauseTrigger(triggerKey);
                // 移除触发器
                scheduler.unscheduleJob(triggerKey);
                // 删除任务
                scheduler.deleteJob(JobKey.jobKey(jobnames[i], jobgroups[i]));
                System.out.println("removeJob:"+JobKey.jobKey(jobnames[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("操作失败。");
        }
        this.writeJson(response,json);
    }


    /**
     * @Description: execution
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2019-6-5 17:34
     */
    @ApiOperation("执行任务接口")
    @RequestMapping(value = "/execution", method = RequestMethod.GET)
    public void execution(@RequestHeader HttpHeaders headers, @RequestParam String jobName, @RequestParam String jobGroup, HttpServletResponse response) throws IOException  {
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("操作成功。");
        try {
            JobKey key = new JobKey(jobName,jobGroup);
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("操作失败。");
        }
        this.writeJson(response,json);
    }


    /**
     * @Description: stopOrRestore
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2019-6-5 17:40
     */
    @ApiOperation("停止或恢复任务接口")
    @RequestMapping(value = "/stopOrRestore", method = RequestMethod.GET)
    public void stopOrRestore(@RequestHeader HttpHeaders headers, @RequestParam String jobName, @RequestParam String jobGroup, @RequestParam String status, HttpServletResponse response) throws IOException  {
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("操作成功。");
        try {
            JobKey key = new JobKey(jobName,jobGroup);
            if(status.equals("stop")){
                scheduler.pauseJob(key);
            }else if(status.equals("restore")){
                scheduler.resumeJob(key);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("操作失败。");
        }
        this.writeJson(response,json);
    }

}
