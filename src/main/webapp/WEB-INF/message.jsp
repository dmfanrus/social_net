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
<fmt:setBundle basename="i18n.message" var="message"/>
<fmt:message var="title" bundle="${message}" key="message.title"/>
<jsp:useBean id="convList" type="java.util.List<model.Conversation>" scope="request"/>
<tags:user title="${title}">
    <h2><c:out value="${title}"/></h2>
    <div class="row">
        <div class="col-sm-4" style="padding-right: 0">
            <ul class="list-group">
                <c:forEach items="${convList}" var="conversation">
                    <c:url var="msgUrl" value="/message_${conversation.id}"/>
                    <a href="${msgUrl}" class="list-group-item">
                        <c:out value="${conversation.firstNameUser} ${conversation.lastNameUser}"/>
                        <c:if test="${conversation.countUnread > 0}">
                            <span class="badge pull-right"><c:out value="${conversation.countUnread}"/></span>
                        </c:if>
                    </a>
                </c:forEach>
            </ul>
        </div>
        <div class="col-sm-8">
            <div class="panel panel-default">
                <div class="panel-body fixed-chat">
                    <div class="chatbody">
                        <table class="table">
                            <c:if test="${msgList != null}">
                                <c:forEach items="${msgList}" var="msg">
                                    <tr>
                                        <td><img src="http://via.placeholder.com/50x50?text=A"/></td>
                                        <td>
                                            <div class="media">
                                                <div class="media-body">
                                                    <h5 class="media-heading"><b><c:out
                                                            value="${msg.lastName} ${msg.firstName}"/></b></h5>
                                                    <p>
                                                        <c:out value="${msg.message}"/>
                                                    </p>
                                                </div>
                                            </div>
                                        </td>
                                        <td><fmt:formatDate type="time" value="${msg.ts_action}"/></td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </div>
                </div>
            </div>

            <div class="row">
                <form method="post">
                    <input type="hidden" name="">
                    <div class="col-xs-9">
                        <input type="text" placeholder="Write..." name="message" id="message" class="form-control"/>
                    </div>
                    <div class="col-xs-3">
                        <button class="btn btn-info btn-block">Send</button>
                    </div>
                </form>
            </div>

        </div>
    </div>
</tags:user>