package org.springframework.base.system.utils;

import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener implements HttpSessionListener
{
    private Logger log = LoggerFactory.getLogger(SessionListener.class);
    
    @Override
    public void sessionCreated(HttpSessionEvent event)
    {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        HashSet sessions = (HashSet)application.getAttribute("sessions");
        if (sessions == null)
        {
            sessions = new HashSet();
            application.setAttribute("sessions", sessions);
        }
        sessions.add(session);
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event)
    {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        HashSet sessions = (HashSet)application.getAttribute("sessions");
        sessions.remove(session);
    }
}
