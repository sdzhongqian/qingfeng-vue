package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.GroupDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: GroupService
 * @ProjectName com.qingfeng
 * @Description: 用户组SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:43
 */
@Service
@Transactional
public class GroupService extends CrudService<GroupDao,PageData> {

@Autowired
protected GroupDao groupdao;


    /** 
     * @Description: saveGroupUser 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:03 
     */ 
    public void saveGroupUser(PageData pd){
        groupdao.saveGroupUser(pd);
    }

    /** 
     * @Description: findGroupUser 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-25 23:03 
     */ 
    public List<PageData> findGroupUser(PageData pd){
        return groupdao.findGroupUser(pd);
    }

    /** 
     * @Description: delGroupUser 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:03 
     */ 
    public void delGroupUser(PageData pd){
        groupdao.delGroupUser(pd);
    }

    /** 
     * @Description: updateGroupUser
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 23:08
     */ 
    public void updateGroupUser(PageData pd){
        groupdao.updateGroupUser(pd);
    }

}