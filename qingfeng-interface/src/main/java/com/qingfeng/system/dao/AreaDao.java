package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: AreaDao
 * @ProjectName qingfeng
 * @Description: 地区DAO层
 * @author anxingtao
 * @date 2020-9-22 22:40
 */
@Mapper
public interface AreaDao extends CrudDao<PageData> {

}
