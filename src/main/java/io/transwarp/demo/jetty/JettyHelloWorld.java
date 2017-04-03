package io.transwarp.demo.jetty;

import io.transwarp.guardian.plugins.jetty.GuardianLoginService;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Constraint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyHelloWorld {

  static class HelloWebHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      resp.setContentType("text/html;charset=utf-8");
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().print("<h2>Hello from HellowWebHandler.</h2>");
    }
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    HelloWebHandler helloWebHandler = new HelloWebHandler();
    ServletHolder holder = new ServletHolder(helloWebHandler);

    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setBaseResource(Resource.newResource("front/"));

    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
    servletContextHandler.setContextPath("/hello");
    servletContextHandler.addServlet(holder, "/");

    ContextHandler contextHandler = new ContextHandler("/");
    contextHandler.setHandler(resourceHandler);


    ContextHandlerCollection collection = new ContextHandlerCollection();
    collection.setHandlers(new Handler[]{servletContextHandler, contextHandler});

    // Security Handler
    GuardianLoginService loginService = new GuardianLoginService("testing");
    loginService.setCredentialCacheEnable(true);
    loginService.setCredentialLifeTime(60 * 2);
    ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
    securityHandler.setLoginService(loginService);
    securityHandler.setAuthenticator(new BasicAuthenticator());

    /*
    * Security contraints
    * Matching is performed in the following order (from Jetty PathMap)
    *   Exact match.
    *   Longest prefix match.
    *   Longest suffix match.
    *   default.
     */
    Constraint constraint1 = new Constraint();
    constraint1.setAuthenticate(true);
    constraint1.setName(Constraint.__BASIC_AUTH);
    constraint1.setRoles(new String[]{"*"});
    ConstraintMapping constraintMapping1 = new ConstraintMapping();
    constraintMapping1.setConstraint(constraint1);
    constraintMapping1.setPathSpec("/hello");

    Constraint constraint2 = new Constraint();
    constraint2.setAuthenticate(true);
    constraint2.setName(Constraint.__BASIC_AUTH);
    constraint2.setRoles(new String[]{"*" });
    ConstraintMapping constraintMapping2 = new ConstraintMapping();
    constraintMapping2.setConstraint(constraint2);
    constraintMapping2.setPathSpec("/*");

    securityHandler.setConstraintMappings(new ConstraintMapping[]{constraintMapping2, constraintMapping1});

    securityHandler.setHandler(collection);
    server.setHandler(securityHandler);
//    server.setHandler(collection);

    server.start();
    server.join();

  }
}
