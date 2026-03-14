<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vn">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/about.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/contact.css">
    <link href='https://fonts.googleapis.com/css?family=Monsieur La Doulaise' rel='stylesheet'>
    <link href='https://fonts.googleapis.com/css?family=Literata' rel='stylesheet'>
    <title>LIÊN HỆ</title>
</head>

<body>
<main>

    <!-- HEADER -->
    <%@ include file="/views/layout/header.jsp" %>

    <div class="link-page" id="up">
        <div class="containt_link">
            <a href="index.jsp"><i class="bi bi-house"></i> Trang chủ </a>
            <span> /</span>
            <a href="">Liên Hệ</a>
        </div>
    </div>

    <div class="search-overlay" id="searchOverlay" onclick="closeSearchPanel()"></div>

    <section class="section_1_contact" id="home">
        <div class="cont">
            <div class="section_1_content">
                <h1>
                    Lightup
                </h1>
                <span class="highlight">LIÊN HỆ</span>
            </div>
        </div>

        <div class="ux-shape-divider ux-shape-divider--bottom">
            <svg viewBox="0 0 1000 100" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="none">
                <path class="ux-shape-fill"
                      d="M1000 100H0V80H479.686C481.808 80 483.843 80.8429 485.343 82.3432L497.879 94.8787C499.05 96.0503 500.95 96.0503 502.121 94.8787L514.657 82.3431C516.157 80.8428 518.192 80 520.314 80H1000V100Z">
                </path>
            </svg>
        </div>
    </section>

    <div id="content" class="content-area page-wrapper" role="main">

        <div class="container">
            <!-- Cột 1: Thông tin công ty -->
            <div class="col">
                <h3 class="title-contact-page">ĐÈN TRANG TRÍ LIGHTUP</h3>
                <p class="des-contact-page">
                    Cảm ơn quý khách đã lựa chọn ĐÈN TRANG TRÍ LIGHTUP, hy vọng quý khách hài lòng với
                    trải nghiệm mua sắm và các sản phẩm đã lựa chọn. Tại đây, chúng tôi sẽ giải đáp các
                    thắc mắc mà quý khách đang gặp phải.
                </p>

                <span class="widget-title">ĐÈN TRANG TRÍ LIGHTUP</span>

                <ul id="footer-text">
                    <li>
                        <i class="fa fa-map-marker"></i>
                        CN 1: Khu Phố 33, Phường Linh Xuân, TP.HCM – SDT: 0283.8966.780
                    </li>
                    <li>
                        <i class="fa fa-map-marker"></i>
                        CN 2: Đường Trần Nhật Duật, Phường Diên Hồng, Tỉnh Gia Lai. – SDT: 0269.3877.035
                    </li>
                    <li>
                        <i class="fa fa-map-marker"></i>
                        CN 3: Số 8 Yên Ninh, Phường Ninh Chữ, Tỉnh Ninh Thuận – SDT: 0259.2472.252
                    </li>
                </ul>

                <div class="google-map">
                    <iframe
                            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3918.5420805298436!2d106.7884143!3d10.8674833!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3175276398969f7b%3A0x9672b7efd0893fc4!2sTr%C6%B0%E1%BB%9Dng%20%C4%90%E1%BA%A1i%20h%E1%BB%8Dc%20N%C3%B4ng%20L%C3%A2m%20TP.HCM!5e0!3m2!1svi!2s!4v1761793349887!5m2!1svi!2s"
                            allowfullscreen="" loading="lazy"
                            referrerpolicy="no-referrer-when-downgrade">
                    </iframe>
                </div>
            </div>

            <!-- Cột 2: Form liên hệ -->
            <div class="col">
                <h3 class="title-contact-page">Để lại lời nhắn</h3>

                <form id="contactForm">
                    <label>Họ và tên*:</label>
                    <input type="text" name="ho-ten" placeholder="Họ và tên" required>

                    <label>Số điện thoại*:</label>
                    <input type="tel" name="so-dien-thoai" placeholder="Số điện thoại" required>

                    <label>Email*:</label>
                    <input type="email" name="email" placeholder="Email của bạn" required>

                    <label>Lời nhắn:</label>
                    <textarea name="thong-diep" placeholder="Thông điệp" maxlength="2000"></textarea>

                    <div class="btn">
                        <a href="#" onclick="submitForm(event)">Gửi Thông Tin</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <%@ include file="/views/layout/footer.jsp" %>

    <a href="#up">
        <button id="scrollToTopBtn">
            <i class="bi bi-chevron-up"></i>
        </button>
    </a>

</main>

<script src="JS/index.js"></script>
</body>
</html>
