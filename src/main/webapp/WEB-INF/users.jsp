<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 06.09.2017
  Time: 20:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.users" var="users"/>
<fmt:message var="title" bundle="${users}" key="users.title"/>

<tags:user title="${title}">
    <h2>${title}</h2>
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-8">
                <%--<div class="container">--%>
            <c:forEach items="${usersList}" var="currentUser">
                <div class="panel panel-default">
                    <div class="row">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-8">
                                ${currentUser.fullName}<br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-8">
                                ${currentUser.email}<br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-8">
                                ${currentUser.dateOfBirth}<br>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-8">
                                ${currentUser.gender.name()}<br>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <li <c:if test="${(currentPage-1) <= 0}">class="disabled"</c:if>>
                        <c:url var="usersUrl" value="/users?currentPage=${(currentPage-1)}"/>
                        <a href="${usersUrl}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach var="iPage" begin="${currentPage}" end="${currentPage+4}" step="1">
                        <c:if test="${iPage>2 && iPage<=(countPages+2)}">
                            <c:url var="usersUrl" value="/users?currentPage=${iPage-2}"/>
                            <li <c:if test="${iPage==(currantPage+2)}">class=".active"</c:if>>
                                <a href="${usersUrl}">${iPage-2}</a></li>
                        </c:if>
                    </c:forEach>
                    <li <c:if test="${(currentPage+1)>(countPages)}">class="disabled"</c:if>>
                        <c:url var="usersUrl" value="/users?currentPage=${(currentPage+1)}"/>
                        <a href="${usersUrl}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
                <%--<form name="searchForm" method="post" action="result">--%>
                <%--<div class="form-group">--%>
                <%--<textarea class="form-control" rows="5" id="search" name="reqText"></textarea>--%>
                <%--</div>--%>
                <%--<button type="submit" class="btn-primary btn-lg btn-block active">Отправить</button>--%>
                <%--</form>--%>
                <%--</div>--%>
            <div class="col-lg-2"></div>
        </div>
    </div>
</tags:user>
