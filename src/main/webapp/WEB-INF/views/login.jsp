<% request.setAttribute("pageTitle", "Login"); %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<form action="<c:url value='/login'/>" method="post">
  <label>Email:
    <input type="text" name="email" value="<c:out value='${param.username}'/>" required/>
  </label><br/>
  <label>Пароль:
    <input type="password" name="password" required/>
  </label><br/>
  <button type="submit">Login</button>
</form>
<%@ include file="footer.jsp" %>
