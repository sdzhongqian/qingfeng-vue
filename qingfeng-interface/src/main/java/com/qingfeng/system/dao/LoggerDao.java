package com.qingfeng.system.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
  * @ClassName: LoggerDao
  * @Description: LoggerDao类
  * @Description: 操作日志记录
  * @author Administrator
  * @date 2018-9-6 15:16
  *
 */
@Mapper
public interface LoggerDao extends CrudDao<PageData> {

}
