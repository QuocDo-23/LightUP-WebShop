<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${not empty category}">
                ${category.name} - LightUp
            </c:when>
            <c:otherwise>
                Sản Phẩm - LightUp
            </c:otherwise>
        </c:choose>
    </title>


    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/cart.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

</head>
<body>
<main>

    <jsp:include page="/views/layout/header.jsp"/>


    <div class="link-page">
        <div class="containt_link">
            <a href="./index.jsp"><i class="bi bi-house"></i> Trang chủ </a>
            <span> /</span>
            <a href="products">Sản Phẩm</a>
            <c:if test="${not empty category}">
                <span> /</span>
                <a href="">${category.name}</a>
            </c:if>
        </div>
    </div>


    <c:if test="${not empty category}">
        <div class="container">
            <div style="text-align: center; padding: 30px 0;">
                <h1 style="font-size: 2rem; font-weight: 600; color: #333; margin-bottom: 10px;">
                        ${category.name}
                </h1>
            </div>
        </div>
    </c:if>


    <form action="products" method="get">


        <div class="filter-sort-bar">


            <div class="filter-container">


                <div class="filter-dropdown">
                    <button type="submit" class="filter-toggle"
                            style="background:none;border:none;">
                        <i class="bi bi-funnel-fill"></i>
                        <span>Bộ lọc</span>
                    </button>
                </div>



                <div class="filter-dropdown">
                    <div class="filter-toggle">
                        <span>Giá</span>
                        <i class="bi bi-chevron-down"></i>
                    </div>
                    <ul class="filter-list">
                        <li>
                            <label>
                                <input type="checkbox" class="price-filter"
                                       name="price" value="0-1000000"
                                       data-min="0" data-max="1000000">
                                Dưới 1.000.000₫
                            </label>
                        </li>
                        <li>
                            <label>
                                <input type="checkbox" class="price-filter"
                                       name="price" value="1000000-5000000"
                                       data-min="1000000" data-max="5000000">
                                1.000.000₫ – 5.000.000₫
                            </label>
                        </li>
                        <li>
                            <label>
                                <input type="checkbox" class="price-filter"
                                       name="price" value="5000000-7500000"
                                       data-min="5000000" data-max="7500000">

                                5.000.000₫ – 7.500.000₫
                            </label>
                        </li>
                        <li>
                            <label>
                                <input type="checkbox" class="price-filter"
                                       name="price" value="7500000-10000000"
                                       data-min="7500000" data-max="10000000">
                                7.500.000₫ – 10.000.000₫
                            </label>
                        </li>
                        <li>
                            <label>
                                <input type="checkbox" class="price-filter"
                                       name="price" value="10000000-999999999"
                                       data-min="10000000" data-max="999999999">
                                Trên 10.000.000₫
                            </label>
                        </li>
                    </ul>
                </div>
            </div>


            <div class="sort-box">
                <div class="sort-toggle">
                    <span>Sắp xếp</span>
                    <i class="bi bi-chevron-down"></i>
                </div>
                <ul class="sort-list">
                    <li data-sort="popular">Bán chạy</li>
                    <li data-sort="price-asc">Giá thấp → cao</li>
                    <li data-sort="price-desc">Giá cao → thấp</li>
                </ul>
            </div>

        </div>
    </form>


    <div class="container product-section">

        <c:choose>

            <c:when test="${not empty category or not empty products}">
                <div class="product-grid" id="productGrid">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card"
                             data-price="${product.discountedPrice}"
                             data-rating="${product.review}"
                             data-category="${product.categoryId}">
                            <div class="product-image">
                                <c:if test="${product.hasDiscount()}">
                                    <div class="product-sale">-
                                        <fmt:formatNumber value="${product.discountRate}" maxFractionDigits="0"/>%
                                    </div>
                                </c:if>
                                <a href="product-detail?id=${product.id}">
                                    <img src="${not empty product.mainImage ? product.mainImage : 'default.jpg'}"
                                         alt="${product.name}" class="img-main">
                                    <c:if test="${not empty product.hoverImage}">
                                        <img src="${product.hoverImage}" alt="${product.name}" class="img-hover">
                                    </c:if>
                                </a>
                            </div>

                            <div class="product-info">
                                <h3 class="product-name">
                                    <a href="product-detail?id=${product.id}">${product.name}</a>
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
            </c:when>


            <c:otherwise>
                <c:forEach var="cat" items="${categories}">
                    <c:set var="products" value="${productsByCategory[cat.id]}"/>
                    <c:if test="${not empty products}">
                        <div class="product-section">
                            <div class="category-header">
                                <h3 class="sub-title">${cat.name}</h3>
                                <a class="view-more-text" href="cate_products?id=${cat.id}">
                                    Xem thêm →
                                </a>
                            </div>

                            <div class="product-grid" id="productGrid">
                                <c:forEach var="product" items="${products}">
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
                                                     alt="${product.name}" class="img-main">
                                                <c:if test="${not empty product.hoverImage}">
                                                    <img src="${product.hoverImage}"
                                                         alt="${product.name}" class="img-hover">
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
                    </c:if>
                </c:forEach>
            </c:otherwise>
        </c:choose>



        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="cate_products?id=${category.id}&page=${currentPage - 1}" class="prev-btn">← Trước</a>
                </c:if>
                <c:if test="${currentPage == 1}">
                    <a href="#" class="prev-btn disabled">← Trước</a>
                </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="cate_products?id=${category.id}&page=${i}"
                       class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="cate_products?id=${category.id}&page=${currentPage + 1}" class="next-btn">Sau →</a>
                </c:if>
                <c:if test="${currentPage == totalPages}">
                    <a href="#" class="next-btn disabled">Sau →</a>
                </c:if>
            </div>
        </c:if>
    </div>


    <jsp:include page="/views/layout/header.jsp"/>


    <a href="#">
        <button id="scrollToTopBtn">
            <i class="bi bi-chevron-up"></i>
        </button>
    </a>



</main>


<script src="${pageContext.request.contextPath}/JS/products.js"></script>

</body>
</html>