<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>${isEditMode ? 'Редактирование' : 'Создание'} задачи - Таск Трекер</title>
</head>
<body>
<div class="container mt-4">
  <h1>${isEditMode ? 'Редактирование задачи' : 'Новая задача'}</h1>
  <p>Для проекта ID: ${projectId}</p> <%-- or ${task.projectId} if editing --%>

  <form method="post" action="${pageContext.request.contextPath}/task/${isEditMode ? 'edit' : 'create'}">
    <input type="hidden" name="projectId" value="${isEditMode ? task.projectId : projectId}">
    <c:if test="${isEditMode}">
      <input type="hidden" name="id" value="${task.id}">
    </c:if>

    <div class="mb-3">
      <label for="title" class="form-label">Заголовок:</label>
      <input type="text" class="form-control" id="title" name="title" value="${task.title}" required>
    </div>

    <div class="mb-3">
      <label for="description" class="form-label">Описание:</label>
      <textarea class="form-control" id="description" name="description" rows="5">${task.description}</textarea>
    </div>

    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="status" class="form-label">Статус:</label>
        <select class="form-select" id="status" name="status">
          <c:forEach var="s" items="${applicationScope.TaskStatus}">
            <option value="${s.dbValue}" ${task.status == s ? 'selected' : ''}>${s.dbValue}</option>
          </c:forEach>
        </select>
      </div>
      <div class="col-md-6 mb-3">
        <label for="priority" class="form-label">Приоритет:</label>
        <select class="form-select" id="priority" name="priority">
          <c:forEach var="p" items="${applicationScope.TaskPriority}">
            <option value="${p.dbValue}" ${task.priority == p ? 'selected' : ''}>${p.dbValue}</option>
          </c:forEach>
        </select>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="assigneeId" class="form-label">Исполнитель:</label>
        <select class="form-select" id="assigneeId" name="assigneeId">
          <option value="0">Не назначен</option>
          <c:forEach var="user" items="${users}">
            <option value="${user.id}" ${task.assigneeId == user.id ? 'selected' : ''}>${user.fullName} (${user.username})</option>
          </c:forEach>
        </select>
      </div>
      <div class="col-md-6 mb-3">
        <label for="dueDate" class="form-label">Срок выполнения:</label>
        <input type="date" class="form-control" id="dueDate" name="dueDate" value="${task.dueDateForInput}">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">${isEditMode ? 'Сохранить изменения' : 'Создать задачу'}</button>
    <a href="${pageContext.request.contextPath}/project/view?id=${isEditMode ? task.projectId : projectId}" class="btn btn-secondary">Отмена</a>
  </form>
</div>
</body>
</html>
