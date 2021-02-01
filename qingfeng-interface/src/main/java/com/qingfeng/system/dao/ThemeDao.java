package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: ThemeDao
 * @ProjectName wdata
 * @Description: 主题设置Dao
 * @author anxingtao
 * @date 2020-9-28 17:44
 */
@Mapper
public interface ThemeDao extends CrudDao<PageData> {

}
