package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: GroupDao
 * @ProjectName qingfeng
 * @Description: 用户组DAO层
 * @author anxingtao
 * @date 2020-9-22 22:42
 */
@Mapper
public interface GroupDao extends CrudDao<PageData> {

    /** 
     * @Description: saveGroupUser 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:04 
     */
    void saveGroupUser(PageData pd);

    /** 
     * @Description: findGroupUser 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:04 
     */
    List<PageData> findGroupUser(PageData pd);

    /** 
     * @Description: delGroupUser 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:04 
     */
    void delGroupUser(PageData pd);

    /** 
     * @Description: updateGroupUser
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:08
     */
    void updateGroupUser(PageData pd);

}
