package com.qingfeng.quartz.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.quartz.model.QuartzEntity;
import com.qingfeng.quartz.service.BusTaskService;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
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
 * @Title: BusTaskController
 * @ProjectName wdata
 * @Description: 业务任务
 * @author anxingtao
 * @date 2020-10-2 12:04
 */
@Api(tags = "quartz业务案例接口")
@Controller
@RequestMapping(value = "/quartz/busTask")
public class BusTaskController extends BaseController {

	@Autowired
	private BusTaskService busTaskService;
	@Autowired @Qualifier("Scheduler")
	private Scheduler scheduler;
	@Autowired
	private UserService userService;


	/**
	* @Description: findListPage
	* @Param: [page, request, response, session]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:00
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
		List<PageData> list = busTaskService.findListPage(page);
		int num = busTaskService.findListSize(page);
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
	* @Param: [request, response, session]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:01
	*/
	@ApiOperation("保存或更新数据接口")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		Json json = new Json();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
		if(Verify.verifyIsNotNull(pd.get("id"))){
			pd.put("update_time", time);
			pd.put("trigger_time",time);
			pd.put("update_user", user_id);
			pd.put("trigger_state","Y");
			PageData p = busTaskService.findInfoForNameAndGroup(pd);
			if(Verify.verifyIsNotNull(p)){
				json.setSuccess(false);
				json.setMsg("操作失败，标题已存在。");
			}else{
				int num = busTaskService.update(pd);
				if(num==1){
					try {
						QuartzEntity quartz = new QuartzEntity();
						quartz.setJobName(pd.get("job_name").toString());
						quartz.setJobGroup(pd.get("job_group").toString());
						quartz.setDescription(pd.get("description").toString()+"#"+pd.get("notice_user").toString());
						quartz.setCronExpression(pd.get("cron_expression").toString());
						quartz.setJobClassName(pd.get("job_class_name").toString());
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
						json.setSuccess(true);
						json.setMsg("操作成功。");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			//主键id
			pd.put("id", GuidUtil.getUuid());
			pd.put("create_time", time);
			pd.put("trigger_time",time);
			pd.put("create_user",user_id);
			pd.put("create_organize",organize_id);

			//处理业务
			String type = pd.getString("type");
//		if(type.equals("1")){//类型为1 ----后续可根据type拓展
			pd.put("job_group",organize_id);//根据组织进行分组
			pd.put("job_class_name","com.qingfeng.quartz.job.MessageJob");
			pd.put("trigger_state","Y");
//		}
			PageData p = busTaskService.findInfoForNameAndGroup(pd);
			if(Verify.verifyIsNotNull(p)){
				json.setSuccess(false);
				json.setMsg("操作失败，标题已存在。");
			}else{
				pd.put("triggerName","");
				int num = busTaskService.save(pd);
				if(num==1){
					try {
						QuartzEntity quartz = new QuartzEntity();
						quartz.setJobName(pd.get("job_name").toString());
						quartz.setJobGroup(pd.get("job_group").toString());
						quartz.setDescription(pd.get("description").toString()+"#"+pd.get("notice_user").toString());
						quartz.setCronExpression(pd.get("cron_expression").toString());
						quartz.setJobClassName(pd.get("job_class_name").toString());

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

						json.setSuccess(true);
						json.setMsg("操作成功。");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.writeJson(response,json);
	}


	/**
	* @Description: del
	* @Param: [request, response]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:03
	*/
	@ApiOperation("删除数据接口")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(@RequestHeader HttpHeaders headers, @RequestParam String param_ids, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		try {
			String[] ids = param_ids.split(",");
			pd.put("ids", Arrays.asList(ids));
			List<PageData> list = busTaskService.findList(pd);
			for (int i = 0; i < list.size(); i++) {
				TriggerKey triggerKey = TriggerKey.triggerKey(list.get(i).get("job_name").toString(), list.get(i).get("job_group").toString());
				// 停止触发器
				scheduler.pauseTrigger(triggerKey);
				// 移除触发器
				scheduler.unscheduleJob(triggerKey);
				// 删除任务
				scheduler.deleteJob(JobKey.jobKey(list.get(i).get("job_name").toString(), list.get(i).get("job_group").toString()));
			}
			busTaskService.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
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
		json.setMsg("执行成功。");
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
	@RequestMapping(value = "/stopOrRestore", method = RequestMethod.POST)
	public void stopOrRestore(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		try {
			JobKey key = new JobKey(pd.get("jobName").toString(),pd.get("jobGroup").toString());
			if(pd.get("status").equals("N")){
				scheduler.pauseJob(key);
				System.out.println("停止。。。。。");
				pd.put("trigger_state","N");
			}else if(pd.get("status").equals("Y")){
				scheduler.resumeJob(key);
				System.out.println("启动。。。。。");
				pd.put("trigger_state","Y");
				pd.put("trigger_time",DateTimeUtil.getDateTimeStr());
			}
			pd.put("update_time",DateTimeUtil.getDateTimeStr());
			System.out.println(pd.toString());
			busTaskService.update(pd);
		} catch (SchedulerException e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败。");
		}
		this.writeJson(response,json);
	}


}
