<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - LightUp</title>
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/login.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>

<body>
<div class="container" id="container">
    <div class="form-container login-container">
        <form action="login" method="post">
            <h1>Đăng Nhập</h1>

            <c:if test="${param.success == 'registered'}">
                <div class="alert alert-success">
                    <i class="bi bi-check-circle"></i> Đăng ký thành công! Vui lòng đăng nhập.
                </div>
            </c:if>

            <c:if test="${param.success == 'password_reset'}">
                <div class="alert alert-success">
                    <i class="bi bi-check-circle"></i> Đặt lại mật khẩu thành công!
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="bi bi-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <div class="input-group">
                <label for="email"><i class="bi bi-envelope-fill"></i>Email</label>
                <input id="email" name="email" type="email" placeholder="Nhập email"
                       value="${param.email}" required>
            </div>

            <div class="input-group">
                <label for="password"><i class="bi bi-key-fill"></i>Mật Khẩu</label>
                <input id="password" name="password" type="password"
                       placeholder="Nhập mật khẩu" required>
            </div>

            <input type="hidden" name="redirect" value="${param.redirect}">

            <a href="${pageContext.request.contextPath}/views/user/forgot_password.jsp" class="forgot-password">Quên mật khẩu?</a>

            <button type="submit">Đăng Nhập</button>
            <span>Hoặc đăng nhập bằng</span>
            <div class="social-buttons">
                <a href="${pageContext.request.contextPath}/google-login" class="btn btn-google">
                    <svg class="icon" width="20" height="20" viewBox="0 0 24 24">
                        <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"></path>
                        <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"></path>
                        <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"></path>
                        <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"></path>
                    </svg>
                    <span>Đăng nhập với Google</span>
                </a>

            </div>

        </form>
    </div>

    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-right">
                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="Logo">
                <h1 class="title">Xin Chào!</h1>
                <p>Nếu bạn chưa có tài khoản, hãy đăng ký tại đây</p>
                <a href="register">
                    <button class="ghost" id="register">
                        Đăng Ký <i class="bi bi-arrow-right"></i>
                    </button>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>