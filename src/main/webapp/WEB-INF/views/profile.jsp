<% request.setAttribute("pageTitle", "Профиль"); %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<c:set var="user" value="${requestScope.user}"/>

<p><strong>ID:</strong> <c:out value='${user.id}'/></p>
<p><strong>Логин:</strong> <c:out value='${user.username}'/></p>
<p><strong>Email:</strong> <c:out value='${user.email}'/></p>

<h2>Обновить профиль</h2>
<form action="<c:url value='/profile'/>" method="post">
  <label>Новый логин:
    <input type="text" name="username" value="<c:out value='${user.username}'/>"/>
  </label><br/>
  <label>Новый пароль:
    <input type="password" name="password"/>
  </label><br/>
  <button type="submit">Сохранить</button>
</form>

<form action="<c:url value='/logout'/>" method="post">
  <button type="submit">Выйти</button>
</form>
<%@ include file="footer.jsp" %>
