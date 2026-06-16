<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Nhập Kho</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
</head>

<body>

<div class="container">

    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">

        <h1>📤 Xuất Kho</h1>

        <div class="table-container">

            <h2>Phiếu xuất kho</h2>

            <form method="post" class="inventory-form"
                  action="${pageContext.request.contextPath}/admin/inventory/export">

                <div style="margin-bottom:20px">

                    <label>Sản phẩm</label>

                    <select name="productId"
                            style="width:500px;padding:10px;display:block">

                        <c:forEach items="${products}" var="p">

                            <option value="${p.id}">
                                    ${p.name}
                            </option>

                        </c:forEach>

                    </select>

                </div>

                <div style="margin-bottom:20px">

                    <label>Số lượng nhập</label>

                    <input type="number"
                           name="quantity"
                           min="1"
                           required
                           style="width:500px;padding:10px;display:block">

                </div>

                <div style="margin-bottom:20px">

                    <label>Lý do</label>

                    <textarea
                            name="reason"
                            rows="4"
                            style="width:500px;padding:10px;display:block">
            </textarea>

                </div>

                <button type="submit">
                    Xuất kho
                </button>

            </form>

        </div>

    </div>

</div>

</body>
</html>