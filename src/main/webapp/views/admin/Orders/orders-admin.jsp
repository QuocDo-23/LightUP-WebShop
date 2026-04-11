<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" sizes="32x32" href="https://i.postimg.cc/26JnYsPT/Logo-Photoroom.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <title>Đơn Hàng - Quản Lý Đèn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/order-admin.css">


</head>

<body data-context-path="${pageContext.request.contextPath}">
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>

    <div class="main-content">
        <div class="header">
            <h1>Quản Lý Đơn Hàng</h1>
            <div class="user-info">
                <div class="avatar">Q</div>
                <div>
                    <div style="font-weight: 600;">Admin</div>
                    <div style="font-size: 12px; color: #718096;">Quản trị viên</div>
                </div>
            </div>
        </div>

        <!-- Thông báo -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${successMessage}
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
            </div>
        </c:if>

        <div class="stats-bar">
            <div class="stat-card ${status == 'all' ? 'active' : ''}" onclick="filterByStatus('all')">
                <div class="stat-label">Tất cả</div>
                <div class="stat-value">${totalRecords}</div>
            </div>
            <div class="stat-card ${status == 'pending' ? 'active' : ''}" onclick="filterByStatus('pending')">
                <div class="stat-label">Chờ xác nhận</div>
                <div class="stat-value">${statusStats['pending']}</div>
            </div>
            <div class="stat-card ${status == 'processing' ? 'active' : ''}" onclick="filterByStatus('processing')">
                <div class="stat-label">Đang xử lý</div>
                <div class="stat-value">${statusStats['processing']}</div>
            </div>
            <div class="stat-card ${status == 'shipped' ? 'active' : ''}" onclick="filterByStatus('shipped')">
                <div class="stat-label">Đang giao</div>
                <div class="stat-value">${statusStats['shipped']}</div>
            </div>
            <div class="stat-card ${status == 'delivered' ? 'active' : ''}" onclick="filterByStatus('delivered')">
                <div class="stat-label">Đã giao</div>
                <div class="stat-value">${statusStats['delivered']}</div>
            </div>
            <div class="stat-card ${status == 'cancelled' ? 'active' : ''}" onclick="filterByStatus('cancelled')">
                <div class="stat-label">Đã hủy</div>
                <div class="stat-value">${statusStats['cancelled']}</div>
            </div>
        </div>

        <div class="filter-section">
            <form method="GET" action="${pageContext.request.contextPath}/admin/orders" id="filterForm">
                <div class="filter-group">
                    <div class="search-box">
                        <i class="fas fa-search"></i>
                        <input type="text" name="search" placeholder="Tìm theo tên, email, mã đơn..."
                               value="${searchKeyword}">
                    </div>

                    <div class="filter-item">
                        <label>Trạng thái</label>
                        <select name="status" onchange="this.form.submit()">
                            <option value="all" ${status == 'all' ? 'selected' : ''}>Tất cả</option>
                            <option value="pending" ${status == 'pending' ? 'selected' : ''}>Chờ xác nhận</option>
                            <option value="processing" ${status == 'processing' ? 'selected' : ''}>Đang xử lý</option>
                            <option value="shipped" ${status == 'shipped' ? 'selected' : ''}>Đang giao</option>
                            <option value="delivered" ${status == 'delivered' ? 'selected' : ''}>Đã giao</option>
                            <option value="cancelled" ${status == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Sắp xếp</label>
                        <select name="sortBy" onchange="this.form.submit()">
                            <option value="order_date" ${sortBy == 'order_date' ? 'selected' : ''}>Ngày đặt</option>
                            <option value="total" ${sortBy == 'total' ? 'selected' : ''}>Tổng tiền</option>
                            <option value="status" ${sortBy == 'status' ? 'selected' : ''}>Trạng thái</option>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Thứ tự</label>
                        <select name="sortOrder" onchange="this.form.submit()">
                            <option value="DESC" ${sortOrder == 'DESC' ? 'selected' : ''}>Giảm dần</option>
                            <option value="ASC" ${sortOrder == 'ASC' ? 'selected' : ''}>Tăng dần</option>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>Hiển thị</label>
                        <select name="pageSize" onchange="this.form.submit()">
                            <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                            <option value="25" ${pageSize == 25 ? 'selected' : ''}>25</option>
                            <option value="50" ${pageSize == 50 ? 'selected' : ''}>50</option>
                            <option value="100" ${pageSize == 100 ? 'selected' : ''}>100</option>
                        </select>
                    </div>

                    <div class="filter-item">
                        <label>&nbsp;</label>
                        <button type="submit" class="btn btn-sm btn-update">
                            <i class="fas fa-filter"></i> Lọc
                        </button>
                    </div>

                    <div class="filter-item">
                        <label>&nbsp;</label>
                        <button type="button" class="btn btn-sm btn-view" onclick="exportOrders()">
                            <i class="fas fa-download"></i> Xuất CSV
                        </button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Bulk actions -->
        <div class="bulk-actions" id="bulkActions">
            <span><strong id="selectedCount">0</strong> đơn đã chọn</span>
            <select id="bulkStatus" class="filter-item select">
                <option value="">-- Chọn trạng thái --</option>
                <option value="pending">Chờ xác nhận</option>
                <option value="processing">Đang xử lý</option>
                <option value="shipped">Đang giao</option>
                <option value="delivered">Đã giao</option>
                <option value="cancelled">Hủy</option>
            </select>
            <button class="btn btn-sm btn-update" onclick="bulkUpdateStatus()">
                <i class="fas fa-edit"></i> Cập nhật hàng loạt
            </button>
            <button class="btn btn-sm btn-view" onclick="clearSelection()">
                <i class="fas fa-times"></i> Bỏ chọn
            </button>
        </div>

        <!-- Bảng đơn hàng -->
        <div class="table-container">
            <div class="table-header">
                <h2>Danh Sách Đơn Hàng (${totalRecords} đơn)</h2>
            </div>
            <table>
                <thead>
                <tr>
                    <th class="checkbox-cell">
                        <input type="checkbox" id="selectAll" onchange="toggleSelectAll(this)">
                    </th>
                    <th>Mã ĐH</th>
                    <th>Mã KH</th>
                    <th>Khách Hàng</th>
                    <th>Ngày</th>
                    <th>Tổng Tiền</th>
                    <th>Trạng Thái</th>
                    <th>Thao Tác</th>
                    <th>Đánh giá</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty orders}">
                        <tr>
                            <td colspan="9" style="text-align: center; padding: 40px;">
                                <i class="fas fa-inbox" style="font-size: 48px; color: #cbd5e0;"></i>
                                <p style="margin-top: 10px; color: #a0aec0;">Không có đơn hàng nào</p>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td class="checkbox-cell">
                                    <input type="checkbox" class="order-checkbox" value="${order.id}"
                                           onchange="updateBulkActions()">
                                </td>
                                <td>#DH<fmt:formatNumber value="${order.id}" pattern="000"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.userId != null}">
                                            #KH<fmt:formatNumber value="${order.userId}" pattern="00"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #a0aec0;">-</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div style="font-weight: 500;">${order.recipientName}</div>
                                    <div style="font-size: 12px; color: #718096;">${order.recipientEmail}</div>
                                </td>
                                <td>
                                    ${order.orderDateFormatted}
                                </td>
                                <td style="font-weight: 600;">
                                    <fmt:formatNumber value="${order.total}" pattern="#,###"/> VNĐ
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.status == 'delivered'}">
                                            <span class="badge badge-success">Đã giao</span>
                                        </c:when>
                                        <c:when test="${order.status == 'shipped'}">
                                            <span class="badge badge-info">Đang giao</span>
                                        </c:when>
                                        <c:when test="${order.status == 'processing'}">
                                            <span class="badge badge-warning">Đang xử lý</span>
                                        </c:when>
                                        <c:when test="${order.status == 'pending'}">
                                            <span class="badge badge-secondary">Chờ xác nhận</span>
                                        </c:when>
                                        <c:when test="${order.status == 'cancelled'}">
                                            <span class="badge badge-danger">Đã hủy</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-secondary">${order.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <button class="btn btn-sm btn-view"
                                                onclick="location.href='${pageContext.request.contextPath}/admin/orders/detail?id=${order.id}'">
                                            <i class="fas fa-eye"></i>
                                        </button>

                                        <button class="btn btn-sm btn-update"
                                                onclick="showUpdateStatusModal(${order.id}, '${order.status}')">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                    </div>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.status == 'delivered'}">
                                            <button class="btn btn-sm btn-view"
                                                    onclick="viewReview(${order.id}, '${order.recipientName}')">
                                                <i class="fas fa-eye"></i> Xem
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-sm btn-view" disabled
                                                    style="opacity: 0.5; cursor: not-allowed;">
                                                <i class="fas fa-eye"></i> Chưa có
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <a href="?page=1&pageSize=${pageSize}&status=${status}&search=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}"
                       class="${currentPages == 1 ? 'disabled' : ''}">
                        <i class="fas fa-angle-double-left"></i>
                    </a>

                    <a href="?page=${currentPages - 1}&pageSize=${pageSize}&status=${status}&search=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}"
                       class="${currentPages == 1 ? 'disabled' : ''}">
                        <i class="fas fa-angle-left"></i>
                    </a>

                    <c:forEach begin="${currentPages - 2 < 1 ? 1 : currentPages - 2}"
                               end="${currentPages + 2 > totalPages ? totalPages : currentPages + 2}"
                               var="i">
                        <a href="?page=${i}&pageSize=${pageSize}&status=${status}&search=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}"
                           class="${currentPages == i ? 'active' : ''}">
                                ${i}
                        </a>
                    </c:forEach>

                    <a href="?page=${currentPages + 1}&pageSize=${pageSize}&status=${status}&search=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}"
                       class="${currentPages == totalPages ? 'disabled' : ''}">
                        <i class="fas fa-angle-right"></i>
                    </a>

                    <a href="?page=${totalPages}&pageSize=${pageSize}&status=${status}&search=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}"
                       class="${currentPages == totalPages ? 'disabled' : ''}">
                        <i class="fas fa-angle-double-right"></i>
                    </a>

                    <span style="margin-left: 15px; color: #718096;">
                            Trang ${currentPages} / ${totalPages}
                        </span>
                </div>
            </c:if>
        </div>
    </div>
