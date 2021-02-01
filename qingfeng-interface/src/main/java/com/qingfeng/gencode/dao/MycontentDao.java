package com.qingfeng.gencode.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: MycontentDao
 * @ProjectName com.qingfeng
 * @Description: MycontentDao
 * @author anxingtao
 * @date 2020-9-22 22:42
 */
@Mapper
public interface MycontentDao extends CrudDao<PageData> {

    /**
    * @Description: updateStatus
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    void updateStatus(PageData pd);




}
