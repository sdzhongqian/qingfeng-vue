package com.qingfeng.common.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.common.service.UploadService;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.util.*;
import com.qingfeng.util.upload.ParaUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**  
 * @Title: UploadController
 * @ProjectName wdata
 * @Description: TODO
 * @author anxingtao
 * @date 2019-3-11 11:08
 */
@Controller
@RequestMapping(value = "/common/upload")
public class UploadController extends BaseController {

    @Autowired
    public UploadService uploadService;


    //=======================================================file 处理================================================

    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public void uploadFile(@RequestHeader HttpHeaders headers, @RequestParam("file") MultipartFile file, HttpServletResponse response , HttpSession session) throws IOException {
        PageData pd = new PageData();
        Json json = new Json();
        System.out.println(file);
        if (null != file) {
            String myFileName = file.getOriginalFilename();// 文件原名称
            String fileSuffix = myFileName.substring(myFileName.lastIndexOf("."));//文件类型
            String fileType = myFileName.substring(myFileName.lastIndexOf(".")+1);//文件后缀

            //保存文件
            String savePath = ParaUtil.localName;
            String path = ParaUtil.common+ DateTimeUtil.getDate()+"/"+ GuidUtil.getGuid()+fileSuffix;
            File files = new File(savePath+path);
            if (!files.getParentFile().exists()){
                files.getParentFile().mkdirs();
            }
            try {

                file.transferTo(files);
                pd.put("id",GuidUtil.getUuid());
                pd.put("name",myFileName);
                pd.put("desnames",myFileName);
                pd.put("file_path",path);
                pd.put("file_type",fileType);
                pd.put("file_size",files.length());
                pd.put("file_suffix",fileSuffix);

                String time = DateTimeUtil.getDateTimeStr();
                pd.put("create_time", time);
                //处理数据权限
                String  token = headers.get("access-token").get(0);
                System.out.println("findUserInfo###########TOKEN:"+token);
                String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
                String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
                pd.put("create_user",user_id);
                pd.put("create_organize",organize_id);
                uploadService.saveFile(pd);
                pd.put("show_file_path", ParaUtil.cloudfile+path);
                json.setData(pd);
                json.setSuccess(true);
                json.setMsg("上传成功");
                json.setFlag(ParaUtil.localName+path);
            } catch (IOException e) {
                json.setSuccess(false);
                json.setMsg("上传失败");
                e.printStackTrace();
            }
        }else{
            json.setSuccess(false);
            json.setMsg("文件为空");
        }
        this.writeJson(response,json);
    }

    /**
     * @Description: delImg
     * @Param: [request, response, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2019-10-10 18:31
     */
    @RequestMapping(value = "/delFile", method = RequestMethod.GET)
    public void delFile(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
        PageData pd = new PageData(request);
        Json json = new Json();
        try {
            //查询信息
            File pathFile = new File(ParaUtil.localName+pd.getString("file_path"));
            pathFile.delete();
            pathFile.deleteOnExit();
            //删除数据
            uploadService.delFile(pd);
            json.setSuccess(true);
            json.setMsg("文件删除成功。");
        } catch (Exception ex) {
            json.setSuccess(false);
            json.setMsg("文件删除失败。");
            ex.printStackTrace();
        }
        this.writeJson(response,json);
    }


    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
        PageData pd = new PageData(request);
        FileUtil.downFile(response,ParaUtil.localName+pd.get("file_path").toString(),pd.get("name").toString());
    }

    /**
     * @Description: uploadthumbnailFile       上传图片并生成缩略图
     * @Param: [request, response, session]
     * @return: void
     * @Author: x
     * @Date: 2020-08-31 13:53
     */
    @RequestMapping(value = "/uploadthumbnailFile", method = RequestMethod.POST)
    public void uploadthumbnailFile(HttpServletRequest request, HttpServletResponse response ,HttpSession session, MultipartFile file) throws Exception {
        PageData pd = new PageData(request);
        String filename = file.getOriginalFilename();// 文件原名称
        String fileSuffix = filename.substring(filename.lastIndexOf("."));//文件类型
        String fileType = filename.substring(filename.lastIndexOf(".")+1);//文件后缀
        //保存文件
//            File directory = new File("");// 参数为空
//            String savePath = directory.getCanonicalPath();
        String savePath = ParaUtil.localName;
        String path = ParaUtil.common+ pd.get("type")+"/" +DateTimeUtil.getDate()+"/"+ GuidUtil.getGuid()+fileSuffix;
        File files = new File(savePath+path);
        if (!files.getParentFile().exists()){
            files.getParentFile().mkdirs();
        }
        file.transferTo(files);
        pd.put("name",filename);
        pd.put("desnames",filename);
        pd.put("file_type",fileType);
        pd.put("file_size",files.length());
        pd.put("file_path",path);
        pd.put("show_path",ParaUtil.cloudfile+path);
        //处理中图middle_path
        String middle_path = ParaUtil.common+ pd.get("type")+"/" +DateTimeUtil.getDate()+"/"+ GuidUtil.getGuid()+fileSuffix;
        File toMiddleFile = new File(savePath+middle_path);
        if (!toMiddleFile.getParentFile().exists()){
            toMiddleFile.getParentFile().mkdirs();
        }
        Thumbnails.of(files)
                .size(500, 500)
                .toFile(toMiddleFile);
        pd.put("middle_path",middle_path);
        pd.put("showmiddle_path",ParaUtil.cloudfile+middle_path);

        Json json = new Json();
        json.setSuccess(true);
        json.setData(pd);
        json.setMsg("上传成功。");
        this.writeJson(response,json);
    }


    //==========================================以下内容暂未用到==============================================================




    /** 
     * @Description: uploadFile 
     * @Param: [request, response, session, file] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2019-3-11 11:04 
     */ 
    @RequestMapping(value = "/uploadOnlyLocalFile", method = RequestMethod.POST)
    public void uploadOnlyLocalFile(HttpServletRequest request, HttpServletResponse response , HttpSession session, MultipartFile file) throws Exception {
        PageData pd = new PageData(request);
        String filename = file.getOriginalFilename();// 文件原名称
        String fileSuffix = filename.substring(filename.lastIndexOf("."));//文件类型
        String fileType = filename.substring(filename.lastIndexOf(".")+1);//文件后缀

        //保存文件
        String savePath = ParaUtil.localName;
        String path = ParaUtil.common+DateTimeUtil.getDate()+"/"+ GuidUtil.getGuid()+fileSuffix;
        File files = new File(savePath+path);
        if (!files.getParentFile().exists()){
            files.getParentFile().mkdirs();
        }
        file.transferTo(files);
        pd.put("name",filename);
        pd.put("desnames",filename);
        pd.put("file_path",path);
        pd.put("file_type",fileType);
        pd.put("file_size",files.length());
        pd.put("show_path",ParaUtil.cloudfile+path);
        Json json = new Json();
        json.setSuccess(true);
        json.setData(pd);
        json.setMsg("上传成功。");
        this.writeJson(response,json);
    }

    /**
     * @Description: delFile
     * @Param: [request, response, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2019-3-11 11:04
     */
    @RequestMapping(value = "/delOnlyLocalFile", method = RequestMethod.GET)
    public void delOnlyLocalFile(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
        PageData pd = new PageData(request);

        String savePath = ParaUtil.localName;
        File files = new File(savePath+pd.get("file_path"));
        files.delete();
        files.deleteOnExit();
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("文件删除成功。");
        this.writeJson(response,json);
    }


    /** 
     * @Description: downloadFile 
     * @Param: [request, response, session] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2019-3-11 11:05 
     */ 
