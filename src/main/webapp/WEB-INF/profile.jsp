<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.profile" var="profile"/>
<fmt:message var="title" bundle="${profile}" key="profile.title"/>
<jsp:useBean id="userInfo" type="model.User" scope="request" />
<tags:user title="${title}">
    <h2>User ID: ${userInfo.id}</h2>
    <h2>Name: ${userInfo.lastName} ${userInfo.firstName}</h2>
    <h2>Login: ${userInfo.login}</h2>
    <h2>Email: ${userInfo.email}</h2>
    <h2>Date of birth: ${userInfo.dateOfBirth}</h2>
    <h2>Gender: ${userInfo.gender}</h2>
</tags:user>