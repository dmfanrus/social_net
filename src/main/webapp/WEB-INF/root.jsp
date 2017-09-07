<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.root" var="root"/>
<fmt:message var="title" bundle="${root}" key="root.title"/>

<tags:user title="${title}">
    <h2>${title}</h2>
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-8">
            <form name="searchForm" method="post" action="result">
                <div class="form-group">
                    <textarea class="form-control" rows="5" id="search" name="reqText"></textarea>
                </div>
                <button type="submit" class="btn-primary btn-lg btn-block active">Отправить</button>
            </form>
        </div>
        <div class="col-lg-2"></div>
    </div>
</tags:user>
