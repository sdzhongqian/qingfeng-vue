package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.OrganizeDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: OrganizeService
 * @ProjectName com.qingfeng
 * @Description: 组织SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class OrganizeService extends CrudService<OrganizeDao,PageData> {

    @Autowired
    protected OrganizeDao organizedao;


    /**
     * @Description: findOrganizeRoleList
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 15:56
     */
    public List<PageData> findOrganizeRoleList(PageData pd){
        return organizedao.findOrganizeRoleList(pd);
    }
    
    /** 
     * @Description: delOrganizeRole 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:10 
     */ 
    public void delOrganizeRole(PageData pd){
        organizedao.delOrganizeRole(pd);
    }

    /** 
     * @Description: saveOrganizeRole 
     * @Param: [list] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:33
     */ 
    public void saveOrganizeRole(List<PageData> list){
        organizedao.saveOrganizeRole(list);
    }

    /** 
     * @Description: findTreeTableList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 18:11
     */ 
    public List<PageData> findTreeTableList(PageData pd){
        return organizedao.findTreeTableList(pd);
    }

    /** 
     * @Description: updateUserLeader
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-11-15 9:35
     */ 
    public void updateUserLeader(PageData pd){
        organizedao.updateUserLeader(pd);
    }


}