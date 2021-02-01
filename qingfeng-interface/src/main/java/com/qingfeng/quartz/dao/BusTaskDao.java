package com.qingfeng.quartz.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: BusTaskDao
 * @ProjectName wdata
 * @Description: 业务任务
 * @author anxingtao
 * @date 2020-10-2 12:03
 */
@Mapper
public interface BusTaskDao extends CrudDao<PageData> {

    /** 
     * @Description: findInfoForNameAndGroup
     * @Param: [pd] 
     * @return: com.qingfeng.util.PageData 
     * @Author: anxingtao
     * @Date: 2020-10-2 12:06
     */ 
    public PageData findInfoForNameAndGroup(PageData pd);

}
