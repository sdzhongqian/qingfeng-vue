package com.qingfeng.common.service;

import com.qingfeng.base.service.CrudService;
import com.qingfeng.common.dao.UploadDao;
import com.qingfeng.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**  
 * @Title: AManageService
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2018-12-5 16:28
 */
@Service
@Transactional
public class UploadService extends CrudService<UploadDao, PageData> {

    @Autowired
    protected UploadDao uploadDao;


    /** 
     * @Description: saveFile 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2018-12-6 13:45 
     */ 
    public void saveFile(PageData pd){
        uploadDao.saveFile(pd);
    }

    /** 
     * @Description: delFile
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2018-12-6 13:45 
     */ 
    public void delFile(PageData pd){
        uploadDao.delFile(pd);
    }

    /**
     * @Description: delFileByObjId
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-10-8 22:50
     */
    public void delFileByObjId(PageData pd){
        uploadDao.delFileByObjId(pd);
    }

    /** 
     * @Description: updateFile
     * @Param: [] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2018-12-6 14:06 
     */ 
    public void updateFile(PageData pd){
        uploadDao.updateFile(pd);
    }


    /** 
     * @Description: findFileList
     * @Param: [pd] 
     * @return: java.util.List<com.wdata.base.util.PageData> 
     * @Author: anxingtao
     * @Date: 2018-12-6 16:33
     */ 
    public List<PageData> findFileList(PageData pd){
        return uploadDao.findFileList(pd);
    }
    
    
    /** 
     * @Description: findFileInfo 
     * @Param: [pd] 
     * @return: com.wdata.base.util.PageData 
     * @Author: anxingtao
     * @Date: 2019-10-10 18:47 
     */ 
    public PageData findFileInfo(PageData pd){
        return uploadDao.findFileInfo(pd);
    }

}
