<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/checkout.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <title>Thanh toán sản phẩm</title>
</head>
<body>
<div class="header">
    <div class="header-cont">
        <a href="${pageContext.request.contextPath}/views/user/index.jsp">
            <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="logo">
            <h1>THANH TOÁN ĐƠN HÀNG</h1>
        </a>
    </div>
</div>

<div class="container">
    <c:if test="${not empty error}">
        <div class="alert alert-error">
            <i class="bi bi-exclamation-triangle"></i> ${error}
        </div>
    </c:if>

    <div class="main-content">

        <div class="content-l">
            <!-- Hiển thị địa chỉ đã chọn -->
            <c:if test="${not empty selectedAddress}">
                <h2 class="section-title">Thông tin nhận hàng</h2>
                <div class="address-display">
                    <div class="address-display-header">
                        <div class="address-display-title">Địa chỉ giao hàng</div>
                        <button type="button" class="btn-change" onclick="openModal()">Thay đổi</button>
                    </div>
                    <div class="address-display-content">
                        <strong>${selectedAddress.recipientName} - ${selectedAddress.phone}</strong>
                        <div>${selectedAddress.commune}, ${selectedAddress.district}, ${selectedAddress.addressDetail}</div>
                    </div>
                </div>
            </c:if>
            <c:if test="${isNewAddress}">
                <div class="address-display-new">
                    <h2 class="section-title">Thông tin nhận hàng</h2>
                    <button type="button" class="btn-change" onclick="openModal()">
                        Chọn địa chỉ khác
                    </button>
                </div>
            </c:if>



            <form id="checkoutForm" action="${pageContext.request.contextPath}/payment" method="post">
                <div class="form-group">
                    <input type="text" name="recipientName" id="recipientName"
                           placeholder="Họ và tên người nhận *"
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.recipientName : sessionScope.user.name)}"
                           required>
                </div>

                <div class="form-group">
                    <input type="tel" name="phone" id="phone"
                           placeholder="Số điện thoại *"
                           pattern="[0-9]{10,11}"
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.phone : '')}"
                           required>
                </div>

                <div class="form-group">
                    <input type="email" name="email" id="email"
                           placeholder="Email"
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.email : sessionScope.user.email)}">
                </div>

                <div class="form-group">
                    <input type="text" name="houseNumber" id="houseNumber"
                           placeholder="Số nhà"
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.house_number : '')}">
                </div>

                <div class="form-group">
                    <input type="text" name="commune" id="commune"
                           placeholder="Phường/xã"
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.commune : '')}">
                </div>

                <div class="form-group">
                    <input type="text" name="district" id="district"
                           placeholder="Quận/Huyện, Tỉnh/Thành phố"
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.district : '')}">
                </div>

                <div class="form-group">
                    <textarea name="addressDetail" id="addressDetail"
                              placeholder="Ghi chú địa chỉ cụ thể (nếu có) *">${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.addressDetail : '')}</textarea>
                </div>

                <c:if test="${not empty sessionScope.user}">
                    <div class="checkbox-group">
                        <label>
                            <input type="checkbox" name="saveAddress" value="true">
                            Lưu địa chỉ
                        </label>
                    </div>
                </c:if>

                <input type="hidden" name="checkoutType" value="cart">
            </form>
        </div>

        <!-- Center: Phương thức vận chuyển và thanh toán -->
        <div class="content-c">
            <div class="delivery">
                <h2 class="section-title">Vận chuyển</h2>
                <label class="payment-method">
                    <input type="radio" name="shippingMethod" value="standard" form="checkoutForm" checked>
                    <span class="radio"></span>
                    <span>Giao hàng tiêu chuẩn (5-7 ngày)</span>
                    <div class="payment-info">
                        <span class="fee">70.000₫</span>
                    </div>
                </label>
                <label class="payment-method">
                    <input type="radio" name="shippingMethod" value="express" form="checkoutForm">
                    <span class="radio"></span>
                    <span>Giao hàng nhanh (2-3 ngày)</span>
                    <div class="payment-info">
                        <span class="fee">150.000₫</span>
                    </div>
                </label>
            </div>

            <div class="payment-section">
                <h2 class="section-title">Thanh toán</h2>
                <label class="payment-method">
                    <input type="radio" name="paymentMethod" value="cod" form="checkoutForm" checked>
                    <span class="radio"></span>
                    <span>Thanh toán khi nhận hàng (COD)</span>
                    <div class="payment-info">
                        <img src="https://cdn-icons-png.freepik.com/512/7630/7630510.png" alt="">
                    </div>
                </label>
                <label class="payment-method">
                    <input type="radio" name="paymentMethod" value="transfer" form="checkoutForm">
                    <span class="radio"></span>
                    <span>Chuyển khoản ngân hàng (Momo)</span>
                    <div class="payment-info">
                        <img src="https://upload.wikimedia.org/wikipedia/vi/f/fe/MoMo_Logo.png" alt="MoMo" style="width:40px;height:40px;border-radius:8px;">
                    </div>
                </label>

                <div id="momoPopup">
                    <p class="momo-title">Quét mã QR để thanh toán qua MoMo</p>
                    <img id="momoQR"
                         src="https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=2|99|0969123456|TEN_CUA_BAN|0|0|0|Thanh%20toan%20don%20hang|transfer_type"
                         alt="QR MoMo" class="momo-qr">
                    <p class="momo-info">Số tài khoản MoMo: <strong>0969 123 456</strong></p>
                    <p class="momo-info">Chủ tài khoản: <strong>TÊN CỦA BẠN</strong></p>
                    <p class="momo-note">Nội dung chuyển khoản: <strong id="momoContent">Thanh toan don hang</strong></p>
                </div>
            </div>
        </div>


        <div class="sidebar">
            <h3 class="order-title">Đơn hàng (${cart.totalItems} sản phẩm)</h3>
            <!-- Hiển thị tất cả items từ cart -->
            <div class="cart-items-wrapper">
                <c:forEach var="item" items="${cart.listItem}">
                    <div class="cart-item">
                        <div class="item-image">
                            <img src="${not empty item.product.mainImage ? item.product.mainImage : 'default.jpg'}"
                                 alt="${item.product.description}" class="img-main">
                            <div class="item-quantity">${item.quantity}</div>
                        </div>
                        <div class="item-details">
                            <div class="item-name">${item.product.name}</div>
                            <c:if test="${not empty item.product.material}">
                                <div class="item-variant">${item.product.material}</div>
                            </c:if>
                        </div>
                        <div class="item-price">
                            <fmt:formatNumber value="${item.product.discountedPrice * item.quantity}"
                                              type="number" groupingUsed="true"/>₫
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="order-summary">
                <div class="summary-row">
                    <span>Tạm tính</span>
                    <span><fmt:formatNumber value="${cart.totalPrice}" type="number" groupingUsed="true"/>₫</span>
                </div>

                <div class="summary-row">
                    <span>Phí vận chuyển</span>
                    <span id="shippingFee">Miễn phí</span>
                </div>

                <div class="summary-row total">
                    <span>Tổng cộng</span>
                    <span class="total-price" id="totalPrice">
                        <fmt:formatNumber value="${cart.totalPrice}" type="number" groupingUsed="true"/>₫
                    </span>
                </div>

                <button type="submit" form="checkoutForm" class="checkout-btn">ĐẶT HÀNG</button>
                <a href="${pageContext.request.contextPath}/cart" class="back-link">← Quay về giỏ hàng</a>
            </div>
        </div>
    </div>
