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
<fmt:setBundle basename="i18n.messages" var="messages"/>
<fmt:message var="title" bundle="${messages}" key="messages.title"/>
<jsp:useBean id="convList" type="java.util.List<model.Conversation>" scope="request"/>
<tags:user title="${title}">
    <h2><c:out value="${title}"/></h2>
    <div class="row">
        <div class="col-sm-4" style="padding-right: 0">
            <ul class="list-group">
                <c:forEach items="${convList}" var="conversation">
                    <c:url var="msgUrl" value="/messages_${conversation.id}"/>
                    <a href="${msgUrl}"
                       class="list-group-item <c:if test="${activ_conv_id == conversation.id}">active</c:if>">
                        <form class="form-inline pull-right vertical-align" style="margin-top: -3px;"
                              id="delete_conv" method="post" action="${deleteConvUrl}">
                            <c:url var="deleteConvUrl" value="/messages/changer"/>
                            <input type="hidden" name="conv_id" value="${conversation.id}">
                            <input type="hidden" name="action" value="deleteConversation">
                            <c:if test="${conversation.countUnread > 0}">
                                <span class="badge" style="margin-top: 3px"><c:out
                                        value="${conversation.countUnread}"/></span>
                            </c:if>
                            <button class="unstyled-button" style="vertical-align: middle">
                                <span class="glyphicon glyphicon-remove pull-right"></span>
                            </button>
                        </form>
                        <span class="text">
                            <c:out value="${conversation.firstNameUser} ${conversation.lastNameUser}"/>
                        </span>
                    </a>
                </c:forEach>
            </ul>
        </div>
        <div class="col-sm-8" style="padding-left: 0">
            <div class="panel panel-default">
                <div class="panel-body fixed-chat" style="padding: 0;">
                    <div class="chatbody">
                        <table class="table">
                            <c:if test="${msgList != null}">
                                <c:forEach items="${msgList}" var="msg">
                                    <tr>
                                        <td><img src="http://via.placeholder.com/50x50?text=A"/></td>
                                        <td class="break-all-word" style="padding-left: 0;padding-right: 0;">
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
                                        <td style="padding-left: 0;padding-right: 0; text-align: center">
                                            <h7>
                                                <fmt:formatDate type="time" value="${msg.ts_action}"/>
                                                <fmt:formatDate pattern="dd/MM/yy" value="${msg.ts_action}"/>
                                            </h7>

                                        </td>
                                        <td style="padding-left: 0;padding-right: 0;">
                                            <c:url var="deleteMsgUrl" value="/messages/changer"/>
                                            <form id="delete_msg" method="post"
                                                  action="${deleteMsgUrl}">
                                                <input type="hidden" name="msg_id" value="${msg.id}">
                                                <input type="hidden" name="conv_id" value="${msg.conv_id}">
                                                <input type="hidden" name="action" value="deleteMessage">
                                                <button class="remove_button_css">
                                                    <span class="glyphicon glyphicon-remove pull-right"></span>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </div>
                </div>
            </div>

            <div class="row">
                <form method="post">
                    <c:if test="${otherUserID != null}">
                        <input type="hidden" name="otherUserID" value="${otherUserID}">
                        <input type="hidden" name="action" value="write">
                    </c:if>
                    <div class="col-xs-9">
                        <fmt:message var="write" bundle="${messages}" key="messages.write"/>
                        <input type="text" placeholder="${write}" name="message" id="message" class="form-control"/>
                    </div>
                    <div class="col-xs-3">
                        <button class="btn btn-info btn-block"><fmt:message bundle="${messages}" key="messages.btn_send"/></button>
                    </div>
                </form>
            </div>

        </div>
    </div>
</tags:user>