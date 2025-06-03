<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Список проектов - Таск Трекер</title>
</head>
<body>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1>Проекты</h1>
    <a href="${pageContext.request.contextPath}/project/create" class="btn btn-primary">Создать проект</a>
  </div>

  <c:if test="${not empty param.deleted}">
    <div class="alert alert-success">Проект успешно удален.</div>
  </c:if>

  <c:choose>
    <c:when test="${empty projects}">
      <p>Пока нет ни одного проекта. <a href="${pageContext.request.contextPath}/project/create">Создать первый?</a></p>
    </c:when>
    <c:otherwise>
      <table class="table table-hover">
        <thead>
        <tr>
          <th>ID</th>
          <th>Название</th>
          <th>Описание</th>
          <th>Владелец ID</th>
          <th>Дата создания</th>
          <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="project" items="${projects}">
          <tr>
            <td>${project.id}</td>
            <td><a href="${pageContext.request.contextPath}/project/view?id=${project.id}">${project.name}</a></td>
            <td><c:out value="${project.description}" escapeXml="true"/></td>
            <td>${project.ownerId}</td>
            <td>
              <fmt:formatDate value="${project.createdAt}" pattern="dd.MM.yyyy HH:mm" />
            </td>
            <td>
              <a href="${pageContext.request.contextPath}/project/view?id=${project.id}" class="btn btn-sm btn-info">Открыть</a>
              <c:if test="${project.ownerId == sessionScope.currentUser.id || sessionScope.currentUser.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/project/edit?id=${project.id}" class="btn btn-sm btn-warning">Редакт.</a>
                <form action="${pageContext.request.contextPath}/project/delete" method="post" style="display:inline;" onsubmit="return confirm('Вы уверены, что хотите удалить этот проект? Все связанные задачи также будут удалены.');">
                  <input type="hidden" name="id" value="${project.id}">
                  <button type="submit" class="btn btn-sm btn-danger">Удалить</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
