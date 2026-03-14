<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/confirm.css">

    <title>Địa chỉ của tôi</title>
</head>

<body>
<main>
    <!-- header -->
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

                    <c:if test="${not empty message}">
                        <div class="message ${messageType}">
                                ${message}
                        </div>
                    </c:if>

                    <div class="address-section" style="margin-top: 0;">
                        <h2 class="section-title">Địa Chỉ Của Tôi</h2>

                        <button class="add-address-btn" onclick="showAddAddressForm()">
                            <i class="bi bi-plus-circle"></i> Thêm Địa Chỉ Mới
                        </button>

                        <div id="addAddressForm" class="form-container">
                            <h3>Thêm Địa Chỉ Mới</h3>
                            <form action="${pageContext.request.contextPath}/address" method="post">
                                <input type="hidden" name="action" value="addAddress">

                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Tên người nhận</label>
                                        <input type="text" name="recipientName" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Số điện thoại</label>
                                        <input type="tel" name="phone" pattern="[0-9]{10,11}" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" name="email" value="${user.email}">
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Số nhà</label>
                                        <input type="text" name="houseNumber" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Phường/Xã</label>
                                        <input type="text" name="commune" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Quận/Huyện</label>
                                    <input type="text" name="district" required>
                                </div>

                                <div class="form-group-area">
                                    <label>Chi tiết địa chỉ</label>
                                    <textarea name="addressDetail" rows="3" placeholder="Ví dụ: Gần chợ, đối diện trường học..."></textarea>
                                </div>

                                <div class="checkbox-group">
                                    <label>
                                        <input type="checkbox" name="isDefault" value="true">
                                        Đặt làm địa chỉ mặc định
                                    </label>
                                </div>

                                <button type="submit" class="save-btn">Lưu Địa Chỉ</button>
                                <button type="button" class="save-btn" onclick="hideAddAddressForm()" style="background: #6c757d">Hủy</button>
                            </form>
                        </div>

                        <div id="editAddressForm" class="form-container">
                            <h3>Cập Nhật Địa Chỉ</h3>
                            <form action="${pageContext.request.contextPath}/address" method="post">
                                <input type="hidden" name="action" value="updateAddress">
                                <input type="hidden" name="addressId" id="edit_addressId">

                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Tên người nhận</label>
                                        <input type="text" name="recipientName" id="edit_recipientName" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Số điện thoại</label>
                                        <input type="tel" name="phone" id="edit_phone" pattern="[0-9]{10,11}" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" name="email" id="edit_email">
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Số nhà</label>
                                        <input type="text" name="houseNumber" id="edit_houseNumber" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Phường/Xã</label>
                                        <input type="text" name="commune" id="edit_commune" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Quận/Huyện</label>
                                    <input type="text" name="district" id="edit_district" required>
                                </div>

                                <div class="form-group-area">
                                    <label>Chi tiết địa chỉ</label>
                                    <textarea name="addressDetail" id="edit_addressDetail" rows="3"></textarea>
                                </div>

                                <div class="checkbox-group">
                                    <label>
                                        <input type="checkbox" name="isDefault" id="edit_isDefault" value="true">
                                        Đặt làm địa chỉ mặc định
                                    </label>
                                </div>

                                <button type="submit" class="save-btn">Cập Nhật</button>
                                <button type="button" class="save-btn" onclick="hideEditAddressForm()" style="background: #6c757d">Hủy</button>
                            </form>
                        </div>

                        <c:choose>
                            <c:when test="${empty addresses}">
                                <p style="color: #666; padding: 20px; text-align: center;">
                                    Bạn chưa có địa chỉ nào. Hãy thêm địa chỉ mới!
                                </p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${addresses}" var="addr">
                                    <div class="address-card ${addr['default'] ? 'default' : ''}">
                                        <div class="address-header">
                                            <strong>${addr.recipientName}</strong>
                                            <c:if test="${addr['default']}">
                                                <span class="default-badge">Mặc định</span>
                                            </c:if>
                                        </div>
                                        <p><strong>SĐT:</strong> ${addr.phone}</p>
                                        <p><strong>Email:</strong> ${addr.email}</p>
                                        <p><strong>Địa chỉ:</strong> ${addr.house_number}, ${addr.commune}, ${addr.district}</p>
                                        <c:if test="${not empty addr.addressDetail}">
                                            <p><strong>Chi tiết:</strong> ${addr.addressDetail}</p>
                                        </c:if>

                                        <div class="address-actions">
                                            <button type="button" class="btn-edit"
                                                    onclick="showEditAddressForm(
                                                            '${addr.id}',
                                                            '${addr.recipientName}',
                                                            '${addr.phone}',
                                                            '${addr.email}',
                                                            '${addr.house_number}',
                                                            '${addr.commune}',
                                                            '${addr.district}',
                                                            '${addr.addressDetail}',
                                                            ${addr['default']}
                                                            )">
                                                Sửa
                                            </button>

                                            <c:if test="${!addr['default']}">
                                                <form action="${pageContext.request.contextPath}/address" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="setDefaultAddress">
                                                    <input type="hidden" name="addressId" value="${addr.id}">
                                                    <button type="submit" class="btn-default">Mặc định</button>
                                                </form>
                                            </c:if>

                                            <form action="${pageContext.request.contextPath}/address" method="post" style="display: inline;" onsubmit="return confirm('Bạn có chắc muốn xóa địa chỉ này?');">
                                                <input type="hidden" name="action" value="deleteAddress">
                                                <input type="hidden" name="addressId" value="${addr.id}">
                                                <button type="submit" class="btn-delete">Xóa</button>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>

        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="/views/layout/footer.jsp"/>

</main>

<script>
    function showAddAddressForm() {
        document.getElementById('addAddressForm').style.display = 'block';
        document.getElementById('editAddressForm').style.display = 'none';
    }

    function hideAddAddressForm() {
        document.getElementById('addAddressForm').style.display = 'none';
    }

    function showEditAddressForm(id, name, phone, email, house, commune, district, detail, isDefault) {
        document.getElementById('addAddressForm').style.display = 'none';
        document.getElementById('editAddressForm').style.display = 'block';

        document.getElementById('edit_addressId').value = id;
        document.getElementById('edit_recipientName').value = name;
        document.getElementById('edit_phone').value = phone;
        document.getElementById('edit_email').value = email;
        document.getElementById('edit_houseNumber').value = house;
        document.getElementById('edit_commune').value = commune;
        document.getElementById('edit_district').value = district;
        document.getElementById('edit_addressDetail').value = detail;
        document.getElementById('edit_isDefault').checked = isDefault;

        document.getElementById('editAddressForm').scrollIntoView({ behavior: 'smooth' });
    }

    function hideEditAddressForm() {
        document.getElementById('editAddressForm').style.display = 'none';
    }

    window.onload = function() {
        const message = document.querySelector('.message');
        if (message) {
            setTimeout(function() {
                message.style.display = 'none';
            }, 5000);
        }
    };
</script>

</body>
</html>