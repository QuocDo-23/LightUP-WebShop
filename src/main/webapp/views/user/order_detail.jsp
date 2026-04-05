<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng #DH${String.format("%06d", order.id)}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/order_detail.css">
</head>
<body>
<main>
    <section class="od-track">
        <!-- Nút đóng -->
        <button class="close-btn" onclick="window.location.href='orders'" title="Quay lại">
            <i class="bi bi-x"></i>
        </button>

        <div class="container">
            <!-- Header -->
            <div class="order-detail-header">
                <h1>Chi tiết đơn hàng #DH${String.format("%06d", order.id)}</h1>
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

            <!-- Trạng thái đơn hàng (Progress Bar) -->
            <c:if test="${order.status != 'cancelled'}">
                <div class="order-status-section">
                    <h2>Trạng thái đơn hàng</h2>
                    <div class="progress-bar">
                        <!-- Bước 1: Đã nhận đơn -->
                        <div class="step ${order.status == 'pending' || order.status == 'processing' || order.status == 'shipped' || order.status == 'delivered' ? 'step_complete' : ''}">
                            <i class="bi bi-cart-check"></i>
                            <p>Đã nhận đơn</p>
                        </div>

                        <!-- Bước 2: Đã xác nhận -->
                        <div class="step ${order.status == 'processing' || order.status == 'shipped' || order.status == 'delivered' ? 'step_complete' : ''}">
                            <i class="bi bi-box2-heart"></i>
                            <p>Đã xác nhận</p>
                        </div>

                        <!-- Bước 3: Đang vận chuyển -->
                        <div class="step ${order.status == 'shipped' || order.status == 'delivered' ? 'step_complete' : ''}">
                            <i class="bi bi-truck"></i>
                            <p>Đang vận chuyển</p>
                        </div>

                        <!-- Bước 4: Đã giao hàng -->
                        <div class="step ${order.status == 'delivered' ? 'step_complete' : ''}">
                            <i class="bi bi-check2-square"></i>
                            <c:set var="itemCount" value="${orderItems.size()}" />
                            <div class="item-quantity">${itemCount}</div>
                            <p>Đã giao hàng</p>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Thông tin đơn hàng -->
            <div class="order-inf">
                <h2>Thông tin đơn hàng</h2>
                <div class="information">
                    <div class="info-row">
                        <span class="info-label">Mã đơn hàng:</span>
                        <span class="info-value"><strong>#DH${String.format("%06d", order.id)}</strong></span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Ngày đặt hàng:</span>
                        <span class="info-value">${order.orderDateFormatted}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Người nhận hàng:</span>
                        <span class="info-value">${order.recipientName}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Số điện thoại:</span>
                        <span class="info-value">${order.recipientPhone}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Email:</span>
                        <span class="info-value">${order.recipientEmail}</span>
                    </div>
                    <c:if test="${order.status == 'pending' || order.status == 'processing'}">

                        <form action="order_detail" method="post" style="margin-top:10px;">

                            <input type="hidden" name="orderId" value="${order.id}">

                            <div style="display:flex; gap:10px; flex-wrap:wrap;">
                                <input type="text" name="house" value="${order.shippingHouseNumber}" placeholder="Số nhà">
                                <input type="text" name="commune" value="${order.shippingCommune}" placeholder="Phường/Xã">
                                <input type="text" name="district" value="${order.shippingDistrict}" placeholder="Quận/Huyện">
                                <input type="text" name="detail" value="${order.shippingAddressDetail}" placeholder="Chi tiết">
                            </div>

                            <button type="submit" class="btn btn-update" style="margin-top:10px;">
                                Cập nhật đơn hàng
                            </button>

                        </form>

                    </c:if>


                    <div class="info-row">
                        <span class="info-label">Địa chỉ giao hàng:</span>
                        <span class="info-value">
                            ${order.shippingHouseNumber}, ${order.shippingCommune}, ${order.shippingDistrict}
                            <c:if test="${not empty order.shippingAddressDetail}">
                                <br>${order.shippingAddressDetail}
                            </c:if>
                        </span>
                    </div>


                    <div class="info-row">
                        <span class="info-label">Phương thức thanh toán:</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${not empty payment}">
                                    <c:choose>
                                        <c:when test="${payment.paymentMethod == 'COD'}">Thanh toán khi nhận hàng</c:when>
                                        <c:when test="${payment.paymentMethod == 'bank_transfer'}">Chuyển khoản ngân hàng</c:when>
                                        <c:otherwise>${payment.paymentMethod}</c:otherwise>
                                    </c:choose>
                                    -
                                    <span class="payment-status status-${payment.paymentStatus}">
                                        <c:choose>
                                            <c:when test="${payment.paymentStatus == 'pending'}">Chờ thanh toán</c:when>
                                            <c:when test="${payment.paymentStatus == 'completed'}">Đã thanh toán</c:when>
                                            <c:when test="${payment.paymentStatus == 'failed'}">Thất bại</c:when>
                                            <c:otherwise>${payment.paymentStatus}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:when>
                                <c:otherwise>Chưa có thông tin</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </div>


            <div class="order-products-section">
                <h2>Sản phẩm trong đơn hàng</h2>
                <div class="products-list">
                    <c:forEach var="item" items="${orderItems}">
                        <div class="product-item-detail">
                            <div class="product-image">
                                <img src="${not empty item.img ? item.img : 'default.jpg'}"
                                     alt="${item.productName}">
                            </div>
                            <div class="product-info">
                                <h3 class="product-name">${item.productName}</h3>
                                <p class="product-material">Chất liệu: ${item.productMaterial}</p>
                                <p class="product-quantity">Số lượng: ${item.quantity}</p>
                                <p class="product-price">
                                    Đơn giá: <fmt:formatNumber value="${item.price}" pattern="#,###"/>₫
                                </p>
                            </div>
                            <div class="product-subtotal">
                                <span class="subtotal-label">Thành tiền:</span>
                                <span class="subtotal-value">
                                    <fmt:formatNumber value="${item.subtotal}" pattern="#,###"/>₫
                                </span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>


            <div class="order-total-section">
                <div class="total-row">
                    <span class="total-label">Tổng tiền hàng:</span>
                    <span class="total-value"><fmt:formatNumber value="${order.total}" pattern="#,###"/>₫</span>
                </div>
                <div class="total-row">
                    <span class="total-label">Phí vận chuyển:</span>
                    <span class="total-value">Miễn phí</span>
                </div>
                <div class="total-row grand-total">
                    <span class="total-label">Tổng thanh toán:</span>
                    <span class="total-value"><fmt:formatNumber value="${order.total}" pattern="#,###"/>₫</span>
                </div>
            </div>


            <div class="action-buttons">
                <c:if test="${order.status == 'pending' || order.status == 'processing'}">
                    <a href="cancel-order?id=${order.id}"
                       class="btn btn-cancel"
                       onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?')">
                        <i class="bi bi-x-circle"></i> Hủy đơn hàng
                    </a>
                </c:if>

                <c:if test="${order.status == 'delivered'}">
                    <a href="cart" class="btn btn-reorder">
                        <i class="bi bi-arrow-repeat"></i> Đánh giá đơn hàng
                    </a>
                </c:if>

                <a href="orders" class="btn btn-back">
                    <i class="bi bi-arrow-left"></i> Quay lại danh sách
                </a>
            </div>
        </div>
    </section>
</main>

<script>

    <c:if test="${not empty sessionScope.successMessage}">
        alert("${sessionScope.successMessage}");
        <c:remove var="successMessage" scope="session" />
    </c:if>
    
    <c:if test="${not empty sessionScope.errorMessage}">
        alert("${sessionScope.errorMessage}");
        <c:remove var="errorMessage" scope="session" />
    </c:if>
</script>
</body>
</html>
