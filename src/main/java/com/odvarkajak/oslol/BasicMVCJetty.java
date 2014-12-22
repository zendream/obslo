
package com.odvarkajak.oslol;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class BasicMVCJetty {

    public static void main(final String args[]) throws Exception {

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext =
                new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(AppConfiguration.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);

        context.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }
}
