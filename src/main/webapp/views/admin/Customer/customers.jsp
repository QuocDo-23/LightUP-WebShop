<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <title>Khách Hàng - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
</head>

<body>
    <div class="container">

        <%@ include file="/views/layout/siderbar_admin.jsp" %>

        <div class="main-content">
            <div class="header">
                <h1>Quản Lý Khách Hàng</h1>
                <div class="user-info">
                    <div class="avatar">
                        ${sessionScope.user.name.charAt(0)}
                    </div>
                    <div>
                        <div style="font-weight: 600;">${sessionScope.user.name}</div>
                        <div style="font-size: 12px; color: #718096;">
                            <c:choose>
                                <c:when test="${sessionScope.user.roleId == 1}">Quản trị viên</c:when>
                                <c:otherwise>Nhân viên</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <div class="table-container">
                <div class="table-header">
                    <h2>Danh Sách Khách Hàng</h2>
                </div>

                <div class="search-box">
                    <form>
                    <input type="text"
                           id="searchInput"
                           name="keyword"
                           placeholder="Tìm ID, tên KH hoặc email..."
                           value="${param.keyword}">
                    </form>
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Khách Hàng</th>
                            <th>Email</th>
                            <th>Số ĐT</th>
                            <th>Đơn Hàng</th>
                            <th>Tổng Chi</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty customers}">
                                <c:forEach items="${customers}" var="c">
                                    <tr>
                                        <td>#${c.id}</td>
                                        <td>
                                            <div class="product-info">
                                                <div>${c.name}</div>
                                            </div>
                                        </td>
                                        <td>${c.email}</td>
                                        <td>${c.phone}</td>
                                        <td>${c.orderCount != null ? c.orderCount : 0}</td>
                                        <td style="font-weight: 600; color: #319795;">
                                            <fmt:formatNumber value="${c.totalSpent != null ? c.totalSpent : 0}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/customer-detail?id=${c.id}" style="color: #3182ce; text-decoration: none; margin-right: 10px;">Chi tiết</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 20px;">Không có khách hàng nào.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/views/JS/actionButton.js"></script>
</body>

</html>