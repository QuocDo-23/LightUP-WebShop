<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Đổi Mật Khẩu</title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/views/CSS/profile.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/views/CSS/style.css">
</head>

<body>

<main>

    <div class="header">
        <div class="header-cont">

            <a href="./" class="logo-link">
                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
                <h1>Đổi Mật Khẩu</h1>
            </a>

            <a href="./">
                <div class="logout-btn">
                    Quay về trang chủ
                </div>
            </a>

        </div>
    </div>

    <div class="container">

        <div class="main-content">

            <jsp:include page="/views/layout/siderbar_user.jsp"/>

            <div class="profile-container">

                <div class="content-area">

                    <h2 class="section-title">
                        Đổi Mật Khẩu
                    </h2>
                    <c:if test="${not empty message}">
                        <div class="message ${messageType}">
                                ${message}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/change-password"
                          method="post">

                        <div class="form-group">

                            <label>Mật khẩu hiện tại</label>

                            <input type="password"
                                   name="currentPassword"
                                   required>

                        </div>

                        <div class="form-group">

                            <label>Mật khẩu mới</label>

                            <input type="password"
                                   name="newPassword"
                                   required>

                        </div>

                        <div class="form-group">

                            <label>Xác nhận mật khẩu mới</label>

                            <input type="password"
                                   name="confirmPassword"
                                   required>

                        </div>

                        <button type="submit"
                                class="save-btn">

                            Đổi Mật Khẩu

                        </button>

                    </form>

                </div>

            </div>

        </div>

    </div>

</main>

</body>
</html>