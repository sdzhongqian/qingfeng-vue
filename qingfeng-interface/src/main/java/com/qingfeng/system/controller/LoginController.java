package com.qingfeng.system.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.common.service.UploadService;
import com.qingfeng.framework.jwt.constant.SystemConstant;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.LoggerService;
import com.qingfeng.system.service.LoginService;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
import eu.bitwalker.useragentutils.UserAgent;
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
import java.util.List;

/**
 * Created by anxingtao on 2020-9-27.
 */
@Api(tags = "登录信息接口")
@Controller
@RequestMapping(value = "/system/login")
public class LoginController extends BaseController {

    @Autowired
    public LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoggerService loggerService;
    @Autowired
    public UploadService uploadService;

    /**
     * @Description: login
     * @Param: [request, response, session]
     * @return: java.lang.String
     * @Author: anxingtao
     * @Date: 2020-9-27 9:09
     */
    @ApiOperation("退出登录接口")
    @RequestMapping(value = "/outLogin", method = RequestMethod.GET)
    public void outLogin(@RequestHeader HttpHeaders headers,HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        PageData pd = new PageData(request);
        String  token = headers.get("access-token").get(0);
        //添加在线用户
        pd.put("token",token);
        pd.put("token_refresh_time",DateTimeUtil.getDateTimeStr());
        pd.put("update_time",DateTimeUtil.getDateTimeStr());
        userService.updateForToken(pd);
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("退出登录。");
        this.writeJson(response,json);
    }


    /** 
     * @Description: postLogin 
     * @Param: [request, response, session] 
     * @return: void 
     * @Author: anxingtao
     * @Date: 2020-9-27 10:42 
     */
    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void postLogin(@RequestBody PageData pd, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String vueToken = "";
        String errMsg = "";
        String errCode = "0";
        boolean errBol = false;
        if(!Verify.verifyIsNotNull(pd.getString("username"))){
            errMsg="登录名称不可为空。";
            errCode = "1";
        }else if(!Verify.verifyIsNotNull(pd.getString("password"))){
            errMsg="登录密码不可为空。";
            errCode = "2";
        }else{
            pd.put("login_name",pd.get("username"));
            PageData uPd = loginService.findUserInfo(pd);
            if(Verify.verifyIsNotNull(uPd)){
                if(uPd.getString("status").equals("2")){
                    errCode = "1";
                    errMsg="账号已休眠，请联系管理员";
                }else{
                    if(uPd.getString("login_password").equals(PasswordUtil.encrypt(pd.get("password").toString(), uPd.get("login_name").toString()))){
                        if(uPd.getString("status").equals("0")){
                            //登录成功
                            errBol=true;
                            //查询当前用户组织
                            pd.put("user_id",uPd.get("id"));
                            PageData orgPd = userService.findUserOrganizeInfo(pd);
                            //把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
                            vueToken = JwtUtils.createJWT(uPd.get("id").toString()+":"+orgPd.get("organize_id").toString(), pd.getString("login_name"), SystemConstant.JWT_TTL);

                            PageData p = new PageData();
                            //主键id
                            p.put("id", GuidUtil.getUuid());
                            p.put("type","1");
                            p.put("title",uPd.get("name")+"在"+DateTimeUtil.getDateTimeStr()+"进行了登录操作");
                            p.put("content",uPd.get("name")+"在"+DateTimeUtil.getDateTimeStr()+"进行了登录操作");
                            p.put("operate_type","POST");
                            p.put("operate_user",uPd.get("name"));
                            p.put("create_user",uPd.get("id"));
                            p.put("create_time", DateTimeUtil.getDateTimeStr());
                            p.put("update_time",DateTimeUtil.getDateTimeStr());
                            loggerService.save(p);

                            int pwd_error_num = 0;
                            String time = DateTimeUtil.getDateTimeStr();
                            PageData pu = new PageData();
                            pu.put("id",uPd.get("id"));
                            pu.put("pwd_error_num",pwd_error_num);
                            pu.put("pwd_error_time",time);
                            pu.put("update_time",time);
                            pu.put("last_login_time",time);
                            pu.put("update_user",uPd.get("id"));
                            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                            // 获取客户端操作系统
                            String os = userAgent.getOperatingSystem().getName();
                            // 获取客户端浏览器
                            String browser = userAgent.getBrowser().getName();
                            pu.put("browser",browser);
                            pu.put("os",os);
                            pu.put("ipaddr",IpUtils.getIpAddr(request));
                            pu.put("iprealaddr",AddressUtils.getRealAddressByIP(IpUtils.getIpAddr(request)));
                            //添加在线用户
                            pu.put("token",vueToken);
                            pu.put("token_refresh_time",time);
                            userService.update(pu);

                            errMsg="登录成功";
                        }else if(uPd.getString("status").equals("N")){
                            errCode = "1";
                            errMsg="账号已禁用，请联系管理员";
                        }else if(uPd.getString("status").equals("2")){
                            errCode = "1";
                            errMsg="账号已休眠，请联系管理员";
                        }
                    }else{
                        //记录密码次数。
                        int pwd_error_num = 0;
                        if(Verify.verifyIsNotNull(uPd.get("pwd_error_num"))){
                            pwd_error_num = Integer.parseInt(uPd.get("pwd_error_num").toString())+1;
                        }else{
                            pwd_error_num = 1;
                        }
                        String time = DateTimeUtil.getDateTimeStr();
                        PageData p = new PageData();
                        p.put("id",uPd.get("id"));
                        p.put("pwd_error_num",pwd_error_num);
                        p.put("pwd_error_time",time);
                        p.put("update_time",time);
                        p.put("update_user",uPd.get("id"));
                        if(pwd_error_num>5){
                            p.put("status","2");
                        }
                        userService.update(p);
                        errCode = "2";
                        if((6-pwd_error_num)>0){
                            errMsg="您所填写的密码不正确，还有"+(6-pwd_error_num)+"次机会！";
                        }else{
                            errMsg="账号已休眠，请联系管理员。";
                        }
                    }
                }
            }else{
                errCode = "1";
                errMsg="登录名称不存在，请重新输入。";
            }
        }
        Json json = new Json();
        json.setMsg(errMsg);
        json.setFlag(errCode);
        json.setToken(vueToken);
        json.setSuccess(errBol);
        this.writeJson(response,json);
    }


    /**
     * @Description: findUserInfo
     * @Param: [token, request, response, session]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-11-26 21:33
     */
    @ApiOperation("登录-查询用户信息及权限信息")
    @RequestMapping(value = "/findUserInfo", method = RequestMethod.GET)
    public void findUserInfo(@RequestHeader HttpHeaders headers, @RequestParam String token, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        PageData pd = new PageData(request);
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
        Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setCode(200);
        json.setData(uPd);
        json.setSuccess(true);
        this.writeJson(response,json);
    }


}
