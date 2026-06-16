<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

    <title>Cảnh Báo Kho</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">

</head>

<body>

<div class="container">

    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">


        <h1 class="page-title">
            ⚠️ Cảnh Báo Kho
        </h1>
        <div class="alert-banner">

            ⚠️ Những sản phẩm dưới mức tồn kho tối thiểu cần được nhập thêm để tránh hết hàng.

        </div>

        <div class="table-container">

            <table>

                <thead>
                <tr>
                    <th>STT</th>
                    <th>Sản phẩm</th>
                    <th>Tồn kho</th>
                    <th>Min Stock</th>
                    <th>Trạng thái</th>
                    <th>Khuyến nghị</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${products}" var="p" varStatus="status">

                    <c:choose>

                        <c:when test="${p.inventoryQuantity == 0}">

                            <tr class="stock-row-danger">

                        </c:when>

                        <c:when test="${p.inventoryQuantity <= p.minStock}">

                            <tr class="stock-row-warning">

                        </c:when>

                        <c:otherwise>

                            <tr>

                        </c:otherwise>

                    </c:choose>

                        <td>${status.count}</td>

                        <td>${p.name}</td>

                        <td>

                            <c:choose>

                                <c:when test="${p.inventoryQuantity == 0}">

            <span class="stock-danger">

                    ${p.inventoryQuantity}

            </span>

                                </c:when>

                                <c:otherwise>

            <span class="stock-warning">

                    ${p.inventoryQuantity}

            </span>

                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>${p.minStock}</td>

                        <td>

                            <c:choose>

                                <c:when test="${p.inventoryQuantity == 0}">

                                    <span class="alert-badge alert-danger">
                                        HẾT HÀNG
                                    </span>

                                </c:when>

                                <c:when test="${p.inventoryQuantity <= p.minStock}">

                                    <span class="alert-badge alert-warning">
                                        SẮP HẾT
                                    </span>

                                </c:when>

                                <c:otherwise>

                                    <span class="alert-badge alert-normal">
                                        BÌNH THƯỜNG
                                    </span>

                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>

                            <c:choose>

                                <c:when test="${p.inventoryQuantity == 0}">

                                    <span class="recommend-danger">
                                        Nhập gấp
                                        (${p.minStock} sản phẩm)
                                    </span>

                                </c:when>

                                <c:when test="${p.inventoryQuantity <= p.minStock}">

                                    <span class="recommend-warning">
                                        Nhập thêm
                                        ${p.minStock - p.inventoryQuantity}
                                        sản phẩm
                                    </span>

                                </c:when>

                                <c:otherwise>

                                    <span class="recommend-ok">
                                        Đủ hàng
                                    </span>

                                </c:otherwise>

                            </c:choose>

                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>

        </div>

    </div>

</div>

</body>
</html>