</div>

<div id="reviewModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-star" style="color: #f39c12;"></i> Chi Tiết Đánh Giá</h3>
            <span class="close" onclick="closeModal('reviewModal')">&times;</span>
        </div>
        <div class="modal-body">
            <div class="review-detail">
                <div class="review-detail-label">Mã đơn hàng</div>
                <div class="review-detail-content" id="modalOrderId"></div>
            </div>
            <div class="review-detail">
                <div class="review-detail-label">Khách hàng</div>
                <div class="review-detail-content" id="modalCustomer"></div>
            </div>
            <div class="review-detail">
                <div class="review-detail-label">Đánh giá</div>
                <div class="review-stars" id="modalRating"></div>
            </div>
            <div class="review-detail">
                <div class="review-detail-label">Ghi chú từ khách hàng</div>
                <div class="review-note-box" id="modalNote"></div>
            </div>
        </div>
    </div>
</div>

<div id="statusModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-edit"></i> Cập Nhật Trạng Thái Đơn Hàng</h3>
            <span class="close" onclick="closeModal('statusModal')">&times;</span>
        </div>
        <div class="modal-body">
            <div class="review-detail">
                <div class="review-detail-label">Mã đơn hàng</div>
                <div class="review-detail-content" id="statusOrderId"></div>
            </div>
            <div class="review-detail">
                <div class="review-detail-label">Trạng thái hiện tại</div>
                <div class="review-detail-content" id="statusCurrent"></div>
            </div>
            <div class="review-detail">
                <div class="review-detail-label">Chọn trạng thái mới</div>
                <select id="statusSelect" class="status-dropdown">
                    <option value="">-- Chọn trạng thái --</option>
                    <option value="pending">Chờ xác nhận</option>
                    <option value="processing">Đang xử lý</option>
                    <option value="shipped">Đang giao</option>
                    <option value="delivered">Đã giao</option>
                    <option value="cancelled">Hủy</option>
                </select>
            </div>
            <div class="modal-actions">
                <button class="btn-cancel" onclick="closeModal('statusModal')">Hủy</button>
                <button class="btn-confirm" onclick="confirmUpdateStatus()">Cập Nhật</button>
            </div>
        </div>
    </div>
</div>


<script src="${pageContext.request.contextPath}/views/JS/admin-orders.js"></script>

</body>

</html>
