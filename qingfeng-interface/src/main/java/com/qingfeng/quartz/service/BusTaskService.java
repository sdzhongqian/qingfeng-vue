package com.qingfeng.quartz.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.quartz.dao.BusTaskDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: BusTaskService
 * @ProjectName wdata
 * @Description: 业务任务
 * @author anxingtao
 * @date 2020-10-2 12:04
 */
@Service
@Transactional
public class BusTaskService extends CrudService<BusTaskDao,PageData> {

@Autowired
protected BusTaskDao busTaskDao;
    
    /**
     * @Description: findInfoForNameAndGroup
     * @Param: [pd]
     * @return: com.qingfeng.util.PageData
     * @Author: anxingtao
     * @Date: 2020-10-2 12:04
     */
    public PageData findInfoForNameAndGroup(PageData pd){
        return busTaskDao.findInfoForNameAndGroup(pd);
    }


}