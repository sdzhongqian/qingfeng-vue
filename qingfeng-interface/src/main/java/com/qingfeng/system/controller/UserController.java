package com.qingfeng.system.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.common.service.UploadService;
import com.qingfeng.framework.jwt.constant.SystemConstant;
import com.qingfeng.framework.jwt.entity.CheckResult;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.*;
import com.qingfeng.util.*;
import com.qingfeng.util.upload.ParaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Title: UserController
 * @ProjectName com.qingfeng
 * @Description: 用户Controller层
 * @author anxingtao
 * @date 2020-9-22 22:46
 */
@Api(tags = "用户信息接口")
@Controller
@RequestMapping(value = "/system/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private OrganizeService organizeService;
	@Autowired
	public LoginService loginService;
	@Autowired
	private ThemeService themeService;
	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	public UploadService uploadService;
	@Autowired
	private MenuService menuService;



	/** 
	 * @Description: findListPage 
	 * @Param: [page, request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:51 
	 */
	@ApiOperation("查询用户分页信息列表接口")
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
		if(Verify.verifyIsNotNull(orgPd)){
			if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
				pd.put("auth_organize_ids", Arrays.asList(orgPd.get("authOrgIds").toString().split(",")));
			}else{
				pd.put("auth_organize_ids",new ArrayList<String>());
			}
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
		List<PageData> list = userService.findListPage(page);
		for (PageData p:list) {
			//处理图片
			PageData param = new PageData();
			param.put("obj_id", pd.get("id"));
			List<PageData> fileList = uploadService.findFileList(param);
			p.put("pathUrl", ParaUtil.cloudfile);
			if(fileList.size()>0){
				p.put("imageUrl", ParaUtil.cloudfile+fileList.get(0).get("file_path"));
			}else{
				p.put("imageUrl", "");
			}
		}
		int num = userService.findListSize(page);
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
     * @Date: 2020-9-22 22:52 
     */
	@ApiOperation("查询用户信息列表")
    @RequestMapping(value = "/findList", method = RequestMethod.POST)
    public void findList(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
    	List<PageData> list = userService.findList(pd);
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
	 * @Date: 2020-9-22 22:52 
	 */
	@ApiOperation("查询用户信息详情")
	@RequestMapping(value = "/findInfo", method = RequestMethod.GET)
	public void findInfo(@RequestParam String id, HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd.put("id",id);
		PageData p = userService.findInfo(pd);
		//查询用户组织
		pd.put("user_id",p.get("id"));
		List<PageData> organizeList = userService.findUserOrganize(pd);
		p.put("organizeList",organizeList);
		Json json = new Json();
		json.setData(p);
		json.setMsg("数据获取成功。");
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/**
	 * @Description: save 
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:52 
	 */
	@ApiOperation("保存用户信息")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws Exception  {
		Json json = new Json();//处理数据权限
		String  token = headers.get("access-token").get(0);
		System.out.println("findUserInfo###########TOKEN:"+token);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
		if(Verify.verifyIsNotNull(pd.get("id"))){
			boolean bol = true;
			if(!pd.get("old_login_name").equals(pd.get("login_name"))){
				//判断登录用户名称是否存在
				pd.put("query_login_name",Arrays.asList(pd.get("login_name").toString().split(",")));
				List<PageData> list = userService.findUserList(pd);
				if(list.size()>0){
					json.setSuccess(false);
					json.setMsg("用户【"+pd.get("login_name").toString()+"】已经存在！");
					bol = false;
				}
			}
			if(bol){
				String id = pd.get("id").toString();
				pd.put("update_time", time);
				pd.put("update_user",user_id);
				userService.update(pd);
				//处理附件
				if(Verify.verifyIsNotNull(pd.get("fileIds"))){
					pd.put("obj_id", id);
					String fileIds[] = pd.get("fileIds").toString().split(",");
					for (int j = 0; j < fileIds.length; j++) {
						pd.put("id", fileIds[j]);
						uploadService.updateFile(pd);
					}
				}
				json.setSuccess(true);
				json.setMsg("操作成功。");
			}
		}else{
			//判断登录用户名称是否存在
			pd.put("query_login_name",Arrays.asList(pd.get("login_name").toString().split(",")));
			List<PageData> list = userService.findUserList(pd);
			if(list.size()>0){
				json.setSuccess(false);
				json.setMsg("用户【"+pd.get("login_name").toString()+"】已经存在！");
			}else{
				//主键id
				String id = GuidUtil.getUuid();
				pd.put("id", id);
				pd.put("create_time", time);
				pd.put("update_time", time);
				pd.put("status","0");
				pd.put("type","1");
				pd.put("pwd_error_num","0");
				pd.put("theme_id","1");
				pd.put("login_password",PasswordUtil.encrypt(pd.get("login_password").toString(), pd.get("login_name").toString()));
				//处理数据权限
				pd.put("create_user",user_id);
				pd.put("create_organize",organize_id);

				int num = userService.save(pd);
				if(num==1){
					//处理组织信息
					PageData orgPd = new PageData();
					orgPd.put("id",GuidUtil.getGuid());
					orgPd.put("user_id",id);
					orgPd.put("type","0");
					orgPd.put("use_status","0");
					orgPd.put("organize_id",pd.get("organize_id"));
					orgPd.put("organize_name",pd.get("organize_name"));
					orgPd.put("position",pd.get("position"));
					orgPd.put("order_by","1");
					orgPd.put("create_user",user_id);
					orgPd.put("create_time", time);
					userService.saveUserOrganize(orgPd);

					//处理附件
					if(Verify.verifyIsNotNull(pd.get("fileIds"))){
						pd.put("obj_id", id);
						String fileIds[] = pd.get("fileIds").toString().split(",");
						for (int j = 0; j < fileIds.length; j++) {
							pd.put("id", fileIds[j]);
							uploadService.updateFile(pd);
						}
					}
				}
				json.setSuccess(true);
				json.setMsg("操作成功。");
			}
		}
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/**
	 * @Description: del 
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:52 
	 */
	@ApiOperation("删除用户信息")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(@RequestHeader HttpHeaders headers, @RequestParam String ids, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		String[] del_ids = ids.split(",");
		pd.put("user_ids", Arrays.asList(del_ids));
		//删除关联组织
		userService.delUserOrganize(pd);
		//删除组用户关联表
		userService.delUserGroup(pd);
		//删除用户角色关联
		userService.delUserRole(pd);
		userService.del(del_ids);
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
	@ApiOperation("更新用户状态信息")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public void updateStatus(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("update_time", time);
		userService.update(pd);

		Json json = new Json();
		json.setSuccess(true);
		json.setFlag("操作成功。");
		this.writeJson(response,json);
	}


	/**
	 * @Description: updatePwd 
	 * @Param: [request, response, session]
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-23 17:48 
	 */
	@ApiOperation("更新用户密码信息")
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public void updatePwd(@RequestHeader HttpHeaders headers,@RequestBody PageData pd,HttpServletResponse response) throws Exception  {
		Json json = new Json();
		String[] ids = pd.get("ids").toString().split(",");
		String[] names = pd.get("names").toString().split(",");
		String time = DateTimeUtil.getDateTimeStr();
		for (int i = 0; i < ids.length; i++) {
			pd.put("id",ids[i]);
			pd.put("login_password",PasswordUtil.encrypt(pd.get("login_password").toString(), names[i]));
			pd.put("update_time",time);
			userService.update(pd);
		}
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: exportData 导出 
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-23 21:57
	 */
	@ApiOperation("导出用户信息")
	@RequestMapping(value = "/exportData", method = RequestMethod.GET)
	public void exportData(@RequestHeader HttpHeaders headers,@RequestParam String login_name,
						   @RequestParam String name,@RequestParam String status,
						   @RequestParam String organize_id,@RequestParam String token,
						   HttpServletResponse response,HttpSession session) throws Exception {
		PageData pd = new PageData();
		pd.put("login_name",login_name);
		pd.put("name",name);
		pd.put("status",status);
		pd.put("organize_id",organize_id);
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
		List<PageData> list = userService.findList(pd);
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("obj", pd);
		beans.put("list", list);
		String tempPath = "";
		String toFile = "";
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"templates";
		tempPath = path+"/excelExport/system_user.xls";
		toFile = path+"/excelExport/temporary/system_user.xls";
		XLSTransformer transformer = new XLSTransformer();
		transformer.transformXLS(tempPath, beans, toFile);
		FileUtil.downFile(response, toFile, "青锋系统用户基础信息_" + DateTimeUtil.getDateTimeStr() + ".xls");
		File file = new File(toFile);
		file.delete();
		file.deleteOnExit();
	}


	/**
	 * @Description: findMyOrganizeListPage 查询用户组织分页信息列表
	 * @Param: [headers, pd, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-12-20 21:47
	 */
	@ApiOperation("查询用户组织分页信息列表")
	@RequestMapping(value = "/findMyOrganizeListPage", method = RequestMethod.POST)
	public void findMyOrganizeListPage(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException {
		Page page = new Page();
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
		List<PageData> list = userService.findMyOrganizeListPage(page);
		int num = userService.findMyOrganizeListSize(page);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setCount(num);
		json.setData(list);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/** 
	 * @Description: saveOrUpdateUserOrganize 保存用户组织信息
	 * @Param: [headers, pd, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-12-21 0:03 
	 */ 
	@ApiOperation("保存用户组织信息")
	@RequestMapping(value = "/saveOrUpdateUserOrganize", method = RequestMethod.POST)
	public void saveOrUpdateUserOrganize(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws Exception  {
		Json json = new Json();//处理数据权限
		String  token = headers.get("access-token").get(0);
		System.out.println("findUserInfo###########TOKEN:"+token);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
		if(Verify.verifyIsNotNull(pd.get("id"))){
			//处理组织信息
			PageData orgPd = new PageData();
			orgPd.put("id",pd.get("id"));
			orgPd.put("user_id",pd.get("user_id"));
			orgPd.put("type",pd.get("type"));
			orgPd.put("use_status","0");
			orgPd.put("organize_id",pd.get("organize_id"));
			orgPd.put("organize_name",pd.get("organize_name"));
			orgPd.put("position",pd.get("position"));
			orgPd.put("order_by",pd.get("order_by"));
			orgPd.put("create_user",user_id);
			orgPd.put("create_time", time);
			userService.updateUserOrganize(orgPd);
			json.setSuccess(true);
			json.setMsg("操作成功。");
		}else{
			//处理组织信息
			PageData orgPd = new PageData();
			orgPd.put("id",GuidUtil.getGuid());
			orgPd.put("user_id",pd.get("user_id"));
			orgPd.put("type",pd.get("type"));
			orgPd.put("use_status","0");
			orgPd.put("organize_id",pd.get("organize_id"));
			orgPd.put("organize_name",pd.get("organize_name"));
			orgPd.put("position",pd.get("position"));
			orgPd.put("order_by",pd.get("order_by"));
			orgPd.put("create_user",user_id);
			orgPd.put("create_time", time);
			userService.saveUserOrganize(orgPd);
			json.setSuccess(true);
			json.setMsg("操作成功。");
		}
		this.writeJson(response,json);
	}

	/**
	 * @Description: delUserOrganize 
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-25 15:33 
	 */
	@ApiOperation("删除用户组织信息")
	@RequestMapping(value = "/delUserOrganize", method = RequestMethod.GET)
	public void delUserOrganize(@RequestHeader HttpHeaders headers,@RequestParam String id, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		pd.put("id",id);
		userService.delUserOrganize(pd);
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	@RequestMapping(value = "/findRoleAuth", method = RequestMethod.POST)
	public void findRoleAuth(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws Exception {
		pd.put("user_id",pd.get("id"));
		List<PageData> roleLs = roleService.findSimpleList(pd);
		List<PageData> myRoleLs = userService.findUserRoleList(pd);
		List<PageData> orgList = userService.findUserOrganize(pd);
		pd.put("roleLs",roleLs);
		pd.put("myRoleLs",myRoleLs);
		pd.put("orgList",orgList);
		Json json = new Json();
		json.setData(pd);
		json.setSuccess(true);
		json.setMsg("数据获取成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: updateAuth
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-26 16:40 
	 */
	@ApiOperation("更新用户权限信息")
	@RequestMapping(value = "/updateAuth", method = RequestMethod.POST)
	public void updateAuth(@RequestHeader HttpHeaders headers, @RequestBody PageData pd,HttpServletResponse response,HttpSession session) throws IOException  {
		Json json = new Json();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String create_user = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		try {
			String time = DateTimeUtil.getDateTimeStr();
			String[] role_ids = pd.get("role_ids").toString().split(",");
			//删除用户角色表。
			pd.put("user_id",pd.get("id"));
			pd.put("role_ids", Arrays.asList(role_ids));
			userService.delUserRole(pd);
			if(Verify.verifyIsNotNull(pd.get("role_ids"))){
				String user_id = pd.get("id").toString();
				List<PageData> list = new ArrayList<PageData>();
				//执行保存
				for (int i = 0; i < role_ids.length; i++) {
					PageData p = new PageData();
					//主键id
					p.put("id",GuidUtil.getUuid());
					p.put("role_id",role_ids[i]);
					p.put("user_id",user_id);
					p.put("create_time",time);
					p.put("create_user",create_user);
					p.put("update_time",time);
					list.add(p);
				}
				userService.saveUserRole(list);
			}
			//处理数据权限
			String[] showAuthData = pd.get("showAuthData").toString().split(",");
			String[] operaAuthData = pd.get("operaAuthData").toString().split(",");
			StringBuilder authOrgIds = new StringBuilder();
			StringBuilder authOrgCascade = new StringBuilder();
			StringBuilder authParams = new StringBuilder();
			if(Verify.verifyIsNotNull(pd.get("showAuthData"))){
				System.out.println("#################################");
				for (int i = 0; i < showAuthData.length; i++) {
					System.out.println(showAuthData[i]);
					String showAuth[] = showAuthData[i].toString().split(":");
					authOrgIds.append(showAuth[0]).append(",");
					authOrgCascade.append(showAuth[1]).append(",");
					if(ArrayUtils.contains(operaAuthData,showAuthData[i])){
						authParams.append(showAuth[0]).append(":Y").append(",");
					}else{
						authParams.append(showAuth[0]).append(":N").append(",");
					}
				}
				if(authOrgIds.length()>0){
					pd.put("authOrgIds",authOrgIds.substring(0,authOrgIds.length()-1));
					pd.put("authOrgCascade",authOrgCascade.substring(0,authOrgCascade.length()-1));
					pd.put("authParams",authParams.substring(0,authParams.length()-1));
				}else{
					pd.put("authOrgIds","");
					pd.put("authOrgCascade","");
					pd.put("authParams","");
				}
				userService.updateAuthForParam(pd);
			}else{
				pd.put("authOrgIds","");
				pd.put("authOrgCascade","");
				pd.put("authParams","");
				userService.updateAuthForParam(pd);
			}
			json.setSuccess(true);
			json.setMsg("操作成功。");
			txManager.commit(status);
		} catch (Exception ex) {
			json.setSuccess(false);
			json.setMsg("操作异常，请联系管理员");
			txManager.rollback(status);
			ex.printStackTrace();
		}
		this.writeJson(response,json);
	}

	/**
	 * @Description: findOrganizeAuth
	 * @Param: [request, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-9-26 17:40
	 */
	@ApiOperation("查询组织权限信息")
	@RequestMapping(value = "/findOrganizeAuth", method = RequestMethod.POST)
	public void findOrganizeAuth(@RequestHeader HttpHeaders headers,@RequestBody PageData pd,HttpServletResponse response) throws IOException {
		//处理数据权限
		String token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		PageData param = new PageData();
		param.put("id", user_id);
		PageData user = userService.findInfo(param);
		param.put("id", organize_id);
		PageData organize = organizeService.findInfo(param);
		if (user.get("type").equals("0")) {//管理员
			pd.put("org_cascade", "org");
		} else {
			pd.put("org_cascade", organize.get("org_cascade"));
		}
		List<PageData> list = organizeService.findTreeTableList(pd);
		//查询用户的数据权限数据
		pd.put("user_id", pd.get("id"));
		PageData p = userService.findUserOrganizeInfo(pd);
		if (Verify.verifyIsNotNull(p.get("authOrgIds"))) {
			String[] authOrgIds = p.get("authOrgIds").toString().split(",");
			String[] authOrgCascade = p.get("authOrgCascade").toString().split(",");
			String[] authParams = p.get("authParams").toString().split(",");
			StringBuilder showAuthData = new StringBuilder();
			StringBuilder operaAuthData = new StringBuilder();
			for (int i = 0; i < authOrgIds.length; i++) {
				showAuthData.append(authOrgIds[i] + ":" + authOrgCascade[i]).append(",");
				if (authParams[i].contains("Y")) {
					operaAuthData.append(authOrgIds[i] + ":" + authOrgCascade[i]).append(",");
				}
			}
			if (showAuthData.length() > 0) {
				p.put("showAuthData", showAuthData.substring(0, showAuthData.length() - 1));
			} else {
				p.put("showAuthData", "");
			}
			if (operaAuthData.length() > 0) {
				p.put("operaAuthData", operaAuthData.substring(0, operaAuthData.length() - 1));
			} else {
				p.put("operaAuthData", "");
			}
		}else{
			p.put("showAuthData", "");
			p.put("operaAuthData", "");
		}
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setData(list);
		json.setObject(p);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/** 
	 * @Description: findUserOrganizeInfo
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-26 17:45 
	 */
	@ApiOperation("查询用户组织详情")
	@RequestMapping(value = "/findUserOrganizeInfo", method = RequestMethod.GET)
	public void findUserOrganizeInfo(HttpServletRequest request, HttpServletResponse response) throws Exception  {
		PageData pd = new PageData(request);

		PageData p = userService.findUserOrganizeInfo(pd);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setData(p);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	//=====================
	/**
	 * @Description: findUserInfo
	 * @Param: [token, request, response, session]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-11-26 21:33
	 */
	@ApiOperation("登录-查询用户信息及权限信息")
	@RequestMapping(value = "/findUserInfo", method = RequestMethod.GET)
	public void findUserInfo(@RequestHeader HttpHeaders headers, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		PageData pd = new PageData(request);
		String  token = headers.get("access-token").get(0);
		pd.put("login_id",JwtUtils.validateJWT(token).getClaims().getId().split(":")[0]);
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		PageData uPd = loginService.findUserInfo(pd);
		PageData rolePd = new PageData();
//        rolePd.put("id","admin");
//        rolePd.put("name","管理部");
//        rolePd.put("describe","'拥有所有权限'");
//        rolePd.put("status","1");
//        rolePd.put("creatorId","'system'");
//        rolePd.put("createTime","1497160610259");
//        rolePd.put("deleted","0");

//        PageData permissionsPd1 = new PageData();
//        permissionsPd1.put("roleId","admin");
//        permissionsPd1.put("permissionId","dashboard");
//        permissionsPd1.put("permissionName","仪表盘");
//        permissionsPd1.put("roleId","admin");
//
//        //权限
//        PageData action1 = new PageData();
//        action1.put("action","add");
//        action1.put("describe","新增");
//        action1.put("defaultCheck",false);
//        PageData action2 = new PageData();
//        action2.put("action","add");
//        action2.put("describe","新增");
//        action2.put("defaultCheck",false);
//        PageData action3 = new PageData();
//        action3.put("action","add");
//        action3.put("describe","新增");
//        action3.put("defaultCheck",false);
//        List<PageData> list = new ArrayList<PageData>();
//        list.add(action1);
//        list.add(action2);
//        list.add(action3);
//
//        permissionsPd1.put("actionEntitySet",list);
//        List<PageData> permList = new ArrayList<PageData>();
//        permList.add(permissionsPd1);
//
//
//        PageData permissionsPd2 = new PageData();
//        permissionsPd2.put("roleId","admin");
//        permissionsPd2.put("permissionId","system");
//        permissionsPd2.put("permissionName","系统管理");
//        permissionsPd2.put("roleId","admin");
//
//        permList.add(permissionsPd2);

		//查询当前用户拥有的角色信息
		pd.put("user_id",pd.get("login_id"));
		pd.put("organize_id",organize_id);
		List<PageData> userRoles = userService.findUserRoleList(pd);
		rolePd.put("permissions",userRoles);
		//查询当前组织信息
		PageData orgPd = userService.findUserOrganizeInfo(pd);
		uPd.put("role",rolePd);
		uPd.put("orgPd",orgPd);
		//查询当前用户拥有的权限信息
		List<PageData> list = menuService.findAuthMenuList(pd);
		uPd.put("authList",list);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setCode(200);
		json.setData(uPd);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/**
	 * @Description: findInfo
	 * @Param: [id, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-12-28 23:04
	 */
	@ApiOperation("查询登录用户信息详情")
	@RequestMapping(value = "/findLoginUser", method = RequestMethod.GET)
	public void findLoginUser(@RequestHeader HttpHeaders headers,HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
//        String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		pd.put("id",user_id);
		PageData p = userService.findInfo(pd);
		//处理图片
		PageData param = new PageData();
		param.put("obj_id", pd.get("id"));
		List<PageData> fileList = uploadService.findFileList(param);
		p.put("pathUrl", ParaUtil.cloudfile);
		if(fileList.size()>0){
			p.put("imageUrl", ParaUtil.cloudfile+fileList.get(0).get("file_path"));
		}else{
			p.put("imageUrl", "");
		}
		//查询用户组织
		pd.put("user_id",p.get("id"));
		List<PageData> organizeList = userService.findUserOrganize(pd);
		p.put("organizeList",organizeList);
		Json json = new Json();
		json.setData(p);
		json.setMsg("数据获取成功。");
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/**
	 * @Description: updateUser
	 * @Param: [headers, pd, response]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-12-28 23:21
	 */
	@ApiOperation("更新用户信息")
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public void updateUser(@RequestHeader HttpHeaders headers, @RequestBody PageData pd,HttpServletResponse response) throws IOException  {
		Json json = new Json();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
//        String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("update_time", time);
		pd.put("update_user",user_id);
		userService.update(pd);
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/**
	 * @Description: updateMyPwd 更新个人密码重置
	 * @Param: [request, response, session]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-9-28 16:24
	 */
	@ApiOperation("更新个人密码接口")
	@RequestMapping(value = "/updateMyPwd", method = RequestMethod.POST)
	public void updateMyPwd(@RequestHeader HttpHeaders headers, @RequestBody PageData pd,HttpServletResponse response) throws Exception  {
		Json json = new Json();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
//        String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		pd.put("id",user_id);
		PageData user = userService.findInfo(pd);
		if(PasswordUtil.encrypt(pd.get("old_password").toString(), user.get("login_name").toString()).equals(user.get("login_password").toString())){
			String time = DateTimeUtil.getDateTimeStr();
			pd.put("login_password",PasswordUtil.encrypt(pd.get("login_password").toString(), user.get("login_name").toString()));
			pd.put("update_time",time);
			userService.update(pd);
			json.setSuccess(true);
			json.setMsg("密码重置成功。");
		}else{
			json.setSuccess(false);
			json.setMsg("旧密码不正确，请重新输入。");
		}
		this.writeJson(response,json);
	}



	/**
	 * @Description: updateSwitchOrganize
	 * @Param: [request, response, session]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-9-28 17:03
	 */
	@ApiOperation("更新切换组织接口")
	@RequestMapping(value = "/updateSwitchOrganize", method = RequestMethod.POST)
	public void updateSwitchOrganize(@RequestHeader HttpHeaders headers, @RequestBody PageData pd,HttpServletResponse response) throws IOException  {
		Json json = new Json();
		String vueToken = "";
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		if(!pd.get("organize_id").equals(organize_id)){
			//将所有的个人信息use_status更新为：1
			PageData param = new PageData();
			param.put("user_id",user_id);
			param.put("use_status","1");
			param.put("update_time",DateTimeUtil.getDateTimeStr());
			userService.updateUserOrgUseStatus(param);
			param.put("use_status","0");
			param.put("organize_id",pd.get("organize_id"));
			userService.updateUserOrgUseStatus(param);
			//更新当前组织session信息
			pd.put("id",user_id);
			PageData uPd = userService.findInfo(pd);
			//把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
			vueToken = JwtUtils.createJWT(user_id+":"+pd.get("organize_id"), uPd.getString("login_name"), SystemConstant.JWT_TTL);
		}
		json.setSuccess(true);
		json.setToken(vueToken);
		json.setMsg("切换成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: validateJWT 验证jwt是否失效
	 * @Param: [headers, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2021-1-2 9:17 
	 */ 
	@RequestMapping(value = "/validateJWT", method = RequestMethod.GET)
	public void validateJWT(@RequestHeader HttpHeaders headers,HttpServletResponse response) throws IOException  {
		Json json = new Json();
		PageData pd = new PageData();
		String vueToken = "";
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		CheckResult checkResult = JwtUtils.validateJWT(token);
		System.out.println(checkResult.getErrCode());
		System.out.println(checkResult.isSuccess());
		System.out.println(JsonToMap.bean2json(checkResult));
		if(checkResult.isSuccess()){
			json.setSuccess(true);
			json.setCode(200);
			json.setMsg("验证成功。");
			//更新token记录时间
			String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
			pd.put("id",user_id);
			pd.put("token_refresh_time",DateTimeUtil.getDateTimeStr());
			pd.put("update_time",DateTimeUtil.getDateTimeStr());
			userService.update(pd);
		}else{
			//根据token 查询用户信息
			pd.put("token",token);
			PageData uPd = loginService.findUserInfo(pd);
			if(Verify.verifyIsNotNull(uPd)){
				pd.put("user_id",uPd.get("id"));
				PageData orgPd = userService.findUserOrganizeInfo(pd);
				vueToken = JwtUtils.createJWT(uPd.get("id").toString()+":"+orgPd.get("organize_id").toString(), uPd.getString("login_name"), SystemConstant.JWT_TTL);
				json.setToken(vueToken);
				json.setCode(checkResult.getErrCode());
			}else{
				json.setCode(SystemConstant.JWT_ERRCODE_TIMEOUT);
			}
			json.setData(checkResult);
			json.setMsg("token失效。");
			json.setSuccess(false);
		}
		this.writeJson(response,json);
	}


}
