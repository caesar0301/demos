package io.transwarp.demo;

import io.transwarp.guardian.plugins.jetty.GuardianLoginService;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JettyFormLogin {

    static class HelloWebHandler extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print("<h2>Hello World</h2>");
        }
    }

    static class LoginHandler extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.getWriter().print("<html><form method='POST' action='/j_security_check'>"
                    + "<input type='text' name='j_username'/>"
                    + "<input type='password' name='j_password'/>"
                    + "<input type='submit' value='Login'/></form></html>");
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context1 = new ServletContextHandler(ServletContextHandler.SECURITY);
        ServletContextHandler context2 = new ServletContextHandler(ServletContextHandler.SECURITY);
        context1.setContextPath("/");
        context2.setContextPath("/login");
        context1.addServlet(new ServletHolder(new HelloWebHandler()), "/");
        context2.addServlet(new ServletHolder(new LoginHandler()), "/");

        // Security handler
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__FORM_AUTH);
        constraint.setRoles(new String[]{"admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        GuardianLoginService loginService = new GuardianLoginService("testGuardian", true);
        FormAuthenticator authenticator = new FormAuthenticator("/login", "/login", true);

        // Session handler
        HashSessionIdManager sessionIdManager = new HashSessionIdManager();
        SessionManager sessionManager = new HashSessionManager();
        sessionManager.setSessionIdManager(sessionIdManager);
        sessionManager.setSessionIdPathParameterName("session");

        ConstraintMapping[] mappings = new ConstraintMapping[]{constraintMapping};

        context1.setSessionHandler(new SessionHandler(sessionManager));
        context1.setSecurityHandler(createSecurityHandler(loginService, authenticator, mappings));

        context2.setSessionHandler(new SessionHandler(sessionManager));
        context2.setSecurityHandler(createSecurityHandler(loginService, authenticator, mappings));

        ContextHandlerCollection collection = new ContextHandlerCollection();
        collection.setHandlers(new Handler[]{context1});
        server.setHandler(collection);

//        SessionHandler sessionHandler1 = new SessionHandler(sessionManager);
//        SessionHandler sessionHandler2 = new SessionHandler(sessionManager);
//        SecurityHandler securityHandler1 = createSecurityHandler(loginService, authenticator, mappings);
//        SecurityHandler securityHandler2 = createSecurityHandler(loginService, authenticator, mappings);
//        securityHandler1.setHandler(context1);
//        securityHandler2.setHandler(context2);
//        sessionHandler1.setHandler(securityHandler1);
//        sessionHandler2.setHandler(securityHandler2);
//        collection.addHandler(sessionHandler1);
//        collection.addHandler(sessionHandler2);

//        SecurityHandler securityHandler = createSecurityHandler(loginService, authenticator, mappings);
//        securityHandler.setHandler(collection);
//
//        server.setHandler(securityHandler);

        server.start();
        server.join();
    }

    static SecurityHandler createSecurityHandler(LoginService loginService, Authenticator authenticator, ConstraintMapping[] constraints) {
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setLoginService(loginService);
        securityHandler.setAuthenticator(authenticator);
        securityHandler.setConstraintMappings(constraints);
        return securityHandler;
    }
}
