<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${isEditMode ? 'Редактирование' : 'Создание'} проекта - Таск Трекер</title>
</head>
<body>
<div class="container mt-4">
    <h1>${isEditMode ? 'Редактирование проекта' : 'Создание нового проекта'}</h1>

    <form method="post" action="${pageContext.request.contextPath}/project/${isEditMode ? 'edit' : 'create'}">
        <c:if test="${isEditMode}">
            <input type="hidden" name="id" value="${project.id}">
        </c:if>

        <div class="mb-3">
            <label for="name" class="form-label">Название проекта:</label>
            <input type="text" class="form-control" id="name" name="name" value="${project.name}" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Описание:</label>
            <textarea class="form-control" id="description" name="description" rows="3">${project.description}</textarea>
        </div>

        <%-- Owner is implicitly the current user on create, or not changed on edit by default --%>
        <%-- Add owner selection if ADMIN is editing/creating for others --%>

        <button type="submit" class="btn btn-primary">${isEditMode ? 'Сохранить изменения' : 'Создать проект'}</button>
        <a href="${pageContext.request.contextPath}/projects" class="btn btn-secondary">Отмена</a>
    </form>
</div>
</body>
</html>
