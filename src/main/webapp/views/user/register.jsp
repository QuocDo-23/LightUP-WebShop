<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký - LightUp</title>
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/register.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="container right-panel-active">
    <div class="form-container register-container">
        <form action="register" method="post" id="registerForm" novalidate>
            <h1>Đăng Ký</h1>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="bi bi-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <div class="input-group">
                <label for="name"><i class="bi bi-person-fill"></i>Họ và tên</label>
                <input id="name" name="name" type="text" placeholder="Nhập tên"
                       value="${param.name}" required>
            </div>

            <div class="input-group">
                <label for="email"><i class="bi bi-envelope-fill"></i>Email</label>
                <input id="email" name="email" type="email" placeholder="Nhập email"
                       value="${param.email}" required>
            </div>

            <div class="input-group">
                <label for="password"><i class="bi bi-key-fill"></i>Mật Khẩu</label>
                <input id="password" name="password" type="password"
                       placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)" required>
            </div>

            <div class="input-group">
                <label for="confirm-password"><i class="bi bi-key-fill"></i>Xác Nhận Mật Khẩu</label>
                <input id="confirm-password" name="confirmPassword" type="password"
                       placeholder="Nhập lại mật khẩu" required>
            </div>

            <button type="submit">Đăng ký</button>
        </form>
    </div>

    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-left">
                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="Logo">
                <h1 class="title">Chào Bạn</h1>
                <p>Nếu bạn có tài khoản, hãy đăng nhập tại đây</p>
                <a href="login">
                    <button class="ghost" id="login">
                        <i class="bi bi-arrow-left"></i> Đăng Nhập
                    </button>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>