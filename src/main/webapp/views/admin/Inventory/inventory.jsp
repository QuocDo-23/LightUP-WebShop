<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Quản Lý Kho</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
</head>

<body>

<div class="container">

    <jsp:include page="/views/layout/siderbar_admin.jsp"/>


    <div class="main-content">

        <h1>📦 Quản Lý Kho</h1>

        <div class="table-container">

            <h2>Danh sách tồn kho</h2>

            <table>

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Sản phẩm</th>
                    <th>Tồn kho</th>
                    <th>Min Stock</th>
                    <th>Nhập cuối</th>
                    <th>Bán cuối</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${products}" var="p">

                    <tr>

                        <td>${p.id}</td>

                        <td>${p.name}</td>

                        <td>

                            <c:choose>

                                <c:when test="${p.inventoryQuantity <= p.minStock}">
                                    <span style="color:red;font-weight:bold">
                                            ${p.inventoryQuantity}
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    ${p.inventoryQuantity}
                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>${p.minStock}</td>

                        <td>
                            <c:choose>
                                <c:when test="${not empty p.lastImportDate}">
                                    ${p.lastImportDate.toString().replace('T',' ')}
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${not empty p.lastSaleDate}">
                                    ${p.lastSaleDate.toString().replace('T',' ')}
                                </c:when>
                                <c:otherwise>-</c:otherwise>
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