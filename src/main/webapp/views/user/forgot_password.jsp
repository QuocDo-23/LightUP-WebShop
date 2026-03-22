<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/forgot_password.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>

<header class="cp-header">
    <div class="header-containt">
        <div class="header-left">
            <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" class="header-logo">
            <h2>Tạo mật khẩu mới</h2>
        </div>
        <div class="header-right">
            <a href="${pageContext.request.contextPath}/contact">Bạn cần giúp đỡ?</a>
        </div>
    </div>
</header>

<div class="fp-container">
    <div class="container">
        <form action="${pageContext.request.contextPath}/forgot-password"
              method="post"
              class="fp-form">

            <div class="abc">
                <div class="back-btn">
                    <a href="${pageContext.request.contextPath}/login">
                        <i class="bi bi-arrow-left-circle"></i>
                    </a>
                </div>
                <h2>Khôi phục mật khẩu</h2>
            </div>

            <div class="form-group">
                <label>Email của bạn</label>
                <input type="email" name="email" required>
            </div>

            <button type="submit" class="btn-request">Gửi yêu cầu</button>

            <c:if test="${not empty message}">
                <p style="color:green">${message}</p>
            </c:if>

            <c:if test="${not empty error}">
                <p style="color:red">${error}</p>
            </c:if>

            <div class="fp-footer">
                <p>Bạn đã nhớ mật khẩu? <a href="${pageContext.request.contextPath}/views/user/login.jsp">Đăng nhập ngay</a></p>
            </div>

        </form>
    </div>
</div>

</body>
</html>
