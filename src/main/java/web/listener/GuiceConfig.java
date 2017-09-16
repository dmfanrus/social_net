package web.listener;

import dao.FriendDao;
import dao.UserDao;
import dao.impl.FriendDaoImpl;
import dao.impl.UserDaoImpl;
import db.PgConfig;
import db.PgConfigProvider;
import db.PostgreDataSourceProvider;
import service.FriendService;
import service.SecurityService;
import service.UserService;
import service.impl.FriendServiceImpl;
import service.impl.SecurityServiceImpl;
import service.impl.UserServiceImpl;
import web.filter.LoggedInFilter;
import web.filter.LoggedOutFilter;
import web.servlet.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class GuiceConfig extends GuiceServletContextListener {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GuiceConfig.class);
    private static class DbModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(PgConfig.class).toProvider(PgConfigProvider.class).in(Singleton.class);
            bind(DataSource.class).toProvider(PostgreDataSourceProvider.class).in(Singleton.class);
        }
    }

    private static class DependancyModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(UserDao.class).to(UserDaoImpl.class).in(Singleton.class);
            bind(FriendDao.class).to(FriendDaoImpl.class).in(Singleton.class);
            bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
            bind(FriendService.class).to(FriendServiceImpl.class).in(Singleton.class);
            bind(SecurityService.class).to(SecurityServiceImpl.class).in(Singleton.class);
        }
    }

    private static class ServletConfigModule extends ServletModule {
        @Override
        protected void configureServlets() {
            filter("/profile","/friends","/message","/profile_update","/logout","/users").through(LoggedInFilter.class);
            filter("/login","/registration").through(LoggedOutFilter.class);
            serve("/").with(RootServlet.class);
            serve("/help").with(HelpServlet.class);
            serve("/login").with(LoginServlet.class);
            serve("/registration").with(RegistrationServlet.class);
            serve("/logout").with(LogoutServlet.class);
            serve("/profile").with(ProfileServlet.class);
            serve("/friends").with(FriendsServlet.class);
            serve("/message").with(MessageServlet.class);
            serve("/profile_update").with(Profile_UPD_Servlet.class);
            serve("/users").with(UsersServlet.class);
        }
    }

    @Override
    protected Injector getInjector() {
        log.info("Started DbModule, DependencyModule, ServletConfigModule");
        return Guice.createInjector(new DbModule(),new DependancyModule(),new ServletConfigModule());
    }
}
