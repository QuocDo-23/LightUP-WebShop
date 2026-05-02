<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt hàng thành công</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/completed_order.css">
</head>

<body>
<div class="wrap">
    <header>
        <a class="co-header" href="${pageContext.request.contextPath}/">
            <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="logo">
            <div>
                <h1>LightUp</h1>
                <p class="compl">Đặt hàng hoàn tất</p>
            </div>
        </a>
    </header>

    <main class="card">
        <!--bên trái chứa hình ảnh đặt thành công-->
        <div class="left">
            <i class="bi bi-check2-circle icon"></i>
        </div>

        <!--Bên phải chứa thông tin đơn hàng-->
        <div class="right">
            <h2 class="title">Đặt hàng thành công!</h2>
            <p class="sub">Cảm ơn bạn đã mua hàng tại <strong>LightUp</strong>.<br>
                Đơn hàng của bạn đã được tiếp nhận và sẽ sớm được xử lý.</p>

            <div class="order-box">
                <div class="order-info">
                    <p><strong>Mã đơn hàng:</strong> DH0-${order.id}</p>
                    <p><strong>Ngày đặt hàng:</strong>
                        ${order.orderDateFormatted}
                    </p>
                    <p><strong>Phương thức:</strong>
                        <c:choose>
                            <c:when test="${payment.paymentMethod == 'cod'}">
                                Thanh toán khi nhận hàng (COD)
                            </c:when>
                            <c:when test="${payment.paymentMethod == 'transfer'}">
                                Chuyển khoản ngân hàng (Momo)
                            </c:when>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div class="summary">
                <div class="items">
                    <p class="muted">Sản phẩm</p>
                    <c:forEach var="item" items="${orderItems}">
                        <p>
                                ${item.productName}
                            <c:if test="${not empty item.productMaterial}">
                                - ${item.productMaterial}
                            </c:if>
                            - SL: ${item.quantity}
                            - <span class="muted">Mã SP: ${item.productId}</span>
                        </p>
                    </c:forEach>
                </div>

                <div class="total">
                    <p class="muted">Tổng tiền</p>
                    <p class="price">
                        <fmt:formatNumber value="${order.total}" type="number" groupingUsed="true" /> VND
                    </p>
                </div>
            </div>

            <div class="button">
                <a href="${pageContext.request.contextPath}/orders" class="btn">Theo dõi đơn hàng</a>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-ghost">Tiếp tục mua sắm</a>
            </div>
        </div>
    </main>

    <footer>
        <p>Bạn cần trợ giúp? <a href="${pageContext.request.contextPath}/contact">Liên hệ hỗ trợ</a></p>
    </footer>
</div>
</body>

</html>