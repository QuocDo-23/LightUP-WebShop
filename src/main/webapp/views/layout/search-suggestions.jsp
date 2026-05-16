<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty searchResults}">
    <div class="suggestions-list">
        <c:forEach var="product" items="${searchResults}">
            <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"
               class="suggestion-item">
                <img src="${not empty product.mainImage ? product.mainImage : '/images/default.jpg'}"
                     alt="${product.name}">
                <div class="suggestion-info">
                    <div class="suggestion-name">${product.name}</div>
                    <div class="suggestion-price">
                        <fmt:formatNumber value="${product.discountedPrice}" pattern="#,###"/>₫
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
</c:if>
<c:if test="${empty searchResults && not empty param.q}">
    <div class="no-suggestion">Không tìm thấy sản phẩm phù hợp</div>
</c:if>