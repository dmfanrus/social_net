<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.profile" var="profile"/>
<fmt:message var="title" bundle="${profile}" key="profile.title"/>

<tags:user title="${title}">
    <h2>ID User: ${user.id}</h2>
    <h2>Full Name: ${user.fullName}</h2>
    <h2>Gender: ${user.gender}</h2>
    <h2>Date of birth: ${user.dateOfBirth}</h2>
    <h2>Email: ${user.email}</h2>
    <h2>Login: ${user.login}</h2>
    <h2>Hash password: ${user.hashPassword}</h2>
</tags:user>