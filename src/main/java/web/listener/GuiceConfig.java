package web.listener;

import dao.ConversationDao;
import dao.NotificationDao;
import dao.RelationshipDao;
import dao.UserDao;
import dao.impl.ConversationDaoImpl;
import dao.impl.NotificationDaoImpl;
import dao.impl.RelationshipDaoImpl;
import dao.impl.UserDaoImpl;
import db.PgConfig;
import db.PgConfigProvider;
import db.PostgreDataSourceProvider;
import service.*;
import service.impl.*;
import web.filter.LoggedInFilter;
import web.filter.LoggedOutFilter;
import web.servlet.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import web.servlet.changers.*;

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
            bind(NotificationDao.class).to(NotificationDaoImpl.class).in(Singleton.class);
            bind(RelationshipDao.class).to(RelationshipDaoImpl.class).in(Singleton.class);
            bind(ConversationDao.class).to(ConversationDaoImpl.class).in(Singleton.class);

            bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
            bind(NotificationService.class).to(NotificationServiceImpl.class).in(Singleton.class);
            bind(ConversationService.class).to(ConversationServiceImpl.class).in(Singleton.class);
            bind(RelationshipService.class).to(RelationshipServiceImpl.class).in(Singleton.class);
            bind(SecurityService.class).to(SecurityServiceImpl.class).in(Singleton.class);
        }
    }

    private static class ServletConfigModule extends ServletModule {
        @Override
        protected void configureServlets() {
            filter("/friends","/messages","/notifications","/messages_new",
                    "/profile/changer","/password/changer","/avatar/changer","/logout","/users", "/users/changer",
                    "/friends/changer", "/messages/changer").through(LoggedInFilter.class);
            filterRegex("/profile_[0-9]+").through(LoggedInFilter.class);
            filterRegex("/messages_[0-9]+").through(LoggedInFilter.class);
            filter("/login","/registration").through(LoggedOutFilter.class);
            serve("/").with(RootServlet.class);
            serve("/login").with(LoginServlet.class);
            serve("/locale").with(LocaleServlet.class);
            serve("/registration").with(RegistrationServlet.class);
            serve("/logout").with(LogoutServlet.class);
            serveRegex("/profile_[0-9]+").with(ProfileServlet.class);
            serveRegex("/messages_[0-9]+").with(MessageServlet.class);
            serve("/messages").with(MessageServlet.class);
            serve("/messages_new").with(MessageNewServlet.class);
            serve("/friends").with(FriendsServlet.class);
            serve("/notifications").with(NotificationServlet.class);

            serve("/users").with(UsersServlet.class);
            serve("/users/changer").with(ChangeRelationshipServlet.class);
            serve("/friends/changer").with(ChangeRelationshipServlet.class);
            serve("/messages/changer").with(ChangerMessageServlet.class);

            serve("/profile/changer").with(ChangerProfileServlet.class);
            serve("/password/changer").with(ChangerPasswordServlet.class);
            serve("/avatar/changer").with(ChangerAvatarServlet.class);
        }
    }

    @Override
    protected Injector getInjector() {
        log.info("Started DbModule, DependencyModule, ServletConfigModule");
        return Guice.createInjector(new DbModule(),new DependancyModule(),new ServletConfigModule());
    }
}
