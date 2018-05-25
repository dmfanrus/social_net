<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.update_profile" var="update_profile"/>
<fmt:setBundle basename="i18n.errorsMessage" var="errorsMessage"/>
<fmt:message var="title" bundle="${update_profile}" key="update_profile.title_password"/>
<jsp:useBean id="user" type="model.User" scope="session"/>

<tags:user title="${title}">
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-10">
            <c:url var="changeMyPassword" value="/password/changer"/>
            <form class="form-horizontal" name="changeProfileForm" action="${changeMyPassword}" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <h2 class="text-center"><c:out value="${title}"/></h2>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input type="password" class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.currentPassword"/>"
                               id="currentPassword" name="currentPassword">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['currentPassword']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['currentPassword']}"/></label>
                            </div>
                        </c:when>
                    </c:choose>
                </div>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input type="password" class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.newPassword"/>"
                               id="newPassword" name="newPassword">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['newPassword']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['newPassword']}"/></label>
                            </div>
                        </c:when>
                    </c:choose>
                </div>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <button type="submit" class="btn-primary btn-lg btn-block active"><fmt:message
                                bundle="${update_profile}" key="update_profile.changePass"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</tags:user>
