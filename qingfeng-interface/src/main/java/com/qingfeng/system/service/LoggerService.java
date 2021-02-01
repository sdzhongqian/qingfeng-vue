package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.LoggerDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author anxingtao
* @Title: LoggerService
* @ProjectName wdata
* @Description: 操作日志记录
* @date 2018-9-314:45
*/
@Service
@Transactional
public class LoggerService extends CrudService<LoggerDao,PageData> {

    @Autowired
    protected LoggerDao loggerdao;


}