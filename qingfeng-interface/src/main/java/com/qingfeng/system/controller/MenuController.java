package com.qingfeng.system.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.MenuService;
import com.qingfeng.system.service.RoleService;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: MenuController
 * @ProjectName com.qingfeng
 * @Description: 菜单Controller层
 * @author anxingtao
 * @date 2020-9-22 22:45
 */
@Api(tags = "菜单信息接口")
@Controller
@RequestMapping(value = "/system/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;


	/**
	 * @Description: findListPage
	 * @Param: [headers, pd, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-12-26 21:29
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
		List<PageData> list = menuService.findListPage(page);
		int num = menuService.findListSize(page);
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
     * @Param: [headers, pd, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-12-26 21:30
     */
	@ApiOperation("查询数据列表接口")
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
	public void findList(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		if(Verify.verifyIsNotNull(pd.get("types"))){
			List<String> list = Arrays.asList(pd.get("types").toString().split(","));
			pd.put("typeList",list);
		}
    	List<PageData> list = menuService.findList(pd);
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
	 * @Date: 2020-9-22 22:49 
	 */
	@ApiOperation("查询数据详情接口")
	@RequestMapping(value = "/findInfo", method = RequestMethod.GET)
	public void findInfo(@RequestParam String id, HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd.put("id",id);
		PageData p = menuService.findInfo(pd);
//		//查询菜单下的功能菜单
//		pd.put("parent_id",pd.get("id"));
//		pd.put("type","button");
//		List<PageData> list = menuService.findList(pd);

		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setData(p);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/**
	 * @Description: save
	 * @Param: [headers, pd, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-12-26 21:32
	 */
	@ApiOperation("保存或更新数据接口")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws Exception  {
		Json json = new Json();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("name",pd.get("title").toString());
		if(Verify.verifyIsNotNull(pd.get("id"))){
			pd.put("update_time", time);
			pd.put("update_user",user_id);
			menuService.update(pd);
			json.setSuccess(true);
			json.setMsg("数据更新成功。");
		}else{
			//主键id
			String id = GuidUtil.getUuid();
			pd.put("id", id);
			pd.put("create_time", time);
			pd.put("status","0");
			//处理数据权限
			pd.put("create_user",user_id);
			pd.put("create_organize",organize_id);

			pd.put("menu_cascade", pd.get("menu_cascade").toString()+id+"_");
			pd.put("level_num",Integer.parseInt(pd.get("level_num").toString())+1);
			menuService.save(pd);
			json.setMsg("数据添加成功。");
			json.setSuccess(true);
		}
		this.writeJson(response,json);
	}


	/**
	 * @Description: del 
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:50 
	 */
	@ApiOperation("删除数据接口")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(@RequestHeader HttpHeaders headers, @RequestParam String ids, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		String[] del_ids = ids.split(",");
		pd.put("menu_ids", Arrays.asList(del_ids));
		//删除角色菜单
		roleService.delRoleMenu(pd);
		menuService.del(del_ids);
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
	 * @Date: 2020-9-22 22:52
	 */
	@ApiOperation("更新数据状态接口")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public void updateStatus(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("update_time", time);
		menuService.update(pd);

		Json json = new Json();
		json.setSuccess(true);
		json.setFlag("操作成功。");
		this.writeJson(response,json);
	}


	/**
	 * @Description: findMenuList
	 * @Param: [request, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-9-27 21:50
	 */
	@ApiOperation("更新菜单列表接口")
	@RequestMapping(value = "/findMenuList", method = RequestMethod.GET)
	public void findMenuList(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException  {
		PageData pd = new PageData(request);
		PageData mPd = menuService.findInfo(pd);

		PageData user = (PageData) session.getAttribute("loginUser");
		pd.put("user_id",user.get("id"));
		PageData organize = (PageData) session.getAttribute("loginOrganize");
		pd.put("organize_id",organize.get("organize_id"));
		pd.put("menu_cascade",mPd.get("menu_cascade"));
		pd.put("type","menu");
		List<PageData> menuList = menuService.findMenuList(pd);
		Json json = new Json();
		json.setSuccess(true);
		json.setData(menuList);
		json.setFlag("操作成功。");
		this.writeJson(response,json);
	}




}
