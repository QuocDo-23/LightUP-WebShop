<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">

    <title>Sản phẩm yêu thích</title>
    <link rel="icon" type="image/png" href="https://i.postimg.cc/26JnYsPT/Logo-Photroom.png">

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/favorite-list.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/mini_cart.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/sub_login.css">

</head>

<body>

<jsp:include page="/views/layout/header.jsp"/>

<div class="favorite-page">

    <div class="favorite-header">

        <h1>SẢN PHẨM YÊU THÍCH</h1>

        <p>
            Bạn đã lưu
            ${favoriteProducts.size()}
            sản phẩm
        </p>

    </div>

    <div class="favorite-grid">

    <c:forEach var="product" items="${favoriteProducts}">

        <div class="product-card">

            <div class="product-image">

                <img src="${product.mainImage}"
                     alt="${product.name}">

            </div>

            <div class="product-info">

                <h3 class="product-name">
                        ${product.name}
                </h3>

                <p class="product-price">
                    <fmt:formatNumber
                            value="${product.price}"
                            pattern="#,###"/>₫
                </p>

            </div>

        </div>

    </c:forEach>

    </div>

</div>

<jsp:include page="/views/layout/footer.jsp"/>

</body>
</html>