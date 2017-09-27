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
<fmt:setBundle basename="i18n.relationship" var="relationship"/>
<fmt:message var="title" bundle="${users}" key="users.title"/>
<jsp:useBean id="usersList" scope="request" type="java.util.List<model.User>"/>
<tags:user title="${title}">
    <h2><c:out value="${title}"/></h2>
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-8">
            <form class="form-horizontal" name="usersForm" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input class="form-control"
                               placeholder="<fmt:message bundle="${users}" key="users.fullName"/>"
                               name="fullName" id="fullName" value="<c:out value="${fullName}"/>">
                    </div>
                    <button class="btn btn-default" type="submit">
                        <fmt:message bundle="${users}" key="users.search"/>
                    </button>
                </div>
            </form>
            <br>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-1 col-sm-1 col-md-1"></div>
        <div class="col-lg-10 col-sm-10 col-md-10">
            <c:if test="${usersList.size() == 0}">
                <fmt:message bundle="${users}" key="users.notFound"/>
            </c:if>
            <c:forEach items="${usersList}" var="currentUser">
                <div class="panel panel-default">
                    <div class="row">
                            <%--<div class="media">--%>
                        <div class="col-lg-3 col-sm-3 col-md-4">
                                <%--<div class="col-lg-8 col-md-8 col-sm-8">--%>
                            <c:url var="userUrl" value="/profile_${currentUser.id}"/>
                                <%--<a class="pull-left" href="${userUrl}">--%>
                            <a href="${userUrl}">
                                <img class="img-rounded img-responsive"
                                    <%--<img class="media-object"--%>
                                     src="${pageContext.request.contextPath}/resources/img/avatar_default.png">
                            </a>
                                <%--<div class="media-body">--%>
                        </div>
                        <div class="col-lg-5 col-sm-5 col-md-8">
                            <br>
                            <h4>
                                <div class="row">
                                        <%--<div class="col-lg-12 col-md-8 col-sm-8">--%>
                                    <c:url var="userUrl" value="/profile_${currentUser.id}"/>
                                    <a href="${userUrl}" aria-label="user">
                                        <c:out value="${currentUser.lastName}"/> <c:out
                                            value="${currentUser.firstName}"/>
                                    </a>
                                        <%--</div>--%>
                                </div>
                            </h4>
                            <h5>
                                <div class="row">
                                    <span class="glyphicon glyphicon-envelope"></span> ${currentUser.email}
                                </div>
                            </h5>
                            <h5>
                                <div class="row">
                                    <span class="glyphicon glyphicon-gift"></span> ${currentUser.dateOfBirth}
                                </div>
                            </h5>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <c:if test="${currentUser.relationStatus.name()!='ME'}">
                                <c:url var="changerUrl" value="/users/changer"/>
                                <form action="${changerUrl}" method="post">
                                    <input class="hidden" name="fullName" value="${fullName}">
                                    <input class="hidden" name="page" value="${page}">
                                    <input class="hidden" name="otherUserID" value="${currentUser.id}">
                                    <div class="btn-group pull-right">
                                        <c:if test="${currentUser.relationStatus.name()!='UNKNOW'}">
                                            <button class="btn btn-custom" type="button">
                                                <fmt:message bundle="${relationship}"
                                                             key="relationship.${currentUser.relationStatus.name()}.label"/>
                                            </button>
                                        </c:if>
                                        <button type="button" class="btn btn-custom dropdown-toggle"
                                                data-toggle="dropdown">
                                            <span class="caret"></span>
                                            <span class="sr-only">Меню с переключением</span>
                                        </button>
                                        <c:choose>
                                            <c:when test="${currentUser.relationStatus.name() == 'NOTCONFIRMBYME'}">
                                                <ul class="dropdown-menu" role="menu">
                                                    <li>
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="confirmRequest">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.confirmRequest"/>
                                                        </button>
                                                    </li>
                                                    <li class="divider"></li>
                                                    <li>
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="deleteRequestFromOther">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.deleteRequestFromOther"/>
                                                        </button>
                                                    </li>
                                                </ul>
                                            </c:when>
                                            <c:when test="${currentUser.relationStatus.name() == 'NOTCONFIRMBYOTHER'}">
                                                <ul class="dropdown-menu">
                                                    <li role="presentation" class="dropdown-header">
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="deleteRequestFromMe">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.deleteRequestFromMe"/>
                                                        </button>
                                                    </li>
                                                </ul>
                                            </c:when>
                                            <c:when test="${currentUser.relationStatus.name() == 'FRIEND'}">
                                                <ul class="dropdown-menu">
                                                    <li role="presentation" class="dropdown-header">
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="deleteFriend">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.deleteFriend"/>
                                                        </button>
                                                    </li>
                                                </ul>
                                            </c:when>
                                            <c:when test="${currentUser.relationStatus.name() == 'BLOCKEDBYME'}">
                                                <ul class="dropdown-menu">
                                                    <li role="presentation" class="dropdown-header">
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="unblockUser">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.unblockUser"/>
                                                        </button>
                                                    </li>
                                                </ul>
                                            </c:when>
                                            <c:when test="${currentUser.relationStatus.name() == 'UNKNOW'}">
                                                <ul class="dropdown-menu">
                                                    <li>
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="sendRequest">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.sendRequest"/>
                                                        </button>
                                                    </li>
                                                    <li class="divider"></li>
                                                    <li>
                                                        <button class="btn btn-custom" name="action"
                                                                style="border: none"
                                                                value="blockUser">
                                                            <fmt:message bundle="${relationship}"
                                                                         key="relationship.action.blockUser"/>
                                                        </button>

                                                    </li>
                                                </ul>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-1 col-sm-1 col-md-1"></div>
        <div class="col-lg-9 col-sm-9 col-md-9">
            <c:if test="${countPages>1}">
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
            </c:if>
        </div>
    </div>
</tags:user>