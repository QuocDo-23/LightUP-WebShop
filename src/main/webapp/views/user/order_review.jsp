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
    <title>Đánh Giá Đơn Hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/order_review.css">
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/orders" class="close-btn" title="Đóng">
        <i class="bi bi-x"></i>
    </a>

    <div class="header">
        <div class="container-header">
            <div class="icon-circle">
                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="Logo">
            </div>
            <h1>Đánh giá đơn hàng</h1>
        </div>
        <p class="subtitle">Chia sẻ trải nghiệm của bạn giúp chúng tôi phục vụ tốt hơn</p>
    </div>


    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-circle"></i> ${errorMessage}
        </div>
    </c:if>


    <div class="order-info">
        <h3>📦 Thông tin đơn hàng</h3>
        <p><strong>Mã đơn hàng:</strong> #DH${String.format("%06d", order.id)}</p>
        <p><strong>Ngày đặt:</strong> ${order.orderDateFormatted}</p>
        <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${order.total}" pattern="#,###"/>₫</p>
    </div>

    <div class="product-section">
        <h3 class="section-title">Sản phẩm trong đơn hàng</h3>
        <c:forEach var="item" items="${orderItems}">
            <div class="product-item">
                <div class="product-image">
                    <img src="${not empty item.img ? item.img : 'default.jpg'}" alt="${item.productName}">
                </div>
                <div class="product-info">
                    <div class="product-name">${item.productName}</div>
                    <div class="product-price">
                        <fmt:formatNumber value="${item.price}" pattern="#,###"/>₫
                        <span class="product-quantity">× ${item.quantity}</span>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Form đánh giá -->
    <form action="${pageContext.request.contextPath}/order-review" method="post" id="reviewForm">
        <input type="hidden" name="orderId" value="${order.id}">

        <div class="review-section-wrapper">
            <h3 class="section-title">Đánh giá đơn hàng</h3>

            <!-- Rating Section -->
            <div class="rating-section">
                <div class="rating-label">Bạn đánh giá sản phẩm/dịch vụ như thế nào?</div>
                <div class="stars">
                    <div class="star-rating">
                        <input type="radio" name="rating" value="1" id="star1" required>
                    </div>
                    <div class="star-rating">
                        <input type="radio" name="rating" value="2" id="star2">
                    </div>
                    <div class="star-rating">
                        <input type="radio" name="rating" value="3" id="star3">
                    </div>
                    <div class="star-rating">
                        <input type="radio" name="rating" value="4" id="star4">
                    </div>
                    <div class="star-rating">
                        <input type="radio" name="rating" value="5" id="star5">
                    </div>
                </div>
                <div class="rating-text"></div>
            </div>


            <div class="review-section">
                <div class="review-label">Nhận xét của bạn</div>
                <textarea
                        class="review-textarea"
                        name="reviewText"
                        id="reviewText"
                        placeholder="Hãy chia sẻ cảm nhận của bạn về sản phẩm, dịch vụ giao hàng, chất lượng đóng gói..."
                        maxlength="500"
                        oninput="updateCharCount(this)"
                ></textarea>
                <div class="char-counter">
                    <span id="charCount">0</span>/500 ký tự
                </div>
            </div>

            <button type="submit" class="submit-btn">Gửi đánh giá</button>
        </div>
    </form>
</div>

<script>
    function updateCharCount(textarea) {
        document.getElementById('charCount').textContent = textarea.value.length;
    }
</script>
</body>
</html>
