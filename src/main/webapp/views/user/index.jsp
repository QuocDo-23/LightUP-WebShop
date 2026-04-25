<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/products.css">
    <base href="${pageContext.request.contextPath}/">

    <link href='https://fonts.googleapis.com/css?family=Monsieur La Doulaise' rel='stylesheet'>
    <link href='https://fonts.googleapis.com/css?family=Literata' rel='stylesheet'>
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LightUP - Đèn Và Thiết Bị Chiếu Sáng</title>
</head>

<body>
<main>
    <jsp:include page="/views/layout/header.jsp"/>

    <div class="search-overlay" id="searchOverlay" onclick="closeSearchPanel()"></div>

    <div class="carousel-container" id="carouselContainer">

        <div class="carousel-slide active">
            <div class="section_1"
                 style="background-image: url('https://anhsanghoanglam.vn/wp-content/uploads/2022/05/z2483765420178_66b758a5966fe34dd8e7e861b837681d-scaled-1.jpg');">
                <div class="cont">
                    <div class="section_1-content">
                        <h1>Lightup</h1>
                        <span class="highlight">Đèn Và Thiết Bị Chiếu Sáng</span>
                        <p class="line1">Chúng tôi mang đến ánh sáng chuẩn mực từ những bộ sưu tập chất lượng.</p>
                        <p class="line2">Tạo nên không gian sang trọng, tinh tế, an tâm và đầy cảm hứng.</p>
                        <a href="products" class="cta-button">Mua Ngay</a>
                    </div>
                </div>
            </div>
        </div>


        <c:forEach var="b" items="${banners}">
            <a href="${b.link_banner}">
            <div class="carousel-slide">
                <div class="section_1" style="background-image: url('${b.img}');">
                </div>
            </div>
            </a>
        </c:forEach>

        <button class="carousel-arrow prev" onclick="prevSlide()">❮</button>
        <button class="carousel-arrow next" onclick="nextSlide()">❯</button>

        <div class="carousel-dots" id="dots"></div>
    </div>

    <div class="section-2">
        <div class="section-header">
            <h2>DÒNG SẢN PHẨM CHÍNH</h2>
            <div class="divider"></div>
        </div>

        <div class="products-grid-2">
            <c:forEach var="category" items="${categories}">
                <a href="cate_products?id=${category.id}">
                    <div class="product-card-2">
                        <div class="product-image-2">
                            <img src="${not empty category.imgCate ? category.imgCate : 'images/default-category.jpg'}"
                                 alt="${category.name}"/>
                        </div>
                        <div class="product-name-2">${category.name}</div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>

    <div class="section-3">
        <div class="section-header">
            <h2>SẢN PHẨM NỔI BẬT</h2>
            <div class="divider"></div>
        </div>

        <div class="products-slider">
            <button class="slider-btn prev" onclick="slideProducts(-1)">‹</button>

            <div class="product-grid" id="productGrid">
                <c:forEach var="product" items="${listProducts}">
                    <div class="product-card">
                        <div class="product-image">
                            <c:if test="${product.hasDiscount()}">
                                <div class="product-sale">
                                    -<fmt:formatNumber value="${product.discountRate}" maxFractionDigits="0"/>%
                                </div>
                            </c:if>
                            <a href="product-detail?id=${product.id}">
                                <img src="${not empty product.mainImage ? product.mainImage : 'images/default-product.jpg'}"
                                     alt="${product.name}" class="img-main">
                                <c:if test="${not empty product.hoverImage}">
                                    <img src="${product.hoverImage}" alt="${product.name}" class="img-hover">
                                </c:if>
                            </a>
                            <form action="${pageContext.request.contextPath}/favorite"
                                  method="post"
                                  class="favorite-form">

                                <input type="hidden"
                                       name="productId"
                                       value="${product.id}">

                                <button type="submit" class="favorite-btn">
                                    <i class="bi bi-heart-fill"></i>
                                </button>

                            </form>
                        </div>

                        <div class="product-info">
                            <h3 class="product-name">
                                <a href="product-detail?id=${product.id}">${product.name}</a>
                            </h3>

                            <div class="rating-box">
                                <div class="star-rating">
                                    <span style="width:${(product.review != null ? product.review : 0) * 20}%;"></span>
                                </div>
                            </div>

                            <div class="product-action">
                                <div class="product-prices">
                                    <span class="current-price">
                                        <fmt:formatNumber value="${product.discountedPrice}" pattern="#,###"/>₫
                                    </span>
                                    <c:if test="${product.hasDiscount()}">
                                        <span class="old-price">
                                            <del><fmt:formatNumber value="${product.price}" pattern="#,###"/>₫</del>
                                        </span>
                                    </c:if>
                                </div>
                                <div class="cart-icon">
                                    <a class="open-cart"
                                       href="${pageContext.request.contextPath}/add-cart?pID=${product.id}&quantity=1">
                                        <i class="bi bi-cart-check"></i>
                                    </a>
                                </div>
                            </div>

                            <div class="product-meta">
                                <span class="sold">Còn lại: ${product.inventoryQuantity}</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <button class="slider-btn next" onclick="slideProducts(1)">›</button>
        </div>
    </div>

    <section class="section_4">
        <div class="cont">
            <div class="section_cont">
                <div class="section-header">
                    <h2>VỀ CHÚNG TÔI</h2>
                    <div class="divider"></div>
                </div>
                <div class="select_colume">
                    <img src="https://th.bing.com/th/id/R.93f5d0df7e38d33e466095264a03a560?rik=%2fmjCTr4MJTbPHQ&pid=ImgRaw&r=0" alt="">
                    <div class="cont-r">
                        <h2>LightUP được thành lập với niềm đam mê mang ánh sáng và sự ấm áp đến mọi không gian sống.
                            Chúng tôi hiểu rằng ánh sáng không đơn thuần là một nhu cầu thiết yếu, mà còn là yếu tố quan
                            trọng tạo nên không khí, phong cách và cảm xúc cho ngôi nhà của bạn.</h2>
                        <p><b>SỨ MỆNH</b>: Mang đến những giải pháp chiếu sáng thông minh, tiết kiệm năng lượng và thẩm
                            mỹ cao, giúp khách hàng tạo nên không gian sống hoàn hảo với chi phí hợp lý nhất.</p>
                        <p><b>TẦM NHÌN</b>: Trở thành thương hiệu thiết bị chiếu sáng được tin dùng nhất tại Việt Nam,
                            nơi khách hàng tìm thấy sự kết hợp hoàn hảo giữa chất lượng, thiết kế và công nghệ.</p>
                        <div class="cont-in">
                            <img src="https://www.mpe.com.vn/media/ivhkb2hx/mpe-smart-control-recovered-02.png" alt="">
                            <img src="https://www.mpe.com.vn/media/jwahhja1/canhquan1.png" alt="">
                            <img src="https://www.mpe.com.vn/media/ekqb0p4h/nha-xu-o-ng.png" alt="">
                            <img src="https://www.mpe.com.vn/media/b0elw3rk/sanvandong.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="stats-section">
        <div class="container">
            <div class="stats-grid">
                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon"><i class="bi bi-person-gear"></i></div>
                    </div>
                    <div class="stat-number">500+</div>
                    <div class="stat-label">Nhà Thầu</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon"><i class="bi bi-award"></i></div>
                    </div>
                    <div class="stat-number">30+</div>
                    <div class="stat-label">Năm Thành Lập</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon"><i class="bi bi-building"></i></div>
                    </div>
                    <div class="stat-number">4000+</div>
                    <div class="stat-label">Đại Lý và Cửa Hàng</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon"><i class="bi bi-lightbulb"></i></div>
                    </div>
                    <div class="stat-number">5000+</div>
                    <div class="stat-label">Mã Sản Phẩm</div>
                </div>

                <div class="stat-item">
                    <div class="icon-wrapper">
                        <div class="icon"><i class="bi bi-box-seam"></i></div>
                    </div>
                    <div class="stat-number">60M+</div>
                    <div class="stat-label">Sản Phẩm/Năm</div>
                </div>
            </div>
        </div>
    </section>

    <section class="section-5">
        <div class="container">
            <div class="section-header">
                <h2>TIN TỨC</h2>
                <div class="divider"></div>
            </div>

            <div class="news-grid">
                <c:forEach var="article" items="${listArticle}" begin="0" end="0">
                    <div class="featured-post">
                        <img src="${article.mainImg}" alt="${article.title}" class="featured-thumbnail">
                        <div class="featured-content">
                            <h3><a href="news_content?id=${article.id}">${article.title}</a></h3>
                            <p>${article.description}</p>
                        </div>
                    </div>
                </c:forEach>
                <div class="side-posts">
                    <c:forEach var="article" items="${listArticle}" begin="1" end="3">
                        <div class="post-item">
                            <img src="${article.mainImg}" alt="${article.title}" class="post-thumbnail">
                            <div class="post-content">
                                <h4><a href="news_content?id=${article.id}">${article.title}</a></h4>
                                <p>${article.description}</p>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="view-more">
                        <a href="news" class="view-more-btn">
                            XEM THÊM <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="/views/layout/footer.jsp"/>

    <a href="#">
        <button id="scrollToTopBtn">
            <i class="bi bi-chevron-up"></i>
        </button>
    </a>

    <div id="cart-overlay"></div>

</main>

<script src="${pageContext.request.contextPath}/views/JS/actionButton.js"></script>
<script src="${pageContext.request.contextPath}/views/JS/banner.js"></script>
<script src="${pageContext.request.contextPath}/views/JS/products.js?v=5"></script>

</body>
</html>
