package com.qingfeng.gencode.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.gencode.dao.MytreeDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title:MytreeService
 * @ProjectName com.qingfeng
 * @Description: SERVICE层
 * @author anxingtao
 * @date 2020-9-22 22:44
 */
@Service
@Transactional
public class MytreeService extends CrudService<MytreeDao,PageData> {

    @Autowired
    protected MytreeDao mytreedao;

    /**
    * @Description: updateStatus
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void updateStatus(PageData pd){
    mytreedao.updateStatus(pd);
    }


    /**
    * @Description: findChildList
    * @Param: [pd]
    * @return: java.util.List<com.qingfeng.util.PageData>
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public List<PageData> findChildList(PageData pd){
        return mytreedao.findChildList(pd);
    }

    /**
    * @Description: saveChild
    * @Param: [pd]
    * @return: int
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public int saveChild(PageData pd){
        return mytreedao.saveChild(pd);
    }

    /**
    * @Description: updateChild
    * @Param: [pd]
    * @return: int
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public int updateChild(PageData pd){
        return mytreedao.updateChild(pd);
    }

    /**
    * @Description: delChild
    * @Param: [pd]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void delChild(String[] ids){
        mytreedao.delChild(ids);
    }

    /**
    * @Description: delChildForPIds
    * @Param: [ids]
    * @return: void
    * @Author: anxingtao
    * @Date: 2020-10-13 11:10
    */
    public void delChildForPIds(String[] ids){
        mytreedao.delChildForPIds(ids);
    }


}