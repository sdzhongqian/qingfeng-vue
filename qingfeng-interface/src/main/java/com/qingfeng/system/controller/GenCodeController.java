package com.qingfeng.system.controller;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfeng.base.controller.BaseController;
import com.qingfeng.base.model.CommonConfig;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.GenCodeService;
import com.qingfeng.system.service.MenuService;
import com.qingfeng.util.*;
import com.qingfeng.util.freemarker.FreemarkerParaUtil;
import com.qingfeng.util.zip.ZipUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @Title: GenCodeController
 * @ProjectName wdata
 * @Description: 代码生成
 * @author anxingtao
 * @date 2020-10-9 13:13
 */
@Api(tags = "代码生成信息接口")
@Controller
@RequestMapping(value = "/system/gencode")
public class GenCodeController extends BaseController {

	@Autowired
	private GenCodeService genCodeService;
	@Autowired
	private CommonConfig commonConfig;
	@Autowired
	private MenuService menuService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	private static String table_schema = "qingfeng_vue";


	/**
	 * @Description: findTableListPage
	 * @Param: [page, request, response, session]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-10-9 15:22
	 */
	@ApiOperation("查询数据表分页信息接口")
	@RequestMapping(value = "/findTableListPage", method = RequestMethod.POST)
	public void findTableListPage(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException {
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
		pd.put("table_schema",table_schema);
		page.setPd(pd);
		List<PageData> list = genCodeService.findTableListPage(page);
		int num = genCodeService.findTableListSize(page);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setCode(0);
		json.setCount(num);
		json.setData(list);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/**
	 * @Description: save
	 * @Param: [request, response, session]
	 * @return: void
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:46
	 */
	@ApiOperation("保存代码生成基础数据接口")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws Exception  {
		//初始化数据表
		System.out.println("#############:"+pd.toString());
		pd.put("table_schema",table_schema);
		pd.put("table_names", Arrays.asList(pd.get("table_names").toString().split(",")));
		List<PageData> list = genCodeService.findTableList(pd);
		String time = DateTimeUtil.getDateTimeStr();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		if(list.size()>0){
			for (PageData param:list) {
				String[] table_name = param.get("table_name").toString().split("_");
				String id = GuidUtil.getUuid();
				param.put("id",id);
				param.put("type","0");
				param.put("temp_type","0");//单表
				param.put("pack_path","com.qingfeng");
				param.put("mod_name",table_name[0]);
				String bus_name = "";
				if(table_name.length>1){
					for (int i = 1; i < table_name.length; i++) {
						if(i==1){
							bus_name+= table_name[1];
						}else{
							bus_name+= StrUtil.upperFirst(table_name[i]);
						}
					}
				}else{
					bus_name = table_name[0];
				}
				param.put("tree_id",id);
				param.put("bus_name",bus_name);
				param.put("menu_name",param.get("table_comment"));
				param.put("gen_type","0");
				param.put("gen_path",PathUtil.getSystemPath());
				param.put("more_add","0");
				param.put("status_type","0");
				param.put("order_by","1");
				param.put("create_time", time);
				param.put("create_user",user_id);
				param.put("create_organize",organize_id);

				//生成数据字段表
				pd.put("table_schema",table_schema);
				pd.put("table_name",param.get("table_name"));
				List<PageData> fieldList = genCodeService.findColumndList(pd);
				if(fieldList.size()>0){
					for (PageData fieldParam:fieldList) {
						fieldParam.put("id",GuidUtil.getUuid());
						fieldParam.put("type","0");
						fieldParam.put("table_id",id);
						fieldParam.put("field_name",fieldParam.get("column_name"));
						fieldParam.put("field_comment",fieldParam.get("column_comment"));
						fieldParam.put("field_type",fieldParam.get("data_type"));
						if(commonConfig.getGencodeField().contains(fieldParam.get("column_name").toString())){
							fieldParam.put("field_operat","N");
							fieldParam.put("field_list","N");
						}else{
							fieldParam.put("field_operat","Y");
							fieldParam.put("field_list","Y");
						}
						if(fieldParam.get("is_nullable").equals("NO")){
							fieldParam.put("verify_rule","required");
						}else{
							fieldParam.put("verify_rule","");
						}
						fieldParam.put("field_query","N");

						fieldParam.put("query_type","");
						fieldParam.put("show_type","1");
						fieldParam.put("order_by",fieldParam.get("ordinal_position"));
						fieldParam.put("remark",fieldParam.get("column_type"));
						fieldParam.put("create_time", time);
						fieldParam.put("create_user",user_id);
						fieldParam.put("create_organize",organize_id);
					}
				}
				genCodeService.saveField(fieldList);
			}
		}
		genCodeService.saveTable(list);
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: findListPage 
	 * @Param: [page, request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:46 
	 */
	@ApiOperation("查询数据分页列表接口")
	@RequestMapping(value = "/findListPage", method = RequestMethod.POST)
	public void findListPage(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException {
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
		List<PageData> list = genCodeService.findListPage(page);
		for (PageData p:list) {
			pd.put("table_id",p.get("id"));
			pd.put("excludeField",Arrays.asList(commonConfig.getGencodeField().split(",")));
			List<PageData> fieldList = genCodeService.findFieldList(pd);
			p.put("fieldList",fieldList);
			p.put("excludeField",commonConfig.getGencodeField());
		}
		int num = genCodeService.findListSize(page);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setCode(0);
		json.setCount(num);
		json.setData(list);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/** 
	 * @Description: findInfo 
	 * @Param: [map, request] 
	 * @return: java.lang.String 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:46 
	 */
	@ApiOperation("查询数据详情接口")
	@RequestMapping(value = "/findInfo", method = RequestMethod.GET)
	public void findInfo(@RequestParam String id, HttpServletResponse response) throws Exception {
		PageData pd = new PageData();
		pd.put("id",id);
		PageData p = genCodeService.findInfo(pd);
		pd.put("table_id",p.get("id"));
		pd.put("excludeField",Arrays.asList(commonConfig.getGencodeField().split(",")));
		List<PageData> fieldList = genCodeService.findFieldList(pd);
		p.put("fieldList",fieldList);
		Json json = new Json();
		json.setMsg("获取数据成功。");
		json.setData(p);
		json.setSuccess(true);
		this.writeJson(response,json);
	}


	/** 
	 * @Description: update 
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:47 
	 */
	@ApiOperation("更新数据信息接口")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws Exception  {
		Json json = new Json();
		System.out.println("###:"+pd.toString());
		String time = DateTimeUtil.getDateTimeStr();
		pd.put("update_time", time);
		String  token = headers.get("access-token").get(0);
		System.out.println("findUserInfo###########TOKEN:"+token);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
        pd.put("update_user",user_id);
		genCodeService.update(pd);
		//更新子表信息
		List<PageData> fieldList = new Gson().fromJson(new Gson().toJson(pd.get("fieldList")), new TypeToken<List<PageData>>(){}.getType());
		for (PageData p:fieldList) {
			if(p.get("field_operat").equals(true)){
				p.put("field_operat","Y");
			}else{
				p.put("field_operat","N");
			}
			if(p.get("field_list").equals(true)){
				p.put("field_list","Y");
			}else{
				p.put("field_list","N");
			}
			if(p.get("field_query").equals(true)){
				p.put("field_query","Y");
			}else{
				p.put("field_query","N");
			}
			p.put("update_time", time);
			p.put("update_user",user_id);
			genCodeService.updateField(p);
		}
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: del 
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:47 
	 */
	@ApiOperation("删除数据接口")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(@RequestHeader HttpHeaders headers, @RequestParam String ids, HttpServletResponse response) throws IOException  {
		PageData pd = new PageData();
		String[] del_ids = ids.split(",");
		genCodeService.delField(del_ids);
		genCodeService.del(del_ids);
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: gencode
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-10-12 22:21 
	 */
	@ApiOperation("代码生成接口")
	@RequestMapping(value = "/gencode", method = RequestMethod.GET)
	public void gencode(@RequestHeader HttpHeaders headers, @RequestParam String id, HttpServletResponse response) throws Exception  {
		PageData pd = new PageData();
		pd.put("id",id);
		PageData tablePd = genCodeService.findInfo(pd);
		pd.put("table_id",tablePd.get("id"));
		List<PageData> fieldList = genCodeService.findFieldList(pd);
		if(tablePd.get("temp_type").toString().equals("0")){//单表
		}else if(tablePd.get("temp_type").toString().equals("1")){//树表
			Iterator<PageData> itLs=fieldList.iterator();
			while(itLs.hasNext()){
				PageData p=itLs.next();
				if(p.get("field_name").equals(tablePd.get("tree_pid"))){
					itLs.remove();
				}
			}
		}
		//1、创建数据模型
		Map<String,Object> root = new HashMap<String,Object>();
		//2、为数据模型添加值
		root.put("pd", pd);
		root.put("tablePd",tablePd);
		root.put("fieldList",fieldList);

		String gen_path = PathUtil.getSystemPath();
		if(Verify.verifyIsNotNull(tablePd.get("gen_path").toString())){
			gen_path = tablePd.get("gen_path").toString();
		}
		gen_path = gen_path+"gencode/";
		//生成Mapper.xml
		fprint("gencode/TemplateMapper.xml.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.mapper+StrUtil.upperFirst(tablePd.get("bus_name").toString())+"Mapper.xml");
		//生成Dao
		fprint("gencode/TemplateDao.java.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.dao+StrUtil.upperFirst(tablePd.get("bus_name").toString())+"Dao.java");
		//生成Service
		fprint("gencode/TemplateService.java.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.service+StrUtil.upperFirst(tablePd.get("bus_name").toString())+"Service.java");
		//生成Controller
		fprint("gencode/TemplateController.java.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.controller+StrUtil.upperFirst(tablePd.get("bus_name").toString())+"Controller.java");

		//生成INDEX
		fprint("gencode/TemplateIndex.vue.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.vue+tablePd.get("bus_name").toString()+"/Index.vue");
		//生成EDIE
		fprint("gencode/TemplateEdit.vue.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.vue+tablePd.get("bus_name").toString()+"/Edit.vue");
		//生成INFo
		fprint("gencode/TemplateInfo.vue.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.vue+tablePd.get("bus_name").toString()+"/Info.vue");
		if(tablePd.get("temp_type").toString().equals("1")) {//树表
			//生成TREE
			fprint("gencode/TemplateTree.vue.ftl", root, gen_path + tablePd.get("bus_name").toString() + File.separator + tablePd.get("mod_name").toString() + File.separator + FreemarkerParaUtil.vue + tablePd.get("bus_name").toString() + "/Tree.vue");
		}
			//生成API
		fprint("gencode/TemplateApi.js.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.vue+tablePd.get("bus_name").toString()+"/"+tablePd.get("bus_name").toString()+".js");

		//=====================================处理菜单信息==================================================
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		//查询父级菜单
		PageData param = new PageData();
		param.put("id",tablePd.get("menu_id"));
		PageData menuPd = menuService.findInfo(param);
		if(Verify.verifyIsNotNull(menuPd)){
			String time = DateTimeUtil.getDateTimeStr();
			List<PageData> menuList = new ArrayList<PageData>();
			//组织菜单信息
			PageData mPd = new PageData();
			String menu_id = GuidUtil.getUuid();
			mPd.put("id",menu_id);
			mPd.put("type","1");
			mPd.put("title",tablePd.get("menu_name"));
			mPd.put("path","/"+tablePd.get("mod_name")+"/"+tablePd.get("bus_name"));
			mPd.put("component","/"+tablePd.get("mod_name")+"/"+tablePd.get("bus_name"));
			mPd.put("permission",tablePd.get("bus_name"));
			mPd.put("keepAlive","true");
			mPd.put("parent_id",menuPd.get("id"));
			mPd.put("order_by",Integer.parseInt(menuPd.get("child_num").toString())+1);
			mPd.put("status","0");
			mPd.put("remark","");
			mPd.put("create_time",time);
			mPd.put("create_user",time);
			mPd.put("create_organize",time);
			mPd.put("create_user",user_id);
			mPd.put("create_organize",organize_id);
			menuList.add(mPd);
			//组织菜单功能按钮-添加
			PageData btnAddPd = new PageData();
			String btnAdd_id = GuidUtil.getUuid();
			btnAddPd.put("id",btnAdd_id);
			btnAddPd.put("title","添加");
			btnAddPd.put("path","/");
			btnAddPd.put("component","/");
			btnAddPd.put("keepAlive","true");
			btnAddPd.put("permission","add");
			btnAddPd.put("parent_id",menu_id);
			btnAddPd.put("type","2");
			btnAddPd.put("order_by","1");
			btnAddPd.put("status","0");
			btnAddPd.put("remark","");
			btnAddPd.put("create_time",time);
			btnAddPd.put("create_user",user_id);
			btnAddPd.put("create_organize",organize_id);
			menuList.add(btnAddPd);
			//组织菜单功能按钮-编辑
			PageData btnEditPd = new PageData();
			String btnEdit_id = GuidUtil.getUuid();
			btnEditPd.put("id",btnEdit_id);
			btnEditPd.put("title","编辑");
			btnEditPd.put("path","/");
			btnEditPd.put("component","/");
			btnEditPd.put("keepAlive","true");
			btnEditPd.put("permission","edit");
			btnEditPd.put("parent_id",menu_id);
			btnEditPd.put("type","2");
			btnEditPd.put("order_by","2");
			btnEditPd.put("status","0");
			btnEditPd.put("remark","");
			btnEditPd.put("create_time",time);
			btnEditPd.put("create_user",user_id);
			btnEditPd.put("create_organize",organize_id);
			menuList.add(btnEditPd);
			//组织菜单功能按钮-删除
			PageData btnDelPd = new PageData();
			String btnDel_id = GuidUtil.getUuid();
			btnDelPd.put("id",btnDel_id);
			btnDelPd.put("title","删除");
			btnDelPd.put("path","/");
			btnDelPd.put("component","/");
			btnDelPd.put("keepAlive","true");
			btnDelPd.put("permission","del");
			btnDelPd.put("parent_id",menu_id);
			btnDelPd.put("type","2");
			btnDelPd.put("order_by","3");
			btnDelPd.put("status","0");
			btnDelPd.put("remark","");
			btnDelPd.put("create_time",time);
			btnDelPd.put("create_user",user_id);
			btnDelPd.put("create_organize",organize_id);
			menuList.add(btnDelPd);
			//组织菜单功能按钮-详情
			PageData btnInfoPd = new PageData();
			String btnInfo_id = GuidUtil.getUuid();
			btnInfoPd.put("id",btnInfo_id);
			btnInfoPd.put("title","详情");
			btnInfoPd.put("path","/");
			btnInfoPd.put("component","/");
			btnInfoPd.put("keepAlive","true");
			btnInfoPd.put("permission","info");
			btnInfoPd.put("parent_id",menu_id);
			btnInfoPd.put("type","2");
			btnInfoPd.put("order_by","4");
			btnInfoPd.put("status","0");
			btnInfoPd.put("remark","");
			btnInfoPd.put("create_time",time);
			btnInfoPd.put("create_user",user_id);
			btnInfoPd.put("create_organize",organize_id);
			menuList.add(btnInfoPd);
			root.put("mPd",mPd);
			root.put("menuList",menuList);
			fprint("gencode/TemplateMenuSql.sql.ftl", root, gen_path+tablePd.get("bus_name").toString()+File.separator+tablePd.get("mod_name").toString()+File.separator+FreemarkerParaUtil.sql+tablePd.get("bus_name").toString()+"_menu.sql");
		}

		Json json = new Json();
		json.setData(gen_path+tablePd.get("bus_name").toString());
		json.setSuccess(true);
		json.setMsg("代码生成成功。");
		this.writeJson(response,json);
	}


	/** 
	 * @Description: downloadCode
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-10-14 13:18
	 */
	@ApiOperation("生成代码下载接口")
	@RequestMapping(value = "/downloadCode", method = RequestMethod.GET)
	public void downloadCode(HttpServletRequest request, HttpServletResponse response , HttpSession session) throws Exception {
		PageData pd = new PageData(request);
		PageData tablePd = genCodeService.findInfo(pd);
		String path = pd.get("path").toString();
		ZipUtils.toZip(path+File.separator+tablePd.get("mod_name").toString(), path+"/"+tablePd.get("bus_name")+".zip", true);
		FileUtil.downFile(response, path+"/"+tablePd.get("bus_name")+".zip",tablePd.get("menu_name").toString()+"【代码】.zip");
	}


	public void fprint(String templatePath,Object obj,String outPath) throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
		System.out.println(outPath);
		//ContextLoader loader = new ContextLoader();
		Template template = configuration.getTemplate(templatePath);

		String dirpath = outPath.substring(0,outPath.lastIndexOf("/"));
		System.out.println(dirpath);
		File dirFile = new File(dirpath);
		if(!dirFile.exists()){
			dirFile.mkdir();
			dirFile.mkdirs();
		}

		File file = new File(outPath);
		Writer out = new FileWriter(file);
		template.process(obj, out);//输出
		out.close();
	}


	/** 
	 * @Description: toViewCode 
	 * @Param: [map, request] 
	 * @return: java.lang.String 
	 * @Author: anxingtao
	 * @Date: 2020-10-16 11:07 
	 */
	@ApiOperation("代码在线预览接口")
	@RequestMapping(value = "/toViewCode", method = RequestMethod.GET)
	public void toViewCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData pd = new PageData(request);
		PageData tablePd = genCodeService.findInfo(pd);
		String path = pd.get("path").toString();
		path = path+File.separator+tablePd.get("mod_name").toString();
		//读取文件夹下所有的文件
		List<File> fileList = FileUtil.traverseFolder1(path);
		List<PageData> list = new ArrayList<PageData>();
		for (File file: fileList) {
			PageData param = new PageData();
			param.put("name",file.getName());
			param.put("content",StringEscapeUtils.escapeHtml(FileUtil.readFileContent(file)));
			list.add(param);
//			System.out.println("##########name:::"+file.getName());
//			System.out.println("##########content:::"+param.get("content"));
		}

		Json json = new Json();
		json.setData(list);
		json.setSuccess(true);
		json.setMsg("代码生成成功。");
		this.writeJson(response,json);
	}


	public static void main(String[] args) {
		String HTMLText="<p>我的<br/>评论</p>";
		System.out.println(StringEscapeUtils.escapeHtml(HTMLText));
	}

}
