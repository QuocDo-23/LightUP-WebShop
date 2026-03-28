<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - LightUp</title>

    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/cart.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/product-detail.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<jsp:include page="/views/layout/header.jsp"/>


<div class="link-page" id="up">
    <div class="containt_link">
        <a href="./index.jsp"><i class="bi bi-house"></i> Trang chủ </a>
        <span> /</span>
        <a href="./products.jsp"> Sản Phẩm </a>
        <span> /</span>
        <a href="">Chi tiết sản phẩm</a>
    </div>
</div>

<div class="row">

    <div class="content_img_l">
        <div class="cont">
            <div class="img_main">
                <img src="${images[0].img}" alt="${product.description}" id="mainImage">
            </div>
            <div class="img_down">
                <c:forEach var="img" items="${images}" varStatus="status">
                    <img src="${img.img}"
                         alt="${product.description}"
                         onclick="changeMainImage('${img.img}')"
                         class="${status.index == 0 ? 'active' : ''}">
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="product-image-summary">
        <div class="content_products">
            <div class="summary-inner">
                <h1 class="product_title">${product.name}</h1>

                <div class="product-prices">
                    <span class="current-price">
                        <fmt:formatNumber value="${product.discountedPrice}" pattern="#,###"/>₫
                    </span>
                    <c:if test="${product.hasDiscount()}">
                        <span class="old-price">
                            <del><fmt:formatNumber value="${product.price}" pattern="#,###"/>₫</del>
                        </span>
                        <span class="discount-badge">
                            -<fmt:formatNumber value="${product.discountRate}" maxFractionDigits="0"/>%
                        </span>
                    </c:if>
                </div>


                <table class="product-attributes">
                    <tbody>
                    <c:if test="${not empty product.warranty}">
                        <tr class="product-attributes-item">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Bảo hành</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.warranty}</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${not empty product.material}">
                        <tr class="product-attributes-item">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Vật liệu</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.material}</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${not empty product.voltage}">
                        <tr class="product-attributes-item">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Hiệu điện thế</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.voltage}</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${not empty product.dimensions}">
                        <tr class="product-attributes-item">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Kích thước</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.dimensions}</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${not empty product.type}">
                        <tr class="product-attributes-item">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Loại bóng</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.type}</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${not empty product.color}">
                        <tr class="product-item-label">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Màu sắc</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.color}</p>
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${not empty product.style}">
                        <tr class="product-attributes-item">
                            <th class="product-item-label">
                                <span class="wd-attr-name">Phong cách</span>
                            </th>
                            <td class="product-item-value">
                                <p>${product.style}</p>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>


                <form method="get" action="${pageContext.request.contextPath}/add-cart" id="productForm">

                    <input type="hidden" name="pID" value="${product.id}">


                    <div class="quantity-section">
                        <label class="quantity-label">Số lượng</label>
                        <div class="quantity-control">
                            <button type="button" class="qty-btn qty-decrease" onclick="decreaseQty()" aria-label="Giảm số lượng">
                                <span>−</span>
                            </button>
                            <input
                                    type="number"
                                    name="quantity"
                                    id="quantity"
                                    value="1"
                                    min="1"
                                    max="${product.inventoryQuantity}"
                                    aria-label="Số lượng sản phẩm"
                                    readonly
                            >
                            <button type="button" class="qty-btn qty-increase" onclick="increaseQty()" aria-label="Tăng số lượng">
                                <span>+</span>
                            </button>
                        </div>
                        <p class="stock-info">Còn lại: ${product.inventoryQuantity}</p>
                    </div>


                    <div class="action-buttons">

                        <button type="submit" class="btn btn-add-cart">
                            THÊM VÀO GIỎ
                        </button>


                        <button type="submit"
                                formmethod="post"
                                formaction="${pageContext.request.contextPath}/buy-now"
                                class="btn btn-buy-now">
                            MUA NGAY
                        </button>
                    </div>
                </form>



                <a href="${pageContext.request.contextPath}/contact">
                    <button class="btn btn-contact" style="width: 100%;">
                        LIÊN HỆ
                    </button>
                </a>
            </div>
        </div>
    </div>
</div>
</div>


