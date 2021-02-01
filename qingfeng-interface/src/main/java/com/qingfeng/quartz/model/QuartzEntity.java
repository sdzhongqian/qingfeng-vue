package com.qingfeng.quartz.model;

import lombok.Data;

/**
 * @Title: 任务类
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2019-6-5 15:10
 */
@Data
public class QuartzEntity {

	private String jobName;//任务名称
	private String jobGroup;//任务分组
	private String description;//任务描述
	private String jobClassName;//执行类
	private String cronExpression;//执行时间
	private String triggerTime;//执行时间
	private String triggerState;//任务状态

	private String oldJobName;//任务名称 用于修改
	private String oldJobGroup;//任务分组 用于修改

	public QuartzEntity() {
		super();
	}
	public QuartzEntity(String jobName, String jobGroup, String description, String jobClassName, String cronExpression, String triggerTime) {
		super();
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.description = description;
		this.jobClassName = jobClassName;
		this.cronExpression = cronExpression;
		this.triggerTime = triggerTime;
	}
}
