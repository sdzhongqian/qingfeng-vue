package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: MenuDao
 * @ProjectName qingfeng
 * @Description: 菜单DAO层
 * @author anxingtao
 * @date 2020-9-22 22:42
 */
@Mapper
public interface MenuDao extends CrudDao<PageData> {

    /**
     * @Description: findMenuList
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-27 13:04
     */
    List<PageData> findMenuList(PageData pd);

    /**
     * @Description: findAuthMenuList
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-30 10:52
     */
    List<PageData> findAuthMenuList(PageData pd);

}
