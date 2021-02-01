package com.qingfeng.common.service;

import com.github.pagehelper.PageHelper;
import com.qingfeng.base.service.CrudService;
import com.qingfeng.common.dao.GraphicDao;
import com.qingfeng.framework.datasource.DataSourceType;
import com.qingfeng.framework.datasource.MyDataSource;
import com.qingfeng.util.Page;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: GraphicService
 * @ProjectName wdata
 * @Description: GraphicService
 * @author anxingtao
 * @date 2020-10-8 20:21
 */
@Service
@Transactional
public class GraphicService extends CrudService<GraphicDao,PageData> {

    @Autowired
    protected GraphicDao graphicdao;

    @MyDataSource(DataSourceType.Slave)
    public List<PageData> findListPage(Page page) {
        PageHelper.startPage(page.getIndex(), page.getShowCount());
        return graphicdao.findListPage(page);
    }

    /**
     * @Description: findListSize 分页总条数
     * @Param: [page]
     * @return: java.lang.Integer
     * @Author: anxingtao
     * @Date: 2020-9-22 21:03
     */
    @MyDataSource(DataSourceType.Slave)
    public Integer findListSize(Page page){
        return graphicdao.findListSize(page);
    }


    /**
     * @Description: findList 列表信息查询
     * @Param: [pd]
     * @return: java.util.List<T>
     * @Author: anxingtao
     * @Date: 2020-9-22 21:03
     */
    @MyDataSource(DataSourceType.Slave)
    public List<PageData> findList(PageData pd){
        List<PageData> list = graphicdao.findList(pd);
        return list;
    }

    /**
     * @Description: findInfo 详情信息查询
     * @Param: [pd]
     * @return: T
     * @Author: anxingtao
     * @Date: 2020-9-22 21:03
     */
    @MyDataSource(DataSourceType.Slave)
    public PageData findInfo(PageData pd){
        return graphicdao.findInfo(pd);
    }

    /**
     * @Description: save 保存方法
     * @Param: [t]
     * @return: int
     * @Author: anxingtao
     * @Date: 2020-9-22 21:03
     */
    @MyDataSource(DataSourceType.Slave)
    public int save(PageData t){
        return graphicdao.save(t);
    }


    /**
     * @Description: update 更新方法
     * @Param: [t]
     * @return: int
     * @Author: anxingtao
     * @Date: 2020-9-22 21:04
     */
    @MyDataSource(DataSourceType.Slave)
    public int update(PageData t){
        return graphicdao.update(t);
    }

    /**
     * @Description: delForParam 根据对象删除
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-9-24 18:12
     */
    @MyDataSource(DataSourceType.Slave)
    public void delForParam(PageData pd){
        graphicdao.delForParam(pd);
    }

    /**
     * @Description: del 根据ids集合删除
     * @Param: [ids]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-9-22 21:04
     */
    @Transactional
    @MyDataSource(DataSourceType.Slave)
    public void del(String[] ids){
        graphicdao.del(ids);
    }


}