<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Вход - Таск Трекер</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { display: flex; align-items: center; justify-content: center; min-height: 100vh; background-color: #f8f9fa; }
    .login-container { max-width: 400px; padding: 2rem; background-color: white; border-radius: 0.5rem; box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.1); }
  </style>
</head>
<body>
<div class="login-container">
  <h2 class="mb-4 text-center">Вход в Таск Трекер</h2>

  <c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/login">
    <div class="mb-3">
      <label for="username" class="form-label">Имя пользователя:</label>
      <input type="text" class="form-control" name="username" id="username" required>
    </div>
    <div class="mb-3">
      <label for="password" class="form-label">Пароль:</label>
      <input type="password" class="form-control" name="password" id="password" required>
    </div>
    <button type="submit" class="btn btn-primary w-100">Войти</button>
  </form>
  <p class="mt-3 text-center">
    Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Зарегистрироваться</a>
  </p>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
