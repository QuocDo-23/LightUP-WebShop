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
    <title>Chi Tiết Đơn Hàng #DH<fmt:formatNumber value="${order.id}" pattern="000"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/order-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/CSS/adminCSS/ad_orderDetail.css">


</head>
<body data-context-path="${pageContext.request.contextPath}">
<div class="container">
    <jsp:include page="/views/layout/siderbar_admin.jsp"/>


    <div class="main-content">
        <div class="header">
            <h1>Chi Tiết Đơn Hàng</h1>
            <div class="user-info">
                <div class="avatar">Q</div>
                <div>
                    <div style="font-weight: 600;">Admin</div>
                    <div style="font-size: 12px; color: #718096;">Quản trị viên</div>
                </div>
            </div>
        </div>

        <div class="detail-container">
            <div class="detail-header">
                <div>
                    <h2>Đơn Hàng #DH<fmt:formatNumber value="${order.id}" pattern="000"/></h2>
                    <p style="color: #718096; margin-top: 5px;">
                        Ngày đặt: ${order.orderDateFormatted}
                    </p>
                </div>

                <div class="actions-bar">
                    <a href="javascript:history.back()" class="back-btn">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                    <c:choose>
                        <c:when test="${order.status == 'delivered'}">
                            <span class="status-badge badge-success">Đã giao</span>
                        </c:when>
                        <c:when test="${order.status == 'shipped'}">
                            <span class="status-badge badge-info">Đang giao</span>
                        </c:when>
                        <c:when test="${order.status == 'processing'}">
                            <span class="status-badge badge-warning">Đang xử lý</span>
                        </c:when>
                        <c:when test="${order.status == 'pending'}">
                            <span class="status-badge badge-secondary">Chờ xác nhận</span>
                        </c:when>
                        <c:when test="${order.status == 'cancelled'}">
                            <span class="status-badge badge-danger">Đã hủy</span>
                        </c:when>
                    </c:choose>
                </div>
            </div>

            <div class="detail-section">
                <div class="container-infor">
                    <div class="detail-infor">
                        <h3><i class="fas fa-user"></i> Thông Tin Khách Hàng</h3>
                        <div class="detail-grid">
                            <div class="detail-item">
                                <span class="detail-label">Họ tên</span>
                                <span class="detail-value">${order.recipientName}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Email</span>
                                <span class="detail-value">${order.recipientEmail}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Số điện thoại</span>
                                <span class="detail-value">${order.recipientPhone}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Mã khách hàng</span>
                                <span class="detail-value">
                                <c:choose>
                                    <c:when test="${order.userId != null}">
                                        #KH<fmt:formatNumber value="${order.userId}" pattern="00"/>
                                    </c:when>
                                    <c:otherwise>Khách vãng lai</c:otherwise>
                                </c:choose>
                            </span>
                            </div>
                        </div>
                    </div>
                    <div class="detail-section">
                        <h3><i class="fas fa-map-marker-alt"></i> Địa Chỉ Giao Hàng</h3>
                        <div class="detail-item">
                            <span class="detail-label">Địa chỉ đầy đủ</span>
                            <span class="detail-value">
                            ${order.shippingHouseNumber}, ${order.shippingCommune},
                            ${order.shippingDistrict}
                            <c:if test="${not empty order.shippingAddressDetail}">
                                <br/><span style="font-weight: 400; color: #718096;">${order.shippingAddressDetail}</span>
                            </c:if>
                        </span>
                        </div>
                    </div>
                </div>
                <div class="detail-section">
                    <h3><i class="fas fa-box"></i> Sản Phẩm Đã Đặt</h3>
                    <table class="products-table">
                        <thead>
                        <tr>
                            <th style="width: 80px;">Hình ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Chất liệu</th>
                            <th>Đơn giá</th>
                            <th>Số lượng</th>
                            <th>Thành tiền</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${order.items}">
                            <tr>
                                <td class="product-img">
                                    <img src="${not empty item.img ? item.img : 'default.jpg'}"
                                         alt="${item.productName}" >
                                </td>
                                <td>
                                    <div style="font-weight: 500;">${item.productName}</div>
                                    <div style="font-size: 12px; color: #718096;">ID: ${item.productId}</div>
                                </td>
                                <td>${item.productMaterial}</td>
                                <td><fmt:formatNumber value="${item.price}" pattern="#,###"/> VNĐ</td>
                                <td style="text-align: center;">${item.quantity}</td>
                                <td style="font-weight: 600;">
                                    <fmt:formatNumber value="${item.subtotal}" pattern="#,###"/> VNĐ
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <div class="total-section">
                        <div class="total-box">
                            <div class="total-row">
                                <span>Tạm tính:</span>
                                <span><fmt:formatNumber value="${order.total}" pattern="#,###"/> VNĐ</span>
                            </div>
                            <div class="total-row">
                                <span>Phí vận chuyển:</span>
                                <span>0 VNĐ</span>
                            </div>
                            <div class="total-row final">
                                <span>Tổng cộng:</span>
                                <span><fmt:formatNumber value="${order.total}" pattern="#,###"/> VNĐ</span>
                            </div>
                        </div>
                    </div>
                </div>



                <div class="detail-section">
                    <div style="display: flex; gap: 10px;">
                        <button class="btn btn-update" onclick="showUpdateStatusModal(${order.id})">
                            <i class="fas fa-edit"></i> Cập Nhật Trạng Thái
                        </button>
                        <button class="btn btn-view" onclick="window.print()">
                            <i class="fas fa-print"></i> In Đơn Hàng
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div id="statusModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-edit"></i> Cập Nhật Trạng Thái</h3>
            <span class="close" onclick="closeModal()">&times;</span>
        </div>
        <div class="modal-body">
            <select id="newStatus" class="status-dropdown">
                <option value="">-- Chọn trạng thái --</option>
                <option value="pending">Chờ xác nhận</option>
                <option value="processing">Đang xử lý</option>
                <option value="shipped">Đang giao</option>
                <option value="delivered">Đã giao</option>
                <option value="cancelled">Hủy</option>
            </select>
            <div class="modal-actions" style="margin-top: 20px;">
                <button class="btn-cancel" onclick="closeModal()">Hủy</button>
                <button class="btn-confirm" onclick="updateStatus()">Cập Nhật</button>
            </div>
        </div>
    </div>
</div>


<script src="${pageContext.request.contextPath}/views/JS/admin_orderDetail.js"></script>

</body>
</html>
