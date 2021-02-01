package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Title: DictionaryDao
 * @ProjectName qingfeng
 * @Description: 字典DAO层
 * @author anxingtao
 * @date 2020-9-22 22:41
 */
@Mapper
public interface DictionaryDao extends CrudDao<PageData> {

}
