<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 16.08.2017
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.registration" var="registration"/>
<fmt:setBundle basename="i18n.errorsMessage" var="errorsMessage"/>
<fmt:message var="title" bundle="${registration}" key="registration.title"/>
<tags:user title="${title}">
    <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-10">
            <form class="form-horizontal" name="registrationForm" method="post">
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <h2 class="text-center"><c:out value="${title}"/></h2>
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
                <div class="form-group">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-7">
                        <input class="form-control"
                               placeholder="<fmt:message bundle="${registration}" key="registration.firstName"/>"
                               id="firstName" name="firstName" value="<c:out value="${fields['firstName']}"/>">
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
                               placeholder="<fmt:message bundle="${registration}" key="registration.lastName"/>"
                               id="lastName" name="lastName" value="<c:out value="${fields['lastName']}"/>">
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
                               placeholder="<fmt:message bundle="${registration}" key="registration.username"/>"
                               id="username" name="username" value="<c:out value="${fields['login']}"/>">
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
                        <input type="password" class="form-control"
                               placeholder="<fmt:message bundle="${registration}" key="registration.password"/>"
                               id="password" name="password">
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
                        <input type="email" class="form-control"
                               placeholder="<fmt:message bundle="${registration}" key="registration.email"/>"
                               id="email" name="email" value="<c:out value="${fields['email']}"/>">
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
                        <label for="date" class="control-label"><fmt:message bundle="${registration}"
                                                                             key="registration.date"/>:</label>
                    </div>
                    <div class="col-lg-4">
                        <input type="date" id="date" name="dateOfBirth" class="form-control"
                               value="${fields['dateOfBirth']}">
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
                        <label for="gender" class="control-label"><fmt:message bundle="${registration}"
                                                                               key="registration.gender"/>:</label>
                    </div>
                    <div class="col-lg-4">
                        <label class="radio-inline"><input type="radio" name="gender" id="gender" value="M"
                                                           <c:if test="${fields['gender']=='M'}">checked</c:if>>
                            <fmt:message bundle="${registration}" key="registration.genderM"/></label>
                        <label class="radio-inline"><input type="radio" name="gender" id="gender" value="F"
                                                           <c:if test="${fields['gender']=='F'}">checked</c:if>>
                            <fmt:message bundle="${registration}" key="registration.genderW"/></label>
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
                        <button type="submit" class="btn-primary btn-lg btn-block active"><fmt:message
                                bundle="${registration}" key="registration.enter"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</tags:user>