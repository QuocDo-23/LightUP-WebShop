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

        <h1 class="page-title">
            📜 Lịch Sử Kho
        </h1>
        <form method="get"
              class="filter-bar">

            <input
                    type="text"
                    name="keyword"
                    value="${keyword}"
                    placeholder="Tìm sản phẩm...">

            <select name="type">

                <option value="">
                    Tất cả giao dịch
                </option>

                <option value="IMPORT"
                ${type == 'IMPORT' ? 'selected' : ''}>
                    Nhập kho
                </option>

                <option value="SALE"
                ${type == 'SALE' ? 'selected' : ''}>
                    Xuất hàng
                </option>

            </select>

            <input
                    type="date"
                    name="fromDate"
                    value="${fromDate}">

            <input
                    type="date"
                    name="toDate"
                    value="${toDate}">

            <button type="submit">
                Lọc
            </button>

        </form>
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
                                           <span class="badge badge-import">
                                                Nhập kho
                                           </span>
                                </c:when>

                                <c:when test="${t.transactionType == 'SALE'}">
                                            <span class="badge badge-sale">
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
