package com.qingfeng.framework.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP根据注解给上下文赋值
 * @ClassName DataSourceAspect
 * @Description TODO
 * @author lide
 * @date 2018年2月27日 下午4:12:01
 */
@Aspect
@Order(1)	//设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中)
@Component
public class DataSourceAspect {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** 
	 * @Description: 切点 
	 * @Param: [] 
	 * @return: void 
	 * @Author: anxingtao
	 * @Date: 2019-4-4 15:30 
	 */ 
//	@Pointcut("execution(* com.qingfeng.*.service..*.*(..)))")
    @Pointcut("execution(public * com.qingfeng.*.service.*.*(..))")
	public void aspect() { }
	
	@Before("aspect()")
	private void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();  
        Class<?> classz = target.getClass();  
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
                .getMethod().getParameterTypes();  
        try {
            Method m = classz.getMethod(method, parameterTypes);
//            System.out.println(m.isAnnotationPresent(MyDataSource.class));
            if (m != null && m.isAnnotationPresent(MyDataSource.class)) {
                MyDataSource data = m.getAnnotation(MyDataSource.class);
//                System.out.println(data.value().getName());
                JdbcContextHolder.putDataSource(data.value().getName()); 
//                logger.info("===============上下文赋值完成:{}",data.value().getName());
//                System.out.println("上下文赋值完成:"+data.value().getName());
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        
	}




    @After("aspect()")
    public void after(JoinPoint joinPoint){
//        System.out.println(DataSourceType.Master.getName());
        JdbcContextHolder.putDataSource(DataSourceType.Master.getName());
//        System.out.println("##################:after");
    }


}
