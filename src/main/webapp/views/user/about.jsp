<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <link href='https://fonts.googleapis.com/css?family=Monsieur La Doulaise' rel='stylesheet'>
    <link href='https://fonts.googleapis.com/css?family=Literata' rel='stylesheet'>
    <title>GIới Thiệu</title>
</head>

<body>
<main>
    <jsp:include page="/views/layout/header.jsp"/>
    <div class="link-page" id="up">
        <div class="containt_link">
            <a href="./"><i class="bi bi-house"></i> Trang chủ </a>
            <span> /</span>
            <a href="">Giới thiệu</a>
        </div>
    </div>
    <div class="search-overlay" id="searchOverlay" onclick="closeSearchPanel()"></div>

    <section class="section_1_about" id="home">
        <div class="cont">
            <div class="section_1_content">
                <h1>
                    Lightup
                </h1>
                <span class="highlight">GIỚI THIỆU</span>
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

    <section class="story-section">
        <div class="section-header">
            <h2>VỀ CHÚNG TÔI</h2>
            <div class="divider"></div>
        </div>
        <div class="story-content">

            <div class="story-text">
                <h3>Khởi Nguồn Từ Đam Mê</h3>
                <p>Được thành lập vào năm <b>2025</b>, xuất phát từ niềm đam mê tạo nên những không gian sống tràn
                    đầy ánh sáng và cảm hứng. Ban đầu chỉ là một cửa hàng nhỏ chuyên cung cấp các mẫu đèn dân dụng
                    và công nghiệp, chúng tôi không ngừng nỗ lực cải thiện chất lượng sản phẩm, dịch vụ và trải
                    nghiệm khách hàng.</p>
                <p>Trải qua quá trình phát triển, LightUp đã mở rộng hệ thống phân phối trên toàn quốc, hợp tác với
                    nhiều đối tác trong và ngoài nước để mang đến các sản phẩm chiếu sáng <b>hiện đại, tiết kiệm
                        năng lượng và thân thiện với môi trường.</b></p>
            </div>
            <div class="story-image">
                <img src="https://i.pinimg.com/736x/43/ae/fa/43aefa6d0523e2eca94110c4af85834c.jpg" alt="">
            </div>
        </div>

        <div class="story-content">
            <div class="story-image">
                <img src="https://c.files.bbci.co.uk/e30d/live/27add5e0-b00e-11f0-8317-a7963fdcfd4f.jpg" alt="">
            </div>
            <div class="story-text">
                <h3>Tầm Nhìn Thiết Kế</h3>
                <p>Tại LightUp, chúng tôi xem ánh sáng là <b>nghệ
                    thuật của không
                    gian </b>– nơi cảm xúc,
                    vẻ đẹp
                    và công năng hòa quyện trong từng chi tiết.</p>
                <p>Mỗi thiết kế ra đời không chỉ để chiếu sáng, mà để <b>kể một câu chuyện </b>– về sự ấm áp, về
                    những
                    khoảnh khắc lặng yên, hay về nguồn năng lượng tràn đầy trong mỗi ngôi nhà.</p>
                <p>Từ đường cong mềm mại của chiếc chụp đèn, cho đến sắc độ dịu nhẹ của từng tia sáng, mọi yếu tố
                    đều được chăm chút tỉ mỉ để đạt đến sự cân bằng giữa <b>nghệ thuật và công năng</b>.</p>
                <p><b>“Chúng tôi không tạo ra ánh sáng — chúng tôi khơi dậy cảm xúc từ ánh sáng.</b>”
                </p>
            </div>
        </div>
    </section>


    <section class="values-section">
        <div class="values-cont">
            <div class="section-header">
                <h2>GIÁ TRỊ CỐT LỖI</h2>
                <div class="divider"></div>
            </div>
            <div class="values-grid">
                <div class="value-card">
                    <div class="value-icon">✨</div>
                    <h3>Chất Lượng</h3>
                    <p>Cam kết sử dụng vật liệu cao cấp và quy trình sản xuất nghiêm ngặt để mang đến sản phẩm bền
                        vững.
                    </p>
                </div>
                <div class="value-card">
                    <div class="value-icon">🎨</div>
                    <h3>Sáng Tạo</h3>
                    <p>Không ngừng đổi mới và cập nhật xu hướng thiết kế hiện đại nhất từ khắp nơi trên thế giới.
                    </p>
                </div>
                <div class="value-card">
                    <div class="value-icon">🤝</div>
                    <h3>Tận Tâm</h3>
                    <p>Luôn lắng nghe và thấu hiểu nhu cầu của khách hàng để tạo ra không gian sống hoàn hảo nhất.
                    </p>
                </div>
                <div class="value-card">
                    <div class="value-icon">🌿</div>
                    <h3>Bền Vững</h3>
                    <p>Ưu tiên sử dụng nguyên liệu thân thiện với môi trường và quy trình sản xuất xanh.</p>
                </div>
            </div>
        </div>
    </section>

    <section class="stats-section" id="about">
        <div class="section-header">
            <h2>Thành tựu đạt được</h2>
            <div class="divider"></div>
        </div>

        <div class="container">
            <div class="stats-grid">
                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon">
                            <i class="bi bi-person-gear"></i>
                        </div>
                    </div>
                    <div class="stat-number">500+</div>
                    <div class="stat-label">Nhà Thầu</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon">
                            <i class="bi bi-award"></i>
                        </div>
                    </div>
                    <div class="stat-number">30+</div>
                    <div class="stat-label">Năm Thành Lập</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon">
                            <i class="bi bi-building"></i>
                        </div>
                    </div>
                    <div class="stat-number">4000+</div>
                    <div class="stat-label">Đại Lý và Cửa Hàng</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon">
                            <i class="bi bi-lightbulb"></i>
                        </div>
                    </div>
                    <div class="stat-number">5000+</div>
                    <div class="stat-label">Mã Sản Phẩm</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon">
                            <i class="bi bi-box-seam"></i>
                        </div>
                    </div>
                    <div class="stat-number">60M+</div>
                    <div class="stat-label">Sản Phẩm/Năm</div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="/views/layout/footer.jsp"/>
    <a href="#up">
        <button id="scrollToTopBtn">
            <i class="bi bi-chevron-up"></i>
        </button>
    </a>
</main>

<script src="./JS/index.js"></script>

</body>
</html>