<div class="product-tabs-wrapper">
    <div class="container">
        <div class="row">
            <div class="col-12 poduct-tabs-inner">
                <div class="wd-accordion">
                    <!-- Description Tab -->
                    <div class="wd-accordion-item">
                        <div class="wd-accordion-title" data-target="description">
                            <div class="wd-accordion-title-text">
                                <span>Mô tả</span>
                            </div>
                            <span class="wd-accordion-opener"></span>
                        </div>

                        <div class="wd-accordion-content" id="tab-description">
                            <div class="wc-tab-inner">
                                <h4>Chi tiết về ${product.description}</h4>

                                <c:if test="${not empty images && fn:length(images) > 0}">
                                    <div class="image-grid">
                                        <c:forEach var="img" items="${images}" begin="0" end="3">
                                            <img src="${img.img}" alt="${product.description}">
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>


                    <div class="wd-single-reviews">
                        <div id="reviews" class="woocommerce-Reviews">
                            <div id="comments">
                                <div class="reviews-title">
                                    ${stats.totalReviews} đánh giá cho <span>${product.name}</span>
                                </div>

                                <div class="star_box">
                                    <div class="star-average">
                                        <div class="product-rating">
                                                <span class="star_average">
                                                    <fmt:formatNumber value="${stats.averageRating}"
                                                                      maxFractionDigits="1"/>
                                                    <i class="devvn-star">★</i>
                                                </span>
                                            <div class="star-rating">
                                                <span style="width:${stats.averageRating * 20}%"></span>
                                            </div>
                                            <strong>Đánh giá trung bình</strong>
                                        </div>
                                    </div>

                                    <div class="star_box_left">
                                        <div class="reviews_bar">
                                            <c:forEach var="star" begin="1" end="5">
                                                <c:set var="starReversed" value="${6 - star}"/>
                                                <div class="devvn_review_row">
                                                    <span class="devvn_stars_value">${starReversed}<i
                                                            class="devvn-star">★</i></span>
                                                    <span class="devvn_rating_bar">
                                                            <span style="background-color: #eee"
                                                                  class="devvn_scala_rating">
                                                                <span class="devvn_perc_rating"
                                                                      style="width: ${stats.getPercentage(starReversed)}%; background-color: #f5a623"></span>
                                                            </span>
                                                        </span>
                                                    <span class="devvn_num_reviews">
                                                            <b><fmt:formatNumber
                                                                    value="${stats.getPercentage(starReversed)}"
                                                                    maxFractionDigits="0"/>%</b>
                                                        </span>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="star_box_right">
                                        <a href="#" class="btn-reviews-now">Đánh giá ngay</a>
                                    </div>
                                </div>


                                <ol class="commentlist">
                                    <c:forEach var="review" items="${reviews}">
                                        <li class="review" id="review-${review.id}">
                                            <div class="comment_container devvn_review_box">
                                                <div class="comment-text">
                                                    <div class="devvn_review_top">
                                                        <p class="meta">
                                                            <strong class="woocommerce-review__author">${review.userName}</strong>
                                                            <em class="woocommerce-review__verified verified">Đã mua tại
                                                                LightUp</em>
                                                        </p>
                                                    </div>

                                                    <div class="devvn_review_mid">
                                                        <div class="star-rating">
                                                            <span style="width:${review.rating * 20}%"></span>
                                                        </div>
                                                        <div class="description">
                                                            <p>${review.text}</p>
                                                        </div>
                                                    </div>

                                                    <div class="devvn_review_bottom">
                                                        <time class="woocommerce-review__published-date"
                                                              datetime="${review.date}">
                                                            <fmt:formatDate value="${review.date}"
                                                                            pattern="dd/MM/yyyy"/>
                                                        </time>
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ol>
                            </div>
                        </div>
                    </div>


                    <div class="related-products">
                        <h3 class="title slider-title">Sản phẩm tương tự</h3>

                        <div class="product-grid">
                            <c:forEach var="product" items="${relatedProducts}">
                                <div class="product-card"
                                     data-price="${product.discountedPrice}"
                                     data-rating="${product.review}"
                                     data-category="${product.categoryId}">
                                    <div class="product-image">
                                        <c:if test="${product.hasDiscount()}">
                                            <div class="product-sale">-<fmt:formatNumber
                                                    value="${product.discountRate}" maxFractionDigits="0"/>%
                                            </div>
                                        </c:if>
                                        <a href="product-detail?id=${product.id}">
                                            <img src="${not empty product.mainImage ? product.mainImage : 'default.jpg'}"
                                                 alt="${product.description}" class="img-main">
                                            <c:if test="${not empty product.hoverImage}">
                                                <img src="${product.hoverImage}"
                                                     alt="${product.description}" class="img-hover">
                                            </c:if>
                                        </a>
                                    </div>

                                    <div class="product-info">
                                        <h3 class="product-name">
                                            <a href="product-detail?id=${product.id}">
                                                    ${product.name}
                                            </a>
                                        </h3>
                                        <c:set var="rating" value="${product.review}"/>
                                        <div class="rating-box">
                                            <div class="star-rating">
                                                <span style="width:${rating * 20}%;"></span>
                                            </div>
                                        </div>
                                        <div class="product-action">
                                            <div class="product-prices">
                                                    <span class="current-price">
                                                        <fmt:formatNumber value="${product.discountedPrice}"
                                                                          pattern="#,###"/>₫
                                                    </span>
                                                <c:if test="${product.hasDiscount()}">
                                                        <span class="old-price">
                                                            <del><fmt:formatNumber value="${product.price}"
                                                                                   pattern="#,###"/>₫</del>
                                                        </span>
                                                </c:if>
                                            </div>
                                            <div class="cart-icon">
                                                <button type="button"
                                                        class="open-cart"
                                                        data-product-id="${product.id}">
                                                    <i class="bi bi-cart-check"></i>
                                                </button>
                                            </div>


                                        </div>

                                        <div class="product-meta">
                                            <span class="sold">Còn lại: ${product.inventoryQuantity}</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/views/layout/footer.jsp"/>


