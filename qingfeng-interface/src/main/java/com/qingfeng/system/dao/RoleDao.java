package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: RoleDao
 * @ProjectName qingfeng
 * @Description: 角色DAO层
 * @author anxingtao
 * @date 2020-9-22 22:42
 */
@Mapper
public interface RoleDao extends CrudDao<PageData> {


    /** 
     * @Description: findRoleMenuList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-9-26 14:53 
     */
    List<String> findRoleMenuList(PageData pd);

    /**
     * @Description: delRoleMenu
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-9-26 15:00
     */
    void delRoleMenu(PageData pd);

    /**
     * @Description: saveRoleMenu
     * @Param: [list]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 15:12
     */
    void saveRoleMenu(List<PageData> list);

    /** 
     * @Description: findSimpleList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:33
     */
    List<PageData> findSimpleList(PageData pd);

    /**
     * @Description: findUserRoleList
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-30 10:36
     */
    List<PageData> findUserRoleList(PageData pd);

}
