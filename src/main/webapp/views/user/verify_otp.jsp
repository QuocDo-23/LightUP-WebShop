<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/forgot_password.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>

<header class="cp-header">
    <div class="header-containt">
        <div class="header-left">
            <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" class="header-logo">
            <h2>Xác thực OTP</h2>
        </div>
        <div class="header-right">
            <a href="${pageContext.request.contextPath}/contact">Bạn cần giúp đỡ?</a>
        </div>
    </div>
</header>

<div class="fp-container">
    <div class="container">
        <form action="${pageContext.request.contextPath}/verify-otp"
              method="post"
              class="fp-form"
              id="otpForm">

            <div class="abc">
                <div class="back-btn">
                    <a href="${pageContext.request.contextPath}/forgot-password">
                        <i class="bi bi-arrow-left-circle"></i>
                    </a>
                </div>
                <h2>Nhập mã OTP</h2>
            </div>

            <p style="text-align: center; color: #666; margin-bottom: 20px;">
                Mã xác thực đã được gửi đến email của bạn
            </p>

            <div class="otp-input-group">
                <input type="text" class="otp-input" maxlength="1" id="otp1" />
                <input type="text" class="otp-input" maxlength="1" id="otp2" />
                <input type="text" class="otp-input" maxlength="1" id="otp3" />
                <input type="text" class="otp-input" maxlength="1" id="otp4" />
                <input type="text" class="otp-input" maxlength="1" id="otp5" />
                <input type="text" class="otp-input" maxlength="1" id="otp6" />
            </div>

            <input type="hidden" name="otp" id="otpValue" />

            <div class="timer" id="timer">
                Mã OTP có thời hạng 5p
            </div>

            <button type="submit" class="btn-request">Xác nhận</button>

            <div class="resend-link">
                <a href="${pageContext.request.contextPath}/forgot-password">Gửi lại mã OTP</a>
            </div>

            <c:if test="${not empty error}">
                <p style="color:red; text-align: center; margin-top: 15px;">${error}</p>
            </c:if>

        </form>
    </div>
</div>

<script>
    const inputs = document.querySelectorAll('.otp-input');

    inputs.forEach((input, index) => {
        input.addEventListener('input', (e) => {
            const value = e.target.value;

            if (value.length === 1 && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }

            updateOTPValue();
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !e.target.value && index > 0) {
                inputs[index - 1].focus();
            }
        });

        input.addEventListener('paste', (e) => {
            e.preventDefault();
            const pasteData = e.clipboardData.getData('text').slice(0, 6);

            pasteData.split('').forEach((char, i) => {
                if (inputs[i]) {
                    inputs[i].value = char;
                }
            });

            updateOTPValue();
        });
    });

    inputs[0].focus();

    function updateOTPValue() {
        const otp = Array.from(inputs).map(input => input.value).join('');
        document.getElementById('otpValue').value = otp;
    }

    document.getElementById('otpForm').addEventListener('submit', (e) => {
        const otp = document.getElementById('otpValue').value;
        if (otp.length !== 6) {
            e.preventDefault();
            alert('Vui lòng nhập đủ 6 số OTP');
        }
    });
</script>

</body>
</html>