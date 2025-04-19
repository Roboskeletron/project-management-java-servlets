<% request.setAttribute("pageTitle", "Регистрация"); %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>

<form action="<c:url value='/register'/>" method="post">
  <label>Логин:
    <input type="text" name="username" value="<c:out value='${param.username}'/>" required/>
  </label><br/>
  <label>Email:
    <input type="email" name="email" value="<c:out value='${param.email}'/>" required/>
  </label><br/>
  <label>Пароль:
    <input type="password" name="password" required/>
  </label><br/>
  <button type="submit">Зарегистрироваться</button>
</form>
<%@ include file="footer.jsp" %>

