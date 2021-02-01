package com.qingfeng.gencode.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.gencode.dao.MycontentDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title:MycontentService
 * @ProjectName com.qingfeng
 * @Description: SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class MycontentService extends CrudService<MycontentDao,PageData> {

    @Autowired
    protected MycontentDao mycontentdao;

    /**
    * @Description: updateStatus
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void updateStatus(PageData pd){
    mycontentdao.updateStatus(pd);
    }




}