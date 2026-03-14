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
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            /*background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);*/
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            position: relative;
            padding: 40px;
        }

        .close-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            width: 40px;
            height: 40px;
            border: none;
            background: #f0f0f0;
            border-radius: 50%;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s;
            font-size: 24px;
            color: #666;
        }

        .close-btn:hover {
            background: #e0e0e0;
            transform: rotate(90deg);
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
        }

        .container-header {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 15px;
            margin-bottom: 10px;
        }

        .icon-circle {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 10px;
        }

        .icon-circle img {
            width: 100%;
            height: 100%;
            object-fit: contain;
        }

        .header h1 {
            font-size: 28px;
            color: #333;
            margin: 0;
        }

        .subtitle {
            color: #666;
            font-size: 14px;
        }

        .order-info {
            background: #f8f9ff;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
            border-left: 4px solid #667eea;
        }

        .order-info h3 {
            color: #333;
            margin-bottom: 15px;
            font-size: 18px;
        }

        .order-info p {
            margin: 8px 0;
            color: #555;
            font-size: 14px;
        }

        .order-info strong {
            color: #333;
            font-weight: 600;
        }

        .section-title {
            font-size: 18px;
            color: #333;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .product-section {
            margin-bottom: 30px;
        }

        .product-item {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 10px;
            margin-bottom: 15px;
        }

        .product-image {
            width: 80px;
            height: 80px;
            border-radius: 8px;
            overflow: hidden;
            flex-shrink: 0;
        }

        .product-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .product-info {
            flex: 1;
        }

        .product-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
            font-size: 14px;
        }

        .product-price {
            color: #667eea;
            font-weight: 700;
            font-size: 16px;
        }

        .product-quantity {
            color: #999;
            font-size: 14px;
        }

        .review-section-wrapper {
            background: #f8f9ff;
            padding: 25px;
            border-radius: 12px;
        }

        .rating-section {
            margin-bottom: 25px;
        }

        .rating-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 15px;
            font-size: 15px;
        }

        .stars {
            display: flex;
            gap: 10px;
            margin-bottom: 10px;
        }

        .star-rating {
            width: 50px;
            height: 50px;
            position: relative;
            cursor: pointer;
            background: #e0e0e0;
            -webkit-mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='currentColor'%3E%3Cpath d='M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z'/%3E%3C/svg%3E") no-repeat center;
            mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='currentColor'%3E%3Cpath d='M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z'/%3E%3C/svg%3E") no-repeat center;
            -webkit-mask-size: contain;
            mask-size: contain;
            transition: all 0.2s;
        }

        .star-rating:hover {
            transform: scale(1.1);
        }

        .star-rating input[type="radio"] {
            position: absolute;
            opacity: 0;
            width: 100%;
            height: 100%;
            cursor: pointer;
            z-index: 2;
        }

        /* Màu vàng khi được chọn - sử dụng :checked */
        .star-rating input[type="radio"]:checked ~ .star-fill,
        .star-rating:has(input[type="radio"]:checked) {
            background: #ffc107;
        }

        /* Hiệu ứng hover cho tất cả các sao trước nó */
        .stars:has(.star-rating:nth-child(1) input:hover) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(2) input:hover) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(2) input:hover) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(3) input:hover) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(3) input:hover) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(3) input:hover) .star-rating:nth-child(3),
        .stars:has(.star-rating:nth-child(4) input:hover) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(4) input:hover) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(4) input:hover) .star-rating:nth-child(3),
        .stars:has(.star-rating:nth-child(4) input:hover) .star-rating:nth-child(4),
        .stars:has(.star-rating:nth-child(5) input:hover) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(5) input:hover) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(5) input:hover) .star-rating:nth-child(3),
        .stars:has(.star-rating:nth-child(5) input:hover) .star-rating:nth-child(4),
        .stars:has(.star-rating:nth-child(5) input:hover) .star-rating:nth-child(5) {
            background: #ffc107;
        }

        /* Hiển thị các sao đã chọn */
        .stars:has(.star-rating:nth-child(1) input:checked) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(2) input:checked) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(2) input:checked) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(3) input:checked) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(3) input:checked) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(3) input:checked) .star-rating:nth-child(3),
        .stars:has(.star-rating:nth-child(4) input:checked) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(4) input:checked) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(4) input:checked) .star-rating:nth-child(3),
        .stars:has(.star-rating:nth-child(4) input:checked) .star-rating:nth-child(4),
        .stars:has(.star-rating:nth-child(5) input:checked) .star-rating:nth-child(1),
        .stars:has(.star-rating:nth-child(5) input:checked) .star-rating:nth-child(2),
        .stars:has(.star-rating:nth-child(5) input:checked) .star-rating:nth-child(3),
        .stars:has(.star-rating:nth-child(5) input:checked) .star-rating:nth-child(4),
        .stars:has(.star-rating:nth-child(5) input:checked) .star-rating:nth-child(5) {
            background: #ffc107;
        }

        .rating-text {
            color: #667eea;
            font-weight: 600;
            min-height: 24px;
            font-size: 14px;
        }

        /* Hiển thị text dựa trên input được checked */
        .stars:has(.star-rating:nth-child(1) input:checked) ~ .rating-text::after {
            content: "😞 Rất không hài lòng";
        }
        .stars:has(.star-rating:nth-child(2) input:checked) ~ .rating-text::after {
            content: "😕 Không hài lòng";
        }
        .stars:has(.star-rating:nth-child(3) input:checked) ~ .rating-text::after {
            content: "😐 Bình thường";
        }
        .stars:has(.star-rating:nth-child(4) input:checked) ~ .rating-text::after {
            content: "😊 Hài lòng";
        }
        .stars:has(.star-rating:nth-child(5) input:checked) ~ .rating-text::after {
            content: "🤩 Rất hài lòng";
        }

        .review-section {
            margin-bottom: 20px;
        }

        .review-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 10px;
            font-size: 15px;
        }

        .review-textarea {
            width: 100%;
            min-height: 120px;
            padding: 15px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 14px;
            font-family: inherit;
            resize: vertical;
            transition: border-color 0.3s;
        }

        .review-textarea:focus {
            outline: none;
            border-color: #667eea;
        }

        .char-counter {
            text-align: right;
            color: #999;
            font-size: 12px;
            margin-top: 5px;
        }

        .submit-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }

        .submit-btn:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .submit-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .error-message {
            background: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #c33;
        }

        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .alert-danger {
            background: #fee;
            color: #c33;
            border-left: 4px solid #c33;
        }

        .alert-success {
            background: #efe;
            color: #3c3;
            border-left: 4px solid #3c3;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }

            .header h1 {
                font-size: 22px;
            }

            .product-item {
                flex-direction: column;
                text-align: center;
            }

            .stars {
                justify-content: center;
            }

            .star-rating {
                width: 45px;
                height: 45px;
            }
        }
    </style>
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

    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-circle"></i> ${errorMessage}
        </div>
    </c:if>

    <!-- Thông tin đơn hàng -->
    <div class="order-info">
        <h3>📦 Thông tin đơn hàng</h3>
        <p><strong>Mã đơn hàng:</strong> #DH${String.format("%06d", order.id)}</p>
        <p><strong>Ngày đặt:</strong> ${order.orderDateFormatted}</p>
        <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${order.total}" pattern="#,###"/>₫</p>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="product-section">
        <h3 class="section-title">🛍️ Sản phẩm trong đơn hàng</h3>
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
            <h3 class="section-title">⭐ Đánh giá đơn hàng</h3>

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

            <!-- Review Text Section -->
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
    // Chỉ dùng JS tối thiểu cho character counter
    function updateCharCount(textarea) {
        document.getElementById('charCount').textContent = textarea.value.length;
    }
</script>
</body>
</html>
