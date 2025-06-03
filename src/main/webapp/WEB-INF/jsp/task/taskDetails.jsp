<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Задача: ${task.title} - Таск Трекер</title>
</head>
<body>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1>Задача: <c:out value="${task.title}"/></h1>
    <div>
      <c:if test="${task.reporterId == sessionScope.currentUser.id || (task.assigneeId != null && task.assigneeId == sessionScope.currentUser.id) || sessionScope.currentUser.role == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/task/edit?id=${task.id}" class="btn btn-warning btn-sm">Редактировать задачу</a>
      </c:if>
      <a href="${pageContext.request.contextPath}/project/view?id=${task.projectId}" class="btn btn-info btn-sm">К проекту</a>
    </div>
  </div>

  <div class="card mb-4">
    <div class="card-body">
      <h5 class="card-title">Детали задачи</h5>
      <p><strong>Проект:</strong> <a href="${pageContext.request.contextPath}/project/view?id=${task.projectId}">${task.projectName}</a></p>
      <p><strong>Описание:</strong></p>
      <p style="white-space: pre-wrap;"><c:out value="${task.description}"/></p>
      <div class="row">
        <div class="col-md-4"><strong>Статус:</strong> <span class="badge bg-${task.status.dbValue == 'DONE' ? 'success' : (task.status.dbValue == 'IN_PROGRESS' ? 'primary' : 'secondary')}">${task.status.dbValue}</span></div>
        <div class="col-md-4"><strong>Приоритет:</strong> <span class="badge bg-${task.priority.dbValue == 'URGENT' ? 'danger' : (task.priority.dbValue == 'HIGH' ? 'warning' : 'info')}">${task.priority.dbValue}</span></div>
        <div class="col-md-4"><strong>Срок:</strong>
          <c:if test="${not empty task.dueDate}">${task.formattedDueDate}</c:if>
          <c:if test="${empty task.dueDate}">Не установлен</c:if>
        </div>
      </div>
      <div class="row mt-2">
        <div class="col-md-6"><strong>Репортер:</strong> ${task.reporterName}</div>
        <div class="col-md-6"><strong>Исполнитель:</strong> ${not empty task.assigneeName ? task.assigneeName : 'Не назначен'}</div>
      </div>
      <div class="row mt-2">
        <div class="col-md-6"><strong>Создана:</strong> ${task.formattedCreatedAt}</div>
        <div class="col-md-6"><strong>Обновлена:</strong> ${task.formattedUpdatedAt}</div>
      </div>
    </div>
  </div>

  <%-- Quick Actions (Change status, assignee) - Optional, can be complex --%>

  <hr>

  <%-- Comments Section --%>
  <div class="card mb-4">
    <div class="card-header">
      <h5>Комментарии</h5>
    </div>
    <div class="card-body">
      <c:forEach var="comment" items="${comments}">
        <div class="mb-2 p-2 border rounded">
          <strong>${comment.username}</strong>
          (${comment.formattedCreatedAt}):
          <p style="white-space: pre-wrap;"><c:out value="${comment.content}"/></p>
            <%-- Optional: Delete comment button --%>
        </div>
      </c:forEach>
      <c:if test="${empty comments}">
        <p>Комментариев пока нет.</p>
      </c:if>
    </div>
    <div class="card-footer">
      <form action="${pageContext.request.contextPath}/task/comment" method="post">
        <input type="hidden" name="taskId" value="${task.id}">
        <div class="mb-2">
          <textarea name="content" class="form-control" rows="2" placeholder="Добавить комментарий..." required></textarea>
        </div>
        <button type="submit" class="btn btn-primary btn-sm">Отправить</button>
      </form>
    </div>
  </div>

  <%-- History Section --%>
  <div class="card">
    <div class="card-header">
      <h5>История изменений</h5>
    </div>
    <ul class="list-group list-group-flush">
      <c:forEach var="entry" items="${history}">
        <li class="list-group-item">
          <strong>${entry.username}</strong>
          (${entry.formattedChangedAt})
          изменил(а) поле <em>"${entry.fieldChanged}"</em>
          <c:if test="${not empty entry.oldValue}">с "${entry.oldValue}"</c:if>
          на "${entry.newValue}".
        </li>
      </c:forEach>
      <c:if test="${empty history}">
        <li class="list-group-item">История изменений пуста.</li>
      </c:if>
    </ul>
  </div>
  <c:if test="${task.reporterId == sessionScope.currentUser.id || sessionScope.currentUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/task/delete" method="post" class="mt-3" onsubmit="return confirm('Вы уверены, что хотите удалить эту задачу?');">
      <input type="hidden" name="id" value="${task.id}">
      <button type="submit" class="btn btn-danger">Удалить задачу</button>
    </form>
  </c:if>

</div>
</body>
</html>