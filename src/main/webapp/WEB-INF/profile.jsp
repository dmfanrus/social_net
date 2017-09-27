<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.profile" var="profile"/>
<fmt:message var="title" bundle="${profile}" key="profile.title"/>
<jsp:useBean id="userInfo" type="model.User" scope="request"/>
<tags:user title="${title}">
    <div class="row">
        <div class="col-md-1 col-lg-1"></div>
        <div class="col-xs-12 col-sm-12 col-md-10 col-lg-10">
        <h2><c:out value="${title}"/></h2>
            <div class="panel">
                <div class="row">
                    <div class="col-md-4 col-lg-4" align="center">
                        <img src="${pageContext.request.contextPath}/resources/img/avatar_default.png"
                             class="img-circle img-responsive"></div>

                    <div class=" col-md-8 col-lg-8 ">
                        <table class="table table-user-information">
                            <tbody>
                            <tr>
                                <td>Name</td>
                                <td>${userInfo.lastName} ${userInfo.firstName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td><a href="mailto:info@support.com">${userInfo.email}</a></td>
                            </tr>
                            <tr>
                                <td>Date of Birth</td>
                                <td>${userInfo.dateOfBirth}</td>
                            </tr>
                            <tr>
                            <tr>
                                <td>Gender</td>
                                <td>${userInfo.gender}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-1 col-lg-1"></div>
        </div>
    </div>

</tags:user>