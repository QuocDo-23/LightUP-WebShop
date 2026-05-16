<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/siderbargroup.css">


<c:set var="isProductGroup"
       value="${currentPage eq 'products' or currentPage eq 'categories'}" />

<div class="sidebar">
    <div class="logo">
        <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="logo">
        LightAdmin
    </div>

    <a href="${pageContext.request.contextPath}/admin/dashboard"
       class="menu-item <c:if test='${currentPage eq "dashboard"}'>active</c:if>">
        Tổng Quan
    </a>

    <div class="menu-group <c:if test='${isProductGroup}'>open</c:if>">
        <a href="${pageContext.request.contextPath}/admin/products"
           class="menu-item menu-toggle <c:if test='${isProductGroup}'>active</c:if>">
            <span>Sản Phẩm</span><i class="menu-arrow bi bi-chevron-down"></i>
        </a>
        <div class="submenu">

            <a href="${pageContext.request.contextPath}/admin/products"
               class="submenu-item <c:if test='${currentPage eq "products"}'>active</c:if>">
                <i class="bi bi-box-seam"></i>Sản Phẩm
            </a>
            <a href="${pageContext.request.contextPath}/admin/categories"
               class="submenu-item <c:if test='${currentPage eq "categories"}'>active</c:if>">

                <i class="bi bi-tags"></i>
                Danh Mục

            </a>

        </div>

    </div>

    <a href="${pageContext.request.contextPath}/admin/orders"
       class="menu-item <c:if test='${currentPage eq "orders"}'>active</c:if>">
        Đơn Hàng
    </a>

    <a href="${pageContext.request.contextPath}/admin/customers"
       class="menu-item <c:if test='${currentPage eq "customers"}'>active</c:if>">
        Khách Hàng
    </a>

    <a href="${pageContext.request.contextPath}/admin/news"
       class="menu-item <c:if test='${currentPage eq "news"}'>active</c:if>">
        Tin Tức
    </a>

    <a href="${pageContext.request.contextPath}/admin/reviews"
       class="menu-item <c:if test='${currentPage eq "reviews"}'>active</c:if>">
        Đánh Giá
    </a>

    <a href="${pageContext.request.contextPath}/admin/analytics"
       class="menu-item <c:if test='${currentPage eq "analytics"}'>active</c:if>">
        Thống Kê
    </a>

    <button type="button" class="logout-btn"
            onclick="window.location.href='${pageContext.request.contextPath}/logout'">
        Đăng xuất
    </button>
</div>



<script>
    function toggleMenu(element) {

        const menuGroup = element.closest(".menu-group");

        menuGroup.classList.toggle("open");

        element.classList.toggle("active");

    }
</script>
