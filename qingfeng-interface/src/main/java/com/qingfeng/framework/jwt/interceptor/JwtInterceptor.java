package com.qingfeng.framework.jwt.interceptor;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingfeng.framework.jwt.constant.SystemConstant;
import com.qingfeng.framework.jwt.entity.CheckResult;
import com.qingfeng.framework.jwt.util.JwtUtils;
import com.qingfeng.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Title: JwtInterceptor
 * @ProjectName wdata
 * @Description: 拦截器 用户权限校验
 * @author anxingtao
 * @date 2020-4-23 15:36
 */
public class JwtInterceptor implements HandlerInterceptor {
    
	private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    /**
     * @Description: 1、成功：0。
    2、客户端错误：1~1000，其中500~1000可设计为有名错误，类似于计算机端口的有名端口，1~499可让程序开发人员任意使用。
    3、服务端错误：1001~2000，其中1001~1500可设计为本服务错误，1501~2000可设计为本服务依赖的服务错误。
     * @Param: [request, response, handler]
     * @return: boolean
     * @Author: anxingtao
     * @Date: 2020-4-27 15:37
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("###############进来了");
        if (handler instanceof HandlerMethod){
            int code = 0;//成功：0。
    		String authHeader = request.getHeader("token");
            System.out.println("###########authHeader:"+authHeader);
            if (StringUtils.isEmpty(authHeader)) {
        	  logger.info("验证失败");
                System.out.println("=====：签名验证不存在");
                code = 1001;//签名验证不存在
        	  print(response, "签名验证不存在",code,false);
              return false;
            }else{
            	//验证JWT的签名，返回CheckResult对象
                CheckResult checkResult = JwtUtils.validateJWT(authHeader);
                if (checkResult.isSuccess()) {
                    System.out.println("=====：验证通过");
                    return true;
                } else {
                    switch (checkResult.getErrCode()) {
                    // 签名验证不通过
                    case SystemConstant.JWT_ERRCODE_FAIL:
                    	logger.info("签名验证不通过");
                        System.out.println("=====：签名验证不通过");
                        code = 1002;//签名验证不通过
                    	print(response,"签名验证不通过",code,false);
                    	break;
                     // 签名过期，返回过期提示码
                    case SystemConstant.JWT_ERRCODE_EXPIRE:
                    	logger.info("签名过期");
                        code = 1003;//签名过期
                        System.out.println("=====：签名过期");
                    	print(response,"签名过期",code,false);
                    	break;
                    default:
                        break;
                    }
                    return false;
                }
            }
		}else{
			return true;
		}
    }  
    public void print(HttpServletResponse response, String message, int code, boolean bol){
    	try {
			response.setStatus(HttpStatus.OK.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
            Json json = new Json();
            json.setMsg(message);
            json.setCode(code);
            json.setSuccess(bol);
            ObjectMapper objMapper = new ObjectMapper();
            JsonGenerator jsonGenerator = objMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
            jsonGenerator.writeObject(json);
            jsonGenerator.flush();
            jsonGenerator.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if(response.getStatus()==500){  
            modelAndView.setViewName("/error/500");  
        }else if(response.getStatus()==404){  
            modelAndView.setViewName("/error/404");  
        }  
    }  
  
    /**  
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，  
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。  
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {  
    }  
}  