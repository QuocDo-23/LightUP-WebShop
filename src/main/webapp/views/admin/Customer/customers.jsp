<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">


<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <title>Khách Hàng - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin_cus.css">
    <fmt:setLocale value="vi_VN"/>


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

            <form method="get" action="${pageContext.request.contextPath}/admin/customers" id="searchForm">
                <div class="toolbar">

                    <div class="search-wrapper">
                        <i class="fas fa-search search-icon"></i>
                        <input type="text"
                               id="searchInput"
                               name="keyword"
                               placeholder="Tìm ID, tên KH hoặc email..."
                               value="${param.keyword}"
                               autocomplete="off">
                        <button type="button" class="clear-btn" onclick="clearSearch()" title="Xóa">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>

                    <select name="status" class="filter-select" onchange="this.form.submit()">
                        <option value="">-- Trạng thái --</option>
                        <option value="active"   <c:if test="${param.status == 'active'}">selected</c:if>>Hoạt động</option>
                        <option value="locked"   <c:if test="${param.status == 'locked'}">selected</c:if>>Đã khóa</option>
                    </select>

                    <button type="submit" class="btn-search">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>

                    <a href="${pageContext.request.contextPath}/admin/customers" class="btn-reset">
                        <i class="fas fa-rotate-left"></i> Đặt lại
                    </a>

                </div>
            </form>

            <p class="result-count">
                Tìm thấy <strong>${customers.size()}</strong> khách hàng
                <c:if test="${not empty param.keyword}">
                    cho từ khóa "<strong>${param.keyword}</strong>"
                </c:if>
                <c:if test="${not empty param.status}">
                    — trạng thái:
                    <strong>
                        <c:choose>
                            <c:when test="${param.status == 'active'}">Hoạt động</c:when>
                            <c:when test="${param.status == 'locked'}">Đã khóa</c:when>
                            <c:otherwise>Ngừng HĐ</c:otherwise>
                        </c:choose>
                    </strong>
                </c:if>
            </p>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Khách Hàng</th>
                    <th>Email</th>
                    <th>Số ĐT</th>
                    <th>Trạng Thái</th>
                    <th>Đơn Hàng</th>
                    <th>Tổng Chi</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty customers}">
                        <c:forEach items="${customers}" var="c">
                            <c:set var="showRow" value="${empty param.status or c.status == param.status}" />
                            <c:if test="${showRow}">
                                <tr>
                                    <td>#${c.id}</td>
                                    <td>
                                        <div class="product-info">
                                            <div>${c.name}</div>
                                        </div>
                                    </td>
                                    <td>${c.email}</td>
                                    <td>${not empty c.phone ? c.phone : '—'}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.status == 'active'}">
                                                <span class="badge-active"><i class="fas fa-circle-check" style="font-size:11px;"></i> Hoạt động</span>
                                            </c:when>
                                            <c:when test="${c.status == 'locked'}">
                                                <span class="badge-locked"><i class="fas fa-lock" style="font-size:11px;"></i> Đã khóa</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-warning">${c.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${c.orderCount != null ? c.orderCount : 0}</td>
                                    <td style="font-weight: 600; color: #319795;">
                                        <fmt:formatNumber
                                                value="${c.totalSpent != null ? c.totalSpent : 0}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0"/>₫
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/customer-detail?id=${c.id}"
                                           style="color: #3182ce; text-decoration: none; margin-right: 10px;">
                                            <i class="fas fa-eye"></i> Chi tiết
                                        </a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" style="text-align: center; padding: 30px; color: #718096;">
                                <i class="fas fa-users-slash" style="font-size: 24px; display: block; margin-bottom: 8px;"></i>
                                Không tìm thấy khách hàng nào.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/views/JS/search_customer.js"></script>

</body>

</html>
