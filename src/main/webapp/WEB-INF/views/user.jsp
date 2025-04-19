<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<% request.setAttribute("pageTitle", "Пользователь"); %>

<c:choose>
  <c:when test="${not empty user}">
    <p><strong>Имя пользователя:</strong> <c:out value='${user.username}'/></p>
    <p><strong>Email:</strong> <c:out value='${user.email}'/></p>
  </c:when>
  <c:otherwise>
    <p>Пользователь не найден.</p>
  </c:otherwise>
</c:choose>
<%@ include file="footer.jsp" %>
