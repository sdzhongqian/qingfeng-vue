package com.qingfeng.base.controller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingfeng.util.Page;
import com.qingfeng.util.PageData;
import com.qingfeng.util.Verify;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Title: BaseController
 * @ProjectName com.qingfeng
 * @Description: TODO
 * @author anxingtao
 * @date 2018-8-24 11:30
 */
public class BaseController {



    public Page dealDataAuth(Page page, PageData pd, HttpSession session){
        PageData user = (PageData) session.getAttribute("loginUser");
        PageData organize = (PageData) session.getAttribute("loginOrganize");
        //用户所在组织
        pd.put("auth_user",user.get("id"));
        pd.put("auth_organize",user.get("organize_id"));
        //用户数据权限组织
        if(Verify.verifyIsNotNull(organize.get("authOrgIds"))){
            pd.put("auth_organize_ids",Arrays.asList(organize.get("authOrgIds").toString().split(",")));
        }else{
            pd.put("auth_organize_ids",new ArrayList<String>());
        }
        page.setPd(pd);
        return page;
    }


    /**
     * @Description: dealDataAuth
     * @Param: [pd, session]
     * @return: com.qingfeng.base.util.PageData
     * @Author: anxingtao
     * @Date: 2019-11-28 13:58
     */
    public PageData dealDataAuth(PageData pd, HttpSession session){
        PageData user = (PageData) session.getAttribute("loginUser");
        PageData organize = (PageData) session.getAttribute("loginOrganize");
        //用户所在组织
        pd.put("auth_user",user.get("id"));
        pd.put("auth_organize",user.get("organize_id"));
        //用户数据权限组织
        if(Verify.verifyIsNotNull(organize.get("authOrgIds"))){
            pd.put("auth_organize_ids",Arrays.asList(organize.get("authOrgIds").toString().split(",")));
        }else{
            pd.put("auth_organize_ids",new ArrayList<String>());
        }
        return pd;
    }

    /**
     * @Description: writeJson
     * @Param: [response, object]
     * @return: void
     * @Author: anxingtao
     * @Date: 2018-8-24 13:09
     */
    public void writeJson(HttpServletResponse response, Object object) throws IOException {

        response.setContentType("text/html;charset=utf-8");
        ObjectMapper objMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objMapper.getFactory().createGenerator(response.getOutputStream(),JsonEncoding.UTF8);
        jsonGenerator.writeObject(object);
        jsonGenerator.flush();
        jsonGenerator.close();
    }

}
