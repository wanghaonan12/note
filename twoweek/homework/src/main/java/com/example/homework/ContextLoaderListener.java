package com.example.homework;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class ContextLoaderListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
    public static Map<String ,Object> sessionMap;

    public ContextLoaderListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        System.out.println("ContextLoaderListener初始化....");
        sessionMap=new HashMap<>(8);
        sce.getServletContext().setAttribute("sessionMap", sessionMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        System.out.println("ContextLoaderListener,销毁,释放资源");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("ContextLoaderListener创建会话");

        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
        System.out.println("ContextLoaderListener销毁会话");

    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        HttpSession session = sbe.getSession();
        System.out.println(session.getAttribute("username")+"上线了");
        sessionMap.put(session.getId(), session.getAttribute("username"));
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        HttpSession session = sbe.getSession();
        System.out.println(session.getAttribute("username")+"下线了");
        sessionMap.put(session.getId(), session.getAttribute("username"));
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
