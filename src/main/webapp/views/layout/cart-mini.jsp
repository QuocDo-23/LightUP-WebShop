<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="cart-overlay"></div>

<div id="khunggiohang" class="cart-widget-side">
    <div class="wd-heading">
        <span class="title">Giỏ hàng</span>
        <button type="button" id="btn-dong-gio" class="close-cart">× Đóng</button>
    </div>
    <div id="mini-cart-body" class="shopping-cart-widget-body wd-scroll">
    </div>
</div>
<script>
    window.CTX = "${pageContext.request.contextPath}";
</script>

<script src="${pageContext.request.contextPath}/views/JS/mini_cart.js"></script>
