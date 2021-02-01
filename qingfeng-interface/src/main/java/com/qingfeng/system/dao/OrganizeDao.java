package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OrganizeDao
 * @ProjectName qingfeng
 * @Description: 组织DAO层
 * @author anxingtao
 * @date 2020-9-22 22:42
 */
@Mapper
public interface OrganizeDao extends CrudDao<PageData> {


    /** 
     * @Description: findOrganizeRoleList 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-9-26 15:57 
     */
    List<PageData> findOrganizeRoleList(PageData pd);

    /** 
     * @Description: delOrganizeRole 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:10 
     */
    void delOrganizeRole(PageData pd);

    /** 
     * @Description: saveOrganizeRole
     * @Param: [list] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:33
     */
    void saveOrganizeRole(List<PageData> list);

    /** 
     * @Description: findTreeTableList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-9-26 21:54
     */
    List<PageData> findTreeTableList(PageData pd);

    /** 
     * @Description: updateUserLeader
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-11-15 9:35
     */
    void updateUserLeader(PageData pd);
    
}
