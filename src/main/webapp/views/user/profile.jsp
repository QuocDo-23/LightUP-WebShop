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

    <title>Thông tin của tôi</title>
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

                    <c:if test="${not empty message}">
                        <div class="message ${messageType}">
                                ${message}
                        </div>
                    </c:if>

                    <h2 class="section-title">Thông Tin Cá Nhân</h2>

                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="updateProfile">

                        <div class="form-row">
                            <div class="form-group">
                                <label>Họ và Tên</label>
                                <input type="text" name="fullName"
                                       value="${user.name}"
                                       placeholder="Nhập họ và tên"
                                       required>
                            </div>

                            <div class="form-group">
                                <label>Email</label>
                                <input type="email" name="email"
                                       value="${user.email}"
                                       placeholder="Nhập email"
                                       readonly
                                       style="background-color: #f0f0f0;">
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label>Giới Tính</label>
                                <select name="gender">
                                    <option value="">Chọn giới tính</option>
                                    <option value="male"   ${user.gender == 'male' ? 'selected' : ''}>Nam</option>
                                    <option value="female" ${user.gender == 'female' ? 'selected' : ''}>Nữ</option>
                                    <option value="other"  ${user.gender == 'other' ? 'selected' : ''}>Khác</option>
                                </select>

                            </div>

                            <div class="form-group">
                                <label>Ngày Sinh</label>
                                <input type="date" name="dob" value="${user.dateOfBirth}" placeholder="Chọn ngày sinh">
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Số Điện Thoại</label>
                            <input type="tel" name="phone"
                                   value="${user.phone}"
                                   placeholder="Nhập số điện thoại"
                                   pattern="[0-9]{10,11}">
                        </div>

                        <button type="submit" class="save-btn">Lưu Thay Đổi</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="/views/layout/footer.jsp"/>

</main>

<div class="avatar-toast" id="avatarToast"></div>

<script>
    window.onload = function () {
        const message = document.querySelector('.message');
        if (message) {
            setTimeout(function () {
                message.style.display = 'none';
            }, 5000);
        }
    };

    function showToast(msg, type) {
        const toast = document.getElementById('avatarToast');
        toast.textContent = msg;
        toast.className = 'avatar-toast ' + type + ' show';
        clearTimeout(toast._timer);
        toast._timer = setTimeout(function () {
            toast.classList.remove('show');
        }, 3500);
    }

    document.getElementById('avatarInput').addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;

        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
        if (!allowedTypes.includes(file.type)) {
            showToast('Hình ảnh không hợp lệ.   ', 'error');
            this.value = '';
            return;
        }
        if (file.size > 5 * 1024 * 1024) {
            showToast('Ảnh không được vượt quá 5MB.', 'error');
            this.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('avatarPreview').src = e.target.result;
        };
        reader.readAsDataURL(file);

        const spinner = document.getElementById('avatarSpinner');
        spinner.classList.add('active');

        const formData = new FormData();
        formData.append('avatar', file);

        fetch('${pageContext.request.contextPath}/upload-avatar', {
            method: 'POST',
            body: formData
        })
            .then(function (res) { return res.json(); })
            .then(function (data) {
                spinner.classList.remove('active');
                if (data.success) {
                    document.getElementById('avatarPreview').src = data.avatarUrl + '?t=' + Date.now();
                    showToast(data.message, 'success');
                } else {
                    showToast(data.message || 'Upload thất bại.', 'error');
                }
            })
            .catch(function () {
                spinner.classList.remove('active');
                showToast('Có lỗi xảy ra khi kết nối server.', 'error');
            });

        this.value = '';
    });

    function showAddAddressForm() {
        document.getElementById('addAddressForm').style.display = 'block';
    }

    function hideAddAddressForm() {
        document.getElementById('addAddressForm').style.display = 'none';
    }
</script>
</body>
</html>