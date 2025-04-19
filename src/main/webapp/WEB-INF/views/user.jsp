<jsp:directive.include file="/WEB-INF/views/layout.jsp"/>
<% request.setAttribute("pageTitle", "Пользователь"); %>

<c:choose>
  <c:when test="${not empty user}">
    <p><strong>ID:</strong> <c:out value='${user.id}'/></p>
    <p><strong>Логин:</strong> <c:out value='${user.username}'/></p>
    <p><strong>Email:</strong> <c:out value='${user.email}'/></p>
  </c:when>
  <c:otherwise>
    <p>Пользователь не найден.</p>
  </c:otherwise>
</c:choose>

