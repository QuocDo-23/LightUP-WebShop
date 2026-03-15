<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <title>Dashboard - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">

</head>

<body>
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">
        <div class="header">
            <h1>Tổng Quan</h1>
            <div class="user-info">
                <div class="avatar">A</div>
                <div>
                    <div style="font-weight: 600;">${sessionScope.user.name}</div>
                    <div style="font-size: 12px; color: #718096;">Quản trị viên</div>
                </div>
            </div>
        </div>


        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-header">
                    <div class="stat-icon" style="background: #e6fffa; color: #319795;">💰</div>
                </div>
                <div class="stat-value">
                    <fmt:formatNumber value="${stats.monthRevenue}" type="number" groupingUsed="true" />đ
                </div>
                <div class="stat-label">Doanh Thu Tháng</div>
            </div>
            <div class="stat-card">
                <div class="stat-header">
                    <div class="stat-icon" style="background: #fef5e7; color: #d69e2e;">🧾</div>
                </div>
                <div class="stat-value">${stats.monthOrders}</div>
                <div class="stat-label">Tổng đơn tháng</div>
            </div>
            <div class="stat-card">
                <div class="stat-header">
                    <div class="stat-icon" style="background: #e9d5ff; color: #7c3aed;">⏳</div>
                </div>
                <div class="stat-value">${stats.pendingOrders}</div>
                <div class="stat-label">Đơn đang xử lý</div>
            </div>
            <div class="stat-card">
                <div class="stat-header">
                    <div class="stat-icon" style="background: #dbeafe; color: #3b82f6;">👥</div>
                </div>
                <div class="stat-value">${stats.totalCustomers}</div>
                <div class="stat-label">Khách Hàng</div>
            </div>
        </div>

        <div class="table-container">
            <div class="table-header">
                <h2>Sản Phẩm Bán Chạy 🔥</h2>
            </div>
            <table>
                <thead>
                <tr>
                    <th>TOP</th>
                    <th>Hình Ảnh</th>
                    <th>Tên Sản Phẩm</th>
                    <th>Danh mục</th>
                    <th>Đã Bán</th>
                    <th>Doanh Thu</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${stats.topProducts}" varStatus="status">
                <tr>
                    <td class="index index-${status.index + 1}">
                            ${status.index + 1}
                    </td>

                    <td>
                        <div class="product-img">
                            <img src="${product.img}"
                                 alt="${product.productName}">
                        </div>
                    </td>
                    <td>
                        <div class="products-name">${product.productName}</div>
                    </td>
                    <td>${product.category}</td>
                    <td>${product.totalSold}</td>
                    <td class="cost-products">
                        <fmt:formatNumber value="${product.revenue}" type="number" groupingUsed="true" />đ
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