<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="sidebar">
    <div class="edit-avatar">

        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <% session.removeAttribute("success"); %>
        </c:if>

        <c:choose>
            <c:when test="${not empty sessionScope.user.avatarImg
                    and fn:startsWith(sessionScope.user.avatarImg, 'http')}">
                <img src="${sessionScope.user.avatarImg}"
                     alt="Avatar"
                     class="profile-pic"
                     id="previewAvatar">
            </c:when>
            <c:when test="${not empty sessionScope.user.avatarImg}">
                <img src="${pageContext.request.contextPath}${sessionScope.user.avatarImg}"
                     alt="Avatar"
                     class="profile-pic"
                     id="previewAvatar">
            </c:when>
            <c:otherwise>
                <img src="https://ui-avatars.com/api/?name=${sessionScope.user.name}&background=cccccc&color=555555&size=110"
                     alt="Avatar"
                     class="profile-pic"
                     id="previewAvatar">
            </c:otherwise>
        </c:choose>

        <div class="cont">
            <label class="edit-avatar-btn">
                Chỉnh Sửa Ảnh
                <input type="file" name="avatar"
                       accept="image/jpeg,image/png,image/webp,image/gif"
                       id="avatarInputSidebar"
                       onchange="handleImageSelect(event)">
            </label>
        </div>
    </div>

    <a href="profile" class="menu-item ${activeTab == 'profile' ? 'active' : ''}">
        <i class="bi bi-person"></i> Thông Tin Cá Nhân
    </a>
    <a href="orders" class="menu-item ${activeTab == 'orders' ? 'active' : ''}">
        <i class="bi bi-clipboard-check"></i> Đơn Hàng
    </a>
    <a href="address" class="menu-item ${activeTab == 'address' ? 'active' : ''}">
        <i class="bi bi-geo-alt"></i> Địa Chỉ
    </a>
    <a href="logout" class="menu-item logout">
        <i class="bi bi-box-arrow-right"></i> Đăng Xuất
    </a>
</div>

<div id="confirmOverlay" class="confirm-overlay" style="display: none;">
    <div class="confirm-dialog">
        <div class="confirm-icon">
            <i class="bi bi-question-circle"></i>
        </div>
        <h3>Xác nhận thay đổi ảnh đại diện</h3>
        <p>Bạn có chắc chắn muốn cập nhật ảnh đại diện này không?</p>
        <div class="confirm-buttons">
            <button class="btn-cancel" onclick="cancelUpload()">
                <i class="bi bi-x-circle"></i> Hủy
            </button>
            <button class="btn-confirm" onclick="confirmAndSubmit()">
                <i class="bi bi-check-circle"></i> Xác nhận
            </button>
        </div>
    </div>
</div>


<script>
    let selectedFile = null;
    let originalAvatarSrc = null;

    function handleImageSelect(event) {
        const file = event.target.files[0];
        if (!file) return;

        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
        if (!allowedTypes.includes(file.type)) {
            alert('Chi chap nhan file JPG, PNG, WEBP, GIF!');
            event.target.value = '';
            return;
        }

        if (file.size > 5 * 1024 * 1024) {
            alert('Anh khong duoc vuot qua 5MB!');
            event.target.value = '';
            return;
        }

        selectedFile = file;

        const preview = document.getElementById('previewAvatar');
        originalAvatarSrc = preview.src;
        const reader = new FileReader();
        reader.onload = function () {
            preview.src = reader.result;
            showConfirmDialog();
        };
        reader.readAsDataURL(file);
    }

    function showConfirmDialog() {
        const overlay = document.getElementById('confirmOverlay');
        overlay.style.display = 'flex';
        setTimeout(() => overlay.classList.add('active'), 10);
    }

    function hideConfirmDialog() {
        const overlay = document.getElementById('confirmOverlay');
        overlay.classList.remove('active');
        setTimeout(() => overlay.style.display = 'none', 300);
    }

    function cancelUpload() {
        if (originalAvatarSrc) {
            document.getElementById('previewAvatar').src = originalAvatarSrc;
        }
        document.getElementById('avatarInput').value = '';
        selectedFile = null;
        hideConfirmDialog();
    }

    function confirmAndSubmit() {
        hideConfirmDialog();
        if (!selectedFile) return;

        const formData = new FormData();
        formData.append('avatar', selectedFile);

        fetch('${pageContext.request.contextPath}/upload-avatar', {
            method: 'POST',
            body: formData
        })
            .then(function (res) { return res.json(); })
            .then(function (data) {
                if (data.success) {
                    document.getElementById('previewAvatar').src = data.avatarUrl + '?t=' + Date.now();
                    alert(data.message);
                } else {
                    if (originalAvatarSrc) {
                        document.getElementById('previewAvatar').src = originalAvatarSrc;
                    }
                    alert(data.message || 'Upload thất bại.');
                }
            })
            .catch(function () {
                if (originalAvatarSrc) {
                    document.getElementById('previewAvatar').src = originalAvatarSrc;
                }
                alert('Có lỗi xảy ra khi kết nối server.');
            });

        document.getElementById('avatarInputSidebar').value = '';
        selectedFile = null;
        originalAvatarSrc = null;
    }

    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(alert => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        });
    }, 3000);
</script>