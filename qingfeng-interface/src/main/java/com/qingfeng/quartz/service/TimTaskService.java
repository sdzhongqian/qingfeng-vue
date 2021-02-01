package com.qingfeng.quartz.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.quartz.dao.TimTaskDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: TimTaskService
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2020-10-1 21:54
 */
@Service
@Transactional
public class TimTaskService extends CrudService<TimTaskDao,PageData> {

    @Autowired
    protected TimTaskDao timTaskDao;


}
