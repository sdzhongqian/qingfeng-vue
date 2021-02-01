package com.qingfeng.framework.monitor.server;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class MySessionContext {

    private static MySessionContext instance;

    private HashMap<String, HttpSession> sessionMap;

    private MySessionContext(){
        sessionMap=new HashMap<String, HttpSession>();
    }

    public static MySessionContext getInstance(){
        if(instance==null){
            instance=new MySessionContext();
        }
        return instance;
    }

    public synchronized void addSession(HttpSession session){
        if(session!=null){
            sessionMap.put(session.getId(),session);
        }
    }

    public synchronized void delSession(HttpSession session){
        if(session!=null){
            sessionMap.remove(session.getId());
        }
    }

    public synchronized HttpSession getSession(String sessionId){
        System.out.println("#############sessionId:"+sessionId);
        if(sessionId!=null){
            return sessionMap.get(sessionId);
        }else{
            return null;
        }
    }
}
