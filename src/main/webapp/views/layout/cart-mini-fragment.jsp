<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<c:choose>
    <c:when test="${empty cart || empty cart.listItem}">
        <p class="mini-cart-empty">Giỏ hàng của bạn đang trống</p>
    </c:when>
    <c:otherwise>
        <ul class="woocommerce-mini-cart">
            <c:forEach var="item" items="${cart.listItem}">
                <li class="woocommerce-mini-cart-item">

                    <div class="cart-item-image">
                        <img src="${item.product.mainImage}" alt="${item.product.name}">
                    </div>

                    <div class="cart-info">
                        <div class="cart-info-top">
                            <span class="wd-entities-title">${item.product.name}</span>
                            <form action="${pageContext.request.contextPath}/remove-mini" method="post"
                                  class="form-remove-mini">
                                <input type="hidden" name="productId" value="${item.product.id}"/>
                                <button type="submit" class="remove-mini-cart">×</button>
                            </form>
                        </div>

                        <form action="${pageContext.request.contextPath}/update-mini" method="post"
                              class="quantity form-update-mini">
                            <input type="hidden" name="productId" value="${item.product.id}"/>
                            <input type="hidden" name="qty" class="qty-value" value="${item.quantity}"/>
                            <button type="button" class="btn-qty-minus" data-qty="${item.quantity - 1}">−</button>
                            <span class="qty">${item.quantity}</span>
                            <button type="button" class="btn-qty-plus"  data-qty="${item.quantity + 1}">+</button>
                            <span class="woocommerce-Price-amount amount">
                                <fmt:formatNumber
                                        value="${item.product.discountedPrice * item.quantity}"
                                        pattern="#,###"/>₫
                            </span>
                        </form>
                    </div>

                </li>
            </c:forEach>
        </ul>

        <div class="mini-cart-footer">
            <div class="woocommerce-mini-cart__total">
                <span>Tổng tiền:</span>
                <strong>
                    <fmt:formatNumber value="${cart.totalPrice}" pattern="#,###"/>₫
                </strong>
            </div>
            <div class="woocommerce-mini-cart__buttons">
                <a href="${pageContext.request.contextPath}/cart"     class="btn-cart">Xem giỏ hàng</a>
                <a href="${pageContext.request.contextPath}/payment"  class="checkout">Thanh toán</a>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<span id="mini-cart-meta"
      data-total-items="${not empty cart ? cart.totalItems : 0}"
      style="display:none"></span>
