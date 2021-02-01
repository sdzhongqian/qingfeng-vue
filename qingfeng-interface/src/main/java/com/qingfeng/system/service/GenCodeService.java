package com.qingfeng.system.service;

import com.github.pagehelper.PageHelper;
import com.qingfeng.base.service.CrudService;
import com.qingfeng.system.dao.GenCodeDao;
import com.qingfeng.util.Page;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: GenCodeService
 * @ProjectName wdata
 * @Description: 代码生成SERVICE
 * @author anxingtao
 * @date 2020-10-9 13:12
 */
@Service
@Transactional
public class GenCodeService extends CrudService<GenCodeDao,PageData> {

    @Autowired
    protected GenCodeDao genCodeDao;


    /**
     * @Description: 查询数据表原信息分页列表
     * @Param: [page]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-9 14:48
     */
    public List<PageData> findTableListPage(Page page){
        PageHelper.startPage(page.getIndex(), page.getShowCount());
        return genCodeDao.findTableListPage(page);
    }

    /**
     * @Description: 查询数据表原信息分页数量
     * @Param: [page]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-10-9 14:49
     */
    public Integer findTableListSize(Page page){
        return genCodeDao.findTableListSize(page);
    }

    /**
     * @Description: updateComment
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-10-9 15:47 
     */
    public void updateComment(PageData pd){
        genCodeDao.updateComment(pd);
    }
    
    /** 
     * @Description: findTableList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-9 23:32
     */ 
    public List<PageData> findTableList(PageData pd){
        return genCodeDao.findTableList(pd);
    }
    
    /**
     * @Description: 查询字段表原信息列表
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-9 14:49
     */
    public List<PageData> findColumndList(PageData pd){
        return genCodeDao.findColumndList(pd);
    }

    /**
     * @Description: 导入保存数据表
     * @Param: [list]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-10-9 14:50
     */
    public Integer saveTable(List<PageData> list){
        return genCodeDao.saveTable(list);
    }

    /**
     * @Description: 导出保存字段表
     * @Param: [list]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-10-9 14:50
     */
    public Integer saveField(List<PageData> list){
        return genCodeDao.saveField(list);
    }

    /**
     * @Description: 查询字段表分页列表
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-9 14:51
     */
    public List<PageData> findFieldList(PageData pd){
        return genCodeDao.findFieldList(pd);
    }

    /**
     * @Description: 更新字段表
     * @Param: [pd]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-10-9 14:51
     */
    public Integer updateField(PageData pd){
        return genCodeDao.updateField(pd);
    }

    /**
     * @Description: 删除字段表
     * @Param: [ids]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-10-9 14:52
     */
    public void delField(String[] ids){
        genCodeDao.delField(ids);
    }



    //=====================处理关联表信息============================

    /**
     * @Description: findTableLinkList 查询关联表
     * @Param: [pd]
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-11 17:46
     */
    public List<PageData> findTableLinkList(PageData pd){
        return genCodeDao.findTableLinkList(pd);
    }

    /** 
     * @Description: findTableLinkInfo 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData>
     * @Author: anxingtao
     * @Date: 2020-10-12 9:08 
     */ 
    public PageData findTableLinkInfo(PageData pd){
        return genCodeDao.findTableLinkInfo(pd);
    }

    /** 
     * @Description: findFieldInfo 查询字段详情 
     * @Param: [pd] 
     * @return: com.qingfeng.util.PageData
     * @Author: anxingtao
     * @Date: 2020-10-12 14:40 
     */ 
    public PageData findFieldInfo(PageData pd){
        return genCodeDao.findFieldInfo(pd);
    }
    
    /**
     * @Description: saveTableLink 保存关联表
     * @Param: [pd]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-10-11 17:46
     */
    public Integer saveTableLink(PageData pd){
        return genCodeDao.saveTableLink(pd);
    }

    /**
     * @Description: updateTableLink 更新关联表
     * @Param: [pd]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-10-11 17:47
     */
    public Integer updateTableLink(PageData pd){
        return genCodeDao.updateTableLink(pd);
    }


    /**
     * @Description: delTableLink 删除表关联
     * @Param: [ids]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-10-11 17:47
     */
    public void delTableLink(String[] ids){
        genCodeDao.delTableLink(ids);
    }



}