<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 06.09.2017
  Time: 20:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.users" var="users"/>
<fmt:message var="title" bundle="${users}" key="users.title"/>
<jsp:useBean id="usersList" scope="request" type="java.util.List"/>
<tags:user title="${title}">
    <h2><c:out value="${title}"/></h2>
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-8">
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
                    <c:if test="${page > 5}">
                        <li>
                            <c:url var="usersUrl" value="/users">
                                <c:param name="page" value="1"/>
                            </c:url>
                            <a href="${usersUrl}" aria-label="First">1</a>
                        </li>
                    </c:if>
                    <c:if test="${(page-1) > 0}">
                        <li>
                            <c:url var="usersUrl" value="/users">
                                <c:param name="page" value="${(page-1)}"/>
                            </c:url>
                            <a href="${usersUrl}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>
                    <c:forEach var="iPage" begin="${startPage}" end="${endPage}" step="1">
                        <li <c:if test="${iPage==page}">class=".active"</c:if>>
                            <c:url var="usersUrl" value="/users">
                                <c:param name="page" value="${iPage}"/>
                            </c:url>
                            <a href="${usersUrl}">${iPage}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${(page+1)<=(countPages)}">
                        <li>
                            <c:url var="usersUrl" value="/users">
                                <c:param name="page" value="${(page+1)}"/>
                            </c:url>
                            <a href="${usersUrl}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
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
