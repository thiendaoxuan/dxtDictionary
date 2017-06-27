/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.testapp.servers;

import com.vng.zing.jettyserver.WebServers;
import com.vng.zing.testapp.handlers.webapp.AuthenHandler;
import com.vng.zing.testapp.handlers.webapp.DictionaryAPIHandler;
import com.vng.zing.testapp.handlers.webapp.HomePageHandler;
import com.vng.zing.testapp.handlers.webapp.StatsHandler;
import com.vng.zing.testapp.handlers.webapp.TrackHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author namnq
 */
public class HServers {

    public boolean setupAndStart() {
        WebServers servers = new WebServers("testapp");

        HandlerCollection handlers = new HandlerCollection();

        // Resource
       
        
        ResourceHandler resource_handler = new ResourceHandler();
       

        resource_handler.setDirectoriesListed(true);
        //resource_handler.setWelcomeFiles(new String[]{"index.html"});
        resource_handler.setResourceBase("./view");
        ContextHandler resourcesContextHandler = new ContextHandler();
        resourcesContextHandler.setContextPath("/resources");
        resourcesContextHandler.setHandler(resource_handler);
        
        
        handlers.addHandler(resourcesContextHandler);

        ServletContextHandler webandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        webandler.addServlet(TrackHandler.class, "/track");
        webandler.addServlet(StatsHandler.class, "/stats");
        webandler.addServlet(DictionaryAPIHandler.class, "/api");
        webandler.addServlet(HomePageHandler.class, "/view");
        webandler.addServlet(AuthenHandler.class, "/authen");
              
        
        handlers.addHandler(webandler);
        
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        sessions.setHandler(handlers);
        
        servers.setup(handlers);
        return servers.start();
    }
}
