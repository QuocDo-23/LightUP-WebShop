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
                           placeholder="Phường/xã" required
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.commune : '')}">
                </div>

                <div class="form-group">
                    <input type="text" name="district" id="district"
                           placeholder="Tỉnh/Thành phố" required
                           value="${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.district : '')}">
                </div>

                <div class="form-group">
                    <textarea name="addressDetail" id="addressDetail"
                              placeholder="Ghi chú địa chỉ cụ thể (nếu có)">${isNewAddress ? '' : (not empty selectedAddress ? selectedAddress.addressDetail : '')}</textarea>
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
                    <span>Chuyển khoản ngân hàng</span>
                    <div class="payment-info">
                        <img src="https://cdn-icons-png.flaticon.com/512/2830/2830284.png" alt="">
                    </div>
                </label>

                <div class="sub-payment-options" id="subPaymentOptions">
                    <label class="sub-payment-method">
                        <input type="radio" name="subPaymentMethod" value="vnpay" checked>
                        <span class="radio"></span>
                        <span>Thanh toán VNPay</span>
                        <img src="https://vinadesign.vn/uploads/images/2023/05/vnpay-logo-vinadesign-25-12-57-55.jpg" alt="VNPay">
                    </label>
                    <label class="sub-payment-method">
                        <input type="radio" name="subPaymentMethod" value="momo">
                        <span class="radio"></span>
                        <span>Thanh toán MoMo</span>
                        <img src="https://developers.momo.vn/v3/assets/images/MOMO-Logo-App-6262c3743a290ef02396a24ea2b66c35.png"
                             alt="MoMo" style="width:36px;height:36px;object-fit:contain;border-radius:6px;margin-left:auto;flex-shrink:0;">
                    </label>
                </div>

                <div id="vnpayPopup">
                    <p class="vnpay-title">Bạn sẽ được chuyển đến cổng thanh toán VNPay</p>
                    <img class="vnpay-logo"
                         src="https://vinadesign.vn/uploads/images/2023/05/vnpay-logo-vinadesign-25-12-57-55.jpg"
                         alt="VNPay">
                    <p class="vnpay-info">Hỗ trợ: ATM, Visa, MasterCard, QR Code</p>
                    <p class="vnpay-info">Nhấn <strong>ĐẶT HÀNG</strong> để tiếp tục thanh toán</p>
                </div>

                <div id="momoPopup" style="display:none;">
                    <p class="momo-title">Quét mã QR để thanh toán qua MoMo</p>
                    <img id="momoQR" src="" alt="QR MoMo" class="momo-qr">
                    <p class="momo-note">Nội dung: <strong id="momoContent">Đang tạo mã QR...</strong></p>
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
            document.getElementById('shippingFee').textContent = fee.toLocaleString('vi-VN') + '₫';
            document.getElementById('totalPrice').textContent = (baseTotal + fee).toLocaleString('vi-VN') + '₫';
            refreshSubPayment();
        });
    });
    document.querySelector('input[name="shippingMethod"]:checked').dispatchEvent(new Event('change'));

    function openModal() { document.getElementById('addressModal').classList.add('show'); }
    function closeModal() { document.getElementById('addressModal').classList.remove('show'); }

    document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const subOptions = document.getElementById('subPaymentOptions');
            if (this.value === 'transfer') {
                subOptions.classList.add('show');
                refreshSubPayment();
            } else {
                subOptions.classList.remove('show');
                document.getElementById('vnpayPopup').style.display = 'none';
                document.getElementById('momoPopup').style.display = 'none';
            }
        });
    });

    document.querySelectorAll('input[name="subPaymentMethod"]').forEach(radio => {
        radio.addEventListener('change', function() {
            refreshSubPayment();
        });
    });

    function refreshSubPayment() {
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
        if (paymentMethod !== 'transfer') return;

        const sub = document.querySelector('input[name="subPaymentMethod"]:checked').value;
        if (sub === 'vnpay') {
            document.getElementById('vnpayPopup').style.display = 'block';
            document.getElementById('momoPopup').style.display = 'none';
        } else {
            document.getElementById('vnpayPopup').style.display = 'none';
            document.getElementById('momoPopup').style.display = 'block';
            loadMoMoQR();
        }
    }

    function loadMoMoQR() {
        const total = parseInt(document.getElementById('totalPrice').textContent.replace(/\D/g, ''));
        const orderId = 'ORDER' + Date.now();

        document.getElementById('momoQR').src = '';
        document.getElementById('momoContent').textContent = 'Đang tạo mã QR...';

        const formData = new FormData();
        formData.append('amount', total);
        formData.append('orderId', orderId);
        formData.append('orderInfo', 'Thanh toán đơn hàng' + orderId);

        fetch('${pageContext.request.contextPath}/momo-payment', {
            method: 'POST',
            body: formData
        })
            .then(res => res.json())
            .then(data => {
                if (data.payUrl) {
                    document.getElementById('momoQR').src =
                        'https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=' + encodeURIComponent(data.payUrl);
                    document.getElementById('momoContent').textContent = orderId;
                    document.getElementById('momoPopup').dataset.payUrl = data.payUrl;
                } else {
                    document.getElementById('momoContent').textContent = 'Lỗi: ' + (data.message || 'Không tạo được QR');
                }
            })
            .catch(err => {
                document.getElementById('momoContent').textContent = 'Lỗi kết nối: ' + err.message;
            });
    }

    document.getElementById('checkoutForm').addEventListener('submit', function(e) {
        const phoneInput = document.getElementById('phone');
        const nameInput = document.getElementById('recipientName');
        const phoneValue =
            document.getElementById('phone').value.trim();

        if (!/^(03|05|07|08|09)\d{8}$/.test(phoneValue)) {
            e.preventDefault();
            alert('Số điện thoại không hợp lệ');
            return;
        }

        phoneInput.dispatchEvent(new Event('blur'));
        nameInput.dispatchEvent(new Event('blur'));

        if ((phoneErr && phoneErr.style.display !== 'none' && phoneErr.textContent.trim() !== '')) {
            e.preventDefault();
            e.stopImmediatePropagation();
            return;
        }

        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
        if (paymentMethod === 'transfer') {
            e.preventDefault();
            const sub = document.querySelector('input[name="subPaymentMethod"]:checked').value;
            const total = parseInt(document.getElementById('totalPrice').textContent.replace(/\D/g, ''));
            const orderId = 'ORDER' + Date.now();

            if (sub === 'vnpay') {
                fetch('${pageContext.request.contextPath}/vnpay-payment?amount=' + total + '&orderId=' + orderId, {
                    method: 'POST'
                })
                    .then(res => res.json())
                    .then(data => {
                        if (data.payUrl) {
                            window.location.href = data.payUrl;
                        } else {
                            alert('Không thể tạo thanh toán VNPay: ' + (data.error || 'Lỗi không xác định'));
                        }
                    })
                    .catch(err => alert('Lỗi kết nối VNPay: ' + err.message));
            } else {
                const payUrl = document.getElementById('momoPopup').dataset.payUrl;
                if (payUrl) {
                    window.location.href = payUrl;
                } else {
                    alert('Vui lòng chờ mã QR tải xong hoặc thử lại');
                }
            }
        }
    });
</script>

<script src="${pageContext.request.contextPath}/views/JS/email_validation.js"></script>
<script src="${pageContext.request.contextPath}/views/JS/phone_validation.js"></script>
<script src="${pageContext.request.contextPath}/views/JS/name_validation.js"></script>
</body>
</html>