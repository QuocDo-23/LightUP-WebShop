<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng của bạn</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/cart_detail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/about.css">

</head>

<body>
<jsp:include page="header.jsp"/>



<main class="cart-page">
    <div class="cart-left">
        <h2>Giỏ hàng của bạn</h2>
        <hr class="line">

        <c:if test="${not empty errorMsg}">
            <div class="toast">
                    ${errorMsg}
            </div>

            <c:remove var="errorMsg" scope="session" />
        </c:if>

        <c:if test="${sessionScope.cart == null || empty sessionScope.cart.listItem}">
            <p>Giỏ hàng của bạn đang trống</p>
        </c:if>

        <%-- Checkbox chọn tất cả --%>
        <c:if test="${not empty sessionScope.cart.listItem}">
            <div class="cart-select-all">
                <label>
                    <input type="checkbox" id="chkSelectAll" checked>
                    <span>Chọn tất cả</span>
                </label>
            </div>
        </c:if>

        <c:forEach var="item" items="${sessionScope.cart.listItem}">
            <div class="cart-item"
                 data-id="${item.product.id}"
                 data-price="${item.product.discountedPrice}"
                 data-qty="${item.quantity}"
                 data-name="${item.product.name}">

                    <%-- Checkbox chọn sản phẩm --%>
                <div class="item-check">
                    <input type="checkbox"
                           class="chk-item"
                           value="${item.product.id}"
                           data-price="${item.product.discountedPrice}"
                           data-qty="${item.quantity}"
                           checked>
                </div>

                <div class="item-image">
                    <img src="${not empty item.product.mainImage ? item.product.mainImage : 'default.jpg'}"
                         alt="${item.product.description}" class="img-main">
                    <c:if test="${not empty item.product.hoverImage}">
                        <img src="${item.product.hoverImage}"
                             alt="${item.product.description}" class="img-hover">
                    </c:if>
                </div>

                <div class="item-info">
                    <h4><a href="product-detail?id=${item.product.id}">
                            ${item.product.name}
                    </a></h4>
                    <div class="price-cart">
                        <div class="price">
                            <fmt:formatNumber value="${item.product.getDiscountedPrice()}" type="number"/>đ
                        </div>
                        <c:if test="${item.product.discountRate > 0}">
                            <div class="discount-badge">
                                -<fmt:formatNumber value="${item.product.discountRate}"/>%
                            </div>
                        </c:if>
                    </div>

                    <div class="quantity">
                        <form action="${pageContext.request.contextPath}/update"
                              method="post" style="display:inline;">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <input type="hidden" name="qty" value="${item.quantity - 1}">
                            <button type="submit" class="qty-btn">-</button>
                        </form>

                        <span class="qty">${item.quantity}</span>

                        <form action="${pageContext.request.contextPath}/update"
                              method="post" style="display:inline;">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <input type="hidden" name="qty" value="${item.quantity + 1}">
                            <button type="submit" class="qty-btn">+</button>
                        </form>
                    </div>
                </div>

                <div class="item-total" id="total-${item.product.id}">
                    <fmt:formatNumber value="${item.product.getDiscountedPrice() * item.quantity}" type="number"/>đ
                </div>

                <form action="${pageContext.request.contextPath}/remove"
                      method="post" style="display:inline;">
                    <input type="hidden" name="productId" value="${item.product.id}">
                    <button type="submit" class="delete-btn">Xóa</button>
                </form>
            </div>
        </c:forEach>
    </div>

    <div class="cart-right">
        <div class="order-box">
            <h3>Thông tin đơn hàng</h3>

            <ul id="selected-summary">
                <c:forEach var="item" items="${sessionScope.cart.listItem}">
                    <li class="summary-item"
                        data-id="${item.product.id}"
                        data-qty="${item.quantity}">
                            ${item.product.name} - <b>SL: ${item.quantity}</b>
                    </li>
                </c:forEach>
            </ul>

            <div id="no-selected-msg" style="display:none; color:#999; font-size:13px;">
                Chưa chọn sản phẩm nào
            </div>

            <div class="total">
                <span>Tổng tiền:</span>
                <strong id="total-display">
                    <fmt:formatNumber value="${sessionScope.cart.totalPrice}" type="number"/>đ
                </strong>
            </div>

            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <button class="checkout-btn" id="btnCheckout" onclick="goCheckout()">THANH TOÁN</button>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login?redirect=payment">
                        <button class="checkout-btn">THANH TOÁN</button>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="order-note">
            <a href="./products"><i class="bi bi-arrow-return-left"></i> Tiếp tục mua hàng</a>
            <ul>
                <li>Không rủi ro.</li>
                <li>Đặt hàng trước, thanh toán sau tại nhà. Miễn phí giao hàng & lắp đặt tại TP.HCM, Hà Nội,...
                </li>
                <li>Đơn hàng sẽ được giao trong vòng 3 ngày, vui lòng chờ nhân viên tư vấn xác nhận.</li>
                <li>Miễn phí 1 đổi 1 - Bảo hành 2 tháng - Bảo trì nhanh chóng.</li>
            </ul>
        </div>
    </div>
</main>

<!-- Footer -->
<jsp:include page="footer.jsp"/>

<script src="./JS/index.js"></script>
<script>
    var CTX = "${pageContext.request.contextPath}";

    function capNhatTong() {
        var items   = document.querySelectorAll(".chk-item");
        var total   = 0;
        var summary = document.getElementById("selected-summary");
        var noMsg   = document.getElementById("no-selected-msg");
        var allLis  = summary.querySelectorAll(".summary-item");

        items.forEach(function(chk) {
            var id  = chk.value;
            var li  = summary.querySelector(".summary-item[data-id='" + id + "']");
            if (chk.checked) {
                var price = parseFloat(chk.dataset.price);
                var qty   = parseInt(chk.dataset.qty);
                total += price * qty;
                if (li) li.style.display = "";
            } else {
                if (li) li.style.display = "none";
            }
        });

        document.getElementById("total-display").textContent =
            total.toLocaleString("vi-VN") + "đ";

        var anyChecked = Array.from(items).some(function(c) { return c.checked; });
        noMsg.style.display   = anyChecked ? "none"  : "block";
        summary.style.display = anyChecked ? "block" : "none";

        // Disable nút nếu không chọn gì
        var btn = document.getElementById("btnCheckout");
        if (btn) btn.disabled = !anyChecked;
    }

    document.getElementById("chkSelectAll").addEventListener("change", function() {
        document.querySelectorAll(".chk-item").forEach(function(chk) {
            chk.checked = this.checked;
        }, this);
        capNhatTong();
    });


    document.querySelectorAll(".chk-item").forEach(function(chk) {
        chk.addEventListener("change", function() {
            var all     = document.querySelectorAll(".chk-item");
            var checked = document.querySelectorAll(".chk-item:checked");
            document.getElementById("chkSelectAll").checked = all.length === checked.length;
            capNhatTong();
        });
    });

    function goCheckout() {
        var selected = Array.from(document.querySelectorAll(".chk-item:checked"))
            .map(function(c) { return c.value; });

        if (selected.length === 0) {
            alert("Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
            return;
        }

        var params = new URLSearchParams();
        selected.forEach(function(id) { params.append("selectedIds", id); });

        fetch(CTX + "/checkout-select", {
            method:  "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body:    params.toString()
        })
            .then(function(res) {
                if (res.ok) {
                    window.location.href = CTX + "/payment";
                } else {
                    alert("Có lỗi xảy ra, vui lòng thử lại!");
                }
            });
    }

    capNhatTong();
</script>
</body>

</html>
