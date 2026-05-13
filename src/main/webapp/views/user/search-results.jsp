<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm kiếm - LightUp</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/search.css">
</head>
<body>
<jsp:include page="/views/layout/header.jsp"/>

<main class="search-container">
    <div class="search-header">
        <h1>
            <i class="bi bi-search"></i> Kết quả tìm kiếm
        </h1>
        <div class="search-info">
            Từ khóa: <strong>"${searchQuery}"</strong>
            <span class="result-count">(<span id="resultCount">${resultCount}</span> kết quả)</span>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty searchResults}">
            <div class="no-results">
                <div class="no-results-icon">
                    <i class="bi bi-inbox"></i>
                </div>
                <div class="no-results-text">
                    Không tìm thấy sản phẩm nào
                </div>
                <div class="no-results-suggestion">
                    Vui lòng thử lại với từ khóa khác
                </div>
                <a href="${pageContext.request.contextPath}/products" class="back-link">
                    <i class="bi bi-arrow-left"></i>Quay lại trang sản phẩm
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-grid">
                <c:forEach var="product" items="${searchResults}">
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
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/views/layout/footer.jsp"/>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        console.log('Search results page loaded');
    });
</script>
</body>
</html>
