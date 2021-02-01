package com.qingfeng.common.controller;

import com.qingfeng.base.controller.BaseController;
import com.qingfeng.util.Json;
import com.qingfeng.util.JsonToMap;
import com.qingfeng.util.PageData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**  
 * @Title: backController
 * @ProjectName com.qingfeng
 * @Description: TODO
 * @author wangcong
 * @date 2018-9-18
 */
@Controller
@RequestMapping(value = "/common/test")
public class TestController extends BaseController {





    /**
     * @Description: index 
     * @Param: [map, request, response] 
     * @return: java.lang.String 
     * @Author: wangcong
     * @Date: 2018-9-18
     */ 
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void index(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PageData  pd = new PageData(request);

        Json json = new Json();
        json.setData(pd);
        this.writeJson(response,json);
    }

    public static void main(String[] args) {

        System.out.println(httpGetRequest("http://localhost:8180/oauth/authorize?response_type=code&client_id=activiti-rest&redirect_uri=https://www.baidu.com&state=state",new PageData()));

        //密码方式获取token
//        System.out.println(httpPostRequest("http://localhost:8180/oauth/token?grant_type=password&username=admin&password=123456&client_id=activiti-rest&client_secret=123456",new PageData()));

        //测试获取接口信息
//        System.out.println(httpGetRequest("http://localhost:8888/port/swaggerTest/findList",new PageData()));
    }


    public static String httpGetRequest(String url, PageData paramPd) {
        // 请求返回结果
        String respContent = "";
        try{
            // get请求
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpClient client = HttpClients.createDefault();

            // 构造实际调用Post参数
            String inputJson = JsonToMap.map2json(paramPd);
            // 解决中文乱码问题
            StringEntity entity = new StringEntity(inputJson, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpGet.setHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYWN0aXZpdGktYXV0aCIsImFjdGl2aXRpLXdlYiIsImFjdGl2aXRpLXJlc3QiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiY3VzdG9tSW5mbyI6ImFkbWluYmVhcmVyIiwiZXhwIjoxNjA2MjM0NTUwLCJhdXRob3JpdGllcyI6WyJ1c2VyOmFkZCJdLCJqdGkiOiI2NmIwOGUxYy03NmIxLTQxMjQtYTZmZC1hNzUyYzZkMGE3ZTQiLCJjbGllbnRfaWQiOiJhY3Rpdml0aS1yZXN0In0.i8bIPfTSW7s--4_lLlCwuRvoXU9RHNePaHvtNB3zNxI");

            HttpResponse resp = client.execute(httpGet);
            // 返回状态码
            int stateCode = resp.getStatusLine().getStatusCode();
            if (200 == stateCode){
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
                // 打印返回结果
                System.out.println(respContent);
            }else{
                System.out.println("stateCode=" + stateCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respContent;
    }

    public static String httpPostRequest(String url, PageData paramPd) {
        // 请求返回结果
        String respContent = "";
        try{
            // post请求
            HttpPost httpPost = new HttpPost(url);
            // get请求
            // HttpGet httpPost = new HttpGet(url);
            CloseableHttpClient client = HttpClients.createDefault();
            // 设置签名
            httpPost.setHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYWN0aXZpdGktYXV0aCIsImFjdGl2aXRpLXdlYiIsImFjdGl2aXRpLXJlc3QiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiY3VzdG9tSW5mbyI6ImFkbWluYmVhcmVyIiwiZXhwIjoxNjA2MjM0NTUwLCJhdXRob3JpdGllcyI6WyJ1c2VyOmFkZCJdLCJqdGkiOiI2NmIwOGUxYy03NmIxLTQxMjQtYTZmZC1hNzUyYzZkMGE3ZTQiLCJjbGllbnRfaWQiOiJhY3Rpdml0aS1yZXN0In0.i8bIPfTSW7s--4_lLlCwuRvoXU9RHNePaHvtNB3zNxI");

            List<NameValuePair> list = new LinkedList<NameValuePair>();
            String[] keys = paramPd.getKeys(paramPd);
            for (int i = 0; i < keys.length; i++) {
                BasicNameValuePair param1 = new BasicNameValuePair(keys[i], paramPd.get(keys[i]).toString());
                list.add(param1);
            }
            System.out.println(list);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(formEntity);
//			httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            System.out.println(resp);
            // 返回状态码
            int stateCode = resp.getStatusLine().getStatusCode();
            if (200 == stateCode){
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
                // 打印返回结果
                System.out.println(respContent);
            }else{
                System.out.println("stateCode=" + stateCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respContent;
    }


    //=======================================测试相关=======================================

    /** 
     * @Description: selectOneOrganize 
     * @Param: [request, session] 
     * @return: org.springframework.web.servlet.ModelAndView 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:58 
     */ 
    @RequestMapping("/selectUserOrOrganize")
    public ModelAndView selectOneOrganize(HttpServletRequest request, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("web/base/index");
        return mv;
    }


    /** 
     * @Description: tableTree
     * @Param: [request, session] 
     * @return: org.springframework.web.servlet.ModelAndView 
     * @Author: anxingtao
     * @Date: 2020-9-26 16:58 
     */ 
    @RequestMapping("/treeTable")
    public ModelAndView tableTree(HttpServletRequest request, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("web/base/treeTable");
        return mv;
    }

    @RequestMapping("/verifyCode")
    public ModelAndView verifyCode(HttpServletRequest request, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("web/base/verifyCode");
        return mv;
    }


}
