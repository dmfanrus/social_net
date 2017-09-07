<%--
  Created by IntelliJ IDEA.
  User: Михаил
  Date: 14.08.2017
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.help" var="help"/>
<fmt:message var="title" bundle="${help}" key="help.title"/>

<tags:user title="${title}">
    <h2>${title}</h2>
    <p>
        YourBooks - рекомендательный сервис книжной литературы.
        В нашей базе собрано более 1500 тысяч книг.
        Если вы не знаете что почитать то этот сервис для вас.
    </p>
    <p>
        Чтобы начать поиск вам необходимо написать описание тех книг которые вы бы хотели почитать.
        После этого, нажать кнопку отправить.
        Наша сверхумная система обработает ваш запрос и разработает для вас личную рекомендацию.
    </p>
    <br>
    <p>Вы также можете оставить свой комментарий разработчику.</p>
    <form action="php/send.php" method="post">
        <div class="form-group">
            <label for="name">Имя:</label>
            <input type="text" name="name" id="name" placeholder="Укажите имя">
            <br><br>
            <label for="message">Комментарий:</label>
            <textarea class="form-control" rows="3" id="message" name="message"></textarea>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Отправить</button>
            </div>
        </div>
    </form>
</tags:user>
