<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Yêu cầu Trả hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/ad_return_list.css">


</head>
<body>
<div class="admin-container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="admin-content">
        <div class="page-header">
            <h1><i class="bi bi-arrow-counterclockwise"></i> Quản lý Yêu cầu Trả hàng</h1>
        </div>

        <!-- Hiển thị thông báo -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success">
                <i class="bi bi-check-circle"></i> ${sessionScope.successMessage}
                <c:remove var="successMessage" scope="session" />
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-circle"></i> ${sessionScope.errorMessage}
                <c:remove var="errorMessage" scope="session" />
            </div>
        </c:if>

        <!-- Thống kê -->
        <div class="stats-container">
            <div class="stat-card pending">
                <h3>${pendingCount}</h3>
                <p>Chờ duyệt</p>
            </div>
            <div class="stat-card approved">
                <h3>${approvedCount}</h3>
                <p>Được duyệt</p>
            </div>
            <div class="stat-card rejected">
                <h3>${rejectedCount}</h3>
                <p>Bị từ chối</p>
            </div>
            <div class="stat-card completed">
                <h3>${completedCount}</h3>
                <p>Hoàn thành</p>
            </div>
        </div>

        <!-- Filter -->
        <div class="filter-section">
            <form method="get" style="display: flex; gap: 10px; width: 100%;">
                <div class="filter-group">
                    <label for="statusFilter">Trạng thái:</label>
                    <select name="status" id="statusFilter">
                        <option value="">Tất cả</option>
                        <option value="pending" ${statusFilter == 'pending' ? 'selected' : ''}>Chờ duyệt</option>
                        <option value="approved" ${statusFilter == 'approved' ? 'selected' : ''}>Được duyệt</option>
                        <option value="rejected" ${statusFilter == 'rejected' ? 'selected' : ''}>Bị từ chối</option>
                        <option value="completed" ${statusFilter == 'completed' ? 'selected' : ''}>Hoàn thành</option>
                    </select>
                </div>
                <div class="filter-group">
                    <label for="searchInput">Tìm kiếm:</label>
                    <input type="text" name="search" id="searchInput" placeholder="Mã đơn/User ID" 
                           value="${searchKeyword}">
                </div>
                <button type="submit" class="btn-filter">
                    <i class="bi bi-search"></i> Tìm
                </button>
            </form>
        </div>

        <!-- Danh sách yêu cầu -->
        <div class="returns-table">
            <c:choose>
                <c:when test="${empty returns}">
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <p>Không có yêu cầu trả hàng</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Mã Đơn Hàng</th>
                                <th>User ID</th>
                                <th>Lý do</th>
                                <th>Trạng thái</th>
                                <th>Ngày yêu cầu</th>
                                <th>Số tiền</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="orderReturn" items="${returns}">
                                <tr>
                                    <td><strong>#${orderReturn.id}</strong></td>
                                    <td>#DH${String.format("%06d", orderReturn.orderId)}</td>
                                    <td>${orderReturn.userId}</td>
                                    <td>${orderReturn.getReasonDisplay()}</td>
                                    <td>
                                        <span class="status-badge status-${orderReturn.status}">
                                            ${orderReturn.getStatusDisplay()}
                                        </span>
                                    </td>
                                    <td>
                                        ${orderReturn.requestDate}
                                    </td>
                                    <td>
                                        <c:if test="${orderReturn.refundAmount > 0}">
                                            <fmt:formatNumber value="${orderReturn.refundAmount}" pattern="#,###"/>₫
                                        </c:if>
                                        <c:if test="${orderReturn.refundAmount == 0}">
                                            -
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="?action=view&id=${orderReturn.id}" class="btn-small btn-view">
                                                <i class="bi bi-eye"></i> Xem
                                            </a>
                                            <c:if test="${orderReturn.status == 'pending'}">
                                                <a href="?action=approve&id=${orderReturn.id}"
                                                   class="btn-small btn-approve"
                                                   onclick="return confirm('Duyệt yêu cầu này?')">
                                                    <i class="bi bi-check"></i> Duyệt
                                                </a>
                                            </c:if>
                                            <c:if test="${orderReturn.status == 'approved'}">
                                                <a href="?action=complete&id=${orderReturn.id}"
                                                   class="btn-small btn-complete"
                                                   onclick="return confirm('Hoàn thành trả hàng?')">
                                                    <i class="bi bi-check2-square"></i> Hoàn tiền
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.display = 'none';
        }, 5000);
    });
</script>
</body>
</html>
