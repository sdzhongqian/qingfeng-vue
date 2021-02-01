package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.LoginDao;
import com.qingfeng.system.dao.RoleDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: LoginService
 * @ProjectName wdata
 * @Description: 登录SERVICE层
 * @author anxingtao
 * @date 2020-9-27 10:20
 */
@Service
@Transactional
public class LoginService extends CrudService<RoleDao,PageData> {

    @Autowired
    protected LoginDao loginDao;


    /**
     * @Description: findUserInfo
     * @Param: [pd]
     * @return: com.qingfeng.util.PageData
     * @Author: anxingtao
     * @Date: 2020-9-27 10:20
     */
    public PageData findUserInfo(PageData pd){
        return loginDao.findUserInfo(pd);
    }


    
}