</div>


<div id="addressModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chọn địa chỉ giao hàng</h3>
            <button type="button" class="close" onclick="closeModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/payment" method="get">
            <div class="modal-body">
                <c:forEach var="addr" items="${savedAddresses}">
                    <label class="address-item-modal">
                        <input type="radio" name="selectedAddressId" value="${addr.id}"
                            ${selectedAddress.id eq addr.id ? 'checked' : ''}>
                        <div>
                            <strong>${addr.recipientName}</strong> - ${addr.phone}
                            <c:if test="${addr['default']}">
                                <span class="badge-default">Mặc định</span>
                            </c:if>
                            <br>
                                ${addr.addressDetail}, ${addr.commune}, ${addr.district}
                        </div>
                    </label>

                </c:forEach>
            </div>
            <div class="modal-footer">
                <a href="${pageContext.request.contextPath}/payment?selectedAddressId=new" class="btn-add-new" style="text-decoration: none; display: flex; align-items: center; justify-content: center;">+ Thêm địa chỉ mới</a>
                <button type="submit" class="btn-confirm">Xác nhận</button>
            </div>
        </form>
    </div>
</div>

<script>
    const baseTotal = ${cart.totalPrice};

    document.querySelectorAll('input[name="shippingMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const fee = this.value === 'express' ? 150000 : 70000;
            document.getElementById('shippingFee').textContent =
                fee === 0 ? 'Miễn phí' : fee.toLocaleString('vi-VN') + '₫';
            document.getElementById('totalPrice').textContent =
                (baseTotal + fee).toLocaleString('vi-VN') + '₫';
        });
    });

    document.querySelector('input[name="shippingMethod"]:checked').dispatchEvent(new Event('change'));

    function openModal() {
        document.getElementById('addressModal').classList.add('show');
    }


    function closeModal() {
        document.getElementById('addressModal').classList.remove('show');
    }




    document.getElementById('checkoutForm').addEventListener('submit', function(e) {
        const recipientName = document.getElementById('recipientName').value.trim();
        const phone = document.getElementById('phone').value.trim();

        if (!recipientName || !phone ) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin nhận hàng');
            return false;
        }

        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test(phone)) {
            e.preventDefault();
            alert('Số điện thoại không hợp lệ (phải có 10-11 chữ số)');
            return false;
        }
    });

    document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const momoPopup = document.getElementById('momoPopup');
            momoPopup.style.display = this.value === 'transfer' ? 'block' : 'none';
            if (this.value === 'transfer') updateMomoQR();
        });
    });

    function updateMomoQR() {
        const total = parseInt(document.getElementById('totalPrice').textContent.replace(/\D/g, ''));
        const phone = document.getElementById('phone').value || '';
        const content = 'Thanh toan don hang ' + phone;
        document.getElementById('momoContent').textContent = content;

        const qrData = encodeURIComponent(`2|99|0969123456|TEN_CUA_BAN|${total}|0|0|${content}|transfer_type`);
        document.getElementById('momoQR').src = `https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=${qrData}`;
    }

    document.querySelectorAll('input[name="shippingMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            if (document.querySelector('input[name="paymentMethod"]:checked').value === 'transfer') {
                updateMomoQR();
            }
        });
    });
</script>
</body>
</html>