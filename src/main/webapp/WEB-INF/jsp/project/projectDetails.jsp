<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Проект: ${project.name} - Таск Трекер</title>
</head>
<body>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center">
    <h1>Проект: <c:out value="${project.name}"/></h1>
    <div>
      <c:if test="${project.ownerId == sessionScope.currentUser.id || sessionScope.currentUser.role == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/project/edit?id=${project.id}" class="btn btn-warning btn-sm">Редактировать проект</a>
      </c:if>
      <a href="${pageContext.request.contextPath}/task/create?projectId=${project.id}" class="btn btn-success btn-sm">Добавить задачу</a>
    </div>
  </div>
  <p><strong>Описание:</strong> <c:out value="${project.description}"/></p>
  <p><strong>Владелец ID:</strong> ${project.ownerId}</p>
  <p><strong>Дата создания:</strong> ${project.formattedCreatedAt}</p>
  <c:if test="${not empty param.taskDeleted}">
    <div class="alert alert-success">Задача успешно удалена.</div>
  </c:if>

  <hr>
  <h2>Задачи в проекте</h2>
  <c:choose>
    <c:when test="${empty tasks}">
      <p>В этом проекте пока нет задач. <a href="${pageContext.request.contextPath}/task/create?projectId=${project.id}">Создать первую?</a></p>
    </c:when>
    <c:otherwise>
      <table class="table table-striped">
        <thead>
        <tr>
          <th>ID</th>
          <th>Название</th>
          <th>Статус</th>
          <th>Приоритет</th>
          <th>Исполнитель</th>
          <th>Срок</th>
          <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="task" items="${tasks}">
          <tr>
            <td>${task.id}</td>
            <td><a href="${pageContext.request.contextPath}/task/view?id=${task.id}">${task.title}</a></td>
            <td><span class="badge bg-${task.status.dbValue == 'DONE' ? 'success' : (task.status.dbValue == 'IN_PROGRESS' ? 'primary' : 'secondary')}">${task.status.dbValue}</span></td>
            <td><span class="badge bg-${task.priority.dbValue == 'URGENT' ? 'danger' : (task.priority.dbValue == 'HIGH' ? 'warning' : 'info')}">${task.priority.dbValue}</span></td>
            <td>${not empty task.assigneeName ? task.assigneeName : 'Не назначен'}</td>
            <td>${task.formattedDueDate}</td>
            <td>
              <a href="${pageContext.request.contextPath}/task/view?id=${task.id}" class="btn btn-sm btn-outline-info">Детали</a>
              <c:if test="${task.reporterId == sessionScope.currentUser.id || (task.assigneeId != null && task.assigneeId == sessionScope.currentUser.id) || sessionScope.currentUser.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/task/edit?id=${task.id}" class="btn btn-sm btn-outline-warning">Редакт.</a>
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
