<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="sidebar">
    <div class="logo">
        <img src="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png" alt="logo">
        LightAdmin
    </div>

    <a href="${pageContext.request.contextPath}/admin/dashboard"
       class="menu-item <c:if test='${currentPage eq "dashboard"}'>active</c:if>">
        📊 Tổng Quan
    </a>

    <a href="${pageContext.request.contextPath}/admin/products"
       class="menu-item <c:if test='${currentPage eq "products"}'>active</c:if>">
        📦 Sản Phẩm
    </a>

    <a href="${pageContext.request.contextPath}/admin/orders"
       class="menu-item <c:if test='${currentPage eq "orders"}'>active</c:if>">
        🛒 Đơn Hàng
    </a>

    <a href="${pageContext.request.contextPath}/admin/customers"
       class="menu-item <c:if test='${currentPage eq "customers"}'>active</c:if>">
        👥 Khách Hàng
    </a>

    <a href="${pageContext.request.contextPath}/admin/news"
       class="menu-item <c:if test='${currentPage eq "news"}'>active</c:if>">
        📰 Tin Tức
    </a>

    <a href="${pageContext.request.contextPath}/admin/reviews"
       class="menu-item <c:if test='${currentPage eq "reviews"}'>active</c:if>">
        ⭐ Đánh Giá
    </a>

    <a href="${pageContext.request.contextPath}/admin/analytics"
       class="menu-item <c:if test='${currentPage eq "analytics"}'>active</c:if>">
        📈 Thống Kê
    </a>

    <button type="button" class="logout-btn"
            onclick="window.location.href='${pageContext.request.contextPath}/logout'">
        Đăng xuất
    </button>
</div>