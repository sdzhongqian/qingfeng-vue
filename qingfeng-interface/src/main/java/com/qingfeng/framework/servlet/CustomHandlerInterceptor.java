package com.qingfeng.framework.servlet;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingfeng.framework.jwt.constant.SystemConstant;
import com.qingfeng.framework.jwt.entity.CheckResult;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.system.service.LoginService;
import com.qingfeng.system.service.UserService;
import com.qingfeng.util.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeaders;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Title: CustomHandlerInterceptor
 * @ProjectName wdata
 * @Description: 登录拦截器
 * @author anxingtao
 * @date 2020-9-27 11:27
 */
public class CustomHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Boolean bol = false;
        Json json = new Json();

        String path = request.getServletPath();
        System.out.println(path);
        if(path.matches(Const.NO_INTERCEPTOR_PATH)){
            bol = true;
        }else {
            String token = request.getHeader("access-token");
            if(Verify.verifyIsNotNull(token)){
                //查询token是否失效
                PageData pd = new PageData();
                pd.put("token",token);
                if (loginService == null) {//解决service为null无法注入问题
                    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    loginService = (LoginService) factory.getBean("loginService");
                }
                System.out.println(loginService);
                PageData uPd = loginService.findUserInfo(pd);
                if(Verify.verifyIsNotNull(uPd)){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = formatter.parse(uPd.get("token_refresh_time").toString());
                    Date date2 = DateTimeUtil.getDateTime();
                    long tims = DateUtil.betweenMs(date, date2);
                    if(tims>SystemConstant.JWT_TTL){//超时
                        json.setCode(SystemConstant.JWT_ERRCODE_TIMEOUT);
                    }else{
                        if (userService == null) {//解决service为null无法注入问题
                            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                            userService = (UserService) factory.getBean("userService");
                        }
                        CheckResult checkResult = JwtUtils.validateJWT(token);
                        System.out.println(checkResult.getErrCode());
                        System.out.println(checkResult.isSuccess());
                        System.out.println(JsonToMap.bean2json(checkResult));
                        if(checkResult.isSuccess()){
                            bol = true;
                            //更新token记录时间
                            pd.put("id",uPd.get("id"));
                            pd.put("token_refresh_time",DateTimeUtil.getDateTimeStr());
                            pd.put("update_time",DateTimeUtil.getDateTimeStr());
                            userService.update(pd);
                        }else{
                            //根据token 查询用户信息
                            pd.put("user_id",uPd.get("id"));
                            PageData orgPd = userService.findUserOrganizeInfo(pd);
                            String vueToken = JwtUtils.createJWT(uPd.get("id").toString()+":"+orgPd.get("organize_id").toString(), uPd.getString("login_name"), SystemConstant.JWT_TTL);
                            json.setToken(vueToken);
//                            Map<String, String> map = new HashMap<>();
//                            map.put("access-token",vueToken);
//                            modifyHeaders(map,request);
                            pd.put("id",uPd.get("id"));
                            pd.put("token",vueToken);
                            pd.put("token_refresh_time",DateTimeUtil.getDateTimeStr());
                            pd.put("update_time",DateTimeUtil.getDateTimeStr());
                            userService.update(pd);
                            json.setCode(SystemConstant.RESCODE_REFTOKEN);
                            bol = false;
                        }
                    }
                }else{
                    json.setCode(SystemConstant.JWT_ERRCODE_NULL);
                }
            }else{
                json.setCode(SystemConstant.JWT_ERRCODE_NULL);
            }
        }
        if(!bol){
            ServletOutputStream out = response.getOutputStream();
            json.setSuccess(false);
            json.setLoseSession("loseSession");
            json.setMsg("登录失效，正在跳转。。。");
            response.setContentType("text/html;charset=utf-8");
            ObjectMapper objMapper = new ObjectMapper();
            JsonGenerator jsonGenerator = objMapper.getJsonFactory()
                    .createJsonGenerator(response.getOutputStream(),
                            JsonEncoding.UTF8);

            jsonGenerator.writeObject(json);
            jsonGenerator.flush();
            jsonGenerator.close();
            bol = false;
        }
        return bol;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    private void modifyHeaders(Map<String, String> headerses, HttpServletRequest request) {
        if (headerses == null || headerses.isEmpty()) {
            return;
        }
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field request1 = requestClass.getDeclaredField("request");
            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders)headers.get(o1);
            for (Map.Entry<String, String> entry : headerses.entrySet()) {
                o2.removeHeader(entry.getKey());
                o2.addHeader(entry.getKey(),entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2021-01-02 09:05:00");
        Date date2 = DateTimeUtil.getDateTime();
        long tims = DateUtil.betweenMs(date, date2);
        System.out.println(tims);
    }
}