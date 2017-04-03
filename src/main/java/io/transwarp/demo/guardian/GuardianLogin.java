package io.transwarp.demo.guardian;

import io.transwarp.guardian.plugins.jetty.GuardianLoginService;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class GuardianLogin {

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
        ServletContextHandler context1 = new ServletContextHandler(ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
        ServletContextHandler context2 = new ServletContextHandler(ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
        context1.setContextPath("/");
        context2.setContextPath("/login");
        context1.addServlet(new ServletHolder(new HelloWebHandler()), "/");
        context2.addServlet(new ServletHolder(new LoginHandler()), "/");

        // Security handler
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"**"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.addConstraintMapping(constraintMapping);
        securityHandler.setLoginService(new GuardianLoginService("testGuardian", true));

        FormAuthenticator authenticator = new FormAuthenticator("/login", "/login", false);
        securityHandler.setAuthenticator(authenticator);

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        securityHandler.setHandler(handlers);

        server.setHandler(securityHandler);

        server.start();
        server.join();
    }
}
