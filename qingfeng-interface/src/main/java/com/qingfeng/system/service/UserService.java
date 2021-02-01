package com.qingfeng.system.service;

import com.github.pagehelper.PageHelper;
import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.UserDao;
import com.qingfeng.util.Page;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: UserService
 * @ProjectName com.qingfeng
 * @Description: 用户SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class UserService extends CrudService<UserDao,PageData> {

    @Autowired
    protected UserDao userdao;

    /**
     * @Description: saveUserOrganize
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-9-25 14:53
     */
    public void saveUserOrganize(PageData pd){
        userdao.saveUserOrganize(pd);
    }

    /** 
     * @Description: findUserOrganize
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-25 15:25
     */ 
    public List<PageData> findUserOrganize(PageData pd){
        return userdao.findUserOrganize(pd);
    }
    
    /** 
     * @Description: delUserOrganize
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 15:34 
     */ 
    public void delUserOrganize(PageData pd){
        userdao.delUserOrganize(pd);
    }

    /** 
     * @Description: updateUserOrganize 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-25 16:03
     */ 
    public void updateUserOrganize(PageData pd){
        userdao.updateUserOrganize(pd);
    }

    /** 
     * @Description: findUserRoleList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-26 16:57
     */ 
    public List<PageData> findUserRoleList(PageData pd){
        return userdao.findUserRoleList(pd);
    }

    /** 
     * @Description: delUserRole 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:35 
     */ 
    public void delUserRole(PageData pd){
        userdao.delUserRole(pd);
    }

    /** 
     * @Description: saveUserRole 
     * @Param: [list] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:36 
     */ 
    public void saveUserRole(List<PageData> list){
        userdao.saveUserRole(list);
    }


    /**
     * @Description: findUserOrganizeInfo
     * @Param: [pd] 
     * @return: com.qingfeng.util.PageData
     * @Author: anxingtao
     * @Date: 2020-9-26 17:46 
     */ 
    public PageData findUserOrganizeInfo(PageData pd){
        return userdao.findUserOrganizeInfo(pd);
    }

    /** 
     * @Description: updateAuthForParam
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-27 0:30
     */ 
    public void updateAuthForParam(PageData pd){
        userdao.updateAuthForParam(pd);
    }

    /** 
     * @Description: delUserGroup 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-28 9:26
     */ 
    public void delUserGroup(PageData pd){
        userdao.delUserGroup(pd);
    }

    /** 
     * @Description: findUserList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-28 15:59
     */ 
    public List<PageData> findUserList(PageData pd){
        return userdao.findUserList(pd);
    }

    /** 
     * @Description: updateUserOrgUseStatus
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-28 17:21
     */ 
    public void updateUserOrgUseStatus(PageData pd){
        userdao.updateUserOrgUseStatus(pd);
    }


    /** 
     * @Description: findRoleUserList 查询用户角色信息
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-30 9:50
     */ 
    public List<PageData> findRoleUserList(PageData pd){
        return userdao.findRoleUserList(pd);
    }

    /** 
     * @Description: findUserOnlineList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-3 8:51
     */ 
    public List<PageData> findUserOnlineListPage(Page page){
        return userdao.findUserOnlineListPage(page);
    }


    //============================接口相关==========================
    /** 
     * @Description: findMyOrganizeListPage 查询用户关联组织信息 
     * @Param: [page] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-12-20 21:52 
     */ 
    public List<PageData> findMyOrganizeListPage(Page page) {
        PageHelper.startPage(page.getIndex(), page.getShowCount());
        return userdao.findMyOrganizeListPage(page);
    }

    /** 
     * @Description: findMyOrganizeListSize 查询用户关联组织分页信息 
     * @Param: [page] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-12-20 21:52 
     */ 
    public Integer findMyOrganizeListSize(Page page){
        return userdao.findMyOrganizeListSize(page);
    }


    /**
     * @Description: updateFOrToken
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2021-1-2 8:50
     */
    public void updateForToken(PageData pd){
        userdao.updateForToken(pd);
    }


    /** 
     * @Description: findOnLineListPage 
     * @Param: [page] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2021-1-2 17:37 
     */ 
    public List<PageData> findOnLineListPage(Page page) {
        PageHelper.startPage(page.getIndex(), page.getShowCount());
        return userdao.findOnLineListPage(page);
    }

    /**
     * @Description: findListSize 分页总条数
     * @Param: [page]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-9-22 21:03
     */
    public Integer findOnLineListSize(Page page){
        return userdao.findOnLineListSize(page);
    }

}