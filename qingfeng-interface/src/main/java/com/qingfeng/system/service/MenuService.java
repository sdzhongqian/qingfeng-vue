package com.qingfeng.system.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.MenuDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: MenuService
 * @ProjectName com.qingfeng
 * @Description: 菜单SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class MenuService extends CrudService<MenuDao,PageData> {

    @Autowired
    protected MenuDao menudao;

    /** 
     * @Description: findMenuList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-27 13:09
     */ 
    public List<PageData> findMenuList(PageData pd){
        return menudao.findMenuList(pd);
    }

    /**
     * @Description: findAuthMenuList 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-9-30 10:51 
     */ 
    public List<PageData> findAuthMenuList(PageData pd){
        return menudao.findAuthMenuList(pd);
    }
    
    
    
}