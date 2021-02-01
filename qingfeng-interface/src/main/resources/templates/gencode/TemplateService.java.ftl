package ${tablePd.pack_path}.${tablePd.mod_name}.service;

import com.qingfeng.base.service.CrudService;
import ${tablePd.pack_path}.${tablePd.mod_name}.dao.${tablePd.bus_name?cap_first}Dao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title:${tablePd.bus_name?cap_first}Service
 * @ProjectName com.qingfeng
 * @Description: SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class ${tablePd.bus_name?cap_first}Service extends CrudService<${tablePd.bus_name?cap_first}Dao,PageData> {

    @Autowired
    protected ${tablePd.bus_name?cap_first}Dao ${tablePd.bus_name}dao;

    /**
    * @Description: updateStatus
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void updateStatus(PageData pd){
    ${tablePd.bus_name}dao.updateStatus(pd);
    }


<#if tablePd.temp_type == '1'>
    /**
    * @Description: findChildList
    * @Param: [pd]
    * @return: java.util.List<com.qingfeng.util.PageData>
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public List<PageData> findChildList(PageData pd){
        return ${tablePd.bus_name}dao.findChildList(pd);
    }

    /**
    * @Description: saveChild
    * @Param: [pd]
    * @return: int
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public int saveChild(PageData pd){
        return ${tablePd.bus_name}dao.saveChild(pd);
    }

    /**
    * @Description: updateChild
    * @Param: [pd]
    * @return: int
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public int updateChild(PageData pd){
        return ${tablePd.bus_name}dao.updateChild(pd);
    }

    /**
    * @Description: delChild
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void delChild(String[] ids){
        ${tablePd.bus_name}dao.delChild(ids);
    }

    /**
    * @Description: delChildForPIds
    * @Param: [ids]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void delChildForPIds(String[] ids){
        ${tablePd.bus_name}dao.delChildForPIds(ids);
    }
</#if>

    <#if tablePd.temp_type == '2'>
    /**
    * @Description: findContainChildList
    * @Param: [pd]
    * @return: java.util.List<com.qingfeng.util.PageData>
    * @Author: anxingtao
    * @Date: 2020-10-15 23:34
    */
    public List<PageData> findContainChildList(PageData pd){
        return ${tablePd.bus_name}dao.findContainChildList(pd);
    }
    </#if>

}