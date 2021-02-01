package com.qingfeng.common.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.common.service.GraphicService;
import com.qingfeng.common.service.UploadService;
import com.qingfeng.util.*;
import com.qingfeng.util.upload.ParaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Title: GraphicController
 * @ProjectName wdata
 * @Description: 图文信息Controller
 * @author anxingtao
 * @date 2020-10-8 20:21
 */
@Controller
@RequestMapping(value = "/common/graphic")
public class GraphicController extends BaseController {

	@Autowired
	private GraphicService graphicService;
	@Autowired
	public UploadService uploadService;

	/**
	* @Description: index
	* @Param: [map, request, response]
	* @return: java.lang.String
	* @Author: anxingtao
	* @Date: 2018-9-3 15:00
	*/
	@RequestMapping(value = "/index", method = RequestMethod.GET)
		public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
		PageData pd = new PageData(request);
		map.put("pd",pd);
		return "web/common/graphic/graphic_list";
	}

	/**
	* @Description: findListPage
	* @Param: [page, request, response, session]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:00
	*/
	@RequestMapping(value = "/findListPage", method = RequestMethod.GET)
	public void findListPage(Page page, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		PageData pd = new PageData(request);
		//处理数据权限
		page = dealDataAuth(page,pd,session);
		//处理分页
		if(Verify.verifyIsNotNull(pd.get("page"))){
			page.setIndex(Integer.parseInt(pd.get("page").toString()));
		}else{
			page.setIndex(1);
		}
		if(Verify.verifyIsNotNull(pd.get("limit"))){
			page.setShowCount(Integer.parseInt(pd.get("limit").toString()));
		}else{
			page.setShowCount(10);
		}
		page.setPd(pd);
		List<PageData> list = graphicService.findListPage(page);
		int num = graphicService.findListSize(page);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setCode(0);
		json.setCount(num);
		json.setData(list);
		json.setSuccess(true);
		this.writeJson(response,json);
	}

    /**
    * @Description: findList
    * @Param: [request, response]
    * @return: void
    * @Author: anxingtao
    * @Date: 2018-9-3 15:01
    */
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    public void findList(HttpServletRequest request, HttpServletResponse response) throws IOException  {
    	PageData pd = new PageData(request);

    	List<PageData> list = graphicService.findList(pd);
		for (PageData p:list) {
			pd.put("obj_id",p.get("id"));
			List<PageData> fileList = uploadService.findFileList(pd);
			p.put("fileList",fileList);
		}
		Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setData(list);
        json.setSuccess(true);
        this.writeJson(response,json);
    }

	/**
	* @Description: findInfo
	* @Param: [map, request]
	* @return: java.lang.String
	* @Author: anxingtao
	* @Date: 2018-9-3 15:01
	*/
	@RequestMapping(value = "/findInfo", method = RequestMethod.GET)
	public String findInfo(ModelMap map, HttpServletRequest request)  {
		PageData pd = new PageData(request);
		PageData p = graphicService.findInfo(pd);
		pd.put("obj_id",p.get("id"));
		List<PageData> fileList = uploadService.findFileList(pd);
		map.put("fileList",fileList);
		map.addAttribute("p",p);
		return "web/common/graphic/graphic_info";
	}


	/**
	* @Description: toAdd
	* @Param: [map, request]
	* @return: java.lang.String
	* @Author: anxingtao
	* @Date: 2018-9-3 15:01
	*/
	@RequestMapping(value = "/toAdd", method = RequestMethod.GET)
		public String toAdd(ModelMap map, HttpServletRequest request)  {
		PageData pd = new PageData(request);
		map.put("pd",pd);
		return "web/common/graphic/graphic_add";
	}

	/**
	* @Description: save
	* @Param: [request, response, session]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:01
	*/
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException  {
		PageData pd = new PageData(request);
        //主键id
        pd.put("id", GuidUtil.getUuid());
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("create_time", time);
		pd.put("status","0");
		//处理数据权限
		PageData user = (PageData) session.getAttribute("loginUser");
		PageData organize = (PageData) session.getAttribute("loginOrganize");
		pd.put("create_user",user.get("id"));
		pd.put("create_organize",organize.get("organize_id"));
		graphicService.save(pd);
		//处理附件
		pd.put("obj_id",pd.get("id"));
		String fileId = pd.get("fileId").toString();
		pd.put("id",fileId);
		uploadService.updateFile(pd);

		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}



        /**
	* @Description: toUpdate
	* @Param: [map, request]
	* @return: java.lang.String
	* @Author: anxingtao
	* @Date: 2018-9-3 15:02
	*/
	@RequestMapping(value = "/toUpdate", method = RequestMethod.GET)
	public String toUpdate(ModelMap map, HttpServletRequest request)  {
		PageData pd = new PageData(request);
		PageData p = graphicService.findInfo(pd);
		pd.put("obj_id",p.get("id"));
		List<PageData> fileList = uploadService.findFileList(pd);
		map.put("fileList",fileList);
		map.put("p",p);
		return "web/common/graphic/graphic_update";
	}

	/**
	* @Description: update
	* @Param: [request, response]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:03
	*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException  {
		PageData pd = new PageData(request);

		String time = DateTimeUtil.getDateTimeStr();
		pd.put("create_time", time);
		pd.put("update_time", time);
        PageData user = (PageData) session.getAttribute("loginUser");
        pd.put("update_user",user.get("id"));
		graphicService.update(pd);
		//处理附件
		pd.put("obj_id",pd.get("id"));
		String fileId = pd.get("fileId").toString();
		pd.put("id",fileId);
		uploadService.updateFile(pd);

		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/**
	* @Description: del
	* @Param: [request, response]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:03
	*/
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData(request);
		String[] ids = pd.get("ids").toString().split(",");
		for (String id:ids) {
			pd.put("id",id);
			PageData filePd = uploadService.findFileInfo(pd);
			if(Verify.verifyIsNotNull(filePd)){
				File pathFile = new File(ParaUtil.localName+filePd.getString("file_path"));
				pathFile.delete();
				pathFile.deleteOnExit();
				//删除数据
				uploadService.delFile(pd);
			}
		}
		graphicService.del(ids);
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/**
	* @Description: updateStatus
	* @Param: [request, response]
	* @return: void
	* @Author: anxingtao
	* @Date: 2018-9-3 15:04
	*/
	@RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
	public void updateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData(request);
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("update_time", time);
		graphicService.update(pd);

		Json json = new Json();
		json.setSuccess(true);
		json.setFlag("操作成功。");
		this.writeJson(response,json);
	}


}
