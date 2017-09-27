<%@ attribute name="title" rtexprvalue="true" required="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.menu" var="menu"/>
<!DOCTYPE html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <c:url var="bsMain" value="/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${bsMain}"/>
    <c:url var="bsTheme" value="/webjars/bootstrap/3.3.7-1/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="${bsTheme}"/>
    <c:url var="flagCss" value="/webjars/flag-icon-css/2.4.0/css/flag-icon.min.css"/>
    <link rel="stylesheet" href="${flagCss}"/>
    <c:url var="bsJs" value="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"/>
    <script src="${bsJs}"></script>
    <c:url var="mainCss" value="/resources/css/mainStyle.css"/>
    <link rel="stylesheet" href="${mainCss}"/>
    <title><c:out value="${title}"/></title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-8">
            <nav class="navbar navbar-inverse navbar-static-top">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                                data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                            <span class="sr-only"><fmt:message bundle="${menu}" key="menu.toggle.navigation"/></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <c:url var="mainUrl" value="/"/>
                        <a class="navbar-brand" href="${mainUrl}"><fmt:message bundle="${menu}" key="menu.title"/></a>
                    </div>

                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav">
                            <c:choose>
                                <c:when test="${sessionScope.user == null}">
                                    <c:url var="loginUrl" value="/login"/>
                                    <li><a href="${loginUrl}"><fmt:message bundle="${menu}" key="menu.loginIn"/></a>
                                    </li>
                                    <c:url var="loginUrl" value="/registration"/>
                                    <li><a href="${loginUrl}"><fmt:message bundle="${menu}"
                                                                           key="menu.registration"/></a></li>
                                    <c:url var="helpUrl" value="/help"/>
                                    <li><a href="${helpUrl}"><fmt:message bundle="${menu}" key="menu.help"/></a></li>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="profileUrl" value="/profile_${sessionScope.user.id}"/>
                                    <li><a href="${profileUrl}"><fmt:message bundle="${menu}" key="menu.profile"/></a>
                                    </li>
                                    <c:url var="usersUrl" value="/users"/>
                                    <li><a href="${usersUrl}"><fmt:message bundle="${menu}" key="menu.users"/></a></li>
                                    <c:url var="messageUrl" value="/message"/>
                                    <li><a href="${messageUrl}"><fmt:message bundle="${menu}" key="menu.message"/></a>
                                    </li>
                                    <c:url var="friendsUrl" value="/friends"/>
                                    <li><a href="${friendsUrl}"><fmt:message bundle="${menu}" key="menu.friends"/></a>
                                    </li>
                                    <c:url var="helpUrl" value="/help"/>
                                    <li><a href="${helpUrl}"><fmt:message bundle="${menu}" key="menu.help"/></a></li>
                                    <c:url var="loginUrl" value="/logout"/>
                                    <li><a href="${loginUrl}"><fmt:message bundle="${menu}" key="menu.loginOut"/></a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </nav>
            <jsp:doBody/>
        </div>
        <div class="col-lg-2"></div>
    </div>
</div>
<footer class="footer">
    <div class="container" id="footer">
        <p class="text-muted" style="text-align: center;">Designed by Mihail Gurko. Copyright &copy 2017. All rights
            reserved.</p>
    </div>
</footer>
</body>
</html>