<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Lịch Sử Kho</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
</head>

<body>

<div class="container">

    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">

        <h1>📜 Lịch Sử Kho</h1>
        <div class="table-container" style="margin-top:30px;">

            <h2>Lịch sử nhập xuất kho</h2>

            <table>

                <thead>
                <tr>
                    <th>STT</th>
                    <th>Sản phẩm</th>
                    <th>Loại giao dịch</th>
                    <th>Số lượng</th>
                    <th>Lý do</th>
                    <th>Ngày giờ</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${transactions}" var="t" varStatus="status">

                    <tr>

                        <td>${status.count}</td>

                        <td>${t.productName}</td>

                        <td>

                            <c:choose>

                                <c:when test="${t.transactionType == 'IMPORT'}">
                                           <span style="
                                    background:#dcfce7;
                                    color:#166534;
                                    padding:4px 10px;
                                    border-radius:20px;
                                    font-weight:bold;">
                                        Nhập kho
                                    </span>
                                </c:when>

                                <c:when test="${t.transactionType == 'SALE'}">
                                            <span style="
                                    background:#fee2e2;
                                    color:#991b1b;
                                    padding:4px 10px;
                                    border-radius:20px;
                                    font-weight:bold;">
                                                Xuất hàng
                                    </span>
                                </c:when>


                                <c:otherwise>
                                    ${t.transactionType}
                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>

                            <c:choose>

                                <c:when test="${t.quantity > 0}">
            <span style="color:green;font-weight:bold">
                +${t.quantity}
            </span>
                                </c:when>

                                <c:otherwise>
            <span style="color:red;font-weight:bold">
                    ${t.quantity}
            </span>
                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>${t.reason}</td>

                        <td>
                                ${t.createdAt.toString().replace('T',' ')}
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