//    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
//    public void downloadFile(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
//        PageData pd = new PageData(request);
////        String savePath = ParaUtil.xmAddress;
//        String savePath= request.getSession().getServletContext().getRealPath("/");
//        System.out.println("##################:"+savePath+pd.get("file_path").toString());
//        FileUtil.downFile(response,savePath+pd.get("file_path").toString(),pd.get("name").toString());
//    }


    /**
     * @Description: uploadFile
     * @Param: [request, response, session, file]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-12-6 13:44
     */
    @ResponseBody
    @RequestMapping(value = "/uploadLocalFile", method = RequestMethod.POST)
    public void uploadLocalFile(HttpServletRequest request, HttpServletResponse response , HttpSession session, MultipartFile file) throws Exception {
        PageData pd = new PageData(request);
        String filename = file.getOriginalFilename();// 文件原名称
        String fileSuffix = filename.substring(filename.lastIndexOf("."));//文件类型
        String fileType = filename.substring(filename.lastIndexOf(".")+1);//文件后缀

        //保存文件
        File directory = new File("");// 参数为空
        String savePath = directory.getCanonicalPath();
        String path = "/upload/commomFile/"+ DateTimeUtil.getDate()+"/"+ GuidUtil.getGuid()+fileSuffix;
        File files = new File(savePath+path);
        if (!files.getParentFile().exists()){
            files.getParentFile().mkdirs();
        }
        file.transferTo(files);
        pd.put("name",filename);
        pd.put("desnames",filename);
        pd.put("file_path",path);
        pd.put("file_type",fileType);
        pd.put("file_size",files.length());
        pd.put("obj_id","");
        pd.put("file_suffix",fileSuffix);
        pd.put("source",pd.get("type"));
        pd.put("order_by","1");

        PageData user = (PageData) session.getAttribute("loginUser");
        PageData organize = (PageData) session.getAttribute("loginOrganize");
        pd.put("create_user",user.get("id"));
        pd.put("create_organize",organize.get("organize_id"));
        pd.put("create_time", DateTimeUtil.getDateTimeStr());
        uploadService.saveFile(pd);

        Json json = new Json();
        json.setSuccess(true);
        json.setData(pd);
        json.setMsg(pd.get("id")+"#"+path+"#"+filename);
        this.writeJson(response,json);
    }


    /**
     * @Description: delFile
     * @Param: [request, response, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-12-6 13:44
     */
    @ResponseBody
    @RequestMapping(value = "/delLocalFile", method = RequestMethod.GET)
    public void delLocalFile(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
        PageData pd = new PageData(request);

        File directory = new File("");// 参数为空
        String savePath = directory.getCanonicalPath();
        File files = new File(savePath+pd.get("file_path"));
        files.delete();
        files.deleteOnExit();
        uploadService.delFile(pd);

        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("文件删除成功。");
        this.writeJson(response,json);
    }


    /**
     * @Description: 删除附件
     * @Param: [pd]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-12-8 10:39
     */
    public void delFiles(PageData pd) throws Exception {
        File directory = new File("");// 参数为空
        String savePath = directory.getCanonicalPath();
        File files = new File(savePath+pd.get("file_path"));
        files.delete();
        files.deleteOnExit();
        uploadService.delFile(pd);
    }


    /**
     * @Description: downloadFile
     * @Param: [request, response, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-12-6 13:44
     */
    @ResponseBody
    @RequestMapping(value = "/downloadLocalFile", method = RequestMethod.GET)
    public void downloadLocalFile(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
        PageData pd = new PageData(request);
        File directory = new File("");// 参数为空
        String savePath = directory.getCanonicalPath();
        FileUtil.downFile(response,savePath+pd.get("file_path").toString(),pd.get("name").toString());
    }


}
