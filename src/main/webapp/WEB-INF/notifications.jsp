<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 29.09.2017
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.users" var="users"/>
<fmt:setBundle basename="i18n.notification" var="notification"/>
<fmt:setBundle basename="i18n.notification_status" var="notification_status"/>
<fmt:message var="title" bundle="${notification}" key="notification.title"/>
<jsp:useBean id="notificationsList" scope="request" type="model.ListNotifications"/>
<tags:user title="${title}">
    <h2><c:out value="${title}"/></h2>
    <div class="row">
        <div class="col-sm-3" style="padding-right: 0">
            <ul class="list-group">
                <c:url var="msgUrl" value="/notifications">
                    <c:param name="setInterval" value="day"/>
                </c:url>
                <a href="${msgUrl}"
                   class="list-group-item <c:if test="${activeInterval == 'day'}">active</c:if>">
                    <c:if test="1 > 0">
                                <span class="badge pull-right"><c:out
                                        value="${notificationsList.countrs.get(0)}"/></span>
                    </c:if>
                    <fmt:message bundle="${notification}" key="notification.day"/>

                </a>
                <c:url var="msgUrl" value="/notifications">
                    <c:param name="setInterval" value="week"/>
                </c:url>
                <a href="${msgUrl}"
                   class="list-group-item <c:if test="${activeInterval == 'week'}">active</c:if>">
                    <c:if test="1 > 0">
                                <span class="badge pull-right"><c:out
                                        value="${notificationsList.countrs.get(1)}"/></span>
                    </c:if>
                    <fmt:message bundle="${notification}" key="notification.week"/>
                </a>
                <c:url var="msgUrl" value="/notifications">
                    <c:param name="setInterval" value="month"/>
                </c:url>
                <a href="${msgUrl}"
                   class="list-group-item <c:if test="${activeInterval == 'month'}">active</c:if>">
                    <c:if test="1 > 0">
                                <span class="badge pull-right"><c:out
                                        value="${notificationsList.countrs.get(2)}"/></span>
                    </c:if>
                    <fmt:message bundle="${notification}" key="notification.month"/>
                </a>
                <c:url var="msgUrl" value="/notifications">
                    <c:param name="setInterval" value="year"/>
                </c:url>
                <a href="${msgUrl}"
                   class="list-group-item <c:if test="${activeInterval == 'year'}">active</c:if>">
                    <c:if test="1 > 0">
                                <span class="badge pull-right"><c:out
                                        value="${notificationsList.countrs.get(3)}"/></span>
                    </c:if>
                    <fmt:message bundle="${notification}" key="notification.year"/>
                </a>
            </ul>
        </div>
        <div class="col-sm-9">
            <table class="table">
                <c:if test="${notificationsList.notificationList != null}">
                    <c:choose>
                        <c:when test="${notificationsList.notificationList.size()==0}">
                            <tr>
                                <td>
                                    <P style="text-align: center">
                                        <fmt:message bundle="${notification}" key="notification.emptyList"/>
                                    </P>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${notificationsList.notificationList}" var="currentNotification">
                                <tr>
                                    <td class="break-all-word" style="padding-left: 0;padding-right: 0;">
                                        <p>
                                            <fmt:message bundle="${notification_status}"
                                                         key="notification_status.sender_user"/>
                                                <%--<c:if test="${currentNotification.not_status>=16}">--%>
                                                <%--<c:url var="userUrl" value="/profile_${currentNotification.sender_id}"/>--%>
                                                <%--</c:if>--%>
                                                <%--<c:if test="${currentNotification.not_status<16}">--%>
                                            <c:url var="userUrl" value="/profile_${currentNotification.recipient_id}"/>
                                                <%--</c:if>--%>
                                            <a href="${userUrl}" aria-label="user">
                                                <c:out value="${currentNotification.lastName} ${currentNotification.firstName}"/>
                                            </a>
                                            <fmt:message bundle="${notification_status}"
                                                         key="notification_status.status_${currentNotification.not_status}"/>
                                        </p>
                                    </td>

                                    <td style="padding-left: 0;padding-right: 0; text-align: center" class="pull-right">
                                        <h7>
                                            <fmt:formatDate type="time" value="${currentNotification.ts_action}"/><br>
                                            <fmt:formatDate pattern="dd/MM/yy"
                                                            value="${currentNotification.ts_action}"/>
                                        </h7>

                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </table>
            <div class="row">
                <c:if test="${countPages>1}">
                    <nav aria-label="Page navigation">
                        <ul class="pagination">
                            <c:if test="${page > 5}">
                                <li>
                                    <c:url var="notificationsUrl" value="/notifications">
                                        <c:param name="page" value="1"/>
                                        <c:param name="setInterval" value="${activeInterval}"/>
                                    </c:url>
                                    <a href="${notificationsUrl}" aria-label="First">1</a>
                                </li>
                            </c:if>
                            <c:if test="${(page-1) > 0}">
                                <li>
                                    <c:url var="notificationsUrl" value="/notifications">
                                        <c:param name="page" value="${(page-1)}"/>
                                        <c:param name="setInterval" value="${activeInterval}"/>
                                    </c:url>
                                    <a href="${notificationsUrl}" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>
                            <c:forEach var="iPage" begin="${startPage}" end="${endPage}" step="1">
                                <li <c:if test="${iPage==page}">class=".active"</c:if>>
                                    <c:url var="notificationsUrl" value="/notifications">
                                        <c:param name="page" value="${iPage}"/>
                                        <c:param name="setInterval" value="${activeInterval}"/>
                                    </c:url>
                                    <a href="${notificationsUrl}">${iPage}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${(page+1)<=(countPages)}">
                                <li>
                                    <c:url var="notificationsUrl" value="/notifications">
                                        <c:param name="page" value="${(page+1)}"/>
                                        <c:param name="setInterval" value="${activeInterval}"/>
                                    </c:url>
                                    <a href="${notificationsUrl}" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
    </div>
</tags:user>