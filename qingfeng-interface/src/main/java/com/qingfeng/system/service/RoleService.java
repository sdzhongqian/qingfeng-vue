package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.RoleDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: RoleService
 * @ProjectName com.qingfeng
 * @Description: 角色SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class RoleService extends CrudService<RoleDao,PageData> {

    @Autowired
    protected RoleDao roledao;

    
    /** 
     * @Description: findRoleMenuList 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 14:53 
     */ 
    public List<String> findRoleMenuList(PageData pd){
        return roledao.findRoleMenuList(pd);
    }
    
    /** 
     * @Description: delRoleMenu 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 15:00 
     */ 
    public void delRoleMenu(PageData pd){
        roledao.delRoleMenu(pd);
    }
    
    /** 
     * @Description: saveRoleMenu
     * @Param: [list] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 15:12 
     */ 
    public void saveRoleMenu(List<PageData> list){
        roledao.saveRoleMenu(list);
    }

    /**
     * @Description: findSimpleList
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 15:56
     */
    public List<PageData> findSimpleList(PageData pd){
        return roledao.findSimpleList(pd);
    }

    /** 
     * @Description: findUserRoleList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-30 11:03
     */ 
    public List<PageData> findUserRoleList(PageData pd){
        return roledao.findUserRoleList(pd);
    }
    
    

}