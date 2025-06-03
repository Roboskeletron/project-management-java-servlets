<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
    body { padding-top: 56px; /* Adjust based on navbar height */ }
    .navbar { margin-bottom: 1rem; }
</style>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Таск Трекер</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <c:if test="${not empty sessionScope.currentUser}">
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${pageContext.request.servletPath == '/projects'}">active</c:if>" href="${pageContext.request.contextPath}/projects">Проекты</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${pageContext.request.servletPath == '/project/create'}">active</c:if>" href="${pageContext.request.contextPath}/project/create">Создать проект</a>
                    </li>
                    <%-- Add more navigation links for tasks, dashboard, etc. --%>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <li class="nav-item">
                            <span class="navbar-text me-2">
                                Привет, <c:out value="${sessionScope.currentUser.username}"/>!
                                (<c:out value="${sessionScope.currentUser.role.dbValue}"/>)
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout">Выйти</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link <c:if test="${pageContext.request.servletPath == '/login'}">active</c:if>" href="${pageContext.request.contextPath}/login">Войти</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link <c:if test="${pageContext.request.servletPath == '/register'}">active</c:if>" href="${pageContext.request.contextPath}/register">Регистрация</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
