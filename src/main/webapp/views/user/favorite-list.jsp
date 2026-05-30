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

    <div class="product-section">

        <div class="category-header">

            <h2 class="sub-title">
                Sản phẩm yêu thích
            </h2>

        </div>

        <c:choose>

            <c:when test="${empty favoriteProducts}">

                <div class="empty-favorite">

                    <i class="bi bi-heart"></i>

                    <h2>
                        Bạn chưa có sản phẩm yêu thích
                    </h2>

                    <p>
                        Hãy khám phá thêm các sản phẩm của LightUp
                    </p>

                    <a href="${pageContext.request.contextPath}/products"
                       class="go-shopping-btn">

                        Xem sản phẩm

                    </a>

                </div>

            </c:when>

            <c:otherwise>

        <div class="product-grid">

            <c:forEach var="product" items="${favoriteProducts}">



        <div class="product-card"
             data-product-id="${product.id}">

            <div class="product-image">

                <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}">


                      <img src="${not empty product.mainImage
                      ? product.mainImage
                       : 'images/default-product.jpg'}"

                          alt="${product.name}"

                          class="img-main">

                </a>


                <form action="${pageContext.request.contextPath}/favorite"
                      method="post"
                      class="favorite-form">

                    <input type="hidden"
                           name="productId"
                           value="${product.id}">

                    <button type="button"
                            class="favorite-btn active"
                            data-remove="true">

                        <i class="bi bi-heart-fill"></i>

                    </button>

                </form>

            </div>

            <div class="product-info">

                <h3 class="product-name">

                    <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}">

                            ${product.name}

                    </a>

                </h3>

                <div class="product-action">

                    <div class="product-prices">

                <span class="current-price">

                    <fmt:formatNumber
                            value="${product.price}"
                            pattern="#,###"/>₫

                </span>

                    </div>

                    <div class="cart-icon">

                        <a class="open-cart"
                           href="${pageContext.request.contextPath}/add-cart?pID=${product.id}&quantity=1">

                            <i class="bi bi-cart-check"></i>

                        </a>

                    </div>

                </div>

            </div>


        </div>

            </c:forEach>

        </div>

            </c:otherwise>

        </c:choose>

        </div>
    </div>




<script>

    document.querySelectorAll("[data-remove='true']")
        .forEach(button => {

            button.addEventListener("click", async function (e) {

                e.preventDefault();

                const productCard =
                    this.closest(".product-card");

                const productId =
                    productCard.dataset.productId;

                try {

                    const response = await fetch(
                        "${pageContext.request.contextPath}/favorite",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/x-www-form-urlencoded"
                            },

                            body:
                                "productId=" + productId
                        }
                    );

                    if (response.ok) {

                        productCard.remove();

                        const countElement =
                            document.querySelector(".favorite-count");

                        let currentCount =
                            parseInt(countElement.innerText);

                        countElement.innerText =
                            Math.max(0, currentCount - 1);

                    }

                } catch (error) {

                    console.log(error);

                }

            });

        });

</script>
        <jsp:include page="/views/layout/footer.jsp"/>
</body>
</html>