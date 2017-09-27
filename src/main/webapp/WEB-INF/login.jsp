<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.login" var="login"/>
<fmt:setBundle basename="i18n.errorsMessage" var="errorsMessage"/>
<fmt:message var="title" bundle="${login}" key="login.title"/>

<jsp:useBean id="data" type="model.Credentials" scope="request"/>

<tags:user title="${title}">
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-10">
            <form class="form-horizontal" name="loginForm" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <h2 class="text-center"><c:out value="${title}"/></h2>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${loginPass == 'invalid'}">
                        <div class="form-group">
                            <div class="col-lg-1"></div>
                            <div class="col-lg-7">
                                <div class="alert alert-danger">
                                    <fmt:message bundle="${errorsMessage}" key="errorsMessage.loginPassWrong"/>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <br>
                    </c:otherwise>
                </c:choose>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input class="form-control"
                               placeholder="<fmt:message bundle="${login}" key="login.username"/>"
                               name="username" id="username" value="<c:out value="${data.login}"/>">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['login']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['login']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty data.login && not empty errors['password']}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input type="password" class="form-control"
                               placeholder="<fmt:message bundle="${login}" key="login.password"/>"
                               name="password" id="password">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['password']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['password']}"/></label>
                            </div>
                        </c:when>
                    </c:choose>
                    <div class="col-lg-1"></div>
                </div>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-10">
                        <label class="checkbox-inline"><input type="checkbox" id="rememberMe"
                                                              name="rememberMe"><fmt:message
                                bundle="${login}"
                                key="login.rememberme"/></label>
                    </div>
                    <div class="col-lg-1"></div>
                </div>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <button type="submit" class="btn-primary btn-lg btn-block active"><fmt:message
                                bundle="${login}"
                                key="login.enter"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</tags:user>