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
<fmt:message var="title" bundle="${update_profile}" key="update_profile.title_profile"/>
<jsp:useBean id="user" type="model.User" scope="session"/>

<tags:user title="${title}">
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-10">
            <form class="form-horizontal" name="changeProfileForm" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <h2 class="text-center"><c:out value="${title}"/></h2>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <c:url var="userUrl" value="/profile_${currentUser.id}"/>
                        <a href="${userUrl}">
                            <img class="img-rounded img-responsive center-block" align="middle"
                                 src="${pageContext.request.contextPath}/resources/img/avatar_default.png">
                        </a>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${not empty errors['other']}">
                        <div class="form-group">
                            <div class="col-lg-1"></div>
                            <div class="col-lg-7">
                                <div class="alert alert-danger">
                                    <fmt:message bundle="${errorsMessage}" key="errorsMessage.otherError"/>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <br>
                    </c:otherwise>
                </c:choose>
            </form>
            <%--<c:url var="uploadPhoto" value="/avatar/changer"/>--%>
            <%--<form action="${uploadPhoto}" method="post" enctype="multipart/form-data">--%>
                <%--<input name="data" type="file"><br>--%>
                <%--<input type="submit"><fmt:message bundle="${update_profile}" key="update_profile.loadAvatar"/><br>--%>
            <%--</form>--%>
            <c:url var="updateProfileUrl" value="/profile/changer"/>
            <form class="form-horizontal" name="changeProfileForm" action="${updateProfileUrl}" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.firstName"/>"
                               id="firstName" name="firstName" value="<c:out value="${user.firstName}"/>">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['firstName']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['firstName']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty fields.firstName}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.lastName"/>"
                               id="lastName" name="lastName" value="<c:out value="${user.lastName}"/>">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['lastName']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['lastName']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty fields.lastName}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input type="text" class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.username"/>"
                               id="username" name="username" value="<c:out value="${user.login}"/>">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['login']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['login']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty fields.login}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input type="email" class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.email"/>"
                               id="email" name="email" value="<c:out value="${user.email}"/>">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['email']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['email']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty fields.email}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-3">
                        <label for="date" class="control-label"><fmt:message bundle="${update_profile}"
                                                                             key="update_profile.date"/>:</label>
                    </div>
                    <div class="col-lg-4">
                        <input type="date" id="date" name="dateOfBirth" class="form-control"
                               value="${user.dateOfBirth}">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['dateOfBirth']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['dateOfBirth']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty fields.dateOfBirth}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-3">
                        <label for="gender" class="control-label"><fmt:message bundle="${update_profile}"
                                                                               key="update_profile.gender"/>:</label>
                    </div>
                    <div class="col-lg-4">
                        <label class="radio-inline"><input type="radio" name="gender" id="gender" value="M"
                                                           <c:if test="${user.gender.name()=='MALE'}">checked</c:if>>
                            <fmt:message bundle="${update_profile}" key="update_profile.genderM"/></label>
                        <label class="radio-inline"><input type="radio" name="gender" id="gender" value="F"
                                                           <c:if test="${user.gender.name()=='FEMALE'}">checked</c:if>>
                            <fmt:message bundle="${update_profile}" key="update_profile.genderW"/></label>
                    </div>

                    <c:choose>
                        <c:when test="${not empty errors['gender']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['gender']}"/></label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty fields.gender}">
                                <div class="col-lg-4-offset has-success">
                                    <label class=" control-label" style="text-align: left">
                                        <span class="glyphicon glyphicon-ok"></span></label>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input type="password" class="form-control"
                               placeholder="<fmt:message bundle="${update_profile}" key="update_profile.currentPassword"/>"
                               id="currentPassword" name="currentPassword">
                    </div>
                    <c:choose>
                        <c:when test="${not empty errors['password']}">
                            <div class="col-lg-4-offset has-error">
                                <label class=" control-label" style="text-align: left">
                                    <span class="glyphicon glyphicon-remove"></span><fmt:message
                                        bundle="${errorsMessage}"
                                        key="errorsMessage.${errors['password']}"/></label>
                            </div>
                        </c:when>
                    </c:choose>
                </div>

                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <button type="submit" class="btn-primary btn-lg btn-block active"><fmt:message
                                bundle="${update_profile}" key="update_profile.enter"/></button>
                    </div>
                </div>
            </form>
            <c:url var="changeMyPassword" value="/password/changer"/>
            <form class="form-horizontal" action="${changeMyPassword}" method="get">
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