<a href="#up">
    <button id="scrollToTopBtn">
        <i class="bi bi-chevron-up"></i>
    </button>
</a>



<div class="overlay"></div>
<div class="review-box">
    <button class="close-btn">✖</button>
    <div class="review-title">Đánh giá sản phẩm</div>

    <form action="${pageContext.request.contextPath}/review"
          method="post"
          enctype="multipart/form-data"
          id="reviewForm"
          style="background:#fff;padding:20px;border-radius:10px">


        <input type="hidden" name="productId" value="${product.id}">


        <div class="review-stars">
            <label style="font-weight:600;">Bạn cảm thấy như thế nào về sản phẩm?</label>

            <div class="stars">
                <label class="star-item">
                    <input type="radio" name="rating" value="1">
                    <div class="star-icon">⭐</div>
                    <div class="star-text">Rất tệ</div>
                </label>

                <label class="star-item">
                    <input type="radio" name="rating" value="2">
                    <div class="star-icon">⭐</div>
                    <div class="star-text">Không tệ</div>
                </label>

                <label class="star-item">
                    <input type="radio" name="rating" value="3">
                    <div class="star-icon">⭐</div>
                    <div class="star-text">Trung bình</div>
                </label>

                <label class="star-item">
                    <input type="radio" name="rating" value="4">
                    <div class="star-icon">⭐</div>
                    <div class="star-text">Tốt</div>
                </label>

                <label class="star-item">
                    <input type="radio" name="rating" value="5">
                    <div class="star-icon">⭐</div>
                    <div class="star-text">Tuyệt vời</div>
                </label>
            </div>
        </div>


        <textarea name="comment"
                  placeholder="Mời bạn chia sẻ cảm nhận về sản phẩm..."
                  required
                  style="width:100%;height:100px;margin-top:15px;padding:10px;border:1px solid #ddd;border-radius:6px;">
    </textarea>


        <div style="margin-top:15px;">
            <label style="cursor:pointer;color:#2563eb;">
                📷 Chọn ảnh
                <input type="file" name="image" accept="image/*" hidden>
            </label>

            <div id="preview" style="margin-top:10px;"></div>
        </div>


        <button type="submit"
                style="margin-top:15px;width:100%;padding:10px;background:#f59e0b;color:white;border:none;border-radius:6px;">
            Gửi đánh giá
        </button>

    </form>
</div>

<script>
    const maxQuantity = ${product.inventoryQuantity};

    function changeMainImage(src) {
        document.getElementById('mainImage').src = src;

        document.querySelectorAll('.img_down img').forEach(img => {
            img.classList.remove('active');
        });
        event.target.classList.add('active');
    }

    function decreaseQty() {
        const input = document.getElementById('quantity');
        if (input.value > 1) {
            input.value = parseInt(input.value) - 1;
        }
    }

    function increaseQty() {
        const input = document.getElementById('quantity');
        if (input.value < maxQuantity) {
            input.value = parseInt(input.value) + 1;
        }
    }


</script>
<script src="${pageContext.request.contextPath}/views/JS/product_detail.js"></script>
<%--<script src="${pageContext.request.contextPath}/views/JS/index.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/views/JS/products.js"></script>--%>
</body>
</html>