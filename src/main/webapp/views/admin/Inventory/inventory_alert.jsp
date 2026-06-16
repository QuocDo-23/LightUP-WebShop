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

        <h1>⚠️ Cảnh Báo Kho</h1>

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

                    <tr>

                        <td>${status.count}</td>

                        <td>${p.name}</td>

                        <td>${p.inventoryQuantity}</td>

                        <td>${p.minStock}</td>

                        <td>

                            <c:choose>

                                <c:when test="${p.inventoryQuantity == 0}">
                                     Hết hàng
                                </c:when>

                                <c:when test="${p.inventoryQuantity <= p.minStock}">
                                     Sắp hết
                                </c:when>

                                <c:otherwise>
                                     Bình thường
                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>

                            <c:choose>

                                <c:when test="${p.inventoryQuantity == 0}">
                                    Nhập gấp
                                </c:when>

                                <c:when test="${p.inventoryQuantity <= p.minStock}">
                                    Nên nhập thêm
                                </c:when>

                                <c:otherwise>
                                    Không cần nhập
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