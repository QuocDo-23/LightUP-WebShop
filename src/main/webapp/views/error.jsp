<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/error.css">
    <link href="https://fonts.googleapis.com/css?family=Monsieur+La+Doulaise" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Literata" rel="stylesheet">
    <title>Oops - LightUP</title>

</head>
<body>
<main class="err-main">
    <div class="err-card">
        <div class="deco deco-1"></div>
        <div class="deco deco-2"></div>

        <span class="err-lamp">&#128161;</span>

        <div class="err-code">
            <c:choose>
                <c:when test="${not empty statusCode}">${statusCode}</c:when>
                <c:when test="${pageContext.errorData.statusCode > 0}">${pageContext.errorData.statusCode}</c:when>
                <c:otherwise>404</c:otherwise>
            </c:choose>
        </div>

        <div class="err-divider"></div>

        <h2 class="err-title">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 403}">Truy cập bị từ chối</c:when>
                <c:when test="${pageContext.errorData.statusCode == 500}">Lỗi hệ thống</c:when>
                <c:when test="${pageContext.errorData.statusCode == 503}">Dịch vụ tạm ngưng</c:when>
                <c:otherwise>Không tìm thấy trang</c:otherwise>
            </c:choose>
        </h2>

        <p class="err-desc">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 403}">
                    Bạn không có quyền truy cập trang này! Vui lòng đăng nhập hoặc liên hệ hỗ trợ.
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 500}">
                    Hệ thống đang gặp sự cố, chúng tôi đang xử lý và sẽ sớm khắc phục. Xin lỗi vì sự bất tiện này!
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 503}">
                    Dịch vụ tạm thời không khả dụng, vui lòng thử lại sau ít phút!
                </c:when>
                <c:otherwise>
                    Trang bạn đang tìm không tồn tại hoặc đã bị di chuyển. Vui lòng thử lại với một đường dẫn khác.
                </c:otherwise>
            </c:choose>
        </p>

        <div class="err-actions">
            <a href="${pageContext.request.contextPath}/" class="btn-primary">
                <i class="bi bi-house-door-fill"></i> Về trang chủ
            </a>
            <a href="javascript:history.back()" class="btn-secondary">
                <i class="bi bi-arrow-left"></i> Quay lại
            </a>
        </div>

        <div class="err-suggest">
            <p>Bạn có thể quan tâm đến:</p>
            <div class="suggest-links">
                <a href="${pageContext.request.contextPath}/products" class="suggest-link">
                    <i class="bi bi-lightbulb"></i> Sản Phẩm
                </a>
                <a href="${pageContext.request.contextPath}/about" class="suggest-link">
                    <i class="bi bi-info-circle"></i> Giới Thiệu
                </a>
                <a href="${pageContext.request.contextPath}/news" class="suggest-link">
                    <i class="bi bi-newspaper"></i> Tin Tức
                </a>
                <a href="${pageContext.request.contextPath}/contact" class="suggest-link">
                    <i class="bi bi-envelope"></i> Liên Hệ
                </a>
            </div>
        </div>
    </div>
</main>
</body>
</html>
