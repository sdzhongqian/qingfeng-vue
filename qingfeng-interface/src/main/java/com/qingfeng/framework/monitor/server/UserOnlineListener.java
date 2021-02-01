package com.qingfeng.framework.monitor.server;

import com.qingfeng.util.PageData;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class UserOnlineListener implements HttpSessionBindingListener {

    private PageData userMap;

    public UserOnlineListener(PageData userMap){
        this.userMap=userMap;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        HttpSession session=event.getSession();
        ServletContext application=session.getServletContext();
        //从application获取当前登录用户列表
        PageData userOnlineObj= (PageData) application.getAttribute("userOnlineObj");
        //如果该属性不存在,则初始化
        if(userOnlineObj==null){
            userOnlineObj=new PageData();
            application.setAttribute("userOnlineObj",userOnlineObj);
        }
        //将当前用户添加至用户列表
        userOnlineObj.putAll(this.userMap);
        System.out.println("session属性绑定=======>"+this.userMap);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        HttpSession session=event.getSession();
        ServletContext application=session.getServletContext();
        //从application获取当前登录用户列表
        PageData userOnlineObj= (PageData) application.getAttribute("userOnlineObj");
        //将该用户从列表中移除
        String key = this.userMap.getKeys(this.userMap)[0];
        System.out.println("key:"+key);
        userOnlineObj.remove(key);
        System.out.println("session属性解除绑定=======>"+this.userMap);
    }
}
