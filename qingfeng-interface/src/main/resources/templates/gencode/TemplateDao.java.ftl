package ${tablePd.pack_path}.${tablePd.mod_name}.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: ${tablePd.bus_name?cap_first}Dao
 * @ProjectName com.qingfeng
 * @Description: ${tablePd.bus_name?cap_first}Dao
 * @author anxingtao
 * @date 2020-9-22 22:42
 */
@Mapper
public interface ${tablePd.bus_name?cap_first}Dao extends CrudDao<PageData> {

    /**
    * @Description: updateStatus
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void updateStatus(PageData pd);


<#if tablePd.temp_type == '1'>

    /**
    * @Description: findChildList
    * @Param: [pd]
    * @return: java.util.List<com.qingfeng.util.PageData>
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public List<PageData> findChildList(PageData pd);

    /**
    * @Description: saveChild
    * @Param: [pd]
    * @return: int
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public int saveChild(PageData pd);

    /**
    * @Description: updateChild
    * @Param: [pd]
    * @return: int
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public int updateChild(PageData pd);

    /**
    * @Description: delChild
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void delChild(String[] ids);

    /**
    * @Description: delChildForPIds
    * @Param: [ids]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void delChildForPIds(String[] ids);

</#if>

<#if tablePd.temp_type == '2'>
    /**
    * @Description: findContainChildList
    * @Param: [pd]
    * @return: java.util.List<com.qingfeng.util.PageData>
    * @Author: anxingtao
    * @Date: 2020-10-15 23:34
    */
    public List<PageData> findContainChildList(PageData pd);
</#if>

}
