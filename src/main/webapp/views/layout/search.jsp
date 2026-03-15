<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${empty searchResults}">
        <div class="no-results">
            <i class="bi bi-search"></i>
            <p>Không tìm thấy sản phẩm</p>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="product" items="${searchResults}">
            <a href="product-detail?id=${product.id}" class="suggestion-item">
                <img src="${not empty product.mainImage ? product.mainImage : 'images/default-product.jpg'}"
                     alt="${product.name}">
                <div class="suggestion-info">
                    <div class="suggestion-name">${product.name}</div>
                    <div class="suggestion-price">
                        <fmt:formatNumber value="${product.discountedPrice}" pattern="#,###"/>₫
                        <c:if test="${product.hasDiscount()}">
                            <del><fmt:formatNumber value="${product.price}" pattern="#,###"/>₫</del>
                        </c:if>
                    </div>
                </div>
            </a>
        </c:forEach>
    </c:otherwise>
</c:choose>