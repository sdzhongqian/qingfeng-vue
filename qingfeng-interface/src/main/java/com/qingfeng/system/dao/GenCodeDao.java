package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.Page;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: GenCodeDao
 * @ProjectName wdata
 * @Description: 代码生成DAO
 * @author anxingtao
 * @date 2020-10-9 13:11
 */
@Mapper
public interface GenCodeDao extends CrudDao<PageData> {

    /** 
     * @Description: 查询数据表原信息分页列表 
     * @Param: [page] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:48 
     */
    List<PageData> findTableListPage(Page page);

    /** 
     * @Description: 查询数据表原信息分页数量 
     * @Param: [page] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:49 
     */
    Integer findTableListSize(Page page);
    
    /** 
     * @Description: updateComment 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-10-9 15:47 
     */
    void updateComment(PageData pd);

    /** 
     * @Description: findTableList
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-10-9 23:16
     */
    List<PageData> findTableList(PageData pd);

    /** 
     * @Description: 查询字段表原信息列表 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:49 
     */
    List<PageData> findColumndList(PageData pd);

    /** 
     * @Description: 导入保存数据表 
     * @Param: [list] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:50 
     */
    Integer saveTable(List<PageData> list);

    /** 
     * @Description: 导出保存字段表 
     * @Param: [list] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:50 
     */
    Integer saveField(List<PageData> list);

    /** 
     * @Description: 查询字段表分页列表
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:51 
     */
    List<PageData> findFieldList(PageData pd);

    /** 
     * @Description: 更新字段表 
     * @Param: [pd] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-10-9 14:51 
     */
    Integer updateField(PageData pd);
    
    /** 
     * @Description: 删除字段表
     * @Param: [ids] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-10-9 15:38
     */
    void delField(String[] ids);


    //=====================处理关联表信息============================

    /** 
     * @Description: findTableLinkList 查询关联表 
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-10-11 17:46 
     */
    List<PageData> findTableLinkList(PageData pd);

    /** 
     * @Description: findTableLinkInfo
     * @Param: [pd] 
     * @return: java.util.List<com.qingfeng.util.PageData> 
     * @Author: anxingtao
     * @Date: 2020-10-12 9:08 
     */
    PageData findTableLinkInfo(PageData pd);

    /** 
     * @Description: findFieldInfo 
     * @Param: [pd] 
     * @return: com.qingfeng.util.PageData 
     * @Author: anxingtao
     * @Date: 2020-10-12 14:40 
     */
    PageData findFieldInfo(PageData pd);
        
    /** 
     * @Description: saveTableLink 保存关联表
     * @Param: [pd] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-10-11 17:46 
     */
    Integer saveTableLink(PageData pd);
    
    /** 
     * @Description: updateTableLink 更新关联表 
     * @Param: [pd] 
     * @return: java.lang.Integer 
     * @Author: anxingtao
     * @Date: 2020-10-11 17:47 
     */
    Integer updateTableLink(PageData pd);


    /** 
     * @Description: delTableLink 删除表关联
     * @Param: [ids] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-10-11 17:47 
     */
    void delTableLink(String[] ids);
    

    
}
