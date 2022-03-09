package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class ContextLoaderListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

private static Map<String,Object> sessionMap;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        System.out.println("监听初始,加载....");
        sessionMap = new HashMap<>(8);
        sce.getServletContext().setAttribute("sessionMap",sessionMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        System.out.println("监听销毁......");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
        System.out.println("session创建会话");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
        System.out.println("session 销毁...");
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
        HttpSession session = sbe.getSession();
        System.out.println(session.getAttribute("username")+"上线了");
        sessionMap.put(session.getId(),session.getAttribute("username"));
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
        HttpSession session = sbe.getSession();
        System.out.println(session.getAttribute("username")+"下线了");
        sessionMap.put(session.getId(),session.getAttribute("username"));

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
