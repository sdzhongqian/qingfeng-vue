package com.qingfeng.framework.monitor.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.framework.jwt.constant.SystemConstant;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anxingtao on 2020-10-3.
 */
@Api(tags = "在线用户信息接口")
@Controller
@RequestMapping(value = "/monitor/userOnline")
public class UserOnlineController extends BaseController {

    @Autowired
    private UserService userService;


    /**
     * @Description: findUserOnlineList 获取当前用户数量
     * @Param: [session, request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-10-3 8:52
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
            pd.put("auth_organize_ids", Arrays.asList(orgPd.get("authOrgIds").toString().split(",")));
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
        pd.put("token_refresh_time",DateTimeUtil.getNowBeforeTime((int)SystemConstant.JWT_TTL/1000/60));
        page.setPd(pd);
        List<PageData> list = userService.findOnLineListPage(page);
        int num = userService.findOnLineListSize(page);
        Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setCode(0);
        json.setCount(num);
        json.setData(list);
        json.setSuccess(true);
        this.writeJson(response,json);
    }


    /**
     * @Description: kickUserOffline 根据Session将用户踢下线
     * @Param: [request, response]
     * @return: void
     * @Author: anxingtao
     * @Date: 2020-10-3 20:01
     */
    @ApiOperation("强退用户登录接口")
    @RequestMapping(value = "/kickUserOffline", method = RequestMethod.GET)
    public void kickUserOffline(@RequestHeader HttpHeaders headers, @RequestParam String ids, HttpServletResponse response) throws Exception  {
        PageData pd = new PageData();
        String[] del_ids = ids.split(",");
        for (int i = 0; i < del_ids.length; i++) {
            String time = DateTimeUtil.getDateTimeStr();
            pd.put("id",del_ids[i]);
            pd.put("token","");
            pd.put("token_refresh_time",time);
            pd.put("update_time",time);
            userService.update(pd);
        }
        Json json = new Json();
        json.setMsg("下线成功");
        json.setSuccess(true);
        this.writeJson(response,json);
    }



}
