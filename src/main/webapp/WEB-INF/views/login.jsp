<% request.setAttribute("pageTitle", "Login"); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<c:if test="${not empty error}">
  <div class="error"><c:out value="${error}"/></div>
</c:if>
<form action="<c:url value='/login'/>" method="post">
  <label>Email:
    <input type="text" name="email" value="<c:out value='${param.email}'/>" required/>
  </label><br/>
  <label>Пароль:
    <input type="password" name="password" required/>
  </label><br/>
  <button type="submit">Войти</button>
</form>
<%@ include file="footer.jsp" %>
