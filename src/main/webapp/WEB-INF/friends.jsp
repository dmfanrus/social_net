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
<fmt:setBundle basename="i18n.friends" var="friends"/>
<fmt:message var="title" bundle="${friends}" key="friends.title"/>
<jsp:useBean id="usersList" scope="request" type="java.util.List<model.User>"/>
<tags:user title="${title}">
    <h2><c:out value="${title}"/></h2>
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-8">
            <form class="form-horizontal" name="friendsForm" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input class="form-control"
                               placeholder="<fmt:message bundle="${friends}" key="friends.fullName"/>"
                               name="fullName" id="fullName" value="<c:out value="${fullName}"/>">
                    </div>
                    <button class="btn btn-default" type="submit">
                        <fmt:message bundle="${friends}" key="friends.search"/>
                    </button>
                </div>
            </form>
            <br>
            <c:forEach items="${usersList}" var="currentUser">
                <div class="panel panel-default">
                    <div class="row">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-8">
                            <c:url var="userUrl" value="/profile_${currentUser.id}"/>
                            <a href="${userUrl}" aria-label="user">
                                <c:out value="${currentUser.lastName}"/> <c:out value="${currentUser.firstName}"/>
                            </a>
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
            <c:if test="${countPages>1}">
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <c:if test="${page > 5}">
                            <li>
                                <c:url var="friendsUrl" value="/friends">
                                    <c:param name="page" value="1"/>
                                </c:url>
                                <a href="${friendsUrl}" aria-label="First">1</a>
                            </li>
                        </c:if>
                        <c:if test="${(page-1) > 0}">
                            <li>
                                <c:url var="friendsUrl" value="/friends">
                                    <c:param name="page" value="${(page-1)}"/>
                                </c:url>
                                <a href="${friendsUrl}" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                        </c:if>
                        <c:forEach var="iPage" begin="${startPage}" end="${endPage}" step="1">
                            <li <c:if test="${iPage==page}">class=".active"</c:if>>
                                <c:url var="friendsUrl" value="/friends">
                                    <c:param name="page" value="${iPage}"/>
                                </c:url>
                                <a href="${friendsUrl}">${iPage}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${(page+1)<=(countPages)}">
                            <li>
                                <c:url var="friendsUrl" value="/friends">
                                    <c:param name="page" value="${(page+1)}"/>
                                </c:url>
                                <a href="${friendsUrl}" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>
            <div class="col-lg-2"></div>
        </div>
    </div>
</tags:user>