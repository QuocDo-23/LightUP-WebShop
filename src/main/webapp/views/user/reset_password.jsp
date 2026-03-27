<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/create_password.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>

<header class="cp-header">
    <div class="header-containt">
        <div class="header-left">
            <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" class="header-logo">
            <h2>Đặt lại mật khẩu</h2>
        </div>
        <div class="header-right">
            <a href="${pageContext.request.contextPath}/contact">Bạn cần giúp đỡ?</a>
        </div>
    </div>
</header>

<div class="cp-container">
    <div class="container">

        <form action="${pageContext.request.contextPath}/reset-password"
              method="post"
              class="cp-form"
              id="resetForm">

            <div class="abc">
                <div class="back-btn">
                    <a href="login"><i class="bi bi-arrow-left-circle"></i></a>
                </div>

                <h2>Tạo mật khẩu mới</h2>

            </div>


            <div class="form-group">
                <label for="password">Nhập mật khẩu mới: </label>
                <ul class="password-requirements">
                    <li id="length">
                        <i class="bi bi-x-circle requirement-not-met"></i>
                        <span>Ít nhất 6 ký tự</span>
                    </li>
                    <li id="letter">
                        <i class="bi bi-x-circle requirement-not-met"></i>
                        <span>Có chữ cái</span>
                    </li>
                    <li id="number">
                        <i class="bi bi-x-circle requirement-not-met"></i>
                        <span>Có số</span>
                    </li>
                </ul>
                <div class="password-toggle">
                    <input type="password"
                           name="newPassword"
                           id="newPassword"
                           required
                           minlength="6">
                </div>
                <div class="password-strength">
                    <div class="password-strength-bar" id="strengthBar"></div>
                </div>

            </div>

            <div class="form-group">
                <label for="password">Xác nhận mật khẩu: </label>
                <div class="password-toggle">
                    <input type="password"
                           name="confirmPassword"
                           id="confirmPassword"
                           required>
                </div>
                <p id="matchMessage" style="font-size: 13px; margin-top: 5px;"></p>
            </div>

            <button type="submit" class="btn-confirm">Đặt lại mật khẩu</button>

            <c:if test="${not empty error}">
                <p style="color:red; text-align: center; margin-top: 15px;">${error}</p>
            </c:if>

        </form>
    </div>
</div>

<script>
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const strengthBar = document.getElementById('strengthBar');
    const matchMessage = document.getElementById('matchMessage');


    newPassword.addEventListener('input', function () {
        const value = this.value;
        const length = value.length >= 6;
        const hasLetter = /[a-zA-Z]/.test(value);
        const hasNumber = /[0-9]/.test(value);

        updateRequirement('length', length);
        updateRequirement('letter', hasLetter);
        updateRequirement('number', hasNumber);

        // Tính độ mạnh
        let strength = 0;
        if (length) strength++;
        if (hasLetter) strength++;
        if (hasNumber) strength++;

        strengthBar.className = 'password-strength-bar';
        if (strength === 1) {
            strengthBar.classList.add('strength-weak');
        } else if (strength === 2) {
            strengthBar.classList.add('strength-medium');
        } else if (strength === 3) {
            strengthBar.classList.add('strength-strong');
        }

        checkPasswordMatch();
    });

    confirmPassword.addEventListener('input', checkPasswordMatch);

    function updateRequirement(id, met) {
        const element = document.getElementById(id);
        const icon = element.querySelector('i');

        if (met) {
            icon.className = 'bi bi-check-circle requirement-met';
        } else {
            icon.className = 'bi bi-x-circle requirement-not-met';
        }
    }

    function checkPasswordMatch() {
        if (confirmPassword.value === '') {
            matchMessage.textContent = '';
            return;
        }

        if (newPassword.value === confirmPassword.value) {
            matchMessage.textContent = '✓ Mật khẩu khớp';
            matchMessage.style.color = '#28a745';
        } else {
            matchMessage.textContent = '✗ Mật khẩu không khớp';
            matchMessage.style.color = '#dc3545';
        }
    }

    // Validate form
    document.getElementById('resetForm').addEventListener('submit', function (e) {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            alert('Mật khẩu xác nhận không khớp!');
        }

        if (newPassword.value.length < 6) {
            e.preventDefault();
            alert('Mật khẩu phải có ít nhất 6 ký tự!');
        }
    });
</script>

</body>
</html>
