<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/order.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/confirm.css">

    <title>Đơn hàng của tôi</title>
</head>
<body>
<main>
    <div class="header">
        <div class="header-cont">
            <a href="./" class="logo-link">
                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="logo">
                <h1>Thông Tin</h1>
            </a>
            <a href="./">
                <div class="logout-btn">Quay về trang chủ</div>
            </a>
        </div>
    </div>

    <div class="container">
        <div class="main-content">
            <jsp:include page="/views/layout/siderbar_user.jsp"/>


            <div class="profile-container">
                <div class="content-area">
                    <h2 class="section-title">📦 Đơn Hàng Của Tôi</h2>

                    <c:choose>
                        <c:when test="${empty orders}">
                            <div class="no-orders">
                                <i class="bi bi-bag-x"></i>
                                <h3>Chưa có đơn hàng nào</h3>
                                <p>Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm ngay!</p>
                                <a href="products" class="btn btn-primary">Mua sắm ngay</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="orders-grid">
                                <c:forEach var="order" items="${orders}">
                                    <div class="order-card">
                                        <div class="order-header">
                                            <span class="order-id">#DH${String.format("%06d", order.id)}</span>
                                            <span class="order-status status-${order.status}">
                                                    <c:choose>
                                                        <c:when test="${order.status == 'pending'}">Chờ xác nhận</c:when>
                                                        <c:when test="${order.status == 'processing'}">Đã xác nhận</c:when>
                                                        <c:when test="${order.status == 'shipped'}">Đang giao hàng</c:when>
                                                        <c:when test="${order.status == 'delivered'}">Đã giao hàng</c:when>
                                                        <c:when test="${order.status == 'cancelled'}">Đã hủy</c:when>
                                                        <c:otherwise>${order.status}</c:otherwise>
                                                    </c:choose>
                                                </span>
                                        </div>

                                        <div class="order-info">
                                            <div class="info-item">
                                                <span class="info-label">Ngày đặt hàng</span>
                                                <span class="info-value">
                                                        ${order.orderDateFormatted}
                                                </span>

                                            </div>
                                            <div class="info-item">
                                                <span class="info-label">Người nhận</span>
                                                <span class="info-value">${order.recipientName}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="info-label">Phương thức thanh toán</span>
                                                <span class="info-value">
                                                        <c:set var="payment" value="${paymentMap[order.id]}"/>
                                                        <c:choose>
                                                            <c:when test="${not empty payment}">
                                                                <c:choose>
                                                                    <c:when test="${payment.paymentMethod == 'COD'}">Thanh toán khi nhận hàng</c:when>
                                                                    <c:when test="${payment.paymentMethod == 'bank_transfer'}">Chuyển khoản</c:when>
                                                                    <c:otherwise>${payment.paymentMethod}</c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise>Chưa thanh toán</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                            </div>
                                        </div>

                                        <div class="order-products">
                                            <c:forEach var="item" items="${orderItemsMap[order.id]}">
                                                <div class="product-item">
                                                    <div class="product-image">
                                                        <img src="${not empty item.img ? item.img : 'default.jpg'}"
                                                             alt="${item.productName}" >
                                                    </div>
                                                    <div class="product-details">
                                                        <div class="product-name">${item.productName}</div>
                                                        <div class="product-variant">${item.productMaterial}</div>
                                                        <div class="product-quantity">Số lượng: ${item.quantity}</div>
                                                    </div>
                                                    <div class="product-price">
                                                        <fmt:formatNumber value="${item.subtotal}" pattern="#,###"/>₫
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>

                                        <div class="order-footer">
                                            <div class="order-total">
                                                Tổng: <fmt:formatNumber value="${order.total}" pattern="#,###"/>₫
                                            </div>
                                            <div class="order-actions">
                                                <c:if test="${order.status == 'pending' || order.status == 'confirmed'}">
                                                    <a href="cancel-order?id=${order.id}"
                                                       class="btn btn-secondary"
                                                       onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?')">
                                                        Hủy Đơn
                                                    </a>
                                                </c:if>
                                                <a href="${pageContext.request.contextPath}/order_detail?id=${order.id}" class="btn btn-secondary">
                                                    Chi tiết
                                                </a>
                                                <c:if test="${order.status == 'delivered'}">
                                                    <c:choose>
                                                        <c:when test="${order.hasReview}">
                                                            <!-- Đã đánh giá -->
                                                            <button class="btn btn-secondary" disabled>
                                                                <i class="bi bi-check-circle"></i> Đã đánh giá
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${pageContext.request.contextPath}/order-review?id=${order.id}" class="btn btn-reviews-now">
                                                                Đánh giá đơn hàng
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </div>

                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/views/layout/footer.jsp"/>
</main>
</body>
</html>