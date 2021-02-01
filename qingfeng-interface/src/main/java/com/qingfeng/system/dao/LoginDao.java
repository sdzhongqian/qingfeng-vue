package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: LoginDao
 * @ProjectName wdata
 * @Description: 登录Dao
 * @author anxingtao
 * @date 2020-9-27 10:19
 */
@Mapper
public interface LoginDao extends CrudDao<PageData> {


    /**
     * @Description: findUserInfo
     * @Param: [pd]
     * @return: com.qingfeng.util.PageData
     * @Author: anxingtao
     * @Date: 2020-9-27 10:19
     */
    PageData findUserInfo(PageData pd);

}
