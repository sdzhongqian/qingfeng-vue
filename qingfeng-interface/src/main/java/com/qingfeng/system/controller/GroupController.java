package com.qingfeng.system.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.GroupService;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jxls.transformer.XLSTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Title: GroupController
 * @ProjectName com.qingfeng
 * @Description: 用户组Controller层
 * @author anxingtao
 * @date 2020-9-22 22:45
 */
@Api(tags = "用户组信息接口")
@Controller
@RequestMapping(value = "/system/group")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;


	/**
	 * @Description: findListPage 
	 * @Param: [page, request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:48 
	 */
	@ApiOperation("查询数据分页列表接口")
	@RequestMapping(value = "/findListPage", method = RequestMethod.POST)
	public void findListPage(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException {
		Page page = new Page();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		pd.put("auth_user",user_id);
		pd.put("auth_organize",organize_id);
		pd.put("user_id",user_id);
		PageData orgPd = userService.findUserOrganizeInfo(pd);
		if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
			pd.put("auth_organize_ids",Arrays.asList(orgPd.get("authOrgIds").toString().split(",")));
		}else{
			pd.put("auth_organize_ids",new ArrayList<String>());
		}
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
		List<PageData> list = groupService.findListPage(page);
		int num = groupService.findListSize(page);
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
     * @Date: 2020-9-22 22:48 
     */
	@ApiOperation("查询数据列表接口")
	@RequestMapping(value = "/findList", method = RequestMethod.GET)
	public void findList(HttpServletRequest request, HttpServletResponse response) throws IOException  {
    	PageData pd = new PageData(request);

    	List<PageData> list = groupService.findList(pd);
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
	 * @Date: 2020-9-22 22:48 
	 */
	@ApiOperation("查询数据详情接口")
	@RequestMapping(value = "/findInfo", method = RequestMethod.GET)
	public void findInfo(@RequestParam String id, HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd.put("id",id);
		PageData p = groupService.findInfo(pd);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setData(p);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/** 
	 * @Description: saveOrUpdate
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:48 
	 */
	@ApiOperation("保存或更新数据接口")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
		Json json = new Json();
		if(Verify.verifyIsNotNull(pd.get("id"))){
			String id = pd.get("id").toString();
			pd.put("update_time", time);
			pd.put("update_user",user_id);
			int num = groupService.update(pd);
			if(num==1){
				PageData param = new PageData();
				//先执行删除
				param.put("group_id",id);
				groupService.delGroupUser(param);
				//处理组织信息
				String[] user_ids = pd.get("user_ids").toString().split(",");
				String[] user_names = pd.get("user_names").toString().split(",");
				for (int i = 0; i < user_ids.length; i++) {
					param.put("id",GuidUtil.getGuid());
					param.put("user_id",user_ids[i]);
					param.put("create_time", time);
					param.put("create_user",user_id);
					groupService.saveGroupUser(param);
				}
			}
			json.setMsg("数据编辑成功。");
		}else{
			//主键id
			String id = GuidUtil.getUuid();
			pd.put("id", id);
			pd.put("create_time", time);
			pd.put("type","1");
			pd.put("status","0");
			//处理数据权限
			pd.put("create_user",user_id);
			pd.put("create_organize",organize_id);
			int num = groupService.save(pd);
			if(num==1){
				String[] user_ids = pd.get("user_ids").toString().split(",");
				String[] user_names = pd.get("user_names").toString().split(",");
				for (int i = 0; i < user_ids.length; i++) {
					PageData param = new PageData();
					param.put("id",GuidUtil.getGuid());
					param.put("user_id",user_ids[i]);
					param.put("group_id",id);
					param.put("create_time", time);
					param.put("create_user",user_id);
					groupService.saveGroupUser(param);
				}
			}
			json.setMsg("数据添加成功。");
		}
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/** 
	 * @Description: toUpdate 
	 * @Param: [map, request] 
	 * @return: java.lang.String 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:48 
	 */
	@RequestMapping(value = "/toUpdate", method = RequestMethod.GET)
	public void toUpdate(@RequestHeader HttpHeaders headers, @RequestParam String id, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		pd.put("id",id);
		PageData p = groupService.findInfo(pd);
		//查询组用户
		pd.put("group_id",p.get("id"));
		List<PageData> list = groupService.findGroupUser(pd);
		p.put("list",list);
		Json json = new Json();
		json.setData(p);
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}

	/**
	 * @Description: del 
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:49 
	 */
	@ApiOperation("删除数据接口")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(@RequestHeader HttpHeaders headers, @RequestParam String ids, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		String[] del_ids = ids.split(",");
		//删除组用户关联表
		pd.put("group_ids", Arrays.asList(del_ids));
		userService.delUserGroup(pd);
		groupService.del(del_ids);
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
	 * @Date: 2020-9-22 22:49 
	 */
	@ApiOperation("更新数据状态接口")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public void updateStatus(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("update_time", time);
		groupService.update(pd);

		Json json = new Json();
		json.setSuccess(true);
		json.setFlag("操作成功。");
		this.writeJson(response,json);
	}


	/**
	 * @Description: exportData
	 * @Param: [request, response, session]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-9-23 22:59
	 */
	@ApiOperation("导出用户组信息接口")
	@RequestMapping(value = "/exportData", method = RequestMethod.GET)
	public void exportData(@RequestHeader HttpHeaders headers,@RequestParam String short_name,
						   @RequestParam String name,@RequestParam String status,
						   @RequestParam String token,
						   HttpServletResponse response,HttpSession session) throws Exception {
		PageData pd = new PageData();
		pd.put("short_name",short_name);
		pd.put("name",name);
		pd.put("status",status);
		//处理数据权限
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organizeId = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		pd.put("auth_user",user_id);
		pd.put("auth_organize",organizeId);
		pd.put("user_id",user_id);
		PageData orgPd = userService.findUserOrganizeInfo(pd);
		if(Verify.verifyIsNotNull(orgPd)){
			if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
				pd.put("auth_organize_ids", Arrays.asList(orgPd.get("authOrgIds").toString().split(",")));
			}else{
				pd.put("auth_organize_ids",new ArrayList<String>());
			}
		}
		List<PageData> list = groupService.findList(pd);
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("obj", pd);
		beans.put("list", list);
		String tempPath = "";
		String toFile = "";
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"templates";
		tempPath = path+"/excelExport/system_group.xls";
		toFile = path+"/excelExport/temporary/system_group.xls";
		XLSTransformer transformer = new XLSTransformer();
		transformer.transformXLS(tempPath, beans, toFile);
		FileUtil.downFile(response, toFile, "青锋系统用户组基础信息_" + DateTimeUtil.getDateTimeStr() + ".xls");
		File file = new File(toFile);
		file.delete();
		file.deleteOnExit();
	}


}
