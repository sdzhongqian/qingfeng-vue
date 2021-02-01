package ${tablePd.pack_path}.${tablePd.mod_name}.controller;

import io.swagger.annotations.ApiOperation;
import com.qingfeng.base.controller.BaseController;
import com.qingfeng.common.service.UploadService;
import com.qingfeng.framework.jwt.util.JwtUtils;
import ${tablePd.pack_path}.${tablePd.mod_name}.service.${tablePd.bus_name?cap_first}Service;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
import com.qingfeng.util.upload.ParaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: ${tablePd.bus_name?cap_first}Controller
 * @ProjectName com.qingfeng
 * @Description: Controller层
 * @author anxingtao
 * @date 2020-9-22 22:45
 */
@Controller
@RequestMapping(value = "/${tablePd.mod_name}/${tablePd.bus_name}")
public class ${tablePd.bus_name?cap_first}Controller extends BaseController {

	@Autowired
	private ${tablePd.bus_name?cap_first}Service ${tablePd.bus_name}Service;
	@Autowired
	public UploadService uploadService;
	@Autowired
	private UserService userService;


	/** 
	 * @Description: findListPage 
	 * @Param: [page, request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:51 
	 */
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
		List<PageData> list = ${tablePd.bus_name}Service.findListPage(page);
		for(PageData p:list){
	<#list fieldList as obj>
	<#if obj.field_operat == 'Y'>
		<#if obj.show_type == '8'>
			//查询${obj.field_comment}附件信息
			PageData filePd = new PageData();
			filePd.put("idList",Arrays.asList(p.get("${obj.field_name}").toString().split(",")));
			List<PageData> ${obj.field_name}FileList = uploadService.findFileList(filePd);
			p.put("${obj.field_name}FileList",${obj.field_name}FileList);
		</#if>
	</#if>
	</#list>
		}
		int num = ${tablePd.bus_name}Service.findListSize(page);
		Json json = new Json();
		json.setMsg("获取数据成功。");
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
     * @Date: 2020-9-22 22:51 
     */
        @RequestMapping(value = "/findList", method = RequestMethod.POST)
        public void findList(@RequestHeader HttpHeaders headers,@RequestBody PageData pd, HttpServletResponse response) throws IOException  {
    	List<PageData> list = ${tablePd.bus_name}Service.findList(pd);
        Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setData(list);
        json.setSuccess(true);
        this.writeJson(response,json);
    }

	/** 
	 * @Description: save 
	 * @Param: [request, response, session] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:51 
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public void save(@RequestHeader HttpHeaders headers, @RequestBody PageData pd, HttpServletResponse response) throws IOException  {
		Json json = new Json();
		//处理数据权限
		String  token = headers.get("access-token").get(0);
		System.out.println("findUserInfo###########TOKEN:"+token);
		String user_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[0];
		String organize_id = JwtUtils.validateJWT(token).getClaims().getId().split(":")[1];
		String time = DateTimeUtil.getDateTimeStr();
        if(Verify.verifyIsNotNull(pd.get("id"))){
            pd.put("update_time", time);
            pd.put("update_user",user_id);
            int num = ${tablePd.bus_name}Service.update(pd);
            if(num==1){
		<#list fieldList as obj>
			<#if obj.field_operat == 'Y'>
				<#if obj.show_type == '8'>
                    //处理${obj.field_comment}附件信息-更新信息主要意义在于可删选删除垃圾图片
                    PageData filePd = new PageData();
                    filePd.put("obj_id",pd.get("id"));
                    filePd.put("update_time", time);
                    filePd.put("update_user", user_id);
                    String file_${obj.field_name}[] = pd.get("${obj.field_name}").toString().split(",");
                    for (String file_${obj.field_name}_id:file_${obj.field_name}) {
                    filePd.put("id", file_${obj.field_name}_id);
                    uploadService.updateFile(filePd);
                    }
				</#if>
			</#if>
		</#list>
            }
            json.setSuccess(true);
            json.setMsg("操作成功。");
		}else{
		<#if tablePd.temp_type == '1'>
            pd.put("${tablePd.tree_pid}", pd.get("parent_id"));
		</#if>
            //主键id
            String id = GuidUtil.getUuid();
            pd.put("id", id);
            pd.put("create_time", time);
            pd.put("status","1");
            //处理数据权限
            pd.put("create_user",user_id);
            pd.put("create_organize",organize_id);

            int num = ${tablePd.bus_name}Service.save(pd);
            if(num==1){
		<#list fieldList as obj>
			<#if obj.field_operat == 'Y'>
				<#if obj.show_type == '8'>
                    //处理${obj.field_comment}附件信息-更新信息主要意义在于可删选删除垃圾图片
                    PageData filePd = new PageData();
                    filePd.put("obj_id",id);
                    filePd.put("update_time", time);
                    filePd.put("update_user", user_id);
                    String file_${obj.field_name}[] = pd.get("${obj.field_name}").toString().split(",");
                    for (String file_${obj.field_name}_id:file_${obj.field_name}) {
                    filePd.put("id", file_${obj.field_name}_id);
                    uploadService.updateFile(filePd);
                    }
				</#if>
			</#if>
		</#list>
            }
            json.setSuccess(true);
            json.setMsg("操作成功。");
		}
		this.writeJson(response,json);
	}


	/**
	 * @Description: del 
	 * @Param: [request, response] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2020-9-22 22:51 
	 */
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public void del(@RequestHeader HttpHeaders headers, @RequestParam String ids, HttpServletResponse response) throws IOException  {
		<#assign delMain = 'false'>
		<#assign delChild = 'false'>
		PageData pd = new PageData();
		String[] del_ids = ids.split(",");
	<#list fieldList as obj>
	<#if obj.field_operat == 'Y'>
	<#if obj.show_type == '8'>
		<#assign delMain = 'true'>
	</#if>
	</#if>
	</#list>
		<#if delMain == 'true'>
        //删除主表附件信息
        for (String id:del_ids) {
			pd.put("obj_id",id);
			List<PageData> fileList = uploadService.findFileList(pd);
			for (PageData fPd:fileList) {
				//查询信息
				File pathFile = new File(ParaUtil.localName+fPd.getString("file_path"));
				pathFile.delete();
				pathFile.deleteOnExit();
				uploadService.delFile(fPd);
			}
        }
		</#if>
		${tablePd.bus_name}Service.del(del_ids);
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg("操作成功。");
		this.writeJson(response,json);
	}

}
