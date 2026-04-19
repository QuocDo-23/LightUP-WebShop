<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/mini_cart.css">

<jsp:include page="/views/layout/cart-mini.jsp"/>

<nav>
    <div class="nav_head">
        <!-- LOGO -->
        <a href="${pageContext.request.contextPath}/">
            <div class="logo">
                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="Logo">
            </div>
        </a>

        <div class="nav-container">
            <ul class="nav-links">

                <!-- TRANG CHỦ -->
                <li>
                    <a href="${pageContext.request.contextPath}/"
                       class="${not pageContext.request.requestURI.contains('/products')
                            and not pageContext.request.requestURI.contains('news')
                            and not pageContext.request.requestURI.contains('/about')
                            and not pageContext.request.requestURI.contains('/contact')
                            and not pageContext.request.requestURI.contains('/login')
                            and not pageContext.request.requestURI.contains('/cart')
                            and not pageContext.request.requestURI.contains('product-detail')
                            and not pageContext.request.requestURI.contains('cate_products')
                            ? 'active' : ''}">
                        TRANG CHỦ
                    </a>
                </li>

                <!-- GIỚI THIỆU -->
                <li>
                    <a href="${pageContext.request.contextPath}/about"
                       class="${pageContext.request.requestURI.contains('/about') ? 'active' : ''}">
                        GIỚI THIỆU
                    </a>
                </li>

                <!-- SẢN PHẨM -->
                <li>
                    <a href="${pageContext.request.contextPath}/products"
                       class="${pageContext.request.requestURI.contains('/products')
                                    or pageContext.request.requestURI.contains('product-detail')
                                    or pageContext.request.requestURI.contains('cate_products')
                                    ? 'active' : ''}">
                        SẢN PHẨM <i class="bi bi-caret-down-fill"></i>
                    </a>

                    <div class="sub-item">
                        <div class="contant-sub">
                            <h2>Danh mục sản phẩm</h2>
                            <div class="cont-item">
                                <c:forEach var="category" items="${categories}">
                                    <a href="${pageContext.request.contextPath}/cate_products?id=${category.id}"
                                       class="product_item">
                                        <div class="product_cont">
                                            <img src="${category.imgCate}" alt="${category.name}">
                                            <div class="item">${category.name}</div>
                                        </div>
                                        <span class="arrow">›</span>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </li>

                <!-- TIN TỨC -->
                <li>
                    <a href="${pageContext.request.contextPath}/news"
                       class="${pageContext.request.requestURI.contains('/news') ? 'active' : ''}">
                        TIN TỨC
                    </a>
                </li>

                <!-- LIÊN HỆ -->
                <li>
                    <a href="${pageContext.request.contextPath}/contact"
                       class="${pageContext.request.requestURI.contains('/contact') ? 'active' : ''}">
                        LIÊN HỆ
                    </a>
                </li>
            </ul>

            <div class="nav_r" id="nav_r">
                <div class="search-icon">
                    <form action="#" method="get" id="searchForm" onsubmit="return false;">
                        <input type="text"
                               name="q"
                               placeholder="Tìm kiếm sản phẩm..."
                               id="searchInput"
                               value="${param.q}"
                               autocomplete="off"
                               oninput="handleSearchInput(this.value)">
                        <button type="button">
                            <i class="bi bi-search"></i>
                        </button>
                    </form>

                    <div id="searchSuggestions" class="search-suggestions"></div>
                </div>

                <div class="icon-group">
                    <div class="shop" id="shop">
                        <a href="${pageContext.request.contextPath}/cart">
                            <i class="bi bi-cart-check" title="Giỏ hàng"></i>
                            <div class="item-quantity" id="cartCount">
                                ${not empty sessionScope.cart ? sessionScope.cart.getTotalItems() : 0}
                            </div>
                        </a>
                    </div>

                    <div class="login" id="login">


                        <div class="login" id="login">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <a href="#" class="avatar-link">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.user.avatarImg
                                                and fn:startsWith(sessionScope.user.avatarImg, 'http')}">
                                                <img src="${sessionScope.user.avatarImg}"
                                                     alt="Avatar"
                                                     class="profile-pic"
                                                     id="previewAvatar">
                                            </c:when>
                                            <c:when test="${not empty sessionScope.user.avatarImg}">
                                                <img src="${pageContext.request.contextPath}/images/${sessionScope.user.avatarImg}"
                                                     alt="Avatar">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png"
                                                     alt="Avatar">
                                            </c:otherwise>
                                        </c:choose>
                                    </a>

                                    <div class="sub_login">
                                        <div class="container-sub-login">
                                            <a href="${pageContext.request.contextPath}/profile">
                                                <i class="bi bi-person"></i> Tài khoản
                                            </a>
                                            <a href="${pageContext.request.contextPath}/orders" class="order-link">
                                                <i class="bi bi-clipboard-check"></i> Đơn Hàng
                                                <c:if test="${not empty orderCount && orderCount > 0}">
                                                    <span class="badge-notification">${orderCount}</span>
                                                </c:if>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/logout">
                                                <i class="bi bi-box-arrow-right"></i> Đăng xuất
                                            </a>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/login">
                                        <i class="bi bi-person-circle" title="Đăng nhập"></i>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</nav>
<c:if test="${not empty successMsg}">
    <div class="toast">
            ${successMsg}
    </div>

    <c:remove var="successMsg" scope="session" />
</c:if>
<script>
    let searchTimeout;

    function handleSearchInput(value) {
        clearTimeout(searchTimeout);
        const suggestionsDiv = document.getElementById('searchSuggestions');

        if (value.length < 2) {
            suggestionsDiv.style.display = 'none';
            return;
        }

        searchTimeout = setTimeout(() => {
            fetch('search-suggestions?q=' + encodeURIComponent(value))
                .then(response => response.text())
                .then(html => {
                    suggestionsDiv.innerHTML = html;
                    suggestionsDiv.style.display = 'block';
                })
                .catch(error => {
                    console.error('Search error:', error);
                });
        }, 300);
    }

    document.addEventListener('click', function(e) {
        const searchIcon = document.querySelector('.search-icon');
        if (searchIcon && !searchIcon.contains(e.target)) {
            document.getElementById('searchSuggestions').style.display = 'none';
        }
    });
</script>
