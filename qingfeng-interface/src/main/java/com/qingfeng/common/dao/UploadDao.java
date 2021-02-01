package com.qingfeng.common.dao;

import com.qingfeng.base.dao.CrudDao;
import com.qingfeng.util.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**  
 * @Title: AManageDao
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2018-12-5 16:28
 */
@Mapper
public interface UploadDao extends CrudDao<PageData> {

    /**
     * @Description: saveFile 
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2018-12-6 13:46 
     */ 
    public void saveFile(PageData pd);


    /** 
     * @Description: delFile
     * @Param: [pd] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2018-12-6 13:46 
     */ 
    public void delFile(PageData pd);


    
    /** @MethodName: delFileByObjId
     * @Description: 通过对象id删除相关的图片
     * @Param:
     * @Return: 
     * @Author: zcx
     * @Date: 2019/12/4 
    **/
    public void delFileByObjId(PageData pd);
    /** 
     * @Description: updateFile 
     * @Param: [pd] 
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-12-6 15:08
     */ 
    public void updateFile(PageData pd);

    /** 
     * @Description: findFileList
     * @Param: [pd] 
     * @return: java.util.List<com.wdata.base.util.PageData> 
     * @Author: anxingtao
     * @Date: 2018-12-6 16:33
     */ 
    public List<PageData> findFileList(PageData pd);


    /** 
     * @Description: findFileInfo 
     * @Param: [pd] 
     * @return: com.wdata.base.util.PageData 
     * @Author: anxingtao
     * @Date: 2019-10-10 18:47 
     */ 
    public PageData findFileInfo(PageData pd);



